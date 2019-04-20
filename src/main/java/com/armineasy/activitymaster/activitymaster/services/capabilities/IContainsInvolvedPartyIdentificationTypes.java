package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationship;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyIdentificationType;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.InvolvedPartyService;
import com.armineasy.activitymaster.activitymaster.services.IIdentificationType;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

import static com.jwebmp.guicedinjection.GuiceContext.*;

public interface IContainsInvolvedPartyIdentificationTypes<P extends WarehouseCoreTable,
		                                                          S extends WarehouseCoreTable,
		                                                          J extends WarehouseRelationshipTable<P, S, ?, ? extends QueryBuilderRelationship, ?, ?>>
{
	@SuppressWarnings("unchecked")
	@CacheResult
	default Optional<J> findIdentificationType(@CacheKey IIdentificationType typeName, @CacheKey Systems originatingSystem, @CacheKey UUID... identityToken)
	{
		InvolvedPartyIdentificationType type = (InvolvedPartyIdentificationType) get(IContainsInvolvedPartyIdentificationTypes.class)
				                                                                         .findIdentificationType(typeName, originatingSystem, identityToken)
				                                                                         .orElseThrow();

		J activityMasterIdentity = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
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
	default Class<J> findInvolvedPartyIdentificationTypeQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedPartyIdentificationTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<J>) genericTypes[2];
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	default boolean hasIdentificationType(IIdentificationType<?> typeName, Systems originatingSystem, UUID... identityToken)
	{
		InvolvedPartyIdentificationType type = (InvolvedPartyIdentificationType) get(IContainsInvolvedPartyIdentificationTypes.class)
				                                                                         .findIdentificationType(typeName, originatingSystem, identityToken)
				                                                                         .orElseThrow();
		J activityMasterIdentity = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) type, type.getEnterpriseID())
		                             .inActiveRange(type.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(type.getEnterpriseID(), identityToken)
		                             .getCount() > 0;
	}

	@SuppressWarnings("unchecked")
	default J addIdentificationType(IIdentificationType<?> typeName, Systems originatingSystem, String value, UUID... identityToken)
	{
		InvolvedPartyIdentificationType type = get(InvolvedPartyService.class)
				                                       .findIdentificationType(typeName, originatingSystem.getEnterpriseID(), identityToken);

		J tableForClassification = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) type, type.getEnterpriseID())
		                                                         .inActiveRange(type.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(),identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID(type.getEnterpriseID());
			tableForClassification.setValue(value);
			tableForClassification.setSystemID(originatingSystem);
			tableForClassification.setOriginalSourceSystemID(originatingSystem);
			tableForClassification.setActiveFlagID(type.getActiveFlagID());
			setMyInvolvedPartyIdentificationTypeLinkValue(tableForClassification, (S) type, type.getEnterpriseID());

			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					    .isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originatingSystem);
			}
		}
		else
		{
			tableForClassification = exists.get();
			if(!tableForClassification.getValue().equals(value))
			{
				tableForClassification.update(value,identityToken);
			}
		}
		return tableForClassification;
	}

	void setMyInvolvedPartyIdentificationTypeLinkValue(J classificationLink, S identificationType, Enterprise enterprise);

}
