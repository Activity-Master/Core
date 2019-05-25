package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationship;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemType;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

public interface IContainsResourceItemTypes<P extends WarehouseCoreTable,
		                                           S extends WarehouseCoreTable,
		                                           J extends WarehouseRelationshipTable<P, S, ?, ? extends QueryBuilderRelationship, ?, ?>>
{
	@SuppressWarnings("unchecked")
	default Optional<J> findResourceItemType(@CacheKey ResourceItemType classification, @CacheKey UUID... identityToken)
	{
		J activityMasterIdentity = GuiceContext.get(findResourceItemTypeQueryRelationshipTableType());
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
	default Class<J> findResourceItemTypeQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsResourceItemTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<J>) genericTypes[2];
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	default boolean hasResourceItemType(ResourceItemType classification, @CacheKey UUID... identityToken)
	{
		J activityMasterIdentity = GuiceContext.get(findResourceItemTypeQueryRelationshipTableType());
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, classification.getEnterpriseID())
		                             .inActiveRange(classification.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(classification.getEnterpriseID(), identityToken)
		                             .getCount() > 0;
	}

	@SuppressWarnings("unchecked")
	default J addResourceItemType(ResourceItemType resourceItemType, String value, UUID... identifyingToken)
	{
		J tableForClassification = GuiceContext.get(findResourceItemTypeQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) resourceItemType, resourceItemType.getEnterpriseID())
		                                                         .inActiveRange(resourceItemType.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .get();
		if (exists.isEmpty())
		{
			ISystems activityMasterSystem = GuiceContext.get(ISystemsService.class)
			                                            .getActivityMaster(resourceItemType.getEnterpriseID());
			tableForClassification.setEnterpriseID(resourceItemType.getEnterpriseID());
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) activityMasterSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) activityMasterSystem);
			tableForClassification.setActiveFlagID(resourceItemType.getActiveFlagID());
			setMyResourceItemTypeLinkValue(tableForClassification, (S) resourceItemType, resourceItemType.getEnterpriseID());

			tableForClassification.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(activityMasterSystem, identifyingToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}

	void setMyResourceItemTypeLinkValue(J classificationLink, S ResourceItemType, Enterprise enterprise);

}
