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
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, C classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue, classification, null, enterprise, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue, NoClassification.classificationName(), null, enterprise, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(String typeValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue, NoClassification.classificationName(), value, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(String typeValue, String classification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue, classification, value, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, C classification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification.getName(), value, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, String classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification, null, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, String classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification, searchValue, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, C classification, String value, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification.getName(), value, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, String classification, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification, null, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, String classification, String searchValue, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification, searchValue, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, C classification, String value, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification.getName(), value, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, String classification, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification, null, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameType(T typeValue, String classification, String searchValue, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification, searchValue, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameTypeFirst(T typeValue, String classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification, searchValue, enterprise, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameTypeFirst(String typeValue, String classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue, classification, searchValue, enterprise, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameTypeFirst(T typeValue, C classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification.getName(), searchValue, enterprise, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findNameTypeFirst(T typeValue, C classification, String searchValue, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameType(typeValue.classificationValue(), classification.getName(), searchValue, enterprise, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findNameType(String typeValue, String classification, String searchValue, IEnterprise<?> enterprise, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findInvolvedPartyNameTypeQueryRelationshipTableType());
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
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(T typeValue, C classification, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameTypesAll(typeValue.classificationValue(), classification.getName(), null, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(T typeValue, C classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameTypesAll(typeValue.classificationValue(), classification.getName(), value, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(T typeValue, String classification, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameTypesAll(typeValue.classificationValue(), classification, null, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(T typeValue, String classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameTypesAll(typeValue.classificationValue(), classification, value, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(String typeValue, String classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameTypesAll(typeValue, classification, value, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(String typeValue, String classification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameTypesAll(typeValue, classification, value, false, enterprise, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(String typeValue, String classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameTypesAll(typeValue, classification, null, enterprise, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findNameTypesAll(String typeValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findNameTypesAll(typeValue, NoClassification.classificationName(), null, enterprise, false, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findNameTypesAll(String typeValue, String classification, String searchValue, IEnterprise<?> enterprise, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findInvolvedPartyNameTypeQueryRelationshipTableType());
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
	
	
	default boolean hasNameTypeBefore(T typeValue, C classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOfAllNameTypes(typeValue, classificationValue.getName(), enterprise, identityToken) > 0;
	}
	
	default boolean hasNameTypeBefore(T typeValue, C classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOfAllNameTypes(typeValue.classificationValue(), classificationValue.getName(), value, enterprise, identityToken) > 0;
	}
	
	default boolean hasNameTypeBefore(T typeValue, String classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAllNameTypes(typeValue.classificationValue(), classificationValue, value, enterprise, identityToken) > 0;
	}
	
	default boolean hasNameType(T typeValue, C classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOfNameType(typeValue, classificationValue.getName(), enterprise, identityToken) > 0;
	}
	
	default boolean hasNameType(T typeValue, C classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOfNameType(typeValue, classificationValue, value, enterprise, identityToken) > 0;
	}
	
	default boolean hasNameType(T typeValue, C classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification, originatingSystem.getEnterprise(), identityToken);
		}
		return numberOfNameType(typeValue, classificationValue.getName(), originatingSystem.getEnterprise(), identityToken) > 0;
	}
	
	default boolean hasNameType(T typeValue, C classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,originatingSystem.getEnterprise(), identityToken);
		}
		return numberOfNameType(typeValue, classificationValue, value, originatingSystem.getEnterprise(), identityToken) > 0;
	}
	
	default long numberOfNameType(String typeValue, String classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfNameType(typeValue, classificationValue, null, enterprise, identityToken);
	}
	
	default long numberOfNameType(T typeValue, String classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfNameType(typeValue.classificationValue(), classificationValue, null, enterprise, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfNameType(String typeValue, String classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findIPNameTypeCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, enterprise, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) stringToNameType(typeValue,enterprise,identityToken), value)
		                             .withType(typeValue, enterprise, identityToken)
		                             .inActiveRange(enterprise)
		                             .inDateRange()
		                             .canRead(enterprise, identityToken)
		                             .getCount();
	}
	
	default long numberOfNameType(T typeValue, C classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOfNameType(typeValue.classificationValue(), classificationValue.getName(), value, enterprise, identityToken);
	}
	
	default long numberOfAllNameTypes(T typeValue, C classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOfAllNameTypes(typeValue.classificationValue(), classificationValue.getName(), null, enterprise, identityToken);
	}
	
	default long numberOfAllNameTypes(String typeValue, String classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAllNameTypes(typeValue, classificationValue, null, enterprise, identityToken);
	}
	
	default long numberOfAllNameTypes(T typeValue, String classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAllNameTypes(typeValue.classificationValue(), classificationValue, null, enterprise, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfAllNameTypes(String typeValue, String classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findIPNameTypeCountableQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, enterprise, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) stringToNameType(typeValue,enterprise,identityToken), value)
		                             .withClassification(classification)
		                             .withType(typeValue, enterprise, identityToken)
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
	
	T stringToNameType(String typeName, IEnterprise<?> enterprise, UUID...identityToken);
	
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
	
	default S stringToNameTypeSecondary(String typeName, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IInvolvedPartyService<?> iInvolvedPartyService = get(IInvolvedPartyService.class);
		return (S) iInvolvedPartyService.createNameType(typeName, typeName,enterprise, identityToken);
	}
	
	void configureIPNameTypesAddable(Q linkTable, P primary, S secondary, C classificationValue, T type, String value, IEnterprise<?> enterprise);
	
	default IRelationshipValue<L, R, ?> addNameType(T typeName, C classificationName, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addNameType(typeName.classificationValue(), classificationName.getName(), null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addNameType(T typeName, C classificationName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addNameType(typeName.classificationValue(), classificationName.getName(), value, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addNameType(T typeName, String classificationName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addNameType(typeName.classificationValue(), classificationName, value, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addNameType(T typeName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addNameType(typeName.classificationValue(), Classifications.NoClassification.classificationName(), value, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addNameType(T typeName, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return addNameType(typeName.classificationValue(), Classifications.NoClassification.classificationName(), value, originatingSystem.getEnterprise(), identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addNameType(String typeName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addNameType(typeName, Classifications.NoClassification.classificationName(), value, enterprise, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addNameType(String typeName, String classificationName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addNameType(typeName, classificationName, value, STRING_EMPTY, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addNameType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addNameType(typeName, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addNameType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addNameType(typeName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addNameType(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, LocalDateTime effectiveToDate, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addNameType(typeName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addNameType(@NotNull String type,
	                                                @NotNull String classificationName,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                LocalDateTime effectiveFromDate,
	                                                LocalDateTime effectiveToDate,
	                                                ISystems<?> originalSystemID,
	                                                @NotNull IEnterprise<?> enterprise,
	                                                UUID... identityToken)
	{
		Q tableForClassification = get(this.findIPNameTypesAddableTableType());
		T typeToUse = this.stringToNameType(type, enterprise, identityToken);
		
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
		
		this.configureIPNameTypesAddable(tableForClassification, (P) this, this.stringToNameTypeSecondary(type, enterprise, identityToken),
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
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull C classificationName,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, null, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull C classificationName,
	                                                       String value,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull C classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull C classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull C classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       LocalDateTime effectiveToDate,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull C classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       LocalDateTime effectiveToDate,
	                                                       ISystems<?> originalSystemID,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull String classificationName,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, null, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       LocalDateTime effectiveToDate,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull T type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       LocalDateTime effectiveToDate,
	                                                       ISystems<?> originalSystemID,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull String type,
	                                                       @NotNull String classificationName,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, null, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull String type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull String type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull String type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull String type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       LocalDateTime effectiveToDate,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		return addOrReuseNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuseNameType(@NotNull String type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       LocalDateTime effectiveToDate,
	                                                       ISystems<?> originalSystemID,
	                                                       @NotNull IEnterprise<?> enterprise,
	                                                       UUID... identityToken)
	{
		Q tableForClassification = get(this.findIPNameTypesAddableTableType());
		S sType = this.stringToNameTypeSecondary(type, enterprise, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.findOrCreate(classificationName, enterprise, identityToken);
		boolean exists = findIPNameTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return (Q) addNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
		}
		else
		{
			tableForClassification = (Q) findIPNameTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull String classificationName,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        LocalDateTime effectiveFromDate,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull C classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        LocalDateTime effectiveFromDate,
	                                                        LocalDateTime effectiveToDate,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull C classificationName,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull C classificationName,
	                                                        String value,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull C classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull T type,
	                                                        @NotNull C classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        LocalDateTime effectiveFromDate,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull String type,
	                                                        @NotNull C classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        LocalDateTime effectiveFromDate,
	                                                        LocalDateTime effectiveToDate,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull String type,
	                                                        @NotNull String classificationName,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull String type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull String type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull String type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        LocalDateTime effectiveFromDate,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull String type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        LocalDateTime effectiveFromDate,
	                                                        LocalDateTime effectiveToDate,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		return addOrUpdateNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateNameType(@NotNull String type,
	                                                        @NotNull String classificationName,
	                                                        String value,
	                                                        String originalSourceSystemUniqueID,
	                                                        LocalDateTime effectiveFromDate,
	                                                        LocalDateTime effectiveToDate,
	                                                        ISystems<?> originalSystemID,
	                                                        @NotNull IEnterprise<?> enterprise,
	                                                        UUID... identityToken)
	{
		Q tableForClassification = get(this.findIPNameTypesAddableTableType());
		S sType = this.stringToNameTypeSecondary(type, enterprise, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.findOrCreate(classificationName, enterprise, identityToken);
		boolean exists = findIPNameTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{ return  addNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken); }
		
		tableForClassification = (Q) findIPNameTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return tableForClassification;
		}
		return updateNameType(tableForClassification, type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> updateNameType(@NotNull IRelationshipValue<L, R, ?>  original,
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
		archiveNameType(original, identityToken);
		return addNameType(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
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
	
	private QueryBuilderRelationship<?, ?, ?, ?, ?, ?> findIPNameTypeQuery(String value, IEnterprise<?> enterprise, Q tableForClassification, S sType, Classification classification, UUID... identityToken)
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
