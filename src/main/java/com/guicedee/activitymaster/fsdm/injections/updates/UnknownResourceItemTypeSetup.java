package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.classifications.ResourceItemTypes;
import com.guicedee.activitymaster.fsdm.client.services.systems.ISystemUpdate;
import com.guicedee.activitymaster.fsdm.client.services.systems.SortedUpdate;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import static com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName;

/**
 * Ensures the "Unknown" ResourceItemType exists in the system.
 * <p>
 * This must run before {@link ResourceItemsBaseSetup} (sortOrder = -200) so that the
 * Unknown type is available as a fallback when auto-creating missing resource items.
 */
@SortedUpdate(sortOrder = -201, taskCount = 1)
@Log4j2
public class UnknownResourceItemTypeSetup implements ISystemUpdate
{
	@Inject
	private ISystemsService<?> systemsService;

	@Inject
	private IResourceItemService<?> resourceItemService;

	@Override
	public Uni<Boolean> update(Mutiny.Session session, IEnterprise<?, ?> enterprise)
	{
		log.info("Creating 'Unknown' resource item type");
		logProgress("Resource Items", "Loading Unknown Resource Item Type...", 1);

		return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
				.chain(activityMasterSystem ->
						resourceItemService.createType(session, ResourceItemTypes.Unknown, activityMasterSystem))
				.map(result -> true)
				.onFailure()
				.invoke(error -> log.error("Error creating 'Unknown' resource item type: {}", error.getMessage(), error));
	}
}
