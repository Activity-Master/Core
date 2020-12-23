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
public interface IContainsInvolvedPartyIdentificationTypes<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable<?, ? extends QueryBuilderDefault, ?, ?>,
		Q extends WarehouseClassificationRelationshipTypesTable<P, S, ?, ? extends QueryBuilderRelationshipClassificationTypes,T, ?, ?, L, R>,
		C extends IClassification<?>,
		T extends IIdentificationType<?>,
		L, R,
		J extends IContainsInvolvedPartyIdentificationTypes<P, S, Q,C, T, L, R, J>>
{
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, C classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue, classification, null, enterprise, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue, NoClassification.classificationName(), null, enterprise, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(String typeValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue, NoClassification.classificationName(), value, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(String typeValue, String classification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue, classification, value, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, C classification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification.getName(), value, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, String classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification, null, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, String classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification, searchValue, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, C classification, String value, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification.getName(), value, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, String classification, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification, null, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, String classification, String searchValue, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification, searchValue, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, C classification, String value, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification.getName(), value, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, String classification, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification, null, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(T typeValue, String classification, String searchValue, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification, searchValue, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationTypeFirst(T typeValue, String classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification, searchValue, enterprise, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationTypeFirst(String typeValue, String classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue, classification, searchValue, enterprise, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationTypeFirst(T typeValue, C classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification.getName(), searchValue, enterprise, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationTypeFirst(T typeValue, C classification, String searchValue, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationType(typeValue.classificationValue(), classification.getName(), searchValue, enterprise, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findIdentificationType(String typeValue, String classification, String searchValue, IEnterprise<?> enterprise, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(enterprise, identityToken)
				                   .withClassification(iClassification)
				                   .withValue(searchValue)
				                   .withType(typeValue, enterprise, identityToken)
				                   .inDateRange()
				                   .withEnterprise(enterprise)
				                   .canRead(enterprise, identityToken);
		if (first)
		{ queryBuilderRelationshipClassification.setMaxResults(1); }
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		
		//noinspection rawtypes
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(T typeValue, C classification, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue.classificationValue(), classification.getName(), null, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(T typeValue, C classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue.classificationValue(), classification.getName(), value, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(T typeValue, String classification, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue.classificationValue(), classification, null, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(T typeValue, String classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue.classificationValue(), classification, value, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(String typeValue, String classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue, classification, value, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(String typeValue, String classification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue, classification, value, false, enterprise, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(String typeValue, String classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue, classification, null, enterprise, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(String typeValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findIdentificationTypesAll(typeValue, NoClassification.classificationName(), null, enterprise, false, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findIdentificationTypesAll(String typeValue, String classification, String searchValue, IEnterprise<?> enterprise, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(enterprise, identityToken)
				                   .withType(typeValue, enterprise, identityToken)
				                   .withValue(searchValue)
				                   .withClassification(iClassification)
				                   .inDateRange()
				                   .canRead(enterprise, identityToken);
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
	
	default boolean hasIdentificationTypeBefore(T typeValue, C classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOfIdentificationTypesAll(typeValue, classificationValue.getName(), enterprise, identityToken) > 0;
	}
	
	default boolean hasIdentificationTypeBefore(T typeValue, C classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOfIdentificationTypesAll(typeValue.classificationValue(), classificationValue.getName(), value, enterprise, identityToken) > 0;
	}
	
	default boolean hasIdentificationTypeBefore(T typeValue, String classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfIdentificationTypesAll(typeValue.classificationValue(), classificationValue, value, enterprise, identityToken) > 0;
	}
	
	default boolean hasIdentificationType(T typeValue, C classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOfIdentificationTypes(typeValue, classificationValue.getName(), enterprise, identityToken) > 0;
	}
	
	default boolean hasIdentificationType(T typeValue, C classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOfIdentificationTypes(typeValue, classificationValue, value, enterprise, identityToken) > 0;
	}
	
	default boolean hasIdentificationType(T typeValue, C classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,originatingSystem.getEnterprise(), identityToken);
		}
		return numberOfIdentificationTypes(typeValue, classificationValue.getName(), originatingSystem.getEnterprise(), identityToken) > 0;
	}
	
	default boolean hasIdentificationType(T typeValue, C classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return numberOfIdentificationTypes(typeValue, classificationValue, value, originatingSystem.getEnterprise(), identityToken) > 0;
	}
	
	default long numberOfIdentificationTypes(String typeValue, String classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfIdentificationTypes(typeValue, classificationValue, null, enterprise, identityToken);
	}
	
	default long numberOfIdentificationTypes(T typeValue, String classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return numberOfIdentificationTypes(typeValue.classificationValue(), classificationValue, null, enterprise, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfIdentificationTypes(String typeValue, String classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyIdentificationTypesCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, enterprise, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) stringToIdentificationType(typeValue, enterprise, identityToken), value)
		                             .withClassification(classification)
		                             .withType(typeValue, enterprise, identityToken)
		                             .inActiveRange(enterprise)
		                             .inDateRange()
		                             .canRead(enterprise, identityToken)
		                             .getCount();
	}
	
	default long numberOfIdentificationTypes(T typeValue, C classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOfIdentificationTypes(typeValue.classificationValue(), classificationValue.getName(), value, enterprise, identityToken);
	}
	
	default long numberOfIdentificationTypesAll(T typeValue, C classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOfIdentificationTypesAll(typeValue.classificationValue(), classificationValue.getName(), null, enterprise, identityToken);
	}
	
	default long numberOfIdentificationTypesAll(String typeValue, String classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfIdentificationTypesAll(typeValue, classificationValue, null, enterprise, identityToken);
	}
	
	default long numberOfIdentificationTypesAll(T typeValue, String classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfIdentificationTypesAll(typeValue.classificationValue(), classificationValue, null, enterprise, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfIdentificationTypesAll(String typeValue, String classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyIdentificationTypesCountableQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, enterprise, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) stringToIdentificationType(typeValue, enterprise, identityToken), value)
		                             .withClassification(classification)
		                             .withType(typeValue, enterprise, identityToken)
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
	
	T stringToIdentificationType(String typeName, IEnterprise<?> enterprise, UUID... identityToken);
	
	default S stringToSecondaryIdentificationType(String typeName, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IInvolvedPartyService<?> service = GuiceContext.get(IInvolvedPartyService.class);
		return (S) service.findIdentificationType(typeName, enterprise, identityToken);
	}
	
	void configureAddableIdentificationType(Q linkTable, P primary, S secondary, C classificationValue, T type, String value, IEnterprise<?> enterprise);
	
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
	
	default IRelationshipValue<L, R, ?> addIdentificationType(T typeName, C classificationName, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addIdentificationType(typeName.classificationValue(), classificationName.getName(), null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addIdentificationType(T typeName, C classificationName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addIdentificationType(typeName.classificationValue(), classificationName.getName(), value, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addIdentificationType(T typeName, String classificationName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addIdentificationType(typeName.classificationValue(), classificationName, value, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addIdentificationType(T typeName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addIdentificationType(typeName.classificationValue(), Classifications.NoClassification.classificationName(), value, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addIdentificationType(T typeName, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return addIdentificationType(typeName.classificationValue(), Classifications.NoClassification.classificationName(), value, originatingSystem.getEnterprise(), identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addIdentificationType(String typeName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addIdentificationType(typeName, Classifications.NoClassification.classificationName(), value, enterprise, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addIdentificationType(String typeName, String classificationName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addIdentificationType(typeName, classificationName, value, STRING_EMPTY, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addIdentificationType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addIdentificationType(typeName, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addIdentificationType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addIdentificationType(typeName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addIdentificationType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, LocalDateTime effectiveToDate, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addIdentificationType(typeName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addIdentificationType(@NotNull String type,
	                                                          @NotNull String classificationName,
	                                                          String value,
	                                                          String originalSourceSystemUniqueID,
	                                                          LocalDateTime effectiveFromDate,
	                                                          LocalDateTime effectiveToDate,
	                                                          ISystems<?> originalSystemID,
	                                                          @NotNull IEnterprise<?> enterprise,
	                                                          UUID... identityToken)
	{
		Q tableForClassification = get(findAddableIdentificationTypeTableType());
		T typeToUse = stringToIdentificationType(type, enterprise, identityToken);
		
		IClassificationService<?> classificationService = GuiceContext.get(IClassificationService.class);
		IClassification<?> classification = classificationService.findOrCreate(classificationName, enterprise, identityToken);
		
		ISystems<?> originatingSystem = GuiceContext.get(SystemsService.class)
		                                            .getActivityMaster(enterprise);
		if (originalSystemID != null)
		{
			originatingSystem = originalSystemID;
		}
		
		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setValue(Strings.nullToEmpty(value));
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(originalSourceSystemUniqueID);
		tableForClassification.setEffectiveFromDate(effectiveFromDate);
		tableForClassification.setEffectiveToDate(effectiveToDate);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		tableForClassification.setClassificationID((Classification) classification);
		
		configureAddableIdentificationType(tableForClassification, (P) this, stringToSecondaryIdentificationType(type, enterprise, identityToken),
		                                   (C) classification, typeToUse, value, originatingSystem.getEnterpriseID());
		
		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				.isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}
		if (EventThread.event.get() != null)
		{
			EventThread.event.get()
			                 .add((IEventClassification<?>) Created, type + " - " + classificationName + " - " + value, originatingSystem, identityToken);
		}
		return (IRelationshipValue<L, R, ?>) tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull C classificationName,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, null, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull C classificationName,
	                                       String value,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull C classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull C classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull C classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       LocalDateTime effectiveToDate,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull C classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       LocalDateTime effectiveToDate,
	                                       ISystems<?> originalSystemID,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull String classificationName,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, null, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       LocalDateTime effectiveToDate,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull T type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       LocalDateTime effectiveToDate,
	                                       ISystems<?> originalSystemID,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull String type,
	                                       @NotNull String classificationName,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, null, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull String type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull String type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull String type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull String type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       LocalDateTime effectiveToDate,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		return addOrReuseIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?>addOrReuseIdentificationType(@NotNull String type,
	                                       @NotNull String classificationName,
	                                       String value,
	                                       String originalSourceSystemUniqueID,
	                                       LocalDateTime effectiveFromDate,
	                                       LocalDateTime effectiveToDate,
	                                       ISystems<?> originalSystemID,
	                                       @NotNull IEnterprise<?> enterprise,
	                                       UUID... identityToken)
	{
		Q tableForClassification = get(findAddableIdentificationTypeTableType());
		S sType = stringToSecondaryIdentificationType(type, enterprise, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.findOrCreate(classificationName, enterprise, identityToken);
		boolean exists = findIdentificationTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return addIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
		}
		else
		{
			tableForClassification = (Q) findIdentificationTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull String classificationName,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull C classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        LocalDateTime effectiveToDate,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull C classificationName,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull C classificationName,
	                                        String value,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull C classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull T type,
	                                        @NotNull C classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull String type,
	                                        @NotNull C classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        LocalDateTime effectiveToDate,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull String type,
	                                        @NotNull String classificationName,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull String type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull String type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull String type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull String type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        LocalDateTime effectiveToDate,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		return addOrUpdateIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?>addOrUpdateIdentificationType(@NotNull String type,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        LocalDateTime effectiveToDate,
	                                        ISystems<?> originalSystemID,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		Q tableForClassification = get(findAddableIdentificationTypeTableType());
		S sType = stringToSecondaryIdentificationType(type, enterprise, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.findOrCreate(classificationName, enterprise, identityToken);
		boolean exists = findIdentificationTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{ return  addIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken); }
		
		tableForClassification = (Q) findIdentificationTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return tableForClassification;
		}
		return updateIdentificationType(tableForClassification, type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> updateIdentificationType(@NotNull IRelationshipValue<L, R, ?>  original,
	                                                             @NotNull String type,
	                                                             @NotNull String classificationName,
	                                                             String value,
	                                                             String originalSourceSystemUniqueID,
	                                                             LocalDateTime effectiveFromDate,
	                                                             LocalDateTime effectiveToDate,
	                                                             ISystems<?> originalSystemID,
	                                                             @NotNull IEnterprise<?> enterprise,
	                                                             UUID... identityToken)
	{
		archiveIdentificationType(original, identityToken);
		return addIdentificationType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
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
	
	private QueryBuilderRelationship<?, ?, ?, ?, ?, ?> findIdentificationTypeQuery(String value, IEnterprise<?> enterprise, Q tableForClassification, S sType, Classification classification, UUID... identityToken)
	{
		return tableForClassification.builder()
		                             .findLink((P) this, sType, value)
		                             .inActiveRange(enterprise)
		                             .inDateRange()
		                             .withClassification(classification)
		                             .withEnterprise(enterprise)
		                             .canCreate(enterprise, identityToken);
	}
	
}
