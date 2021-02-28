package com.guicedee.activitymaster.core.services.capabilities;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IArrangementTypes;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.system.*;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.entityassist.SCDEntity.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

@SuppressWarnings("Duplicates")
public interface IContainsArrangementTypes<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?, ?, ?>,
		T extends IArrangementTypes<?>,
		L, R,
		J extends IContainsArrangementTypes<P, S, Q, T, L, R, J>>
{
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(T classification, ISystems<?> system, UUID... identityToken)
	{
		return findArrangementTypes(classification, null, system, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(T classification, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypes(iClassification, value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(String classification, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypes(iClassification, null, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypes(iClassification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(T classification, String value, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypes(iClassification, value, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(String classification, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypes(iClassification, null, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(String classification, String searchValue, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypes(iClassification, searchValue, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(T classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypes(iClassification, value, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(String classification, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypes(iClassification, null, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(String classification, String searchValue, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypes(iClassification, searchValue, system, first, latest, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypesFirst(T classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypes(iClassification, searchValue, system, true, false, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypesFirst(T classification, String searchValue, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypes(iClassification, searchValue, system, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(IClassification<?> classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findArrangementTypesQueryRelationshipTableType());
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
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(T arrangementType, IClassification<?> classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		IArrangementsService<?> arrangementsService = get(IArrangementsService.class);
		S sType = (S) arrangementsService.find(arrangementType, system, identityToken);
		Q relationshipTable = get(findArrangementTypesQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .findChildLink((S) sType)
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
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(T classification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem, identityToken);
		return findArrangementTypesAll(iClassification, null, originatingSystem, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(T classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypesAll(iClassification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(T classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem, identityToken);
		return findArrangementTypesAll(iClassification, value, originatingSystem, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(T classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypesAll(iClassification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(String classification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem, identityToken);
		return findArrangementTypesAll(iClassification, null, originatingSystem, false, identityToken);
	}
	
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(String classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypesAll(iClassification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(String classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem, identityToken);
		return findArrangementTypesAll(iClassification, value, originatingSystem, false, identityToken);
	}
	
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findArrangementTypesAll(iClassification, value, system, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(IClassification<?> classification, String searchValue, ISystems<?> system, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findArrangementTypesQueryRelationshipTableType());
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
	default Class<Q> findArrangementTypesQueryRelationshipTableType()
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
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findArrangementTypesPrimaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsArrangementTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}
	
	default boolean hasArrangementTypes(T classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return numberOfArrangementTypes(classificationValue, originatingSystem, identityToken) > 0;
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfArrangementTypes(T classificationValue, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> arrangementTypeService = get(IArrangementsService.class);
		IArrangementType<?> classification = arrangementTypeService.find(classificationValue, system, identityToken);
		
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(system.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(system, identityToken)
		                             .getCount();
	}
	
	@SuppressWarnings("unchecked")
	default Q addArrangementTypes(IClassificationValue<?> classificationValue, T arrangementType, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, system, identityToken);
		
		IClassificationService<?> arrangementTypeService = get(IClassificationService.class);
		Classification classification = (Classification) arrangementTypeService.find(classificationValue, system, identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setClassificationID(classification);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
		configureArrangementType(tableForClassification, (P) this, (S) iArrangementType, classification, value, system);
		
		tableForClassification.persist();
		
			tableForClassification.createDefaultSecurity(system, identityToken);
		
		
		return tableForClassification;
	}
	
	void configureArrangementType(Q linkTable, P primary, S secondary, IClassification<?> classificationValue, String value, ISystems<?> system);
	
	@SuppressWarnings("unchecked")
	default Q addOrUpdateArrangementTypes(IClassificationValue<?> classificationValue, T arrangementType, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, system, identityToken);
		
		IClassificationService<?> arrangementTypeService = get(IClassificationService.class);
		Classification classification = (Classification) arrangementTypeService.find(classificationValue, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canCreate(system.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification = addOrReuseArrangementTypes(classificationValue, arrangementType, value, system, identityToken);
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
			
			Q newTableForClassification = get(findArrangementTypesQueryRelationshipTableType());
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
			configureArrangementType(newTableForClassification, (P) this, (S) iArrangementType, classification, value, system);
			newTableForClassification.persist();
			
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addOrReuseArrangementTypes(IClassificationValue<?> classificationValue, T arrangementType, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, system, identityToken);
		
		IClassificationService<?> arrangementTypeService = get(IClassificationService.class);
		Classification classification = (Classification) arrangementTypeService.find(classificationValue, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
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
			configureArrangementType(tableForClassification, (P) this, (S) iArrangementType, classification, value, system);
			
			tableForClassification.persist();
		
				tableForClassification.createDefaultSecurity(system, identityToken);
			
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addArrangementTypes(IClassification<?> classificationValue, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, originatingSystem, identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID((Classification) classificationValue);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureArrangementType(tableForClassification, (P) this, (S) iArrangementType, classificationValue, value, originatingSystem);
		
		tableForClassification.persist();
	
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		
		
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addOrReuseArrangementTypes(IClassification<?> classification, T arrangementType, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
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
			configureArrangementType(tableForClassification, (P) this, (S) iArrangementType, classification, value, system);
			
			tableForClassification.persist();
			
				tableForClassification.createDefaultSecurity(system, identityToken);
			
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addOrUpdateArrangementTypes(IClassification<?> classification, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, originatingSystem, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
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
			configureArrangementType(tableForClassification, (P) this, (S) iArrangementType, classification, value, originatingSystem);
			
			tableForClassification.persist();
		
				tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
			
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
			
			Q newTableForClassification = get(findArrangementTypesQueryRelationshipTableType());
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
			configureArrangementType(newTableForClassification, (P) this, (S) iArrangementType, classification, value, originatingSystem);
			newTableForClassification.persist();
			
		
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q updateArrangementTypes(IClassification<?> classification, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, originatingSystem, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
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
			
			Q newTableForClassification = get(findArrangementTypesQueryRelationshipTableType());
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
			configureArrangementType(newTableForClassification, (P) this, (S) iArrangementType, classification, value, originatingSystem);
			newTableForClassification.persist();
			
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q expireArrangementTypes(T arrangementType, Duration duration, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, originatingSystem, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
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
	default Q archiveArrangementTypes(IClassification<?> classification, T arrangementType, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
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
	default Q removeArrangementTypes(IClassification<?> classification, T arrangementType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		
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
