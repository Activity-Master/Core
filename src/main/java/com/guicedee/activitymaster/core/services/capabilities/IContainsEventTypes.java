package com.guicedee.activitymaster.core.services.capabilities;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.events.EventType;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IEventTypeValue;
import com.guicedee.activitymaster.core.services.system.*;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

import static com.entityassist.SCDEntity.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

@SuppressWarnings("Duplicates")
public interface IContainsEventTypes<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTypesTable<P, S, ?, ? extends QueryBuilderRelationshipClassificationTypes, T, ?, ?, ?, ?>,
		C extends IClassificationValue<?>,
		T extends IEventTypeValue<?>,
		L, R,
		J extends IContainsEventTypes<P, S, Q, C, T, L, R, J>>
{

	default Optional<IRelationshipValue<L, R, ?>> findEventType(T typeValue, C classification, ISystems<?> system, UUID... identityToken)
	{
		return findEventType(typeValue, classification, null, system, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findEventType(T typeValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> service = get(IClassificationService.class);
		IClassification<?> classification = service.find(Classifications.NoClassification, system, identityToken);
		return findEventType(typeValue, (C) classification, null, system, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findEventType(String typeValue, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> service = get(IClassificationService.class);
		IClassification<?> classification = service.find(Classifications.NoClassification, system, identityToken);
		return findEventType(typeValue, classification, value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findEventType(String typeValue, String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventType(typeValue, iClassification, value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findEventType(T typeValue, C classification, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventType(typeValue.classificationValue(), iClassification, value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findEventType(T typeValue, String classification, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventType(typeValue.classificationValue(), iClassification, null, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findEventType(T typeValue, String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventType(typeValue.classificationValue(), iClassification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findEventType(T typeValue, C classification, String value, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventType(typeValue.classificationValue(), iClassification, value, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findEventType(T typeValue, String classification, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventType(typeValue.classificationValue(), iClassification, null, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findEventType(T typeValue, String classification, String searchValue, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventType(typeValue.classificationValue(), iClassification, searchValue, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findEventType(T typeValue, C classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventType(typeValue.classificationValue(), iClassification, value, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findEventType(T typeValue, String classification, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventType(typeValue.classificationValue(), iClassification, null, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findEventType(T typeValue, String classification, String searchValue, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventType(typeValue.classificationValue(), iClassification, searchValue, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findEventTypeFirst(T typeValue, String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventType(typeValue.classificationValue(), iClassification, searchValue, system, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findEventTypeFirst(String typeValue, String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventType(typeValue, iClassification, searchValue, system, true, false, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findEventTypeFirst(T typeValue, C classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventType(typeValue.classificationValue(), iClassification, searchValue, system, true, false, identityToken);
	}

	
	default Optional<IRelationshipValue<L, R, ?>> findEventTypeFirst(T typeValue, C classification, String searchValue, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventType(typeValue.classificationValue(), iClassification, searchValue, system, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findEventType(String typeValue, IClassification<?> classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findEventTypeQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system, identityToken)
				                   .withClassification(classification)
				                   .withValue(searchValue)
				                   .withType(typeValue, system, identityToken)
				                   .inDateRange()
				                   .withEnterprise(system)
				                   .canRead(system, identityToken);
		if (first)
		{ queryBuilderRelationshipClassification.setMaxResults(1); }
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		
		//noinspection rawtypes
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	
	default List<IRelationshipValue<L, R, ?>> findEventTypesAll(T typeValue, C classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventTypesAll(typeValue.classificationValue(), iClassification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findEventTypesAll(T typeValue, C classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventTypesAll(typeValue.classificationValue(), iClassification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findEventTypesAll(T typeValue, String classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventTypesAll(typeValue.classificationValue(), iClassification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findEventTypesAll(T typeValue, String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventTypesAll(typeValue.classificationValue(), iClassification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findEventTypesAll(String typeValue, String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventTypesAll(typeValue, iClassification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findEventTypesAll(String typeValue, String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventTypesAll(typeValue, iClassification, value, system, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findEventTypesAll(String typeValue, String classification, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findEventTypesAll(typeValue, iClassification, null, system, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findEventTypesAll(String typeValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(Classifications.NoClassification, system, identityToken);
		return findEventTypesAll(typeValue, iClassification, null, system, false, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findEventTypesAll(String typeValue, IClassification<?> classification, String searchValue, ISystems<?> system, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findEventTypeQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system, identityToken)
				                   .withType(typeValue, system, identityToken)
				                   .withValue(searchValue)
				                   .withClassification(classification)
				                   .inDateRange()
				                   .canRead(system, identityToken);
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		return (List) queryBuilderRelationshipClassification.getAll();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findEventTypeQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsEventTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findEventTypesPrimaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsEventTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}
	
	default boolean hasEventTypesBefore(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfEventTypesAll(typeValue, classificationValue.classificationName(), system, identityToken) > 0;
	}
	
	default boolean hasEventTypesBefore(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfEventTypesAll(typeValue.classificationValue(), classificationValue.classificationName(), value, system, identityToken) > 0;
	}
	
	default boolean hasEventTypesBefore(T typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfEventTypesAll(typeValue.classificationValue(), classificationValue, value, system, identityToken) > 0;
	}
	
	default boolean hasEventTypes(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfEventTypes(typeValue, classificationValue.classificationName(), system, identityToken) > 0;
	}
	
	default boolean hasEventTypes(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfEventTypes(typeValue, classificationValue, value, system, identityToken) > 0;
	}
	
	
	default long numberOfEventTypes(String typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfEventTypes(typeValue, classificationValue, null, system, identityToken);
	}
	
	default long numberOfEventTypes(T typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfEventTypes(typeValue.classificationValue(), classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfEventTypes(String typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findEventTypesCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, value)
		                             .withType(typeValue, system, identityToken)
		                             .inActiveRange(system)
		                             .inDateRange()
		                             .canRead(system, identityToken)
		                             .getCount();
	}
	
	default long numberOfEventTypes(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfEventTypes(typeValue.classificationValue(), classificationValue.classificationName(), value, system, identityToken);
	}
	
	default long numberOfEventTypesAll(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfEventTypesAll(typeValue.classificationValue(), classificationValue.classificationName(), null, system, identityToken);
	}
	
	default long numberOfEventTypesAll(String typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfEventTypesAll(typeValue, classificationValue, null, system, identityToken);
	}
	
	default long numberOfEventTypesAll(T typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfEventTypesAll(typeValue.classificationValue(), classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfEventTypesAll(String typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findEventTypesCountableQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, value)
		                             .withType(typeValue, system, identityToken)
		                             .getCount();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findEventTypesCountableQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsEventTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	default Q addEventType(IClassificationValue<?> classificationValue, T classificationDataConceptType, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findEventTypeQueryRelationshipTableType());
		
		IEventService<?> classificationDataConceptService = get(IEventService.class);
		EventType classificationDataConcept = (EventType) classificationDataConceptService.findEventType(classificationDataConceptType, system,
		                                                                                                 identityToken);
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setClassificationID(classification);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
		configureEventTypeLinkValue(tableForClassification, (P) this, (S) classificationDataConcept, (C) classificationValue, value, system);
		
		tableForClassification.persist();
		
			tableForClassification.createDefaultSecurity(system, identityToken);
		
		
		return tableForClassification;
	}
	
	void configureEventTypeLinkValue(Q linkTable, P primary, S secondary, C classificationValue, String value, ISystems<?> system);
	
	@SuppressWarnings("unchecked")
	default Q addOrUpdateEventType(IClassificationValue<?> classificationValue, T classificationDataConceptType, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findEventTypeQueryRelationshipTableType());
		IEventService<?> classificationDataConceptService = get(IEventService.class);
		EventType classificationDataConcept = (EventType) classificationDataConceptService.findEventType(classificationDataConceptType, system,
		                                                                                                 identityToken);
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canCreate(system.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification = addOrReuseEventType(classificationValue, classificationDataConceptType, value, system, identityToken);
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
			
			Q newTableForClassification = get(findEventTypeQueryRelationshipTableType());
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
			configureEventTypeLinkValue(newTableForClassification, (P) this, (S) classificationDataConcept, (C) classification, value, system);
			newTableForClassification.persist();
			
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addOrReuseEventType(IClassificationValue<?> classificationValue, T classificationDataConceptType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findEventTypeQueryRelationshipTableType());
		IEventService<?> classificationDataConceptService = get(IEventService.class);
		EventType classificationDataConcept = (EventType) classificationDataConceptService.findEventType(classificationDataConceptType, originatingSystem,
		                                                                                                 identityToken);
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, originatingSystem, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			//			configureInvolvedPartyIdentificationType(tableForClassification,(C) classification, (Q) classificationDataConcept, originatingSystem.getEnterpriseID());
			configureEventTypeLinkValue(tableForClassification, (P) this, (S) classificationDataConcept, (C) classification, value, originatingSystem);
			
			tableForClassification.persist();
		
				tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
			
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addEventType(IClassification<?> classificationValue, T classificationDataConceptType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findEventTypeQueryRelationshipTableType());
		IEventService<?> classificationDataConceptService = get(IEventService.class);
		EventType classificationDataConcept = (EventType) classificationDataConceptService.findEventType(classificationDataConceptType, originatingSystem,
		                                                                                                 identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID((Classification) classificationValue);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureEventTypeLinkValue(tableForClassification, (P) this, (S) classificationDataConcept, (C) classificationValue, value, originatingSystem);
		
		tableForClassification.persist();
	
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		
		
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addOrReuseEventType(IClassification<?> classification, T classificationDataConceptType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findEventTypeQueryRelationshipTableType());
		IEventService<?> classificationDataConceptService = get(IEventService.class);
		EventType classificationDataConcept = (EventType) classificationDataConceptService.findEventType(classificationDataConceptType, originatingSystem,
		                                                                                                 identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			configureEventTypeLinkValue(tableForClassification, (P) this, (S) classificationDataConcept, (C) classification, value, originatingSystem);
			
			tableForClassification.persist();
		
				tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
			
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addOrUpdateEventType(IClassification<?> classification, T classificationDataConceptType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findEventTypeQueryRelationshipTableType());
		IEventService<?> classificationDataConceptService = get(IEventService.class);
		EventType classificationDataConcept = (EventType) classificationDataConceptService.findEventType(classificationDataConceptType, originatingSystem,
		                                                                                                 identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			configureEventTypeLinkValue(tableForClassification, (P) this, (S) classificationDataConcept, (C) classification, value, originatingSystem);
			
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
			
			Q newTableForClassification = get(findEventTypeQueryRelationshipTableType());
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
			configureEventTypeLinkValue(newTableForClassification, (P) this, (S) classificationDataConcept, (C) classification, value, originatingSystem);
			newTableForClassification.persist();
			
			
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q updateEventType(IClassification<?> classification, T classificationDataConceptType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findEventTypeQueryRelationshipTableType());
		IEventService<?> classificationDataConceptService = get(IEventService.class);
		EventType classificationDataConcept = (EventType) classificationDataConceptService.findEventType(classificationDataConceptType, originatingSystem,
		                                                                                                 identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			
			Q newTableForClassification = get(findEventTypeQueryRelationshipTableType());
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
			configureEventTypeLinkValue(newTableForClassification, (P) this, (S) classificationDataConcept, (C) classification, value, originatingSystem);
			newTableForClassification.persist();
			
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			
		}
		return tableForClassification;
	}
	
	
	@SuppressWarnings("unchecked")
	default Q archiveEventType(IClassification<?> classification, T classificationDataConceptType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findEventTypeQueryRelationshipTableType());
		IEventService<?> classificationDataConceptService = get(IEventService.class);
		EventType classificationDataConcept = (EventType) classificationDataConceptService.findEventType(classificationDataConceptType, originatingSystem,
		                                                                                                 identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();
			
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q removeEventType(IClassification<?> classification, T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findEventTypeQueryRelationshipTableType());
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) identificationType, null)
		                                                         .withClassification(classification)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
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
