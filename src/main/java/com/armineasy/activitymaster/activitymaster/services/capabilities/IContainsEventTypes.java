package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventType;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IEventTypeValue;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.system.IClassificationService;
import com.armineasy.activitymaster.activitymaster.services.system.IEventService;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

import static com.jwebmp.guicedinjection.GuiceContext.*;

public interface IContainsEventTypes<P extends WarehouseCoreTable,
		                                    S extends WarehouseCoreTable,
		                                    J extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?>>
{
	@SuppressWarnings("unchecked")
	@CacheResult
	default Optional<J> findType(@CacheKey EventType type, @CacheKey UUID... identityToken)
	{
		J activityMasterIdentity = get(findEventTypeQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) activityMasterIdentity.builder()
		                                                         .findLink((P) this, (S) type, type.getEnterpriseID())
		                                                         .inActiveRange(type.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(type.getEnterpriseID(), identityToken)
		                                                         .get();
		return exists;
	}

	@NotNull
	@SuppressWarnings("unchecked")
	default Class<J> findEventTypeQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsEventTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<J>) genericTypes[2];
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	default boolean hasType(EventType type, @CacheKey UUID... identityToken)
	{
		J activityMasterIdentity = get(findEventTypeQueryRelationshipTableType());
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) type, type.getEnterpriseID())
		                             .inActiveRange(type.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(type.getEnterpriseID(), identityToken)
		                             .getCount() > 0;
	}

	@SuppressWarnings("unchecked")
	default J addEventType(IEventTypeValue<?> typeValue, ISystems originatingSystems, IClassificationValue<?> classificationValue,
	                       String value, UUID... identifyingToken)
	{
		EventType type = get(IEventService.class)
				                 .findEventType(typeValue, originatingSystems.getEnterpriseID(), identifyingToken);
		J tableForClassification = get(findEventTypeQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) type, type.getEnterpriseID())
		                                                         .inActiveRange(type.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystems.getEnterpriseID(), identifyingToken)
		                                                         .get();

		Classification clazz = get(IClassificationService.class).find(classificationValue, originatingSystems.getEnterpriseID(), identifyingToken);
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID(type.getEnterpriseID());
			tableForClassification.setClassificationID(clazz);
			tableForClassification.setValue(value != null ? value : "");
			tableForClassification.setSystemID((Systems) originatingSystems);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystems);
			tableForClassification.setActiveFlagID(type.getActiveFlagID());
			setMyEventTypeLinkValue(tableForClassification, (S) type, type.getEnterpriseID());

			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originatingSystems, identifyingToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}

	void setMyEventTypeLinkValue(J classificationLink, S identificationType, IEnterprise<?> enterprise);

}
