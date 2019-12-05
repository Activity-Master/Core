package com.guicedee.activitymaster.core.services.capabilities;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

public interface IContainsGeographies<P extends WarehouseCoreTable,
		                                     S extends WarehouseCoreTable,
		                                     J extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?,?,?>>
{
	@SuppressWarnings("unchecked")
	default Optional<J> findGeography(@CacheKey Geography classification, @CacheKey UUID... identityToken)
	{
		J activityMasterIdentity = GuiceContext.get(findGeographyQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) activityMasterIdentity.builder()
		                                                         .findLink((P) this, (S) classification, null)
		                                                         .inActiveRange(classification.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(classification.getEnterpriseID(), identityToken)
		                                                         .get();
		return exists;
	}

	@NotNull
	@SuppressWarnings("unchecked")
	default Class<J> findGeographyQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsGeographies.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<J>) genericTypes[2];
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	default boolean hasGeography(Geography classification, @CacheKey UUID... identityToken)
	{
		J activityMasterIdentity = GuiceContext.get(findGeographyQueryRelationshipTableType());
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(classification.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(classification.getEnterpriseID(), identityToken)
		                             .getCount() > 0;
	}

	@SuppressWarnings("unchecked")
	default J add(Geography geography, Classification classification, String value, UUID... identifyingToken)
	{
		J tableForClassification = GuiceContext.get(findGeographyQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
		                                                         .inActiveRange(classification.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .get();
		if (exists.isEmpty())
		{
			Systems activityMasterSystem = (Systems) GuiceContext.get(ISystemsService.class)
			                                                     .getActivityMaster(classification.getEnterpriseID());
			tableForClassification.setEnterpriseID(classification.getEnterpriseID());
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID(activityMasterSystem);
			tableForClassification.setOriginalSourceSystemID(activityMasterSystem);
			tableForClassification.setActiveFlagID(classification.getActiveFlagID());
			setMyGeographyLinkValue(tableForClassification, (S) geography, classification.getEnterpriseID());

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


	void setMyGeographyLinkValue(J classificationLink, S geography, IEnterprise<?> enterprise);

}
