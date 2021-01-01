package com.guicedee.activitymaster.core.services.capabilities.bases;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.UUID;

import static com.guicedee.guicedinjection.GuiceContext.*;

public interface ICountable<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?, L, R>,
		C extends IClassificationValue<?>,
		L, R>
{
	default boolean hasBefore(C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOfAll(classificationValue.classificationName(), system, identityToken) > 0;
	}
	
	default boolean hasBefore(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOfAll(classificationValue.classificationName(), value, system, identityToken) > 0;
	}
	
	default boolean hasBefore(String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAll(classificationValue, value, system, identityToken) > 0;
	}
	
	default boolean has(C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOf(classificationValue.classificationName(), system, identityToken) > 0;
	}
	
	default boolean has(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOf(classificationValue, value, system, identityToken) > 0;
	}

	default long numberOf(String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOf(classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOf(String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, value)
		                             .inActiveRange(system)
		                             .inDateRange()
		                             .canRead(system, identityToken)
		                             .getCount();
	}
	
	default long numberOf(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOf(classificationValue.classificationName(), value, system, identityToken);
	}
	
	default long numberOfAll(C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOfAll(classificationValue.classificationName(), null, system, identityToken);
	}
	
	default long numberOfAll(String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAll(classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfAll(String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findCountableQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, value)
		                             .getCount();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findCountableQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(ICountable.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
}
