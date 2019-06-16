package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationship;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.dto.*;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.ITypeValue;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.services.system.IInvolvedPartyService;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.jwebmp.entityassist.SCDEntity.*;
import static com.jwebmp.entityassist.querybuilder.EntityAssistStrings.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

@SuppressWarnings("Duplicates")
public interface IContainsInvolvedPartyTypes<P extends WarehouseCoreTable,
		                                     S extends WarehouseCoreTable,
		                                            Q extends WarehouseRelationshipTable<P, S, ?, ? extends QueryBuilderRelationship, ?, ?>,
		                                            T extends ITypeValue<?>,
		                                            J extends IContainsInvolvedPartyTypes<P, S, Q, T, J>>
{
/*
	@SuppressWarnings("unchecked")
	@CacheResult
	default Optional<J> findType(@CacheKey InvolvedPartyType type, @CacheKey UUID... identityToken)
	{
		J activityMasterIdentity = GuiceContext.get(findInvolvedPartyTypeQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) activityMasterIdentity.builder()
		                                                         .findLink((P) this, (S) type, null)
		                                                         .inActiveRange(type.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(type.getEnterpriseID(), identityToken)
		                                                         .get();
		return exists;
	}
*/

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

/*
	@SuppressWarnings("unchecked")
	default boolean hasType(InvolvedPartyType type, @CacheKey UUID... identityToken)
	{
		J activityMasterIdentity = GuiceContext.get(findInvolvedPartyTypeQueryRelationshipTableType());
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) type, null)
		                             .inActiveRange(type.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(type.getEnterpriseID(), identityToken)
		                             .getCount() > 0;
	}

	@SuppressWarnings("unchecked")
	default J addOrReuse(ITypeValue<?> typeValue, ISystems originatingSystems, String value, UUID... identifyingToken)
	{
		InvolvedPartyType type = GuiceContext.get(InvolvedPartyService.class)
		                                     .findType(typeValue, originatingSystems.getEnterpriseID(),identifyingToken);
		J tableForClassification = GuiceContext.get(findInvolvedPartyTypeQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) type, null)
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
*/

	void configureInvolvedPartyTypeLinkValue(Q linkTable, P primary, S secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise);

	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<?>> find(T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyTypeQueryRelationshipTableType());
		IInvolvedPartyService involvedPartyIdentificationTypeService = get(IInvolvedPartyService.class);
		IInvolvedPartyType<?> classification = involvedPartyIdentificationTypeService.findType(identificationType, originatingSystem.getEnterpriseID(), identityToken);
		return (Optional<IRelationshipValue<?>>) activityMasterIdentity.builder()
		                                                               .findLink((P) this, (S) classification, null)
		                                                               .inActiveRange(originatingSystem.getEnterpriseID())
		                                                               .inDateRange()
		                                                               .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                               .get();
	}

	@SuppressWarnings("unchecked")
	default Optional<Q> findFirst(T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyTypeQueryRelationshipTableType());
		IInvolvedPartyService involvedPartyIdentificationTypeService = get(IInvolvedPartyService.class);
		IInvolvedPartyType<?> classification = involvedPartyIdentificationTypeService.findType(identificationType, originatingSystem.getEnterpriseID(), identityToken);

		return (Optional<Q>) activityMasterIdentity.builder()
		                                           .findLink((P) this, (S) classification, null)
		                                           .inActiveRange(originatingSystem.getEnterpriseID())
		                                           .inDateRange()
		                                           .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                           .setReturnFirst(true)
		                                           .get();
	}

	@SuppressWarnings("unchecked")
	default List<Q> findAll(T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyTypeQueryRelationshipTableType());
		IInvolvedPartyService involvedPartyIdentificationTypeService = get(IInvolvedPartyService.class);
		IInvolvedPartyType<?> classification = involvedPartyIdentificationTypeService.findType(identificationType, originatingSystem.getEnterpriseID(), identityToken);
		return (List<Q>) activityMasterIdentity.builder()
		                                       .findLink((P) this, (S) classification, null)
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
	default Q add( T identificationType, String value, ISystems<?> originatingSystem, UUID... identityToken)
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
	default Q addOrReuse(T identificationType, String value, ISystems<?> originatingSystem, UUID... identityToken)
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
	default Q addOrUpdate( T identificationType, String value, ISystems<?> originatingSystem, UUID... identityToken)
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
			tableForClassification.setActiveFlagID(flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
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
			newTableForClassification.setActiveFlagID(flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
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
	default Q update( T identificationType, String value, ISystems<?> originatingSystem, UUID... identityToken)
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
			tableForClassification.setActiveFlagID(flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
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
			newTableForClassification.setActiveFlagID(flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
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
	default Q archive( T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
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
			tableForClassification.setActiveFlagID(flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default Q remove( T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
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
			tableForClassification.setActiveFlagID(flagService.getDeletedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}

}
