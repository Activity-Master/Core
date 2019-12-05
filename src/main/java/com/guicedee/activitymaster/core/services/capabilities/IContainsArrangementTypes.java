package com.guicedee.activitymaster.core.services.capabilities;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IArrangementTypes;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.system.*;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IArrangementsService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;

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
public interface IContainsArrangementTypes<P extends WarehouseCoreTable,
		                                     S extends WarehouseCoreTable,
		                                          Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?,?,?>,
		                                          T extends IArrangementTypes<?>,
		                                          J extends IContainsArrangementTypes<P, S, Q, T, J>>
{
	void configureArrangementType(Q linkTable, P primary, S secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise);

	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findArrangementPartyTypeQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsArrangementTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<P,S,?>> find(T arrangementType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findArrangementPartyTypeQueryRelationshipTableType());
		IArrangementsService arrangementTypeService = get(IArrangementsService.class);
		IArrangementType classification = arrangementTypeService.find((IArrangementTypes<?>) arrangementType, originatingSystem.getEnterpriseID(), identityToken);
		return (Optional<IRelationshipValue<P,S,?>>) activityMasterIdentity.builder()
		                                                               .findLink((P) this, (S) classification, null)
		                                                               .inActiveRange(originatingSystem.getEnterpriseID())
		                                                               .inDateRange()
		                                                               .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                               .get();
	}

	@SuppressWarnings("unchecked")
	default Optional<Q> findFirst(T arrangementType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findArrangementPartyTypeQueryRelationshipTableType());
		IArrangementsService arrangementTypeService = get(IArrangementsService.class);
		IArrangementType classification = arrangementTypeService.find((IArrangementTypes<?>) arrangementType, originatingSystem.getEnterpriseID(), identityToken);

		return (Optional<Q>) activityMasterIdentity.builder()
		                                           .findLink((P) this, (S) classification, null)
		                                           .inActiveRange(originatingSystem.getEnterpriseID())
		                                           .inDateRange()
		                                           .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                           .setReturnFirst(true)
		                                           .get();
	}

	@SuppressWarnings("unchecked")
	default List<Q> findAll(T arrangementType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findArrangementPartyTypeQueryRelationshipTableType());
		IArrangementsService arrangementTypeService = get(IArrangementsService.class);
		IArrangementType classification = arrangementTypeService.find((IArrangementTypes<?>) arrangementType, originatingSystem.getEnterpriseID(), identityToken);
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
		Q activityMasterIdentity = get(findArrangementPartyTypeQueryRelationshipTableType());
		IArrangementsService arrangementTypeService = get(IArrangementsService.class);
		IArrangementType classification = arrangementTypeService.find((IArrangementTypes<?>) classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(originatingSystem.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                             .getCount();
	}

	@SuppressWarnings("unchecked")
	default Q add(IClassificationValue<?> classificationValue, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementPartyTypeQueryRelationshipTableType());

		IArrangementsService classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType iArrangementType = classificationDataConceptService.find(
				(IArrangementTypes<?>) arrangementType, originatingSystem.getEnterpriseID(), identityToken);

		IClassificationService arrangementTypeService = get(IClassificationService.class);
		Classification classification = (Classification) arrangementTypeService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID(classification);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureArrangementType(tableForClassification,(P)this,(S)iArrangementType, classification,value,originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}

		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default Q addOrUpdate(IClassificationValue<?> classificationValue, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementPartyTypeQueryRelationshipTableType());
		IArrangementsService classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType iArrangementType = classificationDataConceptService.find(
				(IArrangementTypes<?>) arrangementType, originatingSystem.getEnterpriseID(), identityToken);

		IClassificationService arrangementTypeService = get(IClassificationService.class);
		Classification classification = (Classification) arrangementTypeService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canCreate(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification = addOrReuse(classificationValue, arrangementType, value, originatingSystem, identityToken);
		}
		else
		{
			tableForClassification = exists.get();
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();

			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag)flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();

			Q newTableForClassification = get(findArrangementPartyTypeQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
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
			configureArrangementType(newTableForClassification,(P)this,(S)iArrangementType, classification,value,originatingSystem.getEnterpriseID());
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
	default Q addOrReuse(IClassificationValue<?> classificationValue, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementPartyTypeQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType iArrangementType = classificationDataConceptService.find(
				(IArrangementTypes<?>) arrangementType, originatingSystem.getEnterpriseID(), identityToken);

		IClassificationService arrangementTypeService = get(IClassificationService.class);
		Classification classification = (Classification) arrangementTypeService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .withEnterprise(originatingSystem.getEnterpriseID())
		                                                         .canCreate(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();

		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureArrangementType(tableForClassification,(P)this,(S)iArrangementType, classification,value,originatingSystem.getEnterpriseID());

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
	default Q add(IClassification<?> classificationValue, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementPartyTypeQueryRelationshipTableType());
		IArrangementsService classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType iArrangementType = classificationDataConceptService.find(
				(IArrangementTypes<?>) arrangementType, originatingSystem.getEnterpriseID(), identityToken);

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID((Classification) classificationValue);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureArrangementType(tableForClassification,(P)this,(S)iArrangementType, classificationValue,value,originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}

		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default Q addOrReuse(IClassification<?> classification, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementPartyTypeQueryRelationshipTableType());
		IArrangementsService classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType iArrangementType = classificationDataConceptService.find(
				(IArrangementTypes<?>) arrangementType, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setClassificationID((Classification) classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureArrangementType(tableForClassification,(P)this,(S)iArrangementType, classification,value,originatingSystem.getEnterpriseID());

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
	default Q addOrUpdate(IClassification<?> classification, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementPartyTypeQueryRelationshipTableType());
		IArrangementsService classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType iArrangementType = classificationDataConceptService.find(
				(IArrangementTypes<?>) arrangementType, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setClassificationID((Classification) classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureArrangementType(tableForClassification,(P)this,(S)iArrangementType, classification,value,originatingSystem.getEnterpriseID());

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

			Q newTableForClassification = get(findArrangementPartyTypeQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
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
			configureArrangementType(newTableForClassification,(P)this,(S)iArrangementType, classification,value,originatingSystem.getEnterpriseID());
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
	default Q update(IClassification<?> classification, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementPartyTypeQueryRelationshipTableType());
		IArrangementsService classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType iArrangementType = classificationDataConceptService.find(
				(IArrangementTypes<?>) arrangementType, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
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

			Q newTableForClassification = get(findArrangementPartyTypeQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
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
			configureArrangementType(newTableForClassification,(P)this,(S)iArrangementType, classification,value,originatingSystem.getEnterpriseID());
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
	default Q expire(T arrangementType, Duration duration,ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementPartyTypeQueryRelationshipTableType());
		IArrangementsService classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType iArrangementType = classificationDataConceptService.find(
				(IArrangementTypes<?>) arrangementType, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
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
	default Q archive(IClassification<?> classification, T arrangementType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementPartyTypeQueryRelationshipTableType());
		IArrangementsService classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType iArrangementType = classificationDataConceptService.find(
				(IArrangementTypes<?>) arrangementType, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .withClassification(classification)
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
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default Q remove(IClassification<?> classification, T arrangementType,  ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementPartyTypeQueryRelationshipTableType());

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) arrangementType, null)
		                                                         .withClassification(classification)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .withClassification(classification)
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
