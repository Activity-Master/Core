package com.guicedee.activitymaster.core.services.capabilities;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IArrangementsService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.entityassist.SCDEntity.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

@SuppressWarnings("Duplicates")
public interface IContainsArrangements<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?, ?, ?>,
		T extends IArrangement<?>,
		L, R,
		J extends IContainsArrangements<P, S, Q, T, L, R, J>>
{
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangement(String classification, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangement(iClassification, null, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangement(String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangement(iClassification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangement(String classification, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangement(iClassification, null, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangement(String classification, String searchValue, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangement(iClassification, searchValue, system, first, false, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangement(String classification, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangement(iClassification, null, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangement(String classification, String searchValue, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangement(iClassification, searchValue, system, first, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findArrangement(IClassification<?> classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findArrangementQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system, identityToken)
				                   .withClassification(classification)
				                   .withValue(searchValue)
				                   .inDateRange()
				                   .withEnterprise(system)
				                   .canRead(system, identityToken);
		if (first)
		{
			queryBuilderRelationshipClassification.setMaxResults(1);
		}
		if (latest)
		{
			queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate"));
		}
		
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findArrangement(T arrangementType, IClassification<?> classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		IArrangementsService<?> arrangementsService = get(IArrangementsService.class);
		Q relationshipTable = get(findArrangementQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .findChildLink((S) arrangementType)
				                   .inActiveRange(system, identityToken)
				                   .withClassification(classification)
				                   .withValue(searchValue)
				                   .inDateRange()
				                   .withEnterprise(system)
				                   .canRead(system, identityToken);
		if (first)
		{
			queryBuilderRelationshipClassification.setMaxResults(1);
		}
		if (latest)
		{
			queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate"));
		}
		
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementAll(String classification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem, identityToken);
		return findArrangementAll(iClassification, null, originatingSystem, false, identityToken);
	}
	
	
	default List<IRelationshipValue<L, R, ?>> findArrangementAll(String classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementAll(iClassification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementAll(String classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem, identityToken);
		return findArrangementAll(iClassification, value, originatingSystem, false, identityToken);
	}
	
	
	default List<IRelationshipValue<L, R, ?>> findArrangementAll(String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementAll(iClassification, value, system, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findArrangementAll(IClassification<?> classification, String searchValue, ISystems<?> system, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findArrangementQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system, identityToken)
				                   .withValue(searchValue)
				                   .withClassification(classification)
				                   .inDateRange()
				                   .canRead(system, identityToken);
		if (latest)
		{
			queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate"));
		}
		return (List) queryBuilderRelationshipClassification.getAll();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findArrangementQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsArrangements.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findArrangementPrimaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsArrangements.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}
	
	default boolean hasArrangement(T classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return numberOfArrangement(classificationValue, originatingSystem, identityToken) > 0;
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfArrangement(T classificationValue, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findArrangementQueryRelationshipTableType());
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classificationValue, null)
		                             .inActiveRange(system.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(system, identityToken)
		                             .getCount();
	}
	
	@SuppressWarnings("unchecked")
	default Q addArrangement(IClassificationValue<?> classificationValue, T arrangementType, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementQueryRelationshipTableType());
		
		IArrangementsService<?> arrangementService = get(IArrangementsService.class);
	
		IClassificationService<?> arrangementTypeService = get(IClassificationService.class);
		Classification classification = (Classification) arrangementTypeService.find(classificationValue, system, identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setClassificationID(classification);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
		configureArrangementType(tableForClassification, (P) this, (S) arrangementType, classification, value, system);
		
		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				.isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(system, identityToken);
		}
		
		return tableForClassification;
	}
	
	void configureArrangementType(Q linkTable, P primary, S secondary, IClassification<?> classificationValue, String value, ISystems<?> system);
	
	@SuppressWarnings("unchecked")
	default Q addOrUpdateArrangement(IClassificationValue<?> classificationValue, T arrangementType, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementQueryRelationshipTableType());
		IArrangementsService<?> arrangementService = get(IArrangementsService.class);

		IClassificationService<?> arrangementTypeService = get(IClassificationService.class);
		Classification classification = (Classification) arrangementTypeService.find(classificationValue, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) arrangementType, value)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canCreate(system.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification = addOrReuseArrangement(classificationValue, arrangementType, value, system, identityToken);
		}
		else
		{
			tableForClassification = exists.get();
			if (Strings.nullToEmpty(value)
			           .equals(exists.get()
			                         .getValue()))
			{
				return exists.get();
			}
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();
			
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(system.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
			
			Q newTableForClassification = get(findArrangementQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) system);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(StartOfTime);
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			configureArrangementType(newTableForClassification, (P) this, (S) arrangementType, classification, value, system);
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
	default Q addOrReuseArrangement(IClassificationValue<?> classificationValue, T arrangementType, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementQueryRelationshipTableType());
		IArrangementsService<?> arrangementService = get(IArrangementsService.class);
		
		IClassificationService<?> arrangementTypeService = get(IClassificationService.class);
		Classification classification = (Classification) arrangementTypeService.find(classificationValue, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) arrangementType, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .withEnterprise(system.getEnterpriseID())
		                                                         .canCreate(system.getEnterpriseID(), identityToken)
		                                                         .get();
		
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
			configureArrangementType(tableForClassification, (P) this, (S) arrangementType, classification, value, system);
			
			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(system, identityToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addArrangement(IClassification<?> classificationValue, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementQueryRelationshipTableType());
		IArrangementsService<?> arrangementService = get(IArrangementsService.class);
		
		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID((Classification) classificationValue);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureArrangementType(tableForClassification, (P) this, (S) arrangementType, classificationValue, value, originatingSystem);
		
		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				.isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}
		
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addOrReuseArrangement(IClassification<?> classification, T arrangementType, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementQueryRelationshipTableType());
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) arrangementType, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canRead(system, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			tableForClassification.setClassificationID((Classification) classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
			configureArrangementType(tableForClassification, (P) this, (S) arrangementType, classification, value, system);
			
			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(system, identityToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addOrUpdateArrangement(IClassification<?> classification, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementQueryRelationshipTableType());
		IArrangementsService<?> arrangementService = get(IArrangementsService.class);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) arrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canRead(originatingSystem, identityToken)
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
			configureArrangementType(tableForClassification, (P) this, (S) arrangementType, classification, value, originatingSystem);
			
			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
			}
		}
		else
		{
			if (Strings.nullToEmpty(value)
			           .equals(exists.get()
			                         .getValue()))
			{
				return exists.get();
			}
			tableForClassification = exists.get();
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();
			
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
			
			Q newTableForClassification = get(findArrangementQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(StartOfTime);
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureArrangementType(newTableForClassification, (P) this, (S) arrangementType, classification, value, originatingSystem);
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
	default Q updateArrangement(IClassification<?> classification, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementQueryRelationshipTableType());
		IArrangementsService<?> arrangementService = get(IArrangementsService.class);
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) arrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canRead(originatingSystem, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			//do nothing to the object
		}
		else
		{
			tableForClassification = exists.get();
			if (Strings.nullToEmpty(value)
			           .equals(exists.get()
			                         .getValue()))
			{
				return exists.get();
			}
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();
			
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
			
			Q newTableForClassification = get(findArrangementQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(StartOfTime);
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureArrangementType(newTableForClassification, (P) this, (S) arrangementType, classification, value, originatingSystem);
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
	default Q expireArrangement(T arrangementType, Duration duration, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementQueryRelationshipTableType());
		IArrangementsService<?> arrangementService = get(IArrangementsService.class);
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) arrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem, identityToken)
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
	default Q archiveArrangement(IClassification<?> classification, T arrangementType, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementQueryRelationshipTableType());
		IArrangementsService<?> arrangementService = get(IArrangementsService.class);
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) arrangementType, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .withClassification(classification)
		                                                         .inDateRange()
		                                                         .canRead(system, identityToken)
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
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(system.getEnterpriseID(), identityToken));
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q removeArrangement(IClassification<?> classification, T arrangementType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementQueryRelationshipTableType());
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) arrangementType, null)
		                                                         .withClassification(classification)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .withClassification(classification)
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
		}
		else
		{
			tableForClassification = exists.get();
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getDeletedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}
}
