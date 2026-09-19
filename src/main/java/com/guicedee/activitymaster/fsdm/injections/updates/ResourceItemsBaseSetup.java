package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.ResourceItemClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.ResourceItemTypes;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;

@SortedUpdate(sortOrder = -200, taskCount = 3)
@Log4j2
public class ResourceItemsBaseSetup implements ISystemUpdate
{
	@Inject
	private IClassificationService<?> service;

	@Inject
	private ISystemsService<?> systemsService;

	@Inject
	private IResourceItemService<?> resourceItemService;

	/**
	 * Stateless twin of {@link #update(Mutiny.StatelessSession, IEnterprise)}. The stateless install loop
	 * ({@code EnterpriseService.processUpdates(Mutiny.StatelessSession, …)}) calls
	 * {@link ISystemUpdate#update(Mutiny.StatelessSession, IEnterprise)}; without this override the base
	 * default throws {@code UnsupportedOperationException} and the failure is swallowed, so the resource-item
	 * types and classifications (including {@code ResourceItemClassifications.Icon}) are never seeded — which
	 * later surfaces as a {@code NoResultException} the first time the stateless path looks up "Icon".
	 * This mirrors the managed seeding exactly, using the stateless service overloads.
	 */
	@Override
	public Uni<Boolean> update(Mutiny.StatelessSession session, IEnterprise<?,?> enterprise)
	{
		log.info("Starting stateless creation of resource item types and classifications");
		logProgress("Resource Items", "Loading Default Resource Items...", 1);

		return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
			.chain(activityMasterSystem ->
				// Document types
				createTypes(session, activityMasterSystem, List.of(
						ResourceItemTypes.Documents, ResourceItemTypes.JsonPacket, ResourceItemTypes.XMLPacket,
						ResourceItemTypes.PhysicalDocuments, ResourceItemTypes.Invoices, ResourceItemTypes.Statements,
						ResourceItemTypes.ElectronicDocuments))
					.invoke(() -> logProgress("Resource Items", "Loading Base Image Types...", 1))
					// Image types
					.chain(() -> createTypes(session, activityMasterSystem, List.of(
						ResourceItemTypes.Icon, ResourceItemTypes.Logo, ResourceItemTypes.Flag, ResourceItemTypes.Banner,
						ResourceItemTypes.Gravatar, ResourceItemTypes.Screenshot, ResourceItemTypes.Background,
						ResourceItemTypes.StyleSheets, ResourceItemTypes.JavaScriptTemplates, ResourceItemTypes.HtmlTemplate,
						ResourceItemTypes.StringTemplate)))
					.invoke(() -> logProgress("Resource Items", "Loading Base Device Types...", 1))
					// Device types
					.chain(() -> createTypes(session, activityMasterSystem, List.of(
						ResourceItemTypes.MobileDevice, ResourceItemTypes.BrowserInformation)))
					// File classification group (parent first, then its children)
					.chain(() -> service.create(session, ResourceItemClassifications.FileResourceItemClassifications, activityMasterSystem)
						.chain(() -> createChildren(session, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications, List.of(
							ResourceItemClassifications.AddedANewDevice, ResourceItemClassifications.HadNewConnectionDetails,
							ResourceItemClassifications.Description, ResourceItemClassifications.Extension,
							ResourceItemClassifications.FileName, ResourceItemClassifications.Size,
							ResourceItemClassifications.UUID, ResourceItemClassifications.Icon,
							ResourceItemClassifications.FileLocation))))
					// Event classification group (parent first, then its children)
					.chain(() -> service.create(session, ResourceItemClassifications.EventDefaultResourceItemClassifications, activityMasterSystem)
						.chain(() -> createChildren(session, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications, List.of(
							ResourceItemClassifications.Added, ResourceItemClassifications.Removed,
							ResourceItemClassifications.Updated, ResourceItemClassifications.MovedTo))))
					.onFailure().invoke(error -> log.error("Error creating resource items (stateless): {}", error.getMessage(), error))
					.invoke(() -> logProgress("Resource Items", "Loading Resource Item Classifications...", 1))
					.map(v -> true));
	}

	/**
	 * Sequentially find-or-create each resource-item type. Uses {@code transformToUniAndConcatenate}
	 * (never {@code ...Merge}) so exactly one statement is ever in flight on the single reactive connection —
	 * honouring the "one action per session at a time" rule while reading as a flat list.
	 */
	private Uni<Void> createTypes(Mutiny.StatelessSession session, ISystems<?,?> system, List<ResourceItemTypes> types)
	{
		return Multi.createFrom().iterable(types)
			.onItem().transformToUniAndConcatenate(t -> resourceItemService.createType(session, t, system))
			.collect().last()
			.replaceWithVoid();
	}

	/** Sequential (concatenated) find-or-create of each child classification beneath {@code parent}. */
	private Uni<Void> createChildren(Mutiny.StatelessSession session, ISystems<?,?> system,
									 ResourceItemClassifications parent, List<ResourceItemClassifications> children)
	{
		return Multi.createFrom().iterable(children)
			.onItem().transformToUniAndConcatenate(c -> service.create(session, c, system, parent))
			.collect().last()
			.replaceWithVoid();
	}

}
