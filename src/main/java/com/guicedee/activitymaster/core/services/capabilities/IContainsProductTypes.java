package com.guicedee.activitymaster.core.services.capabilities;

import com.entityassist.enumerations.OrderByType;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.product.ProductType;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRelationshipValue;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.IProductService;


import jakarta.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.entityassist.SCDEntity.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

@SuppressWarnings("Duplicates")
public interface IContainsProductTypes<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTypesTable<P, S, ?, ? extends QueryBuilderRelationshipClassificationTypes, T, ?, ?, ?, ?>,
		C extends IClassificationValue<?>,
		T extends IProductTypeValue<?>,
		L, R,
		J extends IContainsProductTypes<P, S, Q, C, T, L, R, J>>
{
	default Optional<IRelationshipValue<L, R, ?>> findByProductType(R byType, String classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		Q relationshipTable = get(findProductTypeQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IEnterprise<?> enterprise = system.getEnterprise();
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findLink(null,(S) byType)
				                   .inActiveRange(enterprise, identityToken)
				                   .withClassification(iClassification)
				                   .withValue(value)
				                   .inDateRange()
				                   .withEnterprise(enterprise)
				                   .canRead(enterprise, identityToken);
		if (first)
		{ queryBuilderRelationshipClassification.setMaxResults(1); }
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate"), OrderByType.DESC); }
		
		//noinspection rawtypes,unchecked
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProductType(T typeValue, C classification, ISystems<?> system, UUID... identityToken)
	{
		return findProductType(typeValue, classification, null, system, identityToken);
	}

	default Optional<IRelationshipValue<L, R, ?>> findProductType(T typeValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> service = get(IClassificationService.class);
		IClassification<?> classification = service.find(Classifications.NoClassification, system.getEnterprise(), identityToken);
		return findProductType(typeValue, (C) classification, null, system, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProductType(T typeValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> service = get(IClassificationService.class);
		IClassification<?> classification = service.find(Classifications.NoClassification, originatingSystem.getEnterprise(), identityToken);
		return findProductType(typeValue, (C) classification, value, originatingSystem, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProductType(String typeValue, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> service = get(IClassificationService.class);
		IClassification<?> classification = service.find(Classifications.NoClassification, system.getEnterprise(), identityToken);
		return findProductType(typeValue, classification, value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProductType(String typeValue, String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductType(typeValue, iClassification, value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProductType(T typeValue, C classification, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductType(typeValue.classificationValue(), iClassification, value, system, false, false, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findProductType(T typeValue, String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductType(typeValue.classificationValue(), iClassification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProductType(T typeValue, C classification, String value, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductType(typeValue.classificationValue(), iClassification, value, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProductType(T typeValue, String classification, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductType(typeValue.classificationValue(), iClassification, null, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProductType(T typeValue, String classification, String searchValue, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductType(typeValue.classificationValue(), iClassification, searchValue, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProductType(T typeValue, C classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductType(typeValue.classificationValue(), iClassification, value, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProductType(T typeValue, String classification, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductType(typeValue.classificationValue(), iClassification, null, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProductType(T typeValue, String classification, String searchValue, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductType(typeValue.classificationValue(), iClassification, searchValue, system, first, latest, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findProductTypeFirst(String typeValue, String classification, String searchValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findProductType(typeValue, iClassification, searchValue, originatingSystem, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProductTypeFirst(T typeValue, String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductType(typeValue.classificationValue(), iClassification, searchValue, system, true, false, identityToken);
	}
	

	default Optional<IRelationshipValue<L, R, ?>> findProductTypeFirst(T typeValue, C classification, String searchValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findProductType(typeValue.classificationValue(), iClassification, searchValue, originatingSystem, true, false, identityToken);
	}
	

	default Optional<IRelationshipValue<L, R, ?>> findProductTypeFirst(T typeValue, C classification, String searchValue, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findProductType(typeValue.classificationValue(), iClassification, searchValue, originatingSystem, true, latest, identityToken);
	}
	

	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findProductType(String typeValue, IClassification<?> classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		IEnterprise<?> enterprise = system.getEnterprise();
		Q relationshipTable = get(findProductTypeQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(enterprise, identityToken)
				                   .withClassification(classification)
				                   .withValue(searchValue)
				                   .withType(typeValue, system, identityToken)
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
	
	default List<IRelationshipValue<L, R, ?>> findProductTypesAll(T typeValue, C classification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findProductTypesAll(typeValue.classificationValue(), iClassification, null, originatingSystem, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductTypesAll(T typeValue, C classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductTypesAll(typeValue.classificationValue(), iClassification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductTypesAll(T typeValue, C classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findProductTypesAll(typeValue.classificationValue(), iClassification, value, originatingSystem, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductTypesAll(T typeValue, C classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductTypesAll(typeValue.classificationValue(), iClassification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductTypesAll(T typeValue, String classification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findProductTypesAll(typeValue.classificationValue(), iClassification, null, originatingSystem, false, identityToken);
	}

	default List<IRelationshipValue<L, R, ?>> findProductTypesAll(T typeValue, String classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductTypesAll(typeValue.classificationValue(), iClassification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductTypesAll(T typeValue, String classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findProductTypesAll(typeValue.classificationValue(), iClassification, value, originatingSystem, false, identityToken);
	}
	

	default List<IRelationshipValue<L, R, ?>> findProductTypesAll(T typeValue, String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductTypesAll(typeValue.classificationValue(), iClassification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductTypesAll(String typeValue, String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductTypesAll(typeValue, iClassification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductTypesAll(String typeValue, String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductTypesAll(typeValue, iClassification, value, system, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductTypesAll(String typeValue, String classification, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system.getEnterprise(), identityToken);
		return findProductTypesAll(typeValue, iClassification, null, system, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductTypesAll(String typeValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(Classifications.NoClassification, system.getEnterprise(), identityToken);
		return findProductTypesAll(typeValue, iClassification, null, system, false, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findProductTypesAll(String typeValue, IClassification<?> classification, String searchValue, ISystems<?> system, boolean latest, UUID... identityToken)
	{
		IEnterprise<?> enterprise = system.getEnterprise();
		Q relationshipTable = get(findProductTypeQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(enterprise, identityToken)
				                   .withType(typeValue, system, identityToken)
				                   .withValue(searchValue)
				                   .withClassification(classification)
				                   .inDateRange()
				                   .canRead(enterprise, identityToken);
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		return (List) queryBuilderRelationshipClassification.getAll();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findProductTypeQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsProductTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findProductTypesPrimaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsProductTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}
	
	default boolean hasProductTypesBefore(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductTypesAll(typeValue, classificationValue.classificationName(), system, identityToken) > 0;
	}
	
	default boolean hasProductTypesBefore(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductTypesAll(typeValue.classificationValue(), classificationValue.classificationName(), value, system, identityToken) > 0;
	}
	
	default boolean hasProductTypesBefore(T typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductTypesAll(typeValue.classificationValue(), classificationValue, value, system, identityToken) > 0;
	}
	
	default boolean hasProductTypes(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductTypes(typeValue, classificationValue.classificationName(), system, identityToken) > 0;
	}
	
	default boolean hasProductTypes(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductTypes(typeValue, classificationValue, value, system, identityToken) > 0;
	}
	
	default long numberOfProductTypes(String typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductTypes(typeValue, classificationValue, null, system, identityToken);
	}
	
	default long numberOfProductTypes(T typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductTypes(typeValue.classificationValue(), classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfProductTypes(String typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		IEnterprise<?> enterprise = system.getEnterprise();
		Q activityMasterIdentity = get(findProductTypesCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, enterprise, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, value)
		                             .withType(typeValue, system, identityToken)
		                             .inActiveRange(enterprise)
		                             .inDateRange()
		                             .canRead(enterprise, identityToken)
		                             .getCount();
	}
	
	default long numberOfProductTypes(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductTypes(typeValue.classificationValue(), classificationValue.classificationName(), value, system, identityToken);
	}
	
	default long numberOfProductTypesAll(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductTypesAll(typeValue.classificationValue(), classificationValue.classificationName(), null, system, identityToken);
	}
	
	default long numberOfProductTypesAll(String typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductTypesAll(typeValue, classificationValue, null, system, identityToken);
	}
	
	default long numberOfProductTypesAll(T typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductTypesAll(typeValue.classificationValue(), classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfProductTypesAll(String typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findProductTypesCountableQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, system.getEnterprise(), identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, value)
		                             .withType(typeValue, system, identityToken)
		                             .getCount();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findProductTypesCountableQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsProductTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	default Q addProductType(IClassificationValue<?> classificationValue, T productType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findProductTypeQueryRelationshipTableType());
		
		IProductService<?> productService = get(IProductService.class);
		ProductType classificationDataConcept = (ProductType) productType;
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID(classification);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureProductTypeLinkValue(tableForClassification, (P) this, (S) classificationDataConcept, (C) classificationValue, value, originatingSystem);
		
		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				.isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}
		
		return tableForClassification;
	}
	
	void configureProductTypeLinkValue(Q linkTable, P primary, S secondary, C classificationValue, String value, ISystems<?> system);
	
	@SuppressWarnings("unchecked")
	default Q addOrUpdateProductType(IClassificationValue<?> classificationValue, T classificationDataConceptType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findProductTypeQueryRelationshipTableType());
		IProductService<?> classificationDataConceptService = get(IProductService.class);
		ProductType classificationDataConcept = (ProductType) classificationDataConceptService.findProductType(classificationDataConceptType, originatingSystem,
		                                                                                                 identityToken);
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canCreate(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification = addOrReuseProductType(classificationValue, classificationDataConceptType, value, originatingSystem, identityToken);
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
			
			Q newTableForClassification = get(findProductTypeQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureProductTypeLinkValue(newTableForClassification, (P) this, (S) classificationDataConcept, (C) classification, value, originatingSystem);
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
	default Q addOrReuseProductType(IClassificationValue<?> classificationValue, T classificationDataConceptType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findProductTypeQueryRelationshipTableType());
		IProductService<?> classificationDataConceptService = get(IProductService.class);
		ProductType classificationDataConcept = (ProductType) classificationDataConceptService.findProductType(classificationDataConceptType, originatingSystem,
		                                                                                                 identityToken);
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);
		
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
			configureProductTypeLinkValue(tableForClassification, (P) this, (S) classificationDataConcept, (C) classification, value, originatingSystem);
			
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
	default Q addProductType(IClassification<?> classificationValue, T classificationDataConceptType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findProductTypeQueryRelationshipTableType());
		IProductService<?> classificationDataConceptService = get(IProductService.class);
		ProductType classificationDataConcept = (ProductType) classificationDataConceptService.findProductType(classificationDataConceptType, originatingSystem,
		                                                                                                 identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID((Classification) classificationValue);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureProductTypeLinkValue(tableForClassification, (P) this, (S) classificationDataConcept, (C) classificationValue, value, originatingSystem);
		
		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				.isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}
		
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addOrReuseProductType(IClassification<?> classification, T classificationDataConceptType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findProductTypeQueryRelationshipTableType());
		IProductService<?> classificationDataConceptService = get(IProductService.class);
		ProductType classificationDataConcept = (ProductType) classificationDataConceptService.findProductType(classificationDataConceptType, originatingSystem,
		                                                                                                 identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			configureProductTypeLinkValue(tableForClassification, (P) this, (S) classificationDataConcept, (C) classification, value, originatingSystem);
			
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
	default Q addOrUpdateProductType(IClassification<?> classification, T classificationDataConceptType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findProductTypeQueryRelationshipTableType());
		IProductService<?> classificationDataConceptService = get(IProductService.class);
		ProductType classificationDataConcept = (ProductType) classificationDataConceptService.findProductType(classificationDataConceptType, originatingSystem,
		                                                                                                 identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			configureProductTypeLinkValue(tableForClassification, (P) this, (S) classificationDataConcept, (C) classification, value, originatingSystem);
			
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
			
			Q newTableForClassification = get(findProductTypeQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureProductTypeLinkValue(newTableForClassification, (P) this, (S) classificationDataConcept, (C) classification, value, originatingSystem);
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
	default Q updateProductType(IClassification<?> classification, T classificationDataConceptType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findProductTypeQueryRelationshipTableType());
		IProductService<?> classificationDataConceptService = get(IProductService.class);
		ProductType classificationDataConcept = (ProductType) classificationDataConceptService.findProductType(classificationDataConceptType, originatingSystem,
		                                                                                                 identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			
			Q newTableForClassification = get(findProductTypeQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureProductTypeLinkValue(newTableForClassification, (P) this, (S) classificationDataConcept, (C) classification, value, originatingSystem);
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
	default Q archiveProductType(IClassification<?> classification, T classificationDataConceptType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findProductTypeQueryRelationshipTableType());
		IProductService<?> classificationDataConceptService = get(IProductService.class);
		ProductType classificationDataConcept = (ProductType) classificationDataConceptService.findProductType(classificationDataConceptType, originatingSystem,
		                                                                                                 identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q removeProductType(IClassification<?> classification, T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findProductTypeQueryRelationshipTableType());
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) identificationType, null)
		                                                         .withClassification(classification)
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
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getDeletedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}
	
}
