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
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.events.EventThread;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IIdentificationType;
import com.guicedee.activitymaster.core.services.security.Passwords;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.IInvolvedPartyService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
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
public interface IContainsInvolvedPartyIdentificationTypes<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable<?, ? extends QueryBuilderDefault, ?, ?>,
		Q extends WarehouseClassificationRelationshipTypesTable<P, S, ?, ? extends QueryBuilderRelationshipClassificationTypes,T, ?, ?, L, R>,
		C extends IClassification<?>,
		T extends IIdentificationType<?>,
		L, R,
		J extends IContainsInvolvedPartyIdentificationTypes<P, S, Q,C, T, L, R, J>>
{
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, C classification, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue, classification, null, system, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue, NoClassification.classificationName(), null, system, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(String typeValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue, NoClassification.classificationName(), value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(String typeValue, String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue, classification, value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, C classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification.getName(), value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, String classification, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification, null, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, C classification, String value, boolean first, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification.getName(), value, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, String classification, boolean first, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification, null, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, String classification, String searchValue, boolean first, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification, searchValue, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, C classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification.getName(), value, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, String classification, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification, null, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, String classification, String searchValue, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification, searchValue, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationTypeFirst(T typeValue, String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification, searchValue, system, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationTypeFirst(String typeValue, String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue, classification, searchValue, system, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationTypeFirst(T typeValue, C classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification.getName(), searchValue, system, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationTypeFirst(T typeValue, C classification, String searchValue, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification.getName(), searchValue, system, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(String typeValue, String classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system.getEnterprise(), identityToken)
				                   .withClassification(iClassification)
				                   .withValue(new Passwords().integerEncrypt(searchValue.getBytes(StandardCharsets.UTF_8)))
				                   .withType(typeValue, system, identityToken)
				                   .inDateRange()
				                   .withEnterprise(system.getEnterprise())
				                   .canRead(system, identityToken);
		if (first)
		{ queryBuilderRelationshipClassification.setMaxResults(1); }
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		
		//noinspection rawtypes
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(T typeValue, C classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue.classificationValue(), classification.getName(), null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(T typeValue, C classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue.classificationValue(), classification.getName(), value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(T typeValue, String classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue.classificationValue(), classification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(T typeValue, String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue.classificationValue(), classification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(String typeValue, String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue, classification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(String typeValue, String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue, classification, value, false, system, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(String typeValue, String classification, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue, classification, null, system, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(String typeValue, ISystems<?> system, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue, NoClassification.classificationName(), null, system, false, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(String typeValue, String classification, String searchValue, ISystems<?> system, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system.getEnterprise(), identityToken)
				                   .withType(typeValue, system, identityToken)
				                   .withValue(searchValue)
				                   .withClassification(iClassification)
				                   .inDateRange()
				                   .canRead(system, identityToken);
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		return (List) queryBuilderRelationshipClassification.getAll();
	}

	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findInvolvedPartyIdentificationTypePrimaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedPartyIdentificationTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findInvolvedPartyIdentificationTypeQueryRelationshipTableType()
	{
		Type[] genericInterfaces;
		Class<?> currentClass = getClass();
		while (currentClass != Object.class)
		{
			genericInterfaces = currentClass.getGenericInterfaces();
			for (Type genericInterface : genericInterfaces)
			{
				String clazz = genericInterface.getTypeName();
				if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedPartyIdentificationTypes.class.getCanonicalName()))
				{
					Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
					return (Class<Q>) genericTypes[2];
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return null;
	}
	
	default boolean hasIdentificationTypeBefore(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOfIdentificationTypesAll(typeValue, classificationValue.getName(), system, identityToken) > 0;
	}
	
	default boolean hasIdentificationTypeBefore(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOfIdentificationTypesAll(typeValue.classificationValue(), classificationValue.getName(), value, system, identityToken) > 0;
	}
	
	default boolean hasIdentificationTypeBefore(T typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfIdentificationTypesAll(typeValue.classificationValue(), classificationValue, value, system, identityToken) > 0;
	}
	
	default boolean hasIdentificationType(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOfIdentificationTypes(typeValue, classificationValue.getName(), system, identityToken) > 0;
	}
	
	default boolean hasIdentificationType(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOfIdentificationTypes(typeValue, classificationValue, value, system, identityToken) > 0;
	}
	
	default long numberOfIdentificationTypes(String typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfIdentificationTypes(typeValue, classificationValue, null, system, identityToken);
	}
	
	default long numberOfIdentificationTypes(T typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfIdentificationTypes(typeValue.classificationValue(), classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfIdentificationTypes(String typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyIdentificationTypesCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) stringToIdentificationType(typeValue, system, identityToken), value)
		                             .withClassification(classification)
		                             .withType(typeValue, system, identityToken)
		                             .inActiveRange(system.getEnterprise(),identityToken)
		                             .inDateRange()
		                             .canRead(system, identityToken)
		                             .getCount();
	}
	
	default long numberOfIdentificationTypes(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOfIdentificationTypes(typeValue.classificationValue(), classificationValue.getName(), value, system, identityToken);
	}
	
	default long numberOfIdentificationTypesAll(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOfIdentificationTypesAll(typeValue.classificationValue(), classificationValue.getName(), null, system, identityToken);
	}
	
	default long numberOfIdentificationTypesAll(String typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfIdentificationTypesAll(typeValue, classificationValue, null, system, identityToken);
	}
	
	default long numberOfIdentificationTypesAll(T typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfIdentificationTypesAll(typeValue.classificationValue(), classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfIdentificationTypesAll(String typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyIdentificationTypesCountableQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) stringToIdentificationType(typeValue, system, identityToken), value)
		                             .withClassification(classification)
		                             .withType(typeValue, system, identityToken)
		                             .getCount();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findInvolvedPartyIdentificationTypesCountableQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedPartyIdentificationTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	T stringToIdentificationType(String typeName, ISystems<?> system, UUID... identityToken);
	
	default S stringToSecondaryIdentificationType(String typeName, ISystems<?> system, UUID... identityToken)
	{
		IInvolvedPartyService<?> service = GuiceContext.get(IInvolvedPartyService.class);
		return (S) service.findIdentificationType(typeName, system, identityToken);
	}
	
	void configureAddableIdentificationType(Q linkTable, P primary, S secondary, C classificationValue, T type, String value, ISystems<?> system);
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findAddableIdentificationTypeTableType()
	{
		Type[] genericInterfaces;
		Class<?> currentClass = getClass();
		while (currentClass != Object.class)
		{
			genericInterfaces = currentClass.getGenericInterfaces();
			for (Type genericInterface : genericInterfaces)
			{
				String clazz = genericInterface.getTypeName();
				if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedPartyIdentificationTypes.class.getCanonicalName()))
				{
					Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
					return (Class<Q>) genericTypes[2];
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return null;
	}
	
	default IRelationshipValue<L, R, ?> addIdentificationType(T typeName, C classificationName, ISystems<?> system, UUID... identityToken)
	{
		return addIdentificationType(typeName.classificationValue(), classificationName.getName(), null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addIdentificationType(T typeName, C classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addIdentificationType(typeName.classificationValue(), classificationName.getName(), value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addIdentificationType(T typeName, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addIdentificationType(typeName.classificationValue(), classificationName, value, system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addIdentificationType(T typeName, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return addIdentificationType(typeName.classificationValue(), Classifications.NoClassification.classificationName(), value, originatingSystem, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addIdentificationType(String typeName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addIdentificationType(typeName, Classifications.NoClassification.classificationName(), value, system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addIdentificationType(String typeName, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addIdentificationType(typeName, classificationName, value, STRING_EMPTY, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addIdentificationType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, ISystems<?> system, UUID... identityToken)
	{
		return addIdentificationType(typeName, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addIdentificationType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, ISystems<?> system, UUID... identityToken)
	{
		return addIdentificationType(typeName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addIdentificationType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, LocalDateTime effectiveToDate, ISystems<?> system, UUID... identityToken)
	{
		return addIdentificationType(typeName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, system, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addIdentificationType(@NotNull String type,
	                                                          @NotNull String classificationName,
	                                                          String value,
	                                                          String originalSourceSystemUniqueID,
	                                                          LocalDateTime effectiveFromDate,
	                                                          LocalDateTime effectiveToDate,
	                                                          ISystems<?> originalSystemID,
	                                                          @NotNull ISystems<?> system,
	                                                          UUID... identityToken)
	{
		Q tableForClassification = get(findAddableIdentificationTypeTableType());
		T typeToUse = stringToIdentificationType(type, system, identityToken);
		
		IClassificationService<?> classificationService = GuiceContext.get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationName, system, identityToken);

		
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setValue(Strings.nullToEmpty(value));
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemUniqueID(originalSourceSystemUniqueID);
		tableForClassification.setEffectiveFromDate(effectiveFromDate);
		tableForClassification.setEffectiveToDate(effectiveToDate);
		tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
		tableForClassification.setClassificationID((Classification) classification);
		
		configureAddableIdentificationType(tableForClassification, (P) this, stringToSecondaryIdentificationType(type, system, identityToken),
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
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull C classificationName,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull C classificationName,
	                                       String value,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull C classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull C classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull C classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       LocalDateTime effectiveToDate,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull C classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       LocalDateTime effectiveToDate,
	                                       ISystems<?> originalSystemID,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull String classificationName,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       LocalDateTime effectiveToDate,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       LocalDateTime effectiveToDate,
	                                       ISystems<?> originalSystemID,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull String type,
	                                       @NotNull String classificationName,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull String type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull String type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull String type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull String type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       LocalDateTime effectiveToDate,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull String type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       LocalDateTime effectiveToDate,
	                                       ISystems<?> originalSystemID,
	                                       @NotNull ISystems<?> system,
	                                       UUID... identityToken)
	{
		Q tableForClassification = get(findAddableIdentificationTypeTableType());
		S sType = stringToSecondaryIdentificationType(type, system, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		boolean exists = findIdentificationTypeQuery(value, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return addIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		else
		{
			tableForClassification = (Q) findIdentificationTypeQuery(value, system, tableForClassification, sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull String classificationName,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull C classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        LocalDateTime effectiveToDate,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull C classificationName,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull C classificationName,
	                                        String value,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull C classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull C classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull String type,
	                                        @NotNull C classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        LocalDateTime effectiveToDate,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull String type,
	                                        @NotNull String classificationName,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull String type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull String type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull String type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull String type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        LocalDateTime effectiveToDate,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull String type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        LocalDateTime effectiveToDate,
	                                        ISystems<?> originalSystemID,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		Q tableForClassification = get(findAddableIdentificationTypeTableType());
		S sType = stringToSecondaryIdentificationType(type, system, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		boolean exists = findIdentificationTypeQuery(value, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{ return  addIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken); }
		
		tableForClassification = (Q) findIdentificationTypeQuery(value, system, tableForClassification, sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return tableForClassification;
		}
		return updateIdentificationType(tableForClassification, type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> updateIdentificationType(@NotNull IRelationshipValue<L, R, ?>  original,
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
		archiveIdentificationType(original, identityToken);
		return addIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> archiveIdentificationType(@NotNull IRelationshipValue<L, R, ?>  original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	default IRelationshipValue<L, R, ?> removeIdentificationType(@NotNull IRelationshipValue<L, R, ?>  original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getDeletedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	@SuppressWarnings("rawtypes")
	default IRelationshipValue<L, R, ?> expireIdentificationType(@NotNull  IRelationshipValue<L, R, ?>  original, Duration when)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		entity.setEffectiveToDate(entity.getEffectiveToDate()
		                                .plus(when));
		QueryBuilderSCD scd = (QueryBuilderSCD) entity.builder();
		scd.update(entity, when);
		return original;
	}
	
	private QueryBuilderRelationship<?, ?, ?, ?, ?, ?> findIdentificationTypeQuery(String value, ISystems<?> system, Q tableForClassification, S sType, Classification classification, UUID... identityToken)
	{
		return tableForClassification.builder()
		                             .findLink((P) this, sType, value)
		                             .inActiveRange(system.getEnterprise(),identityToken)
		                             .inDateRange()
		                             .withClassification(classification)
		                             .withEnterprise(system.getEnterprise())
		                             .canCreate(system.getEnterprise(), identityToken);
	}
	
}
