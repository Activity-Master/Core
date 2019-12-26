package com.guicedee.activitymaster.core.services.capabilities;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IInvolvedPartyService;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.entityassist.SCDEntity.*;
import static com.entityassist.querybuilder.EntityAssistStrings.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

@SuppressWarnings("Duplicates")
public interface IContainsInvolvedPartyTypes<P extends WarehouseCoreTable,
		                                     S extends WarehouseCoreTable,
		                                            Q extends WarehouseRelationshipTable<P, S, ?, ? extends QueryBuilderRelationship, ?, ?,L,R>,
		                                            T extends ITypeValue<?>,
													L,R,
		                                            J extends IContainsInvolvedPartyTypes<P, S, Q, T,L,R, J>>
{
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findInvolvedPartyTypeQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedPartyTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}

	void configureInvolvedPartyTypeLinkValue(Q linkTable, P primary, S secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise);

	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L,R,?>> find(T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyTypeQueryRelationshipTableType());
		IInvolvedPartyService involvedPartyIdentificationTypeService = get(IInvolvedPartyService.class);
		IInvolvedPartyType<?> classification = involvedPartyIdentificationTypeService.findType(identificationType, originatingSystem.getEnterpriseID(), identityToken);
		return (Optional<IRelationshipValue<L,R,?>>) activityMasterIdentity.builder()
		                                                               .findLink((P) this, (S) classification, null)
		                                                               .inActiveRange(originatingSystem.getEnterpriseID())
		                                                               .inDateRange()
		                                                               .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                               .get();
	}

	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L,R,?>> findFirst(T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyTypeQueryRelationshipTableType());
		IInvolvedPartyService involvedPartyIdentificationTypeService = get(IInvolvedPartyService.class);
		IInvolvedPartyType<?> classification = involvedPartyIdentificationTypeService.findType(identificationType, originatingSystem.getEnterpriseID(), identityToken);

		return (Optional<IRelationshipValue<L,R,?>>) activityMasterIdentity.builder()
		                                           .findLink((P) this, (S) classification, null)
		                                           .inActiveRange(originatingSystem.getEnterpriseID())
		                                           .inDateRange()
		                                           .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                           .setReturnFirst(true)
		                                           .get();
	}

	@SuppressWarnings("unchecked")
	default List<IRelationshipValue<L,R,?>> findAll(T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyTypeQueryRelationshipTableType());
		IInvolvedPartyService involvedPartyIdentificationTypeService = get(IInvolvedPartyService.class);
		IInvolvedPartyType<?> classification = involvedPartyIdentificationTypeService.findType(identificationType, originatingSystem.getEnterpriseID(), identityToken);
		return (List<IRelationshipValue<L,R,?>>) activityMasterIdentity.builder()
		                                       .findLink((P) this, (S) classification, null)
		                                       .inActiveRange(originatingSystem.getEnterpriseID())
		                                       .inDateRange()
		                                       .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                       .getAll();
	}

	@SuppressWarnings("unchecked")
	default List<IRelationshipValue<L, R, ?>> findAllTypes(ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyTypeQueryRelationshipTableType());
		return (List<IRelationshipValue<L, R, ?>>) activityMasterIdentity.builder()
		                                                                 .findParentLink((P) this, null)
		                                                                 .inActiveRange(originatingSystem.getEnterpriseID())
		                                                                 .inDateRange()
		                                                                 .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                                 .getAll();
	}


	default boolean has(T classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return numberOf(classificationValue, originatingSystem, identityToken) > 0;
	}

	@SuppressWarnings("unchecked")
	default long numberOf(T classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyTypeQueryRelationshipTableType());
		IInvolvedPartyService involvedPartyIdentificationTypeService = get(IInvolvedPartyService.class);
		IInvolvedPartyType<?> classification = involvedPartyIdentificationTypeService.findType(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(originatingSystem.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                             .getCount();
	}

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L,R,?> add( T identificationType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyTypeQueryRelationshipTableType());

		IInvolvedPartyService classificationDataConceptService = get(IInvolvedPartyService.class);
		IInvolvedPartyType<?> classificationDataConcept = classificationDataConceptService.findType(identificationType, originatingSystem.getEnterpriseID(), identityToken);

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureInvolvedPartyTypeLinkValue(tableForClassification, (P)this, (S)classificationDataConcept, null, value, originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}

		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L,R,?> addOrReuse(T identificationType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyTypeQueryRelationshipTableType());
		IInvolvedPartyService classificationDataConceptService = get(IInvolvedPartyService.class);
		IInvolvedPartyType<?> classificationDataConcept = classificationDataConceptService.findType(identificationType, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureInvolvedPartyTypeLinkValue(tableForClassification, (P)this, (S)classificationDataConcept, null, value, originatingSystem.getEnterpriseID());

			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					    .isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L,R,?> addOrUpdate( T identificationType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyTypeQueryRelationshipTableType());
		IInvolvedPartyService classificationDataConceptService = get(IInvolvedPartyService.class);
		IInvolvedPartyType<?> classificationDataConcept = classificationDataConceptService.findType(identificationType, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureInvolvedPartyTypeLinkValue(tableForClassification, (P)this, (S)classificationDataConcept, null, value, originatingSystem.getEnterpriseID());

			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					    .isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();

			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag)flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();

			Q newTableForClassification = get(findInvolvedPartyTypeQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag)flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureInvolvedPartyTypeLinkValue(newTableForClassification, (P)this, (S)classificationDataConcept, null, value, originatingSystem.getEnterpriseID());
			newTableForClassification.persist();

			if (get(ActivityMasterConfiguration.class)
					    .isSecurityEnabled())
			{
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			}
		}
		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L,R,?> update( T identificationType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyTypeQueryRelationshipTableType());
		IInvolvedPartyService classificationDataConceptService = get(IInvolvedPartyService.class);
		IInvolvedPartyType<?> classificationDataConcept = classificationDataConceptService.findType(identificationType, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			//do nothing to the object
		}
		else
		{
			tableForClassification = exists.get();
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();

			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag)flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();

			Q newTableForClassification = get(findInvolvedPartyTypeQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag)flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureInvolvedPartyTypeLinkValue(newTableForClassification, (P)this, (S)classificationDataConcept, null, value, originatingSystem.getEnterpriseID());
			newTableForClassification.persist();

			if (get(ActivityMasterConfiguration.class)
					    .isSecurityEnabled())
			{
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			}
		}
		return tableForClassification;
	}


	@SuppressWarnings("unchecked")
	default IRelationshipValue<L,R,?> archive( T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyTypeQueryRelationshipTableType());
		IInvolvedPartyService classificationDataConceptService = get(IInvolvedPartyService.class);
		IInvolvedPartyType<?> classificationDataConcept = classificationDataConceptService.findType(identificationType, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			//do nothing to the object
		}
		else
		{
			tableForClassification = exists.get();
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag)flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L,R,?> remove( T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyTypeQueryRelationshipTableType());

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) identificationType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
		}
		else
		{
			tableForClassification = exists.get();
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag)flagService.getDeletedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}

}
