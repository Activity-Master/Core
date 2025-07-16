package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.ResourceItemClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.ResourceItemTypes;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

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
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;

	@Inject
	private IResourceItemService<?> resourceItemService;

	@Override
	public Uni<Boolean> update(IEnterprise<?,?> enterprise)
	{
		log.info("Starting parallel creation of resource item types and classifications");
		logProgress("Resource Items", "Loading Default Resource Items...", 1);

		// Create document-related resource item types in parallel
		List<Uni<?>> documentTypeOperations = new ArrayList<>();
		documentTypeOperations.add(resourceItemService.createType(ResourceItemTypes.Documents, activityMasterSystem));
		documentTypeOperations.add(resourceItemService.createType(ResourceItemTypes.JsonPacket, activityMasterSystem));
		documentTypeOperations.add(resourceItemService.createType(ResourceItemTypes.XMLPacket, activityMasterSystem));
		documentTypeOperations.add(resourceItemService.createType(ResourceItemTypes.PhysicalDocuments, activityMasterSystem));
		documentTypeOperations.add(resourceItemService.createType(ResourceItemTypes.Invoices, activityMasterSystem));
		documentTypeOperations.add(resourceItemService.createType(ResourceItemTypes.Statements, activityMasterSystem));
		documentTypeOperations.add(resourceItemService.createType(ResourceItemTypes.ElectronicDocuments, activityMasterSystem));

		log.info("Running {} document-related resource item type creation operations in parallel", documentTypeOperations.size());

		// Run all document type operations in parallel
		return Uni.combine().all().unis(documentTypeOperations)
			.discardItems()
			.onFailure().invoke(error -> log.error("Error creating document-related resource item types: {}", error.getMessage(), error))
			.chain(() -> {
				logProgress("Resource Items", "Loading Base Image Types...", 1);

				// Create image-related resource item types in parallel
				List<Uni<?>> imageTypeOperations = new ArrayList<>();
				imageTypeOperations.add(resourceItemService.createType(ResourceItemTypes.Icon, activityMasterSystem));
				imageTypeOperations.add(resourceItemService.createType(ResourceItemTypes.Logo, activityMasterSystem));
				imageTypeOperations.add(resourceItemService.createType(ResourceItemTypes.Flag, activityMasterSystem));
				imageTypeOperations.add(resourceItemService.createType(ResourceItemTypes.Banner, activityMasterSystem));
				imageTypeOperations.add(resourceItemService.createType(ResourceItemTypes.Gravatar, activityMasterSystem));
				imageTypeOperations.add(resourceItemService.createType(ResourceItemTypes.Screenshot, activityMasterSystem));
				imageTypeOperations.add(resourceItemService.createType(ResourceItemTypes.Background, activityMasterSystem));
				imageTypeOperations.add(resourceItemService.createType(ResourceItemTypes.StyleSheets, activityMasterSystem));
				imageTypeOperations.add(resourceItemService.createType(ResourceItemTypes.JavaScriptTemplates, activityMasterSystem));
				imageTypeOperations.add(resourceItemService.createType(ResourceItemTypes.HtmlTemplate, activityMasterSystem));
				imageTypeOperations.add(resourceItemService.createType(ResourceItemTypes.StringTemplate, activityMasterSystem));

				log.info("Running {} image-related resource item type creation operations in parallel", imageTypeOperations.size());

				// Run all image type operations in parallel
				return Uni.combine().all().unis(imageTypeOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating image-related resource item types: {}", error.getMessage(), error))
					.chain(() -> {
						logProgress("Resource Items", "Loading Base Device Types...", 1);

						// Create device-related resource item types in parallel
						List<Uni<?>> deviceTypeOperations = new ArrayList<>();
						deviceTypeOperations.add(resourceItemService.createType(ResourceItemTypes.MobileDevice, activityMasterSystem));
						deviceTypeOperations.add(resourceItemService.createType(ResourceItemTypes.BrowserInformation, activityMasterSystem));

						log.info("Running {} device-related resource item type creation operations in parallel", deviceTypeOperations.size());

						// Run all device type operations in parallel
						return Uni.combine().all().unis(deviceTypeOperations)
							.discardItems()
							.onFailure().invoke(error -> log.error("Error creating device-related resource item types: {}", error.getMessage(), error))
							.chain(() -> {
								log.info("Creating FileResourceItemClassifications and EventDefaultResourceItemClassifications in parallel");

								// Create FileResourceItemClassifications and its children
								Uni<Void> fileClassificationsUni = service.create(ResourceItemClassifications.FileResourceItemClassifications, activityMasterSystem)
									.chain(baseFileClassification -> {
										// Create file-related classifications in parallel
										List<Uni<?>> fileClassificationOperations = new ArrayList<>();
										fileClassificationOperations.add(service.create(ResourceItemClassifications.AddedANewDevice, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications));
										fileClassificationOperations.add(service.create(ResourceItemClassifications.HadNewConnectionDetails, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications));
										fileClassificationOperations.add(service.create(ResourceItemClassifications.Description, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications));
										fileClassificationOperations.add(service.create(ResourceItemClassifications.Extension, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications));
										fileClassificationOperations.add(service.create(ResourceItemClassifications.FileName, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications));
										fileClassificationOperations.add(service.create(ResourceItemClassifications.Size, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications));
										fileClassificationOperations.add(service.create(ResourceItemClassifications.UUID, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications));
										fileClassificationOperations.add(service.create(ResourceItemClassifications.Icon, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications));
										fileClassificationOperations.add(service.create(ResourceItemClassifications.FileLocation, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications));

										log.info("Running {} file-related classification creation operations in parallel", fileClassificationOperations.size());

										// Run all file classification operations in parallel
										return Uni.combine().all().unis(fileClassificationOperations)
											.discardItems()
											.onFailure().invoke(error -> log.error("Error creating file-related classifications: {}", error.getMessage(), error))
											.map(result -> null); // Return Void
									});

								// Create EventDefaultResourceItemClassifications and its children
								Uni<Void> eventClassificationsUni = service.create(ResourceItemClassifications.EventDefaultResourceItemClassifications, activityMasterSystem)
									.chain(baseEventClassification -> {
										// Create event-related classifications in parallel
										List<Uni<?>> eventClassificationOperations = new ArrayList<>();
										eventClassificationOperations.add(service.create(ResourceItemClassifications.Added, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications));
										eventClassificationOperations.add(service.create(ResourceItemClassifications.Removed, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications));
										eventClassificationOperations.add(service.create(ResourceItemClassifications.Updated, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications));
										eventClassificationOperations.add(service.create(ResourceItemClassifications.MovedTo, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications));

										log.info("Running {} event-related classification creation operations in parallel", eventClassificationOperations.size());

										// Run all event classification operations in parallel
										return Uni.combine().all().unis(eventClassificationOperations)
											.discardItems()
											.onFailure().invoke(error -> log.error("Error creating event-related classifications: {}", error.getMessage(), error))
											.map(result -> null); // Return Void
									});

								// Run both classification groups in parallel
								return Uni.combine().all().unis(fileClassificationsUni, eventClassificationsUni)
									.discardItems()
									.onFailure().invoke(error -> log.error("Error creating resource item classifications: {}", error.getMessage(), error))
									.invoke(() -> logProgress("Resource Items", "Loading Resource Item Classifications...", 1))
									.map(result -> true); // Return Boolean
							});
					});
			});
	}

}
