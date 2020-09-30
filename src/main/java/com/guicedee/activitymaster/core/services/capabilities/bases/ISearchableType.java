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

import javax.validation.constraints.NotNull;
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
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, C classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue, classification, null, enterprise, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue, NoClassification.classificationName(), null, enterprise, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(String typeValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue, NoClassification.classificationName(), value, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(String typeValue, String classification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue, classification, value, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, C classification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification.getName(), value, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, String classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification, null, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, String classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification, searchValue, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, C classification, String value, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification.getName(), value, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, String classification, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification, null, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, String classification, String searchValue, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification, searchValue, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, C classification, String value, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification.getName(), value, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, String classification, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification, null, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(T typeValue, String classification, String searchValue, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification, searchValue, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findFirst(T typeValue, String classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification, searchValue, enterprise, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findFirst(String typeValue, String classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue, classification, searchValue, enterprise, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findFirst(T typeValue, C classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification.getName(), searchValue, enterprise, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findFirst(T typeValue, C classification, String searchValue, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return find(typeValue.classificationValue(), classification.getName(), searchValue, enterprise, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> find(String typeValue, String classification, String searchValue, IEnterprise<?> enterprise, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findQueryRelationshipTableType());
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
	
	default List<IRelationshipValue<L, R, ?>> findAll(T typeValue, C classification, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findAll(typeValue.classificationValue(), classification.getName(), null, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(T typeValue, C classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findAll(typeValue.classificationValue(), classification.getName(), value, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(T typeValue, String classification, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findAll(typeValue.classificationValue(), classification, null, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(T typeValue, String classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findAll(typeValue.classificationValue(), classification, value, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(String typeValue, String classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findAll(typeValue, classification, value, enterprise,latest,identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(String typeValue, String classification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findAll(typeValue, classification, value,false, enterprise, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(String typeValue, String classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findAll(typeValue, classification, null, enterprise, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(String typeValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findAll(typeValue, NoClassification.classificationName(), null, enterprise, false, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findAll(String typeValue, String classification, String searchValue, IEnterprise<?> enterprise, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findQueryRelationshipTableType());
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
