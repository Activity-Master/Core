package com.guicedee.activitymaster.core.services.capabilities;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderDefault;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.capabilities.bases.ISearchableType;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.events.EventThread;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.INameType;
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

@SuppressWarnings({"Duplicates", "unused"})
public interface IContainsInvolvedPartyNameTypes<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable<?, ? extends QueryBuilderDefault, ?, ?>,
		Q extends WarehouseClassificationRelationshipTypesTable<P, S, ?, ? extends QueryBuilderRelationshipClassificationTypes,T, ?, ?, L, R>,
		C extends IClassification<?>,
		T extends INameType<?>,
		L, R,
		J extends IContainsInvolvedPartyNameTypes<P, S, Q, C,T, L, R, J>>
{
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, C classification, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue, classification, null, system, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue, NoClassification.classificationName(), null, system, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(String typeValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue, NoClassification.classificationName(), value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(String typeValue, String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue, classification, value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, C classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification.getName(), value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, String classification, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification, null, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, C classification, String value, boolean first, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification.getName(), value, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, String classification, boolean first, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification, null, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, String classification, String searchValue, boolean first, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification, searchValue, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, C classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification.getName(), value, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, String classification, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification, null, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, String classification, String searchValue, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification, searchValue, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameTypeFirst(T typeValue, String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification, searchValue, system, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameTypeFirst(String typeValue, String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue, classification, searchValue, system, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameTypeFirst(T typeValue, C classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification.getName(), searchValue, system, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameTypeFirst(T typeValue, C classification, String searchValue, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification.getName(), searchValue, system, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findNameType(String typeValue, String classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findInvolvedPartyNameTypeQueryRelationshipTableType());
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
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(T typeValue, C classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findNameTypesAll(typeValue.classificationValue(), classification.getName(), null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(T typeValue, C classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findNameTypesAll(typeValue.classificationValue(), classification.getName(), value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(T typeValue, String classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findNameTypesAll(typeValue.classificationValue(), classification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(T typeValue, String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findNameTypesAll(typeValue.classificationValue(), classification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(String typeValue, String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findNameTypesAll(typeValue, classification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(String typeValue, String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return findNameTypesAll(typeValue, classification, value, false, system, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(String typeValue, String classification, ISystems<?> system, UUID... identityToken)
	{
		return findNameTypesAll(typeValue, classification, null, system, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(String typeValue, ISystems<?> system, UUID... identityToken)
	{
		return findNameTypesAll(typeValue, NoClassification.classificationName(), null, system, false, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findNameTypesAll(String typeValue, String classification, String searchValue, ISystems<?> system, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findInvolvedPartyNameTypeQueryRelationshipTableType());
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
	default Class<P> findIPNameTypesPrimaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(ISearchableType.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findInvolvedPartyNameTypeQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedPartyNameTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	
	default boolean hasNameTypeBefore(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system.getEnterprise(), identityToken);
		}
		return numberOfAllNameTypes(typeValue, classificationValue.getName(), system, identityToken) > 0;
	}
	
	default boolean hasNameTypeBefore(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system.getEnterprise(), identityToken);
		}
		return numberOfAllNameTypes(typeValue.classificationValue(), classificationValue.getName(), value, system, identityToken) > 0;
	}
	
	default boolean hasNameTypeBefore(T typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAllNameTypes(typeValue.classificationValue(), classificationValue, value, system, identityToken) > 0;
	}
	
	default boolean hasNameType(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system.getEnterprise(), identityToken);
		}
		return numberOfNameType(typeValue, classificationValue.getName(), system, identityToken) > 0;
	}
	
	default boolean hasNameType(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system.getEnterprise(), identityToken);
		}
		return numberOfNameType(typeValue, classificationValue, value, system, identityToken) > 0;
	}
	
	default long numberOfNameType(String typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfNameType(typeValue, classificationValue, null, system, identityToken);
	}
	
	default long numberOfNameType(T typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfNameType(typeValue.classificationValue(), classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfNameType(String typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findIPNameTypeCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, system.getEnterprise(), identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) stringToNameType(typeValue,system,identityToken), value)
		                             .withType(typeValue, system, identityToken)
		                             .inActiveRange(system.getEnterprise())
		                             .inDateRange()
		                             .canRead(system.getEnterprise(), identityToken)
		                             .getCount();
	}
	
	default long numberOfNameType(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system.getEnterprise(), identityToken);
		}
		return numberOfNameType(typeValue.classificationValue(), classificationValue.getName(), value, system, identityToken);
	}
	
	default long numberOfAllNameTypes(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system.getEnterprise(), identityToken);
		}
		return numberOfAllNameTypes(typeValue.classificationValue(), classificationValue.getName(), null, system, identityToken);
	}
	
	default long numberOfAllNameTypes(String typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAllNameTypes(typeValue, classificationValue, null, system, identityToken);
	}
	
	default long numberOfAllNameTypes(T typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAllNameTypes(typeValue.classificationValue(), classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfAllNameTypes(String typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findIPNameTypeCountableQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, system.getEnterprise(), identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) stringToNameType(typeValue,system,identityToken), value)
		                             .withClassification(classification)
		                             .withType(typeValue, system, identityToken)
		                             .getCount();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findIPNameTypeCountableQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedPartyNameTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	T stringToNameType(String typeName, ISystems<?> system, UUID...identityToken);
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findIPNameTypesAddableTableType()
	{
		Type[] genericInterfaces;
		Class<?> currentClass = getClass();
		while (currentClass != Object.class)
		{
			genericInterfaces = currentClass.getGenericInterfaces();
			for (Type genericInterface : genericInterfaces)
			{
				String clazz = genericInterface.getTypeName();
				if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedPartyNameTypes.class.getCanonicalName()))
				{
					Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
					return (Class<Q>) genericTypes[2];
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return null;
	}
	
	default S stringToNameTypeSecondary(String typeName, ISystems<?> system, UUID... identityToken)
	{
		IInvolvedPartyService<?> iInvolvedPartyService = get(IInvolvedPartyService.class);
		return (S) iInvolvedPartyService.createNameType(typeName, typeName,system.getEnterprise(), identityToken);
	}
	
	void configureIPNameTypesAddable(Q linkTable, P primary, S secondary, C classificationValue, T type, String value, ISystems<?> system);
	
	default IRelationshipValue<L, R, ?> addNameType(T typeName, C classificationName, ISystems<?> system, UUID... identityToken)
	{
		return addNameType(typeName.classificationValue(), classificationName.getName(), null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addNameType(T typeName, C classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addNameType(typeName.classificationValue(), classificationName.getName(), value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addNameType(T typeName, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addNameType(typeName.classificationValue(), classificationName, value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addNameType(T typeName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addNameType(typeName.classificationValue(), Classifications.NoClassification.classificationName(), value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addNameType(String typeName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addNameType(typeName, Classifications.NoClassification.classificationName(), value, system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addNameType(String typeName, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addNameType(typeName, classificationName, value, STRING_EMPTY, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addNameType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, ISystems<?> system, UUID... identityToken)
	{
		return addNameType(typeName, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addNameType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, ISystems<?> system, UUID... identityToken)
	{
		return addNameType(typeName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addNameType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, LocalDateTime effectiveToDate, ISystems<?> system, UUID... identityToken)
	{
		return addNameType(typeName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addNameType(@NotNull String type,
	                                                @NotNull String classificationName,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                LocalDateTime effectiveFromDate,
	                                                LocalDateTime effectiveToDate,
	                                                ISystems<?> originalSystemID,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		Q tableForClassification = get(this.findIPNameTypesAddableTableType());
		T typeToUse = this.stringToNameType(type, system, identityToken);
		
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
		
		this.configureIPNameTypesAddable(tableForClassification, (P) this, this.stringToNameTypeSecondary(type, system, identityToken),
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
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull C classificationName,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull C classificationName,
	                                                       String value,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull C classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull C classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull C classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       LocalDateTime effectiveToDate,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull C classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       LocalDateTime effectiveToDate,
	                                                       ISystems<?> originalSystemID,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull String classificationName,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       LocalDateTime effectiveToDate,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       LocalDateTime effectiveToDate,
	                                                       ISystems<?> originalSystemID,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull String type,
	                                                       @NotNull String classificationName,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull String type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull String type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull String type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull String type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       LocalDateTime effectiveToDate,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull String type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       LocalDateTime effectiveToDate,
	                                                       ISystems<?> originalSystemID,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		Q tableForClassification = get(this.findIPNameTypesAddableTableType());
		S sType = this.stringToNameTypeSecondary(type, system, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.findOrCreate(classificationName, system.getEnterprise(), identityToken);
		boolean exists = findIPNameTypeQuery(value, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return (Q) addNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		else
		{
			tableForClassification = (Q) findIPNameTypeQuery(value, system, tableForClassification, sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull String classificationName,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        LocalDateTime effectiveFromDate,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull C classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        LocalDateTime effectiveFromDate,
	                                                        LocalDateTime effectiveToDate,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull C classificationName,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull C classificationName,
	                                                        String value,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull C classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull C classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        LocalDateTime effectiveFromDate,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull String type,
	                                                        @NotNull C classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        LocalDateTime effectiveFromDate,
	                                                        LocalDateTime effectiveToDate,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull String type,
	                                                        @NotNull String classificationName,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull String type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull String type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull String type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        LocalDateTime effectiveFromDate,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull String type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        LocalDateTime effectiveFromDate,
	                                                        LocalDateTime effectiveToDate,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull String type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        LocalDateTime effectiveFromDate,
	                                                        LocalDateTime effectiveToDate,
	                                                        ISystems<?> originalSystemID,
	                                                        @NotNull ISystems<?> system,
	                                                        UUID... identityToken)
	{
		Q tableForClassification = get(this.findIPNameTypesAddableTableType());
		S sType = this.stringToNameTypeSecondary(type, system, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.findOrCreate(classificationName, system.getEnterprise(), identityToken);
		boolean exists = findIPNameTypeQuery(value, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{ return  addNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken); }
		
		tableForClassification = (Q) findIPNameTypeQuery(value, system, tableForClassification, sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return tableForClassification;
		}
		return updateNameType(tableForClassification, type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> updateNameType(@NotNull IRelationshipValue<L, R, ?>  original,
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
		archiveNameType(original, identityToken);
		return addNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> archiveNameType(@NotNull IRelationshipValue<L, R, ?>  original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	default IRelationshipValue<L, R, ?> removeNameType(@NotNull IRelationshipValue<L, R, ?>  original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getDeletedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	@SuppressWarnings("rawtypes")
	default IRelationshipValue<L, R, ?> expireNameType(@NotNull  IRelationshipValue<L, R, ?>  original, Duration when)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		entity.setEffectiveToDate(entity.getEffectiveToDate()
		                                .plus(when));
		QueryBuilderSCD scd = (QueryBuilderSCD) entity.builder();
		scd.update(entity, when);
		return original;
	}
	
	private QueryBuilderRelationship<?, ?, ?, ?, ?, ?> findIPNameTypeQuery(String value, ISystems<?> system, Q tableForClassification, S sType, Classification classification, UUID... identityToken)
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
