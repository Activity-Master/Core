package com.guicedee.activitymaster.core.services.capabilities.bases;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.validation.constraints.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.UUID;

import static com.guicedee.guicedinjection.GuiceContext.*;

public interface ICountableType<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTypesTable<P, S, ?, ? extends QueryBuilderRelationshipClassificationTypes, T, ?, ?, L, R>,
		C extends IClassification<?>,
		T extends ITypeValue<?>,
		L, R>
{
	default boolean hasBefore(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification, system, identityToken);
		}
		return numberOfAll(typeValue, classificationValue.getName(), system, identityToken) > 0;
	}
	
	default boolean hasBefore(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification, system, identityToken);
		}
		return numberOfAll(typeValue.classificationValue(), classificationValue.getName(), value, system, identityToken) > 0;
	}
	
	default boolean hasBefore(T typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAll(typeValue.classificationValue(), classificationValue, value, system, identityToken) > 0;
	}
	
	default boolean has(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification, system, identityToken);
		}
		return numberOf(typeValue, classificationValue.getName(), system, identityToken) > 0;
	}
	
	default boolean has(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification, system, identityToken);
		}
		return numberOf(typeValue, classificationValue, value, system, identityToken) > 0;
	}
	
	
	default long numberOf(String typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOf(typeValue, classificationValue, null, system, identityToken);
	}
	
	default long numberOf(T typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOf(typeValue.classificationValue(), classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOf(String typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) null, value)
		                             .withClassification(classification)
		                             .withType(typeValue, system, identityToken)
		                             .inActiveRange(system.getEnterprise(), identityToken)
		                             .inDateRange()
		                             .canRead(system, identityToken)
		                             .getCount();
	}
	
	default long numberOf(T typeValue, C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification, system, identityToken);
		}
		return numberOf(typeValue.classificationValue(), classificationValue.getName(), value, system, identityToken);
	}
	
	default long numberOfAll(T typeValue, C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification, system, identityToken);
		}
		return numberOfAll(typeValue.classificationValue(), classificationValue.getName(), null, system, identityToken);
	}
	
	default long numberOfAll(String typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAll(typeValue, classificationValue, null, system, identityToken);
	}
	
	default long numberOfAll(T typeValue, String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAll(typeValue.classificationValue(), classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfAll(String typeValue, String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findCountableQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) null, value)
		                             .withClassification(classification)
		                             .withType(typeValue, system, identityToken)
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
