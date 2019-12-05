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
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IResourceItemService;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.entityassist.SCDEntity.*;
import static com.entityassist.querybuilder.EntityAssistStrings.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

@SuppressWarnings("Duplicates")
public interface IContainsResourceItemTypes<P extends WarehouseCoreTable,
		                                           S extends WarehouseCoreTable,
		                                           Q extends WarehouseRelationshipTable<P, S, ?, ? extends QueryBuilderRelationship, ?, ?,?,?>,
		                                           T extends IResourceType<?>,
		                                           J extends IContainsResourceItemTypes<P, S, Q, T, J>>
{

	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findResourceItemTypeQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsResourceItemTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}

	void configureResourceItemTypeLinkValue(Q linkTable, P primary, S secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise);


	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<P,S,?>> find(T resourceType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService involvedPartyIdentificationTypeService = get(IResourceItemService.class);
		IResourceItemType<?> classification = involvedPartyIdentificationTypeService.findResourceItemType(resourceType, originatingSystem, identityToken);
		return (Optional<IRelationshipValue<P,S,?>>) activityMasterIdentity.builder()
		                                                               .findLink((P) this, (S) classification, null)
		                                                               .inActiveRange(originatingSystem.getEnterpriseID())
		                                                               .inDateRange()
		                                                               .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                               .get();
	}

	@SuppressWarnings("unchecked")
	default Optional<Q> findFirst(T resourceType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService involvedPartyIdentificationTypeService = get(IResourceItemService.class);
		IResourceItemType<?> classification = involvedPartyIdentificationTypeService.findResourceItemType(resourceType, originatingSystem, identityToken);

		return (Optional<Q>) activityMasterIdentity.builder()
		                                           .findLink((P) this, (S) classification, null)
		                                           .inActiveRange(originatingSystem.getEnterpriseID())
		                                           .inDateRange()
		                                           .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                           .setReturnFirst(true)
		                                           .get();
	}

	@SuppressWarnings("unchecked")
	default List<Q> findAll(T resourceType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService involvedPartyIdentificationTypeService = get(IResourceItemService.class);
		IResourceItemType<?> classification = involvedPartyIdentificationTypeService.findResourceItemType(resourceType, originatingSystem, identityToken);
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
		Q activityMasterIdentity = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService involvedPartyIdentificationTypeService = get(IResourceItemService.class);
		IResourceItemType<?> classification = involvedPartyIdentificationTypeService.findResourceItemType(classificationValue, originatingSystem, identityToken);

		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(originatingSystem.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                             .getCount();
	}

	@SuppressWarnings("unchecked")
	default Q add( T resourceType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemTypeQueryRelationshipTableType());

		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		IResourceItemType<?> classificationDataConcept = classificationDataConceptService.findResourceItemType(resourceType, originatingSystem, identityToken);

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureResourceItemTypeLinkValue(tableForClassification, (P)this, (S)classificationDataConcept, null, value, originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}

		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default Q addOrReuse(T resourceType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		IResourceItemType<?> classificationDataConcept = classificationDataConceptService.findResourceItemType(resourceType, originatingSystem, identityToken);

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
			configureResourceItemTypeLinkValue(tableForClassification, (P)this, (S)classificationDataConcept, null, value, originatingSystem.getEnterpriseID());

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
	default Q addOrUpdate( T resourceType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		IResourceItemType<?> classificationDataConcept = classificationDataConceptService.findResourceItemType(resourceType, originatingSystem, identityToken);

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
			configureResourceItemTypeLinkValue(tableForClassification, (P)this, (S)classificationDataConcept, null, value, originatingSystem.getEnterpriseID());

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

			Q newTableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
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
			configureResourceItemTypeLinkValue(newTableForClassification, (P)this, (S)classificationDataConcept, null, value, originatingSystem.getEnterpriseID());
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
	default Q update( T resourceType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		IResourceItemType<?> classificationDataConcept = classificationDataConceptService.findResourceItemType(resourceType, originatingSystem, identityToken);

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

			Q newTableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
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
			configureResourceItemTypeLinkValue(newTableForClassification, (P)this, (S)classificationDataConcept, null, value, originatingSystem.getEnterpriseID());
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
	default Q expire(T resourceType, Duration duration, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		IResourceItemType<?> classificationDataConcept = classificationDataConceptService.findResourceItemType(resourceType, originatingSystem, identityToken);

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
			tableForClassification.setEffectiveToDate(LocalDateTime.now()
			                                                       .plus(duration));
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default Q archive( T resourceType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		IResourceItemType<?> classificationDataConcept = classificationDataConceptService.findResourceItemType(resourceType, originatingSystem, identityToken);

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
	default Q remove( T resourceType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		IResourceItemType<?> classificationDataConcept = classificationDataConceptService.findResourceItemType(resourceType, originatingSystem, identityToken);
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
