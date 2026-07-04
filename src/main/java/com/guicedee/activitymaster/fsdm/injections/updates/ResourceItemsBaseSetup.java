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

	@Override
	public Uni<Boolean> update(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Starting parallel creation of resource item types and classifications");
		logProgress("Resource Items", "Loading Default Resource Items...", 1);

		// Get the ActivityMaster system and then create resource items
		return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
			.chain(activityMasterSystem -> {
				// Create document-related resource item types sequentially
				log.info("Creating document-related resource item types sequentially");
				
				// Start with the first resource item type
				return resourceItemService.createType(session, ResourceItemTypes.Documents, activityMasterSystem)
					// Chain the rest of the resource item types sequentially
					.chain(() -> resourceItemService.createType(session, ResourceItemTypes.JsonPacket, activityMasterSystem))
					.chain(() -> resourceItemService.createType(session, ResourceItemTypes.XMLPacket, activityMasterSystem))
					.chain(() -> resourceItemService.createType(session, ResourceItemTypes.PhysicalDocuments, activityMasterSystem))
					.chain(() -> resourceItemService.createType(session, ResourceItemTypes.Invoices, activityMasterSystem))
					.chain(() -> resourceItemService.createType(session, ResourceItemTypes.Statements, activityMasterSystem))
					.chain(() -> resourceItemService.createType(session, ResourceItemTypes.ElectronicDocuments, activityMasterSystem))
					.onFailure().invoke(error -> log.error("Error creating document-related resource item types: {}", error.getMessage(), error))
					.chain(() -> {
						logProgress("Resource Items", "Loading Base Image Types...", 1);

						// Create image-related resource item types sequentially
						log.info("Creating image-related resource item types sequentially");
						
						// Chain all image-related resource item type creation operations sequentially
						return resourceItemService.createType(session, ResourceItemTypes.Icon, activityMasterSystem)
							.chain(() -> resourceItemService.createType(session, ResourceItemTypes.Logo, activityMasterSystem))
							.chain(() -> resourceItemService.createType(session, ResourceItemTypes.Flag, activityMasterSystem))
							.chain(() -> resourceItemService.createType(session, ResourceItemTypes.Banner, activityMasterSystem))
							.chain(() -> resourceItemService.createType(session, ResourceItemTypes.Gravatar, activityMasterSystem))
							.chain(() -> resourceItemService.createType(session, ResourceItemTypes.Screenshot, activityMasterSystem))
							.chain(() -> resourceItemService.createType(session, ResourceItemTypes.Background, activityMasterSystem))
							.chain(() -> resourceItemService.createType(session, ResourceItemTypes.StyleSheets, activityMasterSystem))
							.chain(() -> resourceItemService.createType(session, ResourceItemTypes.JavaScriptTemplates, activityMasterSystem))
							.chain(() -> resourceItemService.createType(session, ResourceItemTypes.HtmlTemplate, activityMasterSystem))
							.chain(() -> resourceItemService.createType(session, ResourceItemTypes.StringTemplate, activityMasterSystem))
							.onFailure().invoke(error -> log.error("Error creating image-related resource item types: {}", error.getMessage(), error))
							.chain(() -> {
								logProgress("Resource Items", "Loading Base Device Types...", 1);

								// Create device-related resource item types sequentially
								log.info("Creating device-related resource item types sequentially");
								
								// Chain device-related resource item type creation operations sequentially
								return resourceItemService.createType(session, ResourceItemTypes.MobileDevice, activityMasterSystem)
									.chain(() -> resourceItemService.createType(session, ResourceItemTypes.BrowserInformation, activityMasterSystem))
									.onFailure().invoke(error -> log.error("Error creating device-related resource item types: {}", error.getMessage(), error))
									.chain(() -> {
										log.info("Creating FileResourceItemClassifications and EventDefaultResourceItemClassifications in parallel");

										// Create FileResourceItemClassifications and its children sequentially
										log.info("Creating FileResourceItemClassifications and its children sequentially");
										
										// First create the base classification
										Uni<Void> fileClassificationsUni = service.create(session, ResourceItemClassifications.FileResourceItemClassifications, activityMasterSystem)
											.chain(baseFileClassification -> {
												log.info("Creating file-related classifications sequentially");
												
												// Chain file-related classification creation operations sequentially
												return service.create(session, ResourceItemClassifications.AddedANewDevice, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications)
													.chain(() -> service.create(session, ResourceItemClassifications.HadNewConnectionDetails, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications))
													.chain(() -> service.create(session, ResourceItemClassifications.Description, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications))
													.chain(() -> service.create(session, ResourceItemClassifications.Extension, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications))
													.chain(() -> service.create(session, ResourceItemClassifications.FileName, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications))
													.chain(() -> service.create(session, ResourceItemClassifications.Size, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications))
													.chain(() -> service.create(session, ResourceItemClassifications.UUID, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications))
													.chain(() -> service.create(session, ResourceItemClassifications.Icon, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications))
													.chain(() -> service.create(session, ResourceItemClassifications.FileLocation, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications))
													.onFailure().invoke(error -> log.error("Error creating file-related classifications: {}", error.getMessage(), error))
													.replaceWithVoid(); // Return Void
											});

										// Create EventDefaultResourceItemClassifications and its children sequentially
										log.info("Creating EventDefaultResourceItemClassifications and its children sequentially");
										
										// First create the base classification
										Uni<Void> eventClassificationsUni = service.create(session, ResourceItemClassifications.EventDefaultResourceItemClassifications, activityMasterSystem)
											.chain(baseEventClassification -> {
												log.info("Creating event-related classifications sequentially");
												
												// Chain event-related classification creation operations sequentially
												return service.create(session, ResourceItemClassifications.Added, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications)
													.chain(() -> service.create(session, ResourceItemClassifications.Removed, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications))
													.chain(() -> service.create(session, ResourceItemClassifications.Updated, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications))
													.chain(() -> service.create(session, ResourceItemClassifications.MovedTo, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications))
													.onFailure().invoke(error -> log.error("Error creating event-related classifications: {}", error.getMessage(), error))
													.replaceWithVoid(); // Return Void
											});

										// Run classification groups sequentially
										log.info("Running classification groups sequentially");
										
										// First run the file classifications, then chain to the event classifications
										return fileClassificationsUni
											.chain(() -> eventClassificationsUni)
											.onFailure().invoke(error -> log.error("Error creating resource item classifications: {}", error.getMessage(), error))
											.invoke(() -> logProgress("Resource Items", "Loading Resource Item Classifications...", 1))
											.map(result -> true); // Return Boolean
									});
							});
					});
			});
	}

	/**
	 * Stateless twin of {@link #update(Mutiny.Session, IEnterprise)}. The stateless install loop
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
