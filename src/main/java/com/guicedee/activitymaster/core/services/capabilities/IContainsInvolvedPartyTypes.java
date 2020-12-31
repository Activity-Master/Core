package com.guicedee.activitymaster.core.services.capabilities;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.events.EventThread;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.IInvolvedPartyService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.entityassist.SCDEntity.*;
import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
import static com.guicedee.activitymaster.core.services.classifications.events.EventInvolvedPartiesClassifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

@SuppressWarnings("Duplicates")
public interface IContainsInvolvedPartyTypes<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTypesTable<P, S, ?, ? extends QueryBuilderRelationshipClassificationTypes,T, ?, ?, L, R>,
		C extends IClassification<?>,
		T extends ITypeValue<?>,
		L, R,
		J extends IContainsInvolvedPartyTypes<P, S, Q, C,T, L, R, J>>
{
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, C classification, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue, classification, null, system, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue, NoClassification.classificationName(), null, system, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(String typeValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue, NoClassification.classificationName(), value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(String typeValue, String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue, classification, value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, C classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification.getName(), value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, String classification, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification, null, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, C classification, String value, boolean first, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification.getName(), value, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, String classification, boolean first, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification, null, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, String classification, String searchValue, boolean first, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification, searchValue, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, C classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification.getName(), value, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, String classification, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification, null, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, String classification, String searchValue, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification, searchValue, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findFirst(T typeValue, String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification, searchValue, system, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findFirst(String typeValue, String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue, classification, searchValue, system, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findFirst(T typeValue, C classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification.getName(), searchValue, system, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findFirst(T typeValue, C classification, String searchValue, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification.getName(), searchValue, system, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> find(String typeValue, String classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findInvolvedPartyTypeQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system.getEnterprise(), identityToken)
				                   .withClassification(iClassification)
				                   .withValue(searchValue)
				                   .withType(typeValue, system, identityToken)
				                   .inDateRange()
				                   .withEnterprise(system.getEnterprise())
				                   .canRead(system.getEnterprise(), identityToken);
		if (first)
		{ queryBuilderRelationshipClassification.setMaxResults(1); }
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		
		//noinspection rawtypes
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(T typeValue, C classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findAll(typeValue.classificationValue(), classification.getName(), null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(T typeValue, C classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findAll(typeValue.classificationValue(), classification.getName(), value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(T typeValue, String classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findAll(typeValue.classificationValue(), classification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(T typeValue, String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findAll(typeValue.classificationValue(), classification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(String typeValue, String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findAll(typeValue, classification, value, system,latest,identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(String typeValue, String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return findAll(typeValue, classification, value,false, system, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(String typeValue, String classification, ISystems<?> system, UUID... identityToken)
	{
		return findAll(typeValue, classification, null, system, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(String typeValue, ISystems<?> system, UUID... identityToken)
	{
		return findAll(typeValue, NoClassification.classificationName(), null, system, false, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findAll(String typeValue, String classification, String searchValue, ISystems<?> system, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findInvolvedPartyTypeQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system.getEnterprise(), identityToken)
				                   .withType(typeValue, system, identityToken)
				                   .withValue(searchValue)
				                   .withClassification(iClassification)
				                   .inDateRange()
				                   .canRead(system.getEnterprise(), identityToken);
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		return (List) queryBuilderRelationshipClassification.getAll();
	}
	
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
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findIPTypesPrimaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedPartyTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}

	default boolean hasTypesBefore(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system.getEnterprise(), identityToken);
		}
		return numberOfTypesAll(typeValue, classificationValue.getName(), system, identityToken) > 0;
	}
	
	default boolean hasTypesBefore(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system.getEnterprise(), identityToken);
		}
		return numberOfTypesAll(typeValue.classificationValue(), classificationValue.getName(), value, system, identityToken) > 0;
	}
	
	default boolean hasTypesBefore(T typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfTypesAll(typeValue.classificationValue(), classificationValue, value, system, identityToken) > 0;
	}
	
	default boolean hasType(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system.getEnterprise(), identityToken);
		}
		return numberOfType(typeValue, classificationValue.getName(), system, identityToken) > 0;
	}
	
	default boolean hasType(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		
		return numberOfType(typeValue, classificationValue, value, system, identityToken) > 0;
	}
	
	
	default long numberOfType(String typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfType(typeValue, classificationValue, null, system, identityToken);
	}
	
	default long numberOfType(T typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfType(typeValue.classificationValue(), classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfType(String typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findIPTypesCountableQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, system.getEnterprise(), identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) stringToIPTypesType(typeValue,system,identityToken), value)
		                             .withClassification(classification)
		                             .withType(typeValue, system, identityToken)
		                             .inActiveRange(system.getEnterprise())
		                             .inDateRange()
		                             .canRead(system.getEnterprise(), identityToken)
		                             .getCount();
	}
	
	default long numberOfType(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system.getEnterprise(), identityToken);
		}
		return numberOfType(typeValue.classificationValue(), classificationValue.getName(), value, system, identityToken);
	}
	
	default long numberOfTypesAll(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system.getEnterprise(), identityToken);
		}
		return numberOfTypesAll(typeValue.classificationValue(), classificationValue.getName(), null, system, identityToken);
	}
	
	default long numberOfTypesAll(String typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfTypesAll(typeValue, classificationValue, null, system, identityToken);
	}
	
	default long numberOfTypesAll(T typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfTypesAll(typeValue.classificationValue(), classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfTypesAll(String typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findIPTypesCountableQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, system.getEnterprise(), identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) stringToIPTypesType(typeValue, system, identityToken), value)
		                             .withClassification(classification)
		                             .withType(typeValue, system, identityToken)
		                             .getCount();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findIPTypesCountableQueryRelationshipTableType()
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
	
	T stringToIPTypesType(String typeName, ISystems<?> system, UUID...identityToken);
	
	default S stringToIPTypesSecondary(String typeName, ISystems<?> system, UUID... identityToken)
	{
		IInvolvedPartyService<?> service = GuiceContext.get(IInvolvedPartyService.class);
		IInvolvedPartyIdentificationType<?> type = service.createIdentificationType(system.getEnterprise(), typeName, typeName, identityToken);
		return (S) type;
	}
	
	void configureIPTypesAddable(Q linkTable, P primary, S secondary, C classificationValue, T type, String value, ISystems<?> system);
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findIPTypesAddableTableType()
	{
		Type[] genericInterfaces;
		Class<?> currentClass = getClass();
		while (currentClass != Object.class)
		{
			genericInterfaces = currentClass.getGenericInterfaces();
			for (Type genericInterface : genericInterfaces)
			{
				String clazz = genericInterface.getTypeName();
				if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedPartyTypes.class.getCanonicalName()))
				{
					Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
					return (Class<Q>) genericTypes[2];
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return null;
	}

	
	default IRelationshipValue<L, R, ?> addType(T typeName, C classificationName, ISystems<?> system, UUID... identityToken)
	{
		return addType(typeName.classificationValue(), classificationName.getName(), null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addType(T typeName, C classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addType(typeName.classificationValue(), classificationName.getName(), value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addType(T typeName, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addType(typeName.classificationValue(), classificationName, value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addType(T typeName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addType(typeName.classificationValue(), Classifications.NoClassification.classificationName(), value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addType(String typeName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addType(typeName, Classifications.NoClassification.classificationName(), value, system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addType(String typeName, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addType(typeName, classificationName, value, STRING_EMPTY, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, ISystems<?> system, UUID... identityToken)
	{
		return addType(typeName, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, ISystems<?> system, UUID... identityToken)
	{
		return addType(typeName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, LocalDateTime effectiveToDate, ISystems<?> system, UUID... identityToken)
	{
		return addType(typeName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addType(@NotNull String type,
	                                            @NotNull String classificationName,
	                                            String value,
	                                            String originalSourceSystemUniqueID,
	                                            LocalDateTime effectiveFromDate,
	                                            LocalDateTime effectiveToDate,
	                                            ISystems<?> originalSystemID,
	                                            @NotNull ISystems<?> system,
	                                            UUID... identityToken)
	{
		Q tableForClassification = get(this.findIPTypesAddableTableType());
		T typeToUse = stringToIPTypesType(type, system, identityToken);
		
		IClassificationService<?> classificationService = GuiceContext.get(IClassificationService.class);
		IClassification<?> classification = classificationService.findOrCreate(classificationName, system.getEnterprise(), identityToken);
		
		
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setValue(Strings.nullToEmpty(value));
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemUniqueID(originalSourceSystemUniqueID);
		tableForClassification.setEffectiveFromDate(effectiveFromDate);
		tableForClassification.setEffectiveToDate(effectiveToDate);
		tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
		tableForClassification.setClassificationID((Classification) classification);
		
		this.configureIPTypesAddable(tableForClassification, (P) this, this.stringToIPTypesSecondary(type, system, identityToken),
		                             (C) classification, typeToUse, value, system);
		
		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				.isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(system, identityToken);
		}
		if (EventThread.event.get() != null)
		{
			EventThread.event.get()
			                 .add((IEventClassification<?>) Created, type + " - " + classificationName + " - " + value, system, identityToken);
		}
		return (IRelationshipValue<L, R, ?>) tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull T type,
	                                                   @NotNull C classificationName,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull T type,
	                                                   @NotNull C classificationName,
	                                                   String value,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull T type,
	                                                   @NotNull C classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull T type,
	                                                   @NotNull C classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   LocalDateTime effectiveFromDate,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull T type,
	                                                   @NotNull C classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   LocalDateTime effectiveFromDate,
	                                                   LocalDateTime effectiveToDate,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull T type,
	                                                   @NotNull C classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   LocalDateTime effectiveFromDate,
	                                                   LocalDateTime effectiveToDate,
	                                                   ISystems<?> originalSystemID,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull T type,
	                                                   @NotNull String classificationName,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull T type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull T type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull T type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   LocalDateTime effectiveFromDate,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull T type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   LocalDateTime effectiveFromDate,
	                                                   LocalDateTime effectiveToDate,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull T type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   LocalDateTime effectiveFromDate,
	                                                   LocalDateTime effectiveToDate,
	                                                   ISystems<?> originalSystemID,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull String type,
	                                                   @NotNull String classificationName,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull String type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull String type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull String type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   LocalDateTime effectiveFromDate,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull String type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   LocalDateTime effectiveFromDate,
	                                                   LocalDateTime effectiveToDate,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuseType(@NotNull String type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   LocalDateTime effectiveFromDate,
	                                                   LocalDateTime effectiveToDate,
	                                                   ISystems<?> originalSystemID,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		Q tableForClassification = get(this.findIPTypesAddableTableType());
		S sType = this.stringToIPTypesSecondary(type, system, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.findOrCreate(classificationName, system.getEnterprise(), identityToken);
		boolean exists = findIPTypeTypeQuery(value, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return (Q) addType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		else
		{
			tableForClassification = (Q) findIPTypeTypeQuery(value, system, tableForClassification, sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull T type,
	                                                    @NotNull String classificationName,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type, classificationName, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull T type,
	                                                    @NotNull String classificationName,
	                                                    String value,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type, classificationName, value, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull T type,
	                                                    @NotNull String classificationName,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull T type,
	                                                    @NotNull String classificationName,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    LocalDateTime effectiveFromDate,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull T type,
	                                                    @NotNull C classificationName,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    LocalDateTime effectiveFromDate,
	                                                    LocalDateTime effectiveToDate,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull T type,
	                                                    @NotNull C classificationName,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type, classificationName, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull T type,
	                                                    @NotNull C classificationName,
	                                                    String value,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type, classificationName, value, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull T type,
	                                                    @NotNull C classificationName,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull T type,
	                                                    @NotNull C classificationName,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    LocalDateTime effectiveFromDate,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull String type,
	                                                    @NotNull C classificationName,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    LocalDateTime effectiveFromDate,
	                                                    LocalDateTime effectiveToDate,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type, classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull String type,
	                                                    @NotNull String classificationName,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type, classificationName, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull String type,
	                                                    @NotNull String classificationName,
	                                                    String value,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type, classificationName, value, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull String type,
	                                                    @NotNull String classificationName,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull String type,
	                                                    @NotNull String classificationName,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    LocalDateTime effectiveFromDate,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull String type,
	                                                    @NotNull String classificationName,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    LocalDateTime effectiveFromDate,
	                                                    LocalDateTime effectiveToDate,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateType(@NotNull String type,
	                                                    @NotNull String classificationName,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    LocalDateTime effectiveFromDate,
	                                                    LocalDateTime effectiveToDate,
	                                                    ISystems<?> originalSystemID,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		Q tableForClassification = get(this.findIPTypesAddableTableType());
		S sType = this.stringToIPTypesSecondary(type, system, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.findOrCreate(classificationName, system.getEnterprise(), identityToken);
		boolean exists = findIPTypeTypeQuery(value, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{ return  addType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken); }
		
		tableForClassification = (Q) findIPTypeTypeQuery(value, system, tableForClassification, sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return tableForClassification;
		}
		return updateType(tableForClassification, type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> updateType(@NotNull IRelationshipValue<L, R, ?>  original,
	                                               @NotNull String type,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               LocalDateTime effectiveFromDate,
	                                               LocalDateTime effectiveToDate,
	                                               ISystems<?> originalSystemID,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		archiveType(original, identityToken);
		return addType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> archiveType(@NotNull IRelationshipValue<L, R, ?>  original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	default IRelationshipValue<L, R, ?> removeType(@NotNull IRelationshipValue<L, R, ?>  original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getDeletedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	@SuppressWarnings("rawtypes")
	default IRelationshipValue<L, R, ?> expireType(@NotNull  IRelationshipValue<L, R, ?>  original, Duration when)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		entity.setEffectiveToDate(entity.getEffectiveToDate()
		                                .plus(when));
		QueryBuilderSCD scd = (QueryBuilderSCD) entity.builder();
		scd.update(entity, when);
		return original;
	}
	
	private QueryBuilderRelationship<?, ?, ?, ?, ?, ?> findIPTypeTypeQuery(String value, ISystems<?> system, Q tableForClassification, S sType, Classification classification, UUID... identityToken)
	{
		return tableForClassification.builder()
		                             .findLink((P) this, sType, value)
		                             .inActiveRange(system)
		                             .inDateRange()
		                             .withClassification(classification)
		                             .withEnterprise(system)
		                             .canCreate(system, identityToken);
	}
	
	
	
	
}
