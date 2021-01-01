package com.guicedee.activitymaster.core.services.capabilities.bases;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRelationshipValue;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;
import com.guicedee.activitymaster.core.services.system.IClassificationService;

import jakarta.validation.constraints.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

public interface ISearchableType<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTypesTable<P, S, ?, ? extends QueryBuilderRelationshipClassificationTypes, T, ?, ?, L, R>,
		C extends IClassification<?>,
		T extends ITypeValue<?>,
		L, R>
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
		Q relationshipTable = get(findQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system.getEnterprise(), identityToken)
				                   .withClassification(iClassification)
				                   .withValue(searchValue)
				                   .withType(typeValue, system, identityToken)
				                   .inDateRange()
				                   .withEnterprise(system.getEnterprise())
				                   .canRead(system, identityToken);
		if (first)
		{
			queryBuilderRelationshipClassification.setMaxResults(1);
		}
		if (latest)
		{
			queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate"));
		}
		
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
		return findAll(typeValue, classification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(String typeValue, String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return findAll(typeValue, classification, value, false, system, identityToken);
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
		Q relationshipTable = get(findQueryRelationshipTableType());
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
		{
			queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate"));
		}
		return (List) queryBuilderRelationshipClassification.getAll();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(ISearchableType.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findPrimaryTableType()
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
	default Class<S> findSecondaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(ISearchableType.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<S>) genericTypes[1];
			}
		}
		return null;
	}
}
