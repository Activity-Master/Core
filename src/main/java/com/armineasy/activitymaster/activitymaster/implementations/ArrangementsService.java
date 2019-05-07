package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class ArrangementsService
{
	@SuppressWarnings("unchecked")
	public ResourceItem create(IContainsResourceItems<?, ?, ? extends WarehouseClassificationRelationshipTable> parent,
	                           IResourceItemClassification<?> classification, String name, String mimeType, String data, Systems originatingSystem,
	                           boolean append, UUID... identityToken)
	{
		ResourceItem item = new ResourceItem();
		Class<? extends WarehouseClassificationRelationshipTable<?, ? extends ResourceItem, ?, ?, ?, ?>> linkClass = (Class<? extends WarehouseClassificationRelationshipTable<?, ? extends ResourceItem, ?, ?, ?, ?>>) parent.findResourceItemQueryRelationshipTableType();

		WarehouseClassificationRelationshipTable resourceTable = GuiceContext.get(linkClass);
		QueryBuilderRelationshipClassification builder = (QueryBuilderRelationshipClassification) resourceTable.builder();

		Optional<WarehouseClassificationRelationshipTable> exists = ActivityMasterConfiguration
				                                                            .get()
				                                                            .isDoubleCheckDisabled() ? Optional.empty() :
		                                                            builder
				                                                            .findLink((WarehouseCoreTable) parent, item, originatingSystem.getEnterpriseID())
				                                                            .inDateRange()
				                                                            .inActiveRange(originatingSystem.getEnterpriseID())
				                                                            .get();
		if (exists.isEmpty() || append)
		{

		}
		else
		{
			resourceTable = exists.get();
		}


		return null;
	}

}
