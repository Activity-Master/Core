package com.guicedee.activitymaster.core.services.capabilities;

import com.entityassist.SCDEntity;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.events.EventThread;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.IResourceItemService;
import com.guicedee.activitymaster.core.services.threads.StoreResourceItemThread;
import com.guicedee.activitymaster.core.systems.ResourceItemSystem;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.JobService;

import jakarta.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.entityassist.SCDEntity.*;
import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.core.services.classifications.events.EventInvolvedPartiesClassifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;
import static jakarta.persistence.criteria.JoinType.*;

@SuppressWarnings("Duplicates")
public interface IContainsResourceItems<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?, L, R>,
		C extends IClassificationValue<?>,
		L, R,
		J extends IContainsResourceItems<P, S, Q, C,L, R, J>>
{
	
	default double sumAll(C reesourceItemType, ISystems<?> originatingSystem, UUID identityToken)
	{
		List<String> results = getValues(reesourceItemType, null, originatingSystem, identityToken);
		double d = 0.0d;
		for (String result : results)
		{
			Double D = Double.parseDouble(result);
			d += D;
		}
		return d;
	}
	
	/**
	 * Returns either a List with Strings, or a List with Object[] for each row and values returned
	 *
	 * @param resourceItemType  The value to apply
	 * @param originatingSystem The system coming from
	 * @param identityToken     The identity token to use
	 * @param values            Any additional values to select
	 * @return The result of List&gt;String&lt; or List&gt;Object[]&lt;
	 */
	default List getValues(C resourceItemType, String searchValue, ISystems<?> originatingSystem, UUID identityToken, C... values)
	{
		Q activityMasterIdentity = get(findQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		List<C> fetching = new ArrayList<>();
		fetching.add(resourceItemType);
		if (values != null)
		{
			fetching.addAll(Arrays.asList(values));
		}
		
		WarehouseBaseTable base = (WarehouseBaseTable) this;
		QueryBuilderTable baseTableBuilder = (QueryBuilderTable) base.builder();
		
		QueryBuilderRelationship builder = activityMasterIdentity.builder();
		List<JoinExpression> joins = new ArrayList<>();
		
		for (C valuesToGet : fetching)
		{
			IClassification<?> classification = classificationService.find(valuesToGet, originatingSystem, identityToken);
			
			JoinExpression newExpression = new JoinExpression();
			baseTableBuilder.join(baseTableBuilder.getAttribute("classifications"), LEFT, newExpression);
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("classificationID")), Equals, classification);
			
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("activeFlagID")), InList, get(IActiveFlagService.class).findActiveRange(originatingSystem.getEnterpriseID(), identityToken));
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("enterpriseID")), Equals, originatingSystem.getEnterpriseID());
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("effectiveFromDate")), LessThanEqualTo, LocalDateTime.now());
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("effectiveToDate")), GreaterThanEqualTo, LocalDateTime.now());
			
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get(builder.getPrimaryAttribute()
			                                                 .getName())), Equals, base.getId());
			
			baseTableBuilder.selectColumn(newExpression.getGeneratedRoot()
			                                           .get("value"));
			joins.add(newExpression);
		}
		if (searchValue != null && !searchValue.isEmpty())
		{
			baseTableBuilder.where(baseTableBuilder.getRoot()
			                                       .get("value"), Equals, searchValue);
		}
		List list = baseTableBuilder.getAll();
		return list;
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(C classification, ISystems<?> system, UUID... identityToken)
	{
		return findResourceItem(classification, null, system, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(C classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return findResourceItem(classification.classificationName(), value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(String classification, ISystems<?> system, UUID... identityToken)
	{
		
		return findResourceItem(classification, null, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		
		return findResourceItem(classification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(C classification, String value, boolean first, ISystems<?> system, UUID... identityToken)
	{
		
		return findResourceItem(classification.classificationName(), value, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(String classification, boolean first, ISystems<?> system, UUID... identityToken)
	{
		
		return findResourceItem(classification, null, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(String classification, String searchValue, boolean first, ISystems<?> system, UUID... identityToken)
	{
		
		return findResourceItem(classification, searchValue, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(C classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findResourceItem(classification.classificationName(), value, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(String classification, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findResourceItem(classification, null, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(String classification, String searchValue, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findResourceItem(classification, searchValue, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItemFirst(C classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		
		return findResourceItem(classification.classificationName(), searchValue, system, true, false, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItemFirst(C classification, String searchValue, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findResourceItem(classification.classificationName(), searchValue, system, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(String classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
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
				                   .withEnterprise(system.getEnterprise())
				                   .canRead(system, identityToken);
		if (first)
		{ queryBuilderRelationshipClassification.setMaxResults(1); }
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		
		//noinspection rawtypes
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	
	
	default List<IRelationshipValue<L, R, ?>> findResourceItemsAll(C classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findResourceItemsAll(classification.classificationName(), null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findResourceItemsAll(C classification, String value, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return findResourceItemsAll(classification.classificationName(), value, originatingSystem, latest, identityToken);
	}

	default List<IRelationshipValue<L, R, ?>> findResourceItemsAll(String classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findResourceItemsAll(classification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findResourceItemsAll(String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findResourceItemsAll(classification, value, system, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findResourceItemsAll(String classification, String searchValue, ISystems<?> system, boolean latest, UUID... identityToken)
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
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsResourceItems.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findContainsResourceItemsPrimaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsResourceItems.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	default List<IRelationshipValue<L, R, ?>> findAllResourceItems(ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findQueryRelationshipTableType());
		return (List<IRelationshipValue<L, R, ?>>) activityMasterIdentity.builder()
		                                                                 .findParentLink((P) this, null)
		                                                                 .inActiveRange(system.getEnterpriseID())
		                                                                 .inDateRange()
		                                                                 .canRead(system, identityToken)
		                                                                 .getAll();
	}
	
	
	default boolean hasBeforeResourceItem(C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification, system, identityToken);
		}
		return numberOfAllResourceItems(classificationValue.classificationName(), system, identityToken) > 0;
	}
	
	default boolean hasBeforeResourceItem(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOfAllResourceItems(classificationValue.classificationName(), value, system, identityToken) > 0;
	}
	
	default boolean hasBeforeResourceItem(String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAllResourceItems(classificationValue, value, system, identityToken) > 0;
	}
	
	default boolean hasResourceItems(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOfResourceItems(classificationValue, value, system, identityToken) > 0;
	}
	
	default boolean hasResourceItems(C classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification, originatingSystem, identityToken);
		}
		return numberOfResourceItems(classificationValue.classificationName(), originatingSystem, identityToken) > 0;
	}
	
	default long numberOfResourceItems(String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfResourceItems(classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfResourceItems(String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findResourceItemCountableQueryRelationshipTableType());
		
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
	
	default long numberOfResourceItems(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOfResourceItems(classificationValue.classificationName(), value, system, identityToken);
	}
	
	default long numberOfAllResourceItems(C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,system, identityToken);
		}
		return numberOfAllResourceItems(classificationValue.classificationName(), null, system, identityToken);
	}
	
	default long numberOfAllResourceItems(String classificationValue, ISystems<?> system, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAllResourceItems(classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfAllResourceItems(String classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findResourceItemCountableQueryRelationshipTableType());
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
	default Class<Q> findResourceItemCountableQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsResourceItems.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	default IRelationshipValue<L, R, ?> addResourceItem(R typeAdd,
	                                        String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        LocalDateTime effectiveToDate,
	                                        ISystems<?> originalSystemID,
	                                        ISystems<?> system,
	                                        UUID... identityToken )
	{
		Q tableForClassification = get(findResourceItemAddableTableType());
		IClassificationService<?> classificationService = GuiceContext.get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationName, system, identityToken);
		if (originalSystemID != null)
		{
			system = originalSystemID;
		}
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setValue(Strings.nullToEmpty(value));
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemUniqueID(originalSourceSystemUniqueID);
		tableForClassification.setEffectiveFromDate(effectiveFromDate);
		tableForClassification.setEffectiveToDate(effectiveToDate);
		tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
		tableForClassification.setClassificationID((Classification) classification);
		
		configureResourceItemAddable(tableForClassification, (P) this,
						(S)typeAdd,
		                 (C) classification, value, system);
		
		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				.isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(system, identityToken);
		}
		if (EventThread.event.get() != null)
		{
			EventThread.event.get()
			                 .add((IEventClassification<?>) Created, " - " + classificationName + " - " + value, system, identityToken);
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(C classificationValue,
	                                                            IResourceType<?> type,
	                                                            String searchValue,
	                                                            String storeValue,
	                                                            byte[] data,
	                                                            String mimeType,
	                                                            ISystems<?> system,
	                                                            UUID... identityToken)
	{
		Q tableForClassification = get(findQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .withValue(searchValue)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canCreate(system.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification = (Q) addResourceItem(classificationValue, type, storeValue, data, mimeType, system, identityToken);
		}
		else
		{
			if (Strings.nullToEmpty(storeValue)
			           .equals(exists.get()
			                         .getValue()))
			{
				return exists.get();
			}
			tableForClassification = exists.get();
			
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(system.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
			
			Q newTableForClassification = get(findQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) system);
			newTableForClassification.setOriginalSourceSystemID(tableForClassification.getSystemID());
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(system.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(storeValue == null ? STRING_EMPTY : storeValue);
			newTableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			configureResourceItemLinkValue(newTableForClassification, (P) tableForClassification.getPrimary(), (S) tableForClassification.getSecondary(),
			                               classification, storeValue,
			                               system);
			newTableForClassification.persist();
			
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				newTableForClassification.createDefaultSecurity(system, identityToken);
			}
		}
		return tableForClassification;
	}
	
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull R sType,
	                                                            @NotNull IResourceType<?> type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            String mimeType,
	                                                            byte[] data,
	                                                            String originalSourceSystemUniqueID,
	                                                            LocalDateTime effectiveFromDate,
	                                                            LocalDateTime effectiveToDate,
	                                                            ISystems<?> originalSystemID,
	                                                            @NotNull ISystems<?> system,
	                                                            UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemAddableTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		
		
		boolean exists = findResourceItemTypeQuery(value, system, tableForClassification,(R) sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{ return addOrUpdateResourceItem((C)classification,type, value,value, data,mimeType,originalSystemID, identityToken); }
		
		tableForClassification = (Q) findResourceItemTypeQuery(value, system, tableForClassification,(R)  sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return tableForClassification;
		}
		return updateResourceItem(tableForClassification,(S) sType, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addResourceItem(C resourceClassification,
	                                                    IResourceType<?> type,
	                                                    String storeValue,
	                                                    byte[] data,
	                                                    String mimeType,
	                                                    ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = GuiceContext.get(findQueryRelationshipTableType());
		
		IResourceItemService<?> service = GuiceContext.get(IResourceItemService.class);
		ResourceItem item = (ResourceItem) service.create(type, mimeType, system, identityToken);
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(resourceClassification, system, identityToken);
		
		boolean async = GuiceContext.get(ActivityMasterConfiguration.class)
		                            .isAsyncEnabled();
		
		if(data != null)
		{
			StoreResourceItemThread storeThread = GuiceContext.get(StoreResourceItemThread.class);
			storeThread.setItem(item);
			storeThread.setData(data);
			storeThread.setIdentifyingToken(identityToken);
			storeThread.setOriginatingSystem(system);
			if (async)
			{
				JobService.getInstance()
				          .addJob("StoreResourceItemThread", storeThread);
			}
			else
			{
				storeThread.run();
			}
		}
		
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setValue(storeValue == null ? STRING_EMPTY : storeValue);
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setClassificationID(classification);
		tableForClassification.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                                                .getActiveFlag(system.getEnterpriseID(), identityToken));
		
		configureResourceItemLinkValue(tableForClassification, (P) this, (S) item, classification, tableForClassification.getValue(), system);
		
		tableForClassification.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(system, identityToken);
		}
		
		return tableForClassification;
	}
	
	void configureResourceItemLinkValue(Q linkTable, P primary, S secondary, IClassification<?> classificationValue, String value, ISystems<?> system);
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuseResourceItem(C classificationValue,
	                                                           IResourceType<?> type,
	                                                           String searchValue,
	                                                           String storeValue,
	                                                           byte[] data,
	                                                           String mimeType,
	                                                           ISystems<?> system,
	                                                           UUID... identityToken)
	{
		Q tableForClassification = get(findQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .withValue(searchValue)
		                                                         .withEnterprise(system.getEnterpriseID())
		                                                         .canCreate(system.getEnterpriseID(), identityToken)
		                                                         .get();
		
		if (exists.isEmpty())
		{
			tableForClassification = (Q) addResourceItem(classificationValue, type, storeValue, data, mimeType, system, identityToken);
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	@NotNull
	default S stringToResourceItemSecondary(String typeName, ISystems<?> system, UUID... identityToken)
	{
		IResourceItemService<?> service = GuiceContext.get(IResourceItemService.class);
		try
		{
			return (S) service.findResourceItemType(typeName, system, identityToken);
		}catch (NoSuchElementException e)
		{
			return (S) service.createType(typeName, typeName, system, identityToken);
		}
	}
	
	void configureResourceItemAddable(Q linkTable, P primary, S secondary, C classificationValue, String value, ISystems<?> system);
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findResourceItemAddableTableType()
	{
		Type[] genericInterfaces;
		Class<?> currentClass = getClass();
		while (currentClass != Object.class)
		{
			genericInterfaces = currentClass.getGenericInterfaces();
			for (Type genericInterface : genericInterfaces)
			{
				String clazz = genericInterface.getTypeName();
				if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsResourceItems.class.getCanonicalName()))
				{
					Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
					return (Class<Q>) genericTypes[2];
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return null;
	}
	
	default IRelationshipValue<L, R, ?> addResourceItem(R typeName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addResourceItem(typeName, Classifications.NoClassification.classificationName(), value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addResourceItem(R typeName, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		return addResourceItem(typeName, classificationName, value, STRING_EMPTY, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addResourceItem(R typeName, String classificationName, String value, String originalSourceSystemUniqueID, ISystems<?> system, UUID... identityToken)
	{
		return addResourceItem(typeName, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addResourceItem(R secondaryName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, ISystems<?> system, UUID... identityToken)
	{
		return addResourceItem(secondaryName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, SCDEntity.EndOfTime, system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addResourceItem(R secondaryName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, LocalDateTime effectiveToDate, ISystems<?> system, UUID... identityToken)
	{
		return addResourceItem(secondaryName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	
	
	default IRelationshipValue<L, R, ?> addOrReuseResourceItem(@NotNull R type,
	                                                           @NotNull String classificationName,
	                                                           @NotNull ISystems<?> system,
	                                                           UUID... identityToken)
	{
		return addOrReuseResourceItem(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseResourceItem(@NotNull R type,
	                                                           @NotNull String classificationName,
	                                                           String value,
	                                                           @NotNull ISystems<?> system,
	                                                           UUID... identityToken)
	{
		return addOrReuseResourceItem(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseResourceItem(@NotNull R type,
	                                                           @NotNull String classificationName,
	                                                           String value,
	                                                           String originalSourceSystemUniqueID,
	                                                           @NotNull ISystems<?> system,
	                                                           UUID... identityToken)
	{
		return addOrReuseResourceItem(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseResourceItem(@NotNull R type,
	                                                           @NotNull String classificationName,
	                                                           String value,
	                                                           String originalSourceSystemUniqueID,
	                                                           LocalDateTime effectiveFromDate,
	                                                           @NotNull ISystems<?> system,
	                                                           UUID... identityToken)
	{
		return addOrReuseResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseResourceItem(@NotNull R type,
	                                                           @NotNull String classificationName,
	                                                           String value,
	                                                           String originalSourceSystemUniqueID,
	                                                           LocalDateTime effectiveFromDate,
	                                                           LocalDateTime effectiveToDate,
	                                                           @NotNull ISystems<?> system,
	                                                           UUID... identityToken)
	{
		return addOrReuseResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuseResourceItem(@NotNull R type,
	                                                           @NotNull String classificationName,
	                                                           String value,
	                                                           String originalSourceSystemUniqueID,
	                                                           LocalDateTime effectiveFromDate,
	                                                           LocalDateTime effectiveToDate,
	                                                           ISystems<?> originalSystemID,
	                                                           @NotNull ISystems<?> system,
	                                                           UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemAddableTableType());
		S sType = (S) type;
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		boolean exists = findResourceItemTypeQuery(value, system, tableForClassification, type, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return (Q) addResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		else
		{
			tableForClassification = (Q) findResourceItemTypeQuery(value, system, tableForClassification,(R) sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull R type,
	                                                            @NotNull C classificationName,
	                                                            String value,
	                                                            String originalSourceSystemUniqueID,
	                                                            LocalDateTime effectiveFromDate,
	                                                            LocalDateTime effectiveToDate,
	                                                            @NotNull ISystems<?> system,
	                                                            UUID... identityToken)
	{
		return addOrUpdateResourceItem(type, classificationName.classificationName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull R type,
	                                                            @NotNull String classificationName,
	                                                            @NotNull ISystems<?> system,
	                                                            UUID... identityToken)
	{
		return addOrUpdateResourceItem(type, classificationName, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull R type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            @NotNull ISystems<?> system,
	                                                            UUID... identityToken)
	{
		return addOrUpdateResourceItem(type, classificationName, value, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull R type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            String originalSourceSystemUniqueID,
	                                                            @NotNull ISystems<?> system,
	                                                            UUID... identityToken)
	{
		return addOrUpdateResourceItem(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull R type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            String originalSourceSystemUniqueID,
	                                                            LocalDateTime effectiveFromDate,
	                                                            @NotNull ISystems<?> system,
	                                                            UUID... identityToken)
	{
		return addOrUpdateResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull R type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            String originalSourceSystemUniqueID,
	                                                            LocalDateTime effectiveFromDate,
	                                                            LocalDateTime effectiveToDate,
	                                                            @NotNull ISystems<?> system,
	                                                            UUID... identityToken)
	{
		return addOrUpdateResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull R type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            String originalSourceSystemUniqueID,
	                                                            LocalDateTime effectiveFromDate,
	                                                            LocalDateTime effectiveToDate,
	                                                            ISystems<?> originalSystemID,
	                                                            @NotNull ISystems<?> system,
	                                                            UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemAddableTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName,system,identityToken);
		boolean exists = findResourceItemTypeQuery(value, system, tableForClassification, type, classification, identityToken).getCount() > 0;
		if (!exists)
		{ return addResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken); }
		
		tableForClassification = (Q) findResourceItemTypeQuery(value, system, tableForClassification, type, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return tableForClassification;
		}
		return updateResourceItem(tableForClassification, type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> updateResourceItem(@NotNull IRelationshipValue<L, R, ?> original,
	                                           @NotNull S type,
	                                           @NotNull String classificationName,
	                                           String value,
	                                           String originalSourceSystemUniqueID,
	                                           LocalDateTime effectiveFromDate,
	                                           LocalDateTime effectiveToDate,
	                                           ISystems<?> originalSystemID,
	                                           @NotNull ISystems<?> system,
	                                           UUID... identityToken)
	{
		archiveResourceItem(original, identityToken);
		return addResourceItem((R)type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> updateResourceItem(@NotNull IRelationshipValue<L, R, ?> original,
	                                                       @NotNull R type,
	                                                       @NotNull String classificationName,
	                                                       String value,
	                                                       String originalSourceSystemUniqueID,
	                                                       LocalDateTime effectiveFromDate,
	                                                       LocalDateTime effectiveToDate,
	                                                       ISystems<?> originalSystemID,
	                                                       @NotNull ISystems<?> system,
	                                                       UUID... identityToken)
	{
		archiveResourceItem(original, identityToken);
		return addResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> archiveResourceItem(@NotNull IRelationshipValue<L, R, ?>  original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	default IRelationshipValue<L, R, ?> removeResourceItem(@NotNull IRelationshipValue<L, R, ?>  original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getDeletedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	@SuppressWarnings("rawtypes")
	default IRelationshipValue<L, R, ?> expireResourceItem(@NotNull  IRelationshipValue<L, R, ?>  original, Duration when)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		entity.setEffectiveToDate(entity.getEffectiveToDate()
		                                .plus(when));
		QueryBuilderSCD scd = (QueryBuilderSCD) entity.builder();
		scd.update(entity, when);
		return original;
	}
	
	private QueryBuilderRelationship<?, ?, ?, ?, ?, ?> findResourceItemTypeQuery(String value, ISystems<?> system, Q tableForClassification, R sType, Classification classification, UUID... identityToken)
	{
		return tableForClassification.builder()
		                             .findLink((P) this, (S)sType, value)
		                             .inActiveRange(system,identityToken)
		                             .inDateRange()
		                             .withClassification(classification)
		                             .withEnterprise(system.getEnterprise())
		                             .canCreate(system, identityToken);
	}
	
}
