package com.guicedee.activitymaster.core.services.capabilities;

import com.entityassist.SCDEntity;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.events.EventThread;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.IProductService;
import com.guicedee.guicedinjection.GuiceContext;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.entityassist.SCDEntity.*;
import static com.guicedee.activitymaster.core.services.classifications.events.EventInvolvedPartiesClassifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

public interface IContainsProducts<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?, ?, ?>,
		C extends IClassification<?>,
		L, R,
		J extends IContainsProducts<P, S, Q, C, L, R, J>>
{
	default Optional<IRelationshipValue<L, R, ?>> findProduct(C classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findProduct(classification, null, enterprise, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(C classification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findProduct(classification.getName(), value, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(String classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findProduct(classification, null, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(String classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findProduct(classification, searchValue, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(C classification, String value, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findProduct(classification.getName(), value, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(String classification, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findProduct(classification, null, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(String classification, String searchValue, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findProduct(classification, searchValue, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(C classification, String value, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findProduct(classification.getName(), value, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(String classification, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findProduct(classification, null, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProduct(String classification, String searchValue, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findProduct(classification, searchValue, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findProductFirst(C classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findProduct(classification.getName(), searchValue, enterprise, true, false, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findProductFirst(C classification, String searchValue, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findProduct(classification.getName(), searchValue, enterprise, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findProduct(String classification, String searchValue, IEnterprise<?> enterprise, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findProductQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(enterprise, identityToken)
				                   .withClassification(iClassification)
				                   .withValue(searchValue)
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
	
	
	default List<IRelationshipValue<L, R, ?>> findProductsAll(C classification, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findProductsAll(classification.getName(), null, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductsAll(C classification, String value, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return findProductsAll(classification.getName(), value, originatingSystem.getEnterprise(), latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductsAll(C classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findProductsAll(classification.getName(), value, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductsAll(String classification, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findProductsAll(classification, null, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findProductsAll(String classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findProductsAll(classification, value, enterprise, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findProductsAll(String classification, String searchValue, IEnterprise<?> enterprise, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findProductQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(enterprise, identityToken)
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
	
	default boolean hasProductsBefore(C classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return numberOfProductsAll(classificationValue.getName(), enterprise, identityToken) > 0;
	}
	
	default boolean hasProductsBefore(C classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return numberOfProductsAll(classificationValue.getName(), value, enterprise, identityToken) > 0;
	}
	
	default boolean hasProductsBefore(String classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return numberOfProductsAll(classificationValue, value, enterprise, identityToken) > 0;
	}
	
	default boolean hasProducts(C classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return numberOfProducts(classificationValue.getName(), enterprise, identityToken) > 0;
	}
	
	default boolean hasProducts(C classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return numberOfProducts(classificationValue, value, enterprise, identityToken) > 0;
	}
	
	default boolean hasProducts(C classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return numberOfProducts(classificationValue.getName(), originatingSystem.getEnterprise(), identityToken) > 0;
	}
	
	default boolean hasProducts(C classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return numberOfProducts(classificationValue, value, originatingSystem.getEnterprise(), identityToken) > 0;
	}
	
	default long numberOfProducts(String classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return numberOfProducts(classificationValue, null, enterprise, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfProducts(String classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findProductsCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, enterprise, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, value)
		                             .inActiveRange(enterprise)
		                             .inDateRange()
		                             .canRead(enterprise, identityToken)
		                             .getCount();
	}
	
	default long numberOfProducts(C classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return numberOfProducts(classificationValue.getName(), value, enterprise, identityToken);
	}
	
	default long numberOfProductsAll(C classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return numberOfProductsAll(classificationValue.getName(), null, enterprise, identityToken);
	}
	
	default long numberOfProductsAll(String classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return numberOfProductsAll(classificationValue, null, enterprise, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfProductsAll(String classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findProductsCountableQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, enterprise, identityToken);
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
	
	default S stringToSecondaryProduct(String typeName, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IProductService<?> service = GuiceContext.get(IProductService.class);
		return (S) service.findProduct(typeName, enterprise, identityToken);
	}
	
	void configureAddableProduct(Q linkTable, P primary, S secondary, C classificationValue, String value, IEnterprise<?> enterprise);
	
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
	
	default IRelationshipValue<L, R, ?> addProduct(String typeName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addProduct(typeName, Classifications.NoClassification.classificationName(), value, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addProduct(String typeName, String classificationName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addProduct(typeName, classificationName, value, STRING_EMPTY, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addProduct(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addProduct(typeName, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addProduct(String secondaryName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addProduct(secondaryName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, SCDEntity.EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addProduct(String secondaryName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, LocalDateTime effectiveToDate, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addProduct(secondaryName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addProduct(@NotNull String secondaryName,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               LocalDateTime effectiveFromDate,
	                                               LocalDateTime effectiveToDate,
	                                               ISystems<?> originalSystemID,
	                                               @NotNull IEnterprise<?> enterprise,
	                                               UUID... identityToken)
	{
		Q tableForClassification = get(findAddableProductTableType());
		IClassificationService<?> classificationService = GuiceContext.get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationName, enterprise, identityToken);
		
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
		
		configureAddableProduct(tableForClassification, (P) this,
		                        stringToSecondaryProduct(secondaryName, enterprise, identityToken),
		                        (C) classification, value, originatingSystem.getEnterpriseID());
		
		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				.isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}
		if (EventThread.event.get() != null)
		{
			EventThread.event.get()
			                 .add((IEventClassification<?>) Created, " - " + classificationName + " - " + value, originatingSystem, identityToken);
		}
		return (IRelationshipValue<L, R, ?>) tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseProduct(@NotNull String type,
	                            @NotNull String classificationName,
	                            @NotNull IEnterprise<?> enterprise,
	                            UUID... identityToken)
	{
		return addOrReuseProduct(type, classificationName, null, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseProduct(@NotNull String type,
	                            @NotNull String classificationName,
	                            String value,
	                            @NotNull IEnterprise<?> enterprise,
	                            UUID... identityToken)
	{
		return addOrReuseProduct(type, classificationName, value, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseProduct(@NotNull String type,
	                            @NotNull String classificationName,
	                            String value,
	                            String originalSourceSystemUniqueID,
	                            @NotNull IEnterprise<?> enterprise,
	                            UUID... identityToken)
	{
		return addOrReuseProduct(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseProduct(@NotNull String type,
	                            @NotNull String classificationName,
	                            String value,
	                            String originalSourceSystemUniqueID,
	                            LocalDateTime effectiveFromDate,
	                            @NotNull IEnterprise<?> enterprise,
	                            UUID... identityToken)
	{
		return addOrReuseProduct(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseProduct(@NotNull String type,
	                            @NotNull String classificationName,
	                            String value,
	                            String originalSourceSystemUniqueID,
	                            LocalDateTime effectiveFromDate,
	                            LocalDateTime effectiveToDate,
	                            @NotNull IEnterprise<?> enterprise,
	                            UUID... identityToken)
	{
		return addOrReuseProduct(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuseProduct(@NotNull String type,
	                            @NotNull String classificationName,
	                            String value,
	                            String originalSourceSystemUniqueID,
	                            LocalDateTime effectiveFromDate,
	                            LocalDateTime effectiveToDate,
	                            ISystems<?> originalSystemID,
	                            @NotNull IEnterprise<?> enterprise,
	                            UUID... identityToken)
	{
		Q tableForClassification = get(findAddableProductTableType());
		S sType = stringToSecondaryProduct(type, enterprise, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, enterprise, identityToken);
		boolean exists = findProductTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return addProduct(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
		}
		else
		{
			tableForClassification = (Q) findProductTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken)
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
	                             @NotNull IEnterprise<?> enterprise,
	                             UUID... identityToken)
	{
		return addOrUpdateProduct(type, classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateProduct(@NotNull String type,
	                             @NotNull String classificationName,
	                             @NotNull IEnterprise<?> enterprise,
	                             UUID... identityToken)
	{
		return addOrUpdateProduct(type, classificationName, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateProduct(@NotNull String type,
	                             @NotNull String classificationName,
	                             String value,
	                             @NotNull IEnterprise<?> enterprise,
	                             UUID... identityToken)
	{
		return addOrUpdateProduct(type, classificationName, value, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateProduct(@NotNull String type,
	                             @NotNull String classificationName,
	                             String value,
	                             String originalSourceSystemUniqueID,
	                             @NotNull IEnterprise<?> enterprise,
	                             UUID... identityToken)
	{
		return addOrUpdateProduct(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateProduct(@NotNull String type,
	                             @NotNull String classificationName,
	                             String value,
	                             String originalSourceSystemUniqueID,
	                             LocalDateTime effectiveFromDate,
	                             @NotNull IEnterprise<?> enterprise,
	                             UUID... identityToken)
	{
		return addOrUpdateProduct(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateProduct(@NotNull String type,
	                             @NotNull String classificationName,
	                             String value,
	                             String originalSourceSystemUniqueID,
	                             LocalDateTime effectiveFromDate,
	                             LocalDateTime effectiveToDate,
	                             @NotNull IEnterprise<?> enterprise,
	                             UUID... identityToken)
	{
		return addOrUpdateProduct(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateProduct(@NotNull String type,
	                             @NotNull String classificationName,
	                             String value,
	                             String originalSourceSystemUniqueID,
	                             LocalDateTime effectiveFromDate,
	                             LocalDateTime effectiveToDate,
	                             ISystems<?> originalSystemID,
	                             @NotNull IEnterprise<?> enterprise,
	                             UUID... identityToken)
	{
		Q tableForClassification = get(findAddableProductTableType());
		S sType = stringToSecondaryProduct(type, enterprise, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, enterprise, identityToken);
		boolean exists = findProductTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{ return addProduct(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken); }
		
		tableForClassification = (Q) findProductTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return (IRelationshipValue<L, R, ?>) tableForClassification;
		}
		return update((IRelationshipValue<L, R, ?>)tableForClassification, type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> update(@NotNull IRelationshipValue<L, R, ?> original,
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
		archive((IRelationshipValue<L, R, ?>)original, identityToken);
		return addProduct(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> archive(@NotNull IRelationshipValue<L, R, ?> original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	default IRelationshipValue<L, R, ?> remove(@NotNull IRelationshipValue<L, R, ?> original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getDeletedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	@SuppressWarnings("rawtypes")
	default IRelationshipValue<L, R, ?> expire(@NotNull IRelationshipValue<L, R, ?> original, Duration when)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		entity.setEffectiveToDate(entity.getEffectiveToDate()
		                                    .plus(when));
		QueryBuilderSCD scd = (QueryBuilderSCD) entity.builder();
		scd.update(entity, when);
		return original;
	}
	
	private QueryBuilderRelationship<?, ?, ?, ?, ?, ?> findProductTypeQuery(String value, IEnterprise<?> enterprise, Q tableForClassification, S sType, Classification classification, UUID... identityToken)
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
