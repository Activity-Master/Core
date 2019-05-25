package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationship;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyType;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.InvolvedPartyService;
import com.armineasy.activitymaster.activitymaster.services.ITypeValue;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.system.IInvolvedPartyService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

public interface IContainsInvolvedPartyTypes<P extends WarehouseCoreTable,
		                                     S extends WarehouseCoreTable,
		                                     J extends WarehouseRelationshipTable<P, S, ?, ? extends QueryBuilderRelationship, ?, ?>>
{
	@SuppressWarnings("unchecked")
	@CacheResult
	default Optional<J> findType(@CacheKey InvolvedPartyType type, @CacheKey UUID... identityToken)
	{
		J activityMasterIdentity = GuiceContext.get(findInvolvedPartyTypeQueryRelationshipTableType());
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
	default Class<J> findInvolvedPartyTypeQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedPartyTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<J>) genericTypes[2];
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	default boolean hasType(InvolvedPartyType type, @CacheKey UUID... identityToken)
	{
		J activityMasterIdentity = GuiceContext.get(findInvolvedPartyTypeQueryRelationshipTableType());
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) type, type.getEnterpriseID())
		                             .inActiveRange(type.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(type.getEnterpriseID(), identityToken)
		                             .getCount() > 0;
	}

	@SuppressWarnings("unchecked")
	default J addType(ITypeValue<?> typeValue, ISystems originatingSystems, String value, UUID... identifyingToken)
	{
		InvolvedPartyType type = GuiceContext.get(InvolvedPartyService.class)
		                                     .findType(typeValue, originatingSystems.getEnterpriseID(),identifyingToken);
		J tableForClassification = GuiceContext.get(findInvolvedPartyTypeQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) type, type.getEnterpriseID())
		                                                         .inActiveRange(type.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystems.getEnterpriseID(),identifyingToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID(type.getEnterpriseID());
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystems);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystems);
			tableForClassification.setActiveFlagID(type.getActiveFlagID());
			setMyInvolvedPartyTypeLinkValue(tableForClassification, (S) type, type.getEnterpriseID());

			tableForClassification.persist();
			if(GuiceContext.get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originatingSystems,identifyingToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}

	void setMyInvolvedPartyTypeLinkValue(J classificationLink, S identificationType, Enterprise enterprise);

}
