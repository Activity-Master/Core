package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemData;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.services.IResourceTypeValue;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.system.IClassificationService;
import com.armineasy.activitymaster.activitymaster.services.system.IResourceItemService;
import com.armineasy.activitymaster.activitymaster.threads.PersistingThread;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.interfaces.JobService;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

@SuppressWarnings("Duplicates")
public interface IContainsResourceItems<P extends WarehouseCoreTable,
		                                       S extends WarehouseCoreTable,
		                                       J extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?>>
{
	@SuppressWarnings("unchecked")
	@CacheResult
	default Optional<J> findResourceItem(@CacheKey ResourceItem classification, @CacheKey UUID... identityToken)
	{
		J activityMasterIdentity = GuiceContext.get(findResourceItemQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) activityMasterIdentity.builder()
		                                                         .findLink((P) this, (S) classification, classification.getEnterpriseID())
		                                                         .inActiveRange(classification.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(classification.getEnterpriseID(), identityToken)
		                                                         .get();
		return exists;
	}

	@NotNull
	@SuppressWarnings("unchecked")
	default Class<J> findResourceItemQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsResourceItems.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<J>) genericTypes[2];
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	default boolean hasResourceItem(ResourceItem classification, @CacheKey UUID... identityToken)
	{
		J activityMasterIdentity = GuiceContext.get(findResourceItemQueryRelationshipTableType());
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, classification.getEnterpriseID())
		                             .inActiveRange(classification.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(classification.getEnterpriseID(), identityToken)
		                             .getCount() > 0;
	}

	@SuppressWarnings("unchecked")
	default ResourceItem addResourceItem(IResourceTypeValue<?> resourceTypeValue, IResourceItemClassification classification,
	                                     byte[] data,
	                                     String mimeType,
	                                     Systems originatingSystem, UUID... identifyingToken)
	{
		J tableForClassification = GuiceContext.get(findResourceItemQueryRelationshipTableType());

		IResourceItemService service = GuiceContext.get(IResourceItemService.class);

		ResourceItem item = service.create(resourceTypeValue,mimeType, originatingSystem, identifyingToken);

		boolean async = GuiceContext.get(ActivityMasterConfiguration.class)
		                            .isAsyncEnabled();
		if (async)
		{
			JobService.getInstance()
			          .addJob("ResourceItemDataStore", () -> storeResourceItemData(item, data, originatingSystem, identifyingToken));
		}
		else
		{
			storeResourceItemData(item, data, originatingSystem, identifyingToken);
		}

		tableForClassification.setEnterpriseID(originatingSystem.getEnterpriseID());
		tableForClassification.setValue("");
		tableForClassification.setSystemID(originatingSystem);
		tableForClassification.setOriginalSourceSystemID(originatingSystem);
		tableForClassification.setActiveFlagID(originatingSystem.getActiveFlagID());
		tableForClassification.setClassificationID(GuiceContext.get(IClassificationService.class)
		                                                       .find(classification, originatingSystem.getEnterpriseID(), identifyingToken));
		setMyResourceItemLinkValue(tableForClassification, (S) item, originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identifyingToken);
		}

		return item;
	}

	@SuppressWarnings("unchecked")
	default ResourceItem addOrUseResourceItem(IResourceTypeValue<?> resourceTypeValue, IResourceItemClassification classification,
	                                          byte[] data,
	                                          String mimeType,
	                                          Systems originatingSystem, UUID... identifyingToken)
	{
		J tableForClassification = GuiceContext.get(findResourceItemQueryRelationshipTableType());

		IResourceItemService service = GuiceContext.get(IResourceItemService.class);

		ResourceItem item = service.create(resourceTypeValue, mimeType, originatingSystem, identifyingToken);

		boolean async = GuiceContext.get(ActivityMasterConfiguration.class)
		                            .isAsyncEnabled();
		if (async)
		{
			JobService.getInstance()
			          .addJob("ResourceItemDataStore", () -> storeResourceItemData(item, data, originatingSystem, identifyingToken));
		}
		else
		{
			storeResourceItemData(item, data, originatingSystem, identifyingToken);
		}

		tableForClassification.setEnterpriseID(originatingSystem.getEnterpriseID());
		tableForClassification.setValue("");
		tableForClassification.setSystemID(originatingSystem);
		tableForClassification.setOriginalSourceSystemID(originatingSystem);
		tableForClassification.setActiveFlagID(originatingSystem.getActiveFlagID());
		tableForClassification.setClassificationID(GuiceContext.get(IClassificationService.class)
		                                                       .find(classification, originatingSystem.getEnterpriseID(), identifyingToken));
		setMyResourceItemLinkValue(tableForClassification, (S) item, originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identifyingToken);
		}

		return item;
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	default void storeResourceItemData(ResourceItem item, byte[] data, Systems originatingSystem, UUID... identifyingToken)
	{
		ResourceItemData itemData = new ResourceItemData();
		itemData.setResource(item);
		itemData.setResourceItemData(data);

		itemData.setEnterpriseID(originatingSystem.getEnterpriseID());
		itemData.setSystemID(originatingSystem);
		itemData.setOriginalSourceSystemID(originatingSystem);

		itemData.setActiveFlagID(originatingSystem.getActiveFlagID());
		itemData.persist();

		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			itemData.createDefaultSecurity(originatingSystem, identifyingToken);
		}
	}


	@SuppressWarnings("unchecked")
	default J add(ResourceItem resourceItem, IResourceItemClassification<?> iclassification, Systems originatingSystem, UUID... identifyingToken)
	{
		J tableForClassification = get(findResourceItemQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) resourceItem, originatingSystem.getEnterpriseID())
		                                                         .inActiveRange(resourceItem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identifyingToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			Classification classification = get(ClassificationService.class).find(iclassification,
			                                                                      originatingSystem.getEnterpriseID(), identifyingToken);

			tableForClassification.setEnterpriseID(resourceItem.getEnterpriseID());
			tableForClassification.setSystemID(originatingSystem);
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue("");
			tableForClassification.setOriginalSourceSystemID(originatingSystem);
			tableForClassification.setActiveFlagID(resourceItem.getActiveFlagID());
			setMyResourceItemLinkValue(tableForClassification, (S) resourceItem, resourceItem.getEnterpriseID());
			tableForClassification.persist();

			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originatingSystem, identifyingToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}


	void setMyResourceItemLinkValue(J classificationLink, S resourceItem, Enterprise enterprise);

}
