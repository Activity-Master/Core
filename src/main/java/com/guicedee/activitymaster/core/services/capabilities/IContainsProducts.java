package com.guicedee.activitymaster.core.services.capabilities;

import com.entityassist.SCDEntity;
import com.entityassist.enumerations.OrderByType;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.*;
import com.guicedee.activitymaster.core.db.abstraction.builders.*;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.events.EventThread;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.system.*;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.entityassist.SCDEntity.*;
import static com.guicedee.activitymaster.core.services.classifications.events.EventInvolvedPartiesClassifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

public interface IContainsProducts<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?, ?, ?>,
		C extends IClassificationValue<?>,
		L, R,
		J extends IContainsProducts<P, S, Q, C, L, R, J>>
{
	default Optional<IRelationshipValue<L, R, ?>> findByProduct(R byType, String classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		Q relationshipTable = get(findProductQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IEnterprise<?> enterprise = system.getEnterprise();
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findLink(null,(S) byType)
				                   .inActiveRange(enterprise, identityToken)
				                   .withClassification(iClassification)
				                   .withValue(value)
				                   .inDateRange()
				                   .withEnterprise(enterprise)
				                   .canRead(system, identityToken);
		if (first)
		{ queryBuilderRelationshipClassification.setMaxResults(1); }
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate"), OrderByType.DESC); }
		
		//noinspection rawtypes,unchecked
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	default List<IRelationshipValue<L, R, ?>> findAllByProduct(R byType, String classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		Q relationshipTable = get(findProductQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IEnterprise<?> enterprise = system.getEnterprise();
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findLink(null,(S) byType)
				                   .inActiveRange(enterprise, identityToken)
				                   .withClassification(iClassification)
				                   .withValue(value)
				                   .inDateRange()
				                   .withEnterprise(enterprise)
				                   .canRead(system, identityToken);
		if (first)
		{ queryBuilderRelationshipClassification.setMaxResults(1); }
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate"), OrderByType.DESC); }
		
		//noinspection rawtypes,unchecked
		return (List) queryBuilderRelationshipClassification.getAll();
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(C classification, ISystems<?> system, UUID... identityToken)
	{
		return findProduct(classification, null, system, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(C classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return findProduct(((IClassification)classification).getName(), value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(String classification, ISystems<?> system, UUID... identityToken)
	{
		
		return findProduct(classification, null, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		
		return findProduct(classification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(C classification, String value, boolean first, ISystems<?> system, UUID... identityToken)
	{
		
		return findProduct(((IClassification)classification).getName(), value, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(String classification, boolean first, ISystems<?> system, UUID... identityToken)
	{
		
		return findProduct(classification, null, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(String classification, String searchValue, boolean first, ISystems<?> system, UUID... identityToken)
	{
		
		return findProduct(classification, searchValue, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(C classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findProduct(((IClassification)classification).getName(), value, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(String classification, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findProduct(classification, null, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(String classification, String searchValue, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findProduct(classification, searchValue, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProductFirst(C classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		
		return findProduct(((IClassification)classification).getName(), searchValue, system, true, false, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findProductFirst(C classification, String searchValue, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findProduct(((IClassification)classification).getName(), searchValue, system, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findProduct(String classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findProductQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IEnterprise<?> enterprise = system.getEnterprise();
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(enterprise, identityToken)
				                   .withClassification(iClassification)
				                   .withValue(searchValue)
				                   .inDateRange()
				                   .withEnterprise(enterprise)
				                   .canRead(system, identityToken);
		if (first)
		{ queryBuilderRelationshipClassification.setMaxResults(1); }
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate"), OrderByType.DESC); }
		
		//noinspection rawtypes
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	
	
	default List<IRelationshipValue<L, R, ?>> findProductsAll(C classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findProductsAll(((IClassification)classification).getName(), null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductsAll(C classification, String value, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return findProductsAll(((IClassification)classification).getName(), value, originatingSystem, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductsAll(String classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findProductsAll(classification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductsAll(String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findProductsAll(classification, value, system, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findProductsAll(String classification, String searchValue, ISystems<?> system, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findProductQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IEnterprise<?> enterprise = system.getEnterprise();
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(enterprise, identityToken)
				                   .withValue(searchValue)
				                   .withClassification(iClassification)
				                   .inDateRange()
				                   .canRead(system, identityToken);
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate"), OrderByType.DESC); }
		return (List) queryBuilderRelationshipClassification.getAll();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findProductQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsProducts.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findProductPrimaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsProducts.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}
	
	default boolean hasProductsBefore(C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductsAll(((IClassification)classificationValue).getName(), system, identityToken) > 0;
	}
	
	default boolean hasProductsBefore(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductsAll(((IClassification)classificationValue).getName(), value, system, identityToken) > 0;
	}
	
	default boolean hasProductsBefore(String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductsAll(classificationValue, value, system, identityToken) > 0;
	}
	
	default boolean hasProducts(C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProducts(((IClassification)classificationValue).getName(), system, identityToken) > 0;
	}
	
	default boolean hasProducts(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProducts(classificationValue, value, system, identityToken) > 0;
	}
	
	default long numberOfProducts(String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProducts(classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfProducts(String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findProductsCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IEnterprise<?> enterprise = system.getEnterprise();
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, value)
		                             .inActiveRange(enterprise)
		                             .inDateRange()
		                             .canRead(system, identityToken)
		                             .getCount();
	}
	
	default long numberOfProducts(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProducts(((IClassification)classificationValue).getName(), value, system, identityToken);
	}
	
	default long numberOfProductsAll(C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductsAll(((IClassification)classificationValue).getName(), null, system, identityToken);
	}
	
	default long numberOfProductsAll(String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfProductsAll(classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfProductsAll(String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findProductsCountableQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IEnterprise<?> enterprise = system.getEnterprise();
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, value)
		                             .getCount();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findProductsCountableQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsProducts.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	default S stringToSecondaryProduct(String productName, ISystems<?> system, UUID... identityToken)
	{
		IProductService<?> service = GuiceContext.get(IProductService.class);
		return (S) service.findProduct(productName, system, identityToken);
	}
	
	void configureAddableProduct(Q linkTable, P primary, S secondary, C classificationValue, String value, ISystems<?> system);
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findAddableProductTableType()
	{
		Type[] genericInterfaces;
		Class<?> currentClass = getClass();
		while (currentClass != Object.class)
		{
			genericInterfaces = currentClass.getGenericInterfaces();
			for (Type genericInterface : genericInterfaces)
			{
				String clazz = genericInterface.getTypeName();
				if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsProducts.class.getCanonicalName()))
				{
					Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
					return (Class<Q>) genericTypes[2];
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return null;
	}
	
	default IRelationshipValue<L, R, ?> addProduct(String productName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addProduct(productName, Classifications.NoClassification.classificationName(), value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addProduct(String productName, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addProduct(productName, classificationName, value, STRING_EMPTY, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addProduct(String productName, String classificationName, String value, String originalSourceSystemUniqueID, ISystems<?> system, UUID... identityToken)
	{
		return addProduct(productName, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addProduct(String productName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, ISystems<?> system, UUID... identityToken)
	{
		return addProduct(productName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, SCDEntity.EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addProduct(String productName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, LocalDateTime effectiveToDate, ISystems<?> system, UUID... identityToken)
	{
		return addProduct(productName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addProduct(@NotNull String productName,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               LocalDateTime effectiveFromDate,
	                                               LocalDateTime effectiveToDate,
	                                               ISystems<?> originalSystemID,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		IEnterprise<?> enterprise = system.getEnterprise();
		Q tableForClassification = get(findAddableProductTableType());
		IClassificationService<?> classificationService = GuiceContext.get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationName, system, identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setValue(Strings.nullToEmpty(value));
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemID(((Systems) (originalSystemID == null ? system : originalSystemID)));
		tableForClassification.setOriginalSourceSystemUniqueID(originalSourceSystemUniqueID);
		tableForClassification.setEffectiveFromDate(effectiveFromDate);
		tableForClassification.setEffectiveToDate(effectiveToDate);
		tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
		tableForClassification.setClassificationID((Classification) classification);
		
		configureAddableProduct(tableForClassification, (P) this,
		                        stringToSecondaryProduct(productName, system, identityToken),
		                        (C) classification, value, system);
		
		tableForClassification.persist();
		
			tableForClassification.createDefaultSecurity(system, identityToken);
		
		if (EventThread.event.get() != null)
		{
			EventThread.event.get()
			                 .add((IEventClassification<?>) Added, " - " + classificationName + " - " + value, system, identityToken);
		}
		return (IRelationshipValue<L, R, ?>) tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseProduct(@NotNull String type,
	                            @NotNull String classificationName,
	                            @NotNull ISystems<?> system,
	                            UUID... identityToken)
	{
		return addOrReuseProduct(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseProduct(@NotNull String type,
	                            @NotNull String classificationName,
	                            String value,
	                            @NotNull ISystems<?> system,
	                            UUID... identityToken)
	{
		return addOrReuseProduct(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseProduct(@NotNull String type,
	                            @NotNull String classificationName,
	                            String value,
	                            String originalSourceSystemUniqueID,
	                            @NotNull ISystems<?> system,
	                            UUID... identityToken)
	{
		return addOrReuseProduct(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseProduct(@NotNull String type,
	                            @NotNull String classificationName,
	                            String value,
	                            String originalSourceSystemUniqueID,
	                            LocalDateTime effectiveFromDate,
	                            @NotNull ISystems<?> system,
	                            UUID... identityToken)
	{
		return addOrReuseProduct(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseProduct(@NotNull String type,
	                            @NotNull String classificationName,
	                            String value,
	                            String originalSourceSystemUniqueID,
	                            LocalDateTime effectiveFromDate,
	                            LocalDateTime effectiveToDate,
	                            @NotNull ISystems<?> system,
	                            UUID... identityToken)
	{
		return addOrReuseProduct(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuseProduct(@NotNull String type,
	                            @NotNull String classificationName,
	                            String value,
	                            String originalSourceSystemUniqueID,
	                            LocalDateTime effectiveFromDate,
	                            LocalDateTime effectiveToDate,
	                            ISystems<?> originalSystemID,
	                            @NotNull ISystems<?> system,
	                            UUID... identityToken)
	{
		Q tableForClassification = get(findAddableProductTableType());
		S sType = stringToSecondaryProduct(type, system, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IEnterprise<?> enterprise = system.getEnterprise();
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		boolean exists = findProductTypeQuery(value, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return addProduct(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		else
		{
			tableForClassification = (Q) findProductTypeQuery(value, system, tableForClassification, sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return (IRelationshipValue<L, R, ?>) tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateProduct(@NotNull String type,
	                             @NotNull C classificationName,
	                             String value,
	                             String originalSourceSystemUniqueID,
	                             LocalDateTime effectiveFromDate,
	                             LocalDateTime effectiveToDate,
	                             @NotNull ISystems<?> system,
	                             UUID... identityToken)
	{
		return addOrUpdateProduct(type, ((IClassification)classificationName).getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateProduct(@NotNull String type,
	                             @NotNull String classificationName,
	                             @NotNull ISystems<?> system,
	                             UUID... identityToken)
	{
		return addOrUpdateProduct(type, classificationName, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateProduct(@NotNull String type,
	                             @NotNull String classificationName,
	                             String value,
	                             @NotNull ISystems<?> system,
	                             UUID... identityToken)
	{
		return addOrUpdateProduct(type, classificationName, value, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateProduct(@NotNull String type,
	                             @NotNull String classificationName,
	                             String value,
	                             String originalSourceSystemUniqueID,
	                             @NotNull ISystems<?> system,
	                             UUID... identityToken)
	{
		return addOrUpdateProduct(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateProduct(@NotNull String type,
	                             @NotNull String classificationName,
	                             String value,
	                             String originalSourceSystemUniqueID,
	                             LocalDateTime effectiveFromDate,
	                             @NotNull ISystems<?> system,
	                             UUID... identityToken)
	{
		return addOrUpdateProduct(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateProduct(@NotNull String type,
	                             @NotNull String classificationName,
	                             String value,
	                             String originalSourceSystemUniqueID,
	                             LocalDateTime effectiveFromDate,
	                             LocalDateTime effectiveToDate,
	                             @NotNull ISystems<?> system,
	                             UUID... identityToken)
	{
		return addOrUpdateProduct(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateProduct(@NotNull String type,
	                             @NotNull String classificationName,
	                             String value,
	                             String originalSourceSystemUniqueID,
	                             LocalDateTime effectiveFromDate,
	                             LocalDateTime effectiveToDate,
	                             ISystems<?> originalSystemID,
	                             @NotNull ISystems<?> system,
	                             UUID... identityToken)
	{
		Q tableForClassification = get(findAddableProductTableType());
		S sType = stringToSecondaryProduct(type, system, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IEnterprise<?> enterprise = system.getEnterprise();
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		boolean exists = findProductTypeQuery(value, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{ return addProduct(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken); }
		
		tableForClassification = (Q) findProductTypeQuery(value, system, tableForClassification, sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return (IRelationshipValue<L, R, ?>) tableForClassification;
		}
		return updateProduct((IRelationshipValue<L, R, ?>)tableForClassification, type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> updateProduct(@NotNull IRelationshipValue<L, R, ?> original,
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
		archiveProduct((IRelationshipValue<L, R, ?>)original, identityToken);
		return addProduct(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> archiveProduct(@NotNull IRelationshipValue<L, R, ?> original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	default IRelationshipValue<L, R, ?> removeProduct(@NotNull IRelationshipValue<L, R, ?> original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getDeletedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	@SuppressWarnings("rawtypes")
	default IRelationshipValue<L, R, ?> expireProduct(@NotNull IRelationshipValue<L, R, ?> original, Duration when)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		entity.setEffectiveToDate(entity.getEffectiveToDate()
		                                    .plus(when));
		QueryBuilderSCD scd = (QueryBuilderSCD) entity.builder();
		scd.update(entity, when);
		return original;
	}
	
	private QueryBuilderRelationship<?, ?, ?, ?, ?, ?> findProductTypeQuery(String value, ISystems<?> system, Q tableForClassification, S sType, Classification classification, UUID... identityToken)
	{
		IEnterprise<?> enterprise = system.getEnterprise();
		return tableForClassification.builder()
		                             .findLink((P) this, sType, value)
		                             .inActiveRange(enterprise)
		                             .inDateRange()
		                             .withClassification(classification)
		                             .withEnterprise(enterprise)
		                             .canCreate(enterprise, identityToken);
	}
}
