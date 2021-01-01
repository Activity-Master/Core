package com.guicedee.activitymaster.core.services.capabilities.bases;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IRelationshipValue;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.guicedee.guicedinjection.GuiceContext.*;

public interface ISearchable<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?, L, R>,
		C extends IClassificationValue<?>,
		L, R>
{
	default Optional<IRelationshipValue<L, R, ?>> find(C classification, ISystems<?> system, UUID... identityToken)
	{
		return find(classification, null, system, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(C classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return find(classification.classificationName(), value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(String classification, ISystems<?> system, UUID... identityToken)
	{
		
		return find(classification, null, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		
		return find(classification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(C classification, String value, boolean first, ISystems<?> system, UUID... identityToken)
	{
		
		return find(classification.classificationName(), value, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(String classification, boolean first, ISystems<?> system, UUID... identityToken)
	{
		
		return find(classification, null, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(String classification, String searchValue, boolean first, ISystems<?> system, UUID... identityToken)
	{
		
		return find(classification, searchValue, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(C classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return find(classification.classificationName(), value, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(String classification, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return find(classification, null, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> find(String classification, String searchValue, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return find(classification, searchValue, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findFirst(C classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		
		return find(classification.classificationName(), searchValue, system, true, false, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findFirst(C classification, String searchValue, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return find(classification.classificationName(), searchValue, system, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> find(String classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system, identityToken)
				                   .withClassification(iClassification)
				                   .withValue(searchValue)
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
	
	
	default List<IRelationshipValue<L, R, ?>> findAll(C classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findAll(classification.classificationName(), null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(C classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findAll(classification.classificationName(), value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(String classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findAll(classification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAll(String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findAll(classification, value, system, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findAll(String classification, String searchValue, ISystems<?> system, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system, identityToken)
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
	default Class<Q> findQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(ISearchable.class.getCanonicalName()))
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
			if (genericInterface instanceof ParameterizedType && clazz.contains(ISearchable.class.getCanonicalName()))
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
			if (genericInterface instanceof ParameterizedType && clazz.contains(ISearchable.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<S>) genericTypes[1];
			}
		}
		return null;
	}
}
