package com.guicedee.activitymaster.core.services.capabilities;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.services.classifications.involvedparty.IInvolvedPartyClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.guicedee.guicedinjection.GuiceContext.*;

public interface IContainsInvolvedParties<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?, ?, ?>,
		T extends IClassificationValue<?>,
		L, R,
		J extends IContainsInvolvedParties<P, S, Q, T, L, R, J>>
{
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(T addressClassification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return findInvolvedParty(addressClassification, null, originatingSystem, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(T classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findInvolvedParty(classification, null, enterprise, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(T classification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedParty(iClassification, value, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(T classification, String searchValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		
		return findInvolvedParty(iClassification, searchValue, iClassification.getEnterprise(), false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(String classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedParty(iClassification, null, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(String classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedParty(iClassification, searchValue, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(T classification, String value, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedParty(iClassification, value, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(String classification, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedParty(iClassification, null, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(String classification, String searchValue, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedParty(iClassification, searchValue, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(T classification, String value, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedParty(iClassification, value, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(String classification, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedParty(iClassification, null, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(String classification, String searchValue, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedParty(iClassification, searchValue, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedPartyFirst(T classification, String searchValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findInvolvedParty(iClassification, searchValue, originatingSystem.getEnterprise(), true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedPartyFirst(T classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedParty(iClassification, searchValue, enterprise, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedPartyFirst(T classification, String searchValue, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findInvolvedParty(iClassification, searchValue, originatingSystem.getEnterprise(), true, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedPartyFirst(T classification, String searchValue, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedParty(iClassification, searchValue, enterprise, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(IClassification<?> classification, String searchValue, IEnterprise<?> enterprise, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(this.findInvolvedPartyQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(enterprise, identityToken)
				                   .withClassification(classification)
				                   .withValue(searchValue)
				                   .inDateRange()
				                   .withEnterprise(enterprise)
				                   .canRead(enterprise, identityToken);
		if (first)
		{ queryBuilderRelationshipClassification.setMaxResults(1); }
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		
		//noinspection rawtypes
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(T classification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findInvolvedPartyAll(iClassification, null, originatingSystem.getEnterprise(), false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(T classification, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findInvolvedPartyAll(iClassification, null, originatingSystem.getEnterprise(), latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(T classification, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedPartyAll(iClassification, null, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(T classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findInvolvedPartyAll(iClassification, value, originatingSystem.getEnterprise(), false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(T classification, String value, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findInvolvedPartyAll(iClassification, value, originatingSystem.getEnterprise(), latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(T classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedPartyAll(iClassification, value, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(String classification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findInvolvedPartyAll(iClassification, null, originatingSystem.getEnterprise(), false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(String classification, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findInvolvedPartyAll(iClassification, null, originatingSystem.getEnterprise(), latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(String classification, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedPartyAll(iClassification, null, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(String classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findInvolvedPartyAll(iClassification, value, originatingSystem.getEnterprise(), false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(String classification, String value, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findInvolvedPartyAll(iClassification, value, originatingSystem.getEnterprise(), latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(String classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findInvolvedPartyAll(iClassification, value, enterprise, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(IClassification<?> classification, String searchValue, IEnterprise<?> enterprise, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(this.findInvolvedPartyQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(enterprise, identityToken)
				                   .withValue(searchValue)
				                   .withClassification(classification)
				                   .inDateRange()
				                   .canRead(enterprise, identityToken);
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		return (List) queryBuilderRelationshipClassification.getAll();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findInvolvedPartyQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedParties.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findInvolvedPartyPrimaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedParties.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	default boolean hasInvolvedParty(IInvolvedPartyClassification<?> InvolvedPartyClassification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyQueryRelationshipTableType());
		ISystems<?> activityMasterSystem = get(ISystemsService.class)
				.getActivityMaster(enterprise);
		Classification classification = (Classification) GuiceContext.get(ClassificationService.class)
		                                                             .find(InvolvedPartyClassification,
		                                                                   activityMasterSystem.getEnterpriseID(), identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(classification.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(classification.getEnterpriseID(), identityToken)
		                             .getCount() > 0;
	}
	
	@SuppressWarnings("unchecked")
	default Q addInvolvedParty(IInvolvedPartyClassification<?> involvedPartyClassification, ISystems<?> originatingSystem, String value, UUID... identifyingToken)
	{
		ISystems<?> activityMasterSystem = get(ISystemsService.class)
				.getActivityMaster(originatingSystem.getEnterpriseID(), identifyingToken);
		
		Classification classification = (Classification) get(ClassificationService.class).find(involvedPartyClassification,
		                                                                                       originatingSystem.getEnterpriseID(), identifyingToken);
		
		InvolvedParty addy = new InvolvedParty();
		Optional<InvolvedParty> InvolvedPartyExists = addy.builder()
		                                                  .hasClassification(classification, value)
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
		
		Q tableForClassification = get(findInvolvedPartyQueryRelationshipTableType());
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) addy, value)
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
			
			setMyInvolvedPartyLinkValue(tableForClassification, (P) this, (S) addy, classification.getEnterpriseID());
			
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
	default Q addInvolvedParty(IInvolvedParty<?> addy, IInvolvedPartyClassification<?> iclassification, String value, ISystems<?> originatingSystem, UUID... identifyingToken)
	{
		Q tableForClassification = get(findInvolvedPartyQueryRelationshipTableType());
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) addy, value)
		                                                         .inActiveRange(addy.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identifyingToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			Classification classification = (Classification) get(ClassificationService.class).find(iclassification,
			                                                                                       originatingSystem.getEnterpriseID(), identifyingToken);
			
			tableForClassification.setEnterpriseID((Enterprise) addy.getEnterpriseID());
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue(value);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setActiveFlagID((ActiveFlag) addy.getActiveFlagID());
			setMyInvolvedPartyLinkValue(tableForClassification, (P) this, (S) addy, addy.getEnterpriseID());
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
	
	
	void setMyInvolvedPartyLinkValue(Q classificationLink, P first, S involvedParty, IEnterprise<?> enterprise);
}
