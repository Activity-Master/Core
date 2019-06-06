package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.services.classifications.involvedparty.IInvolvedPartyClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;

import javax.cache.annotation.CacheKey;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

import static com.jwebmp.guicedinjection.GuiceContext.*;

public interface IContainsInvolvedParties<P extends WarehouseCoreTable,
		                                         S extends WarehouseCoreTable,
		                                         J extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?>>
{
	@SuppressWarnings("unchecked")
	default Optional<J> findInvolvedParty(@CacheKey InvolvedParty InvolvedParty, @CacheKey UUID... identityToken)
	{
		J activityMasterIdentity = get(findInvolvedPartyQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) activityMasterIdentity.builder()
		                                                         .findLink((P) this, (S) InvolvedParty, InvolvedParty.getEnterpriseID())
		                                                         .inActiveRange(InvolvedParty.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(InvolvedParty.getEnterpriseID(), identityToken)
		                                                         .get();
		return exists;
	}

	@NotNull
	@SuppressWarnings("unchecked")
	default Class<J> findInvolvedPartyQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedParties.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<J>) genericTypes[2];
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	default boolean hasInvolvedParty(IInvolvedPartyClassification<?> InvolvedPartyClassification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		J activityMasterIdentity = get(findInvolvedPartyQueryRelationshipTableType());
		ISystems activityMasterSystem = get(ISystemsService.class)
				                               .getActivityMaster(enterprise);
		Classification classification = get(ClassificationService.class).find(InvolvedPartyClassification,
		                                                                      activityMasterSystem.getEnterpriseID(), identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, classification.getEnterpriseID())
		                             .inActiveRange(classification.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(classification.getEnterpriseID(), identityToken)
		                             .getCount() > 0;
	}

	@SuppressWarnings("unchecked")
	default J add(IInvolvedPartyClassification<?> involvedPartyClassification, ISystems originatingSystem, String value, UUID... identifyingToken)
	{
		ISystems activityMasterSystem = get(ISystemsService.class)
				                               .getActivityMaster(originatingSystem.getEnterpriseID(),identifyingToken);

		Classification classification = get(ClassificationService.class).find(involvedPartyClassification,
		                                                                      originatingSystem.getEnterpriseID(), identifyingToken);

		InvolvedParty addy = new InvolvedParty();
		Optional<InvolvedParty> InvolvedPartyExists = addy.builder()
		                                                  .withClassification(classification, value)
		                                                  .withEnterprise(originatingSystem.getEnterpriseID())
		                                                  .inDateRange()
		                                                  .get();
		if (InvolvedPartyExists.isEmpty())
		{
			addy.setEnterpriseID(classification.getEnterpriseID());
			addy.setSystemID((Systems) activityMasterSystem);
			addy.setOriginalSourceSystemID((Systems) activityMasterSystem);
			addy.setActiveFlagID(classification.getActiveFlagID());
			addy.persist();
			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				addy.createDefaultSecurity(activityMasterSystem, identifyingToken);
			}
			addy.add(involvedPartyClassification, value, originatingSystem, identifyingToken);
		}
		else
		{
			addy = InvolvedPartyExists.get();
		}

		J tableForClassification = get(findInvolvedPartyQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) addy, classification.getEnterpriseID())
		                                                         .inActiveRange(classification.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID(classification.getEnterpriseID());
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) activityMasterSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) activityMasterSystem);
			tableForClassification.setActiveFlagID(classification.getActiveFlagID());

			setMyInvolvedPartyLinkValue(tableForClassification, (S) addy, classification.getEnterpriseID());

			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
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


	@SuppressWarnings("unchecked")
	default J add(InvolvedParty addy, IInvolvedPartyClassification<?> iclassification, ISystems originatingSystem, UUID... identifyingToken)
	{
		J tableForClassification = get(findInvolvedPartyQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) addy, originatingSystem.getEnterpriseID())
		                                                         .inActiveRange(addy.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identifyingToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			Classification classification = get(ClassificationService.class).find(iclassification,
			                                                                      originatingSystem.getEnterpriseID(), identifyingToken);

			tableForClassification.setEnterpriseID(addy.getEnterpriseID());
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue("");
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setActiveFlagID(addy.getActiveFlagID());
			setMyInvolvedPartyLinkValue(tableForClassification, (S) addy, addy.getEnterpriseID());
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


	void setMyInvolvedPartyLinkValue(J classificationLink, S involvedParty, IEnterprise<?> enterprise);
}
