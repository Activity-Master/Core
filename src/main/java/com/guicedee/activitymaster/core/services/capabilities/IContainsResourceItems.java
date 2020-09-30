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
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.ResourceItemClassifications;
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

import javax.validation.constraints.NotNull;
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
import static javax.persistence.criteria.JoinType.*;

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
			IClassification<?> classification = classificationService.find(valuesToGet, originatingSystem.getEnterpriseID(), identityToken);
			
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
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(C classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findResourceItem(classification, null, enterprise, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(C classification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findResourceItem(classification.classificationName(), value, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(String classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findResourceItem(classification, null, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(String classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findResourceItem(classification, searchValue, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(C classification, String value, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findResourceItem(classification.classificationName(), value, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(String classification, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findResourceItem(classification, null, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(String classification, String searchValue, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findResourceItem(classification, searchValue, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(C classification, String value, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findResourceItem(classification.classificationName(), value, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(String classification, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findResourceItem(classification, null, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(String classification, String searchValue, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findResourceItem(classification, searchValue, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItemFirst(C classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findResourceItem(classification.classificationName(), searchValue, enterprise, true, false, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findResourceItemFirst(C classification, String searchValue, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findResourceItem(classification.classificationName(), searchValue, enterprise, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findResourceItem(String classification, String searchValue, IEnterprise<?> enterprise, boolean first, boolean latest, UUID... identityToken)
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
	
	
	default List<IRelationshipValue<L, R, ?>> findResourceItemsAll(C classification, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findResourceItemsAll(classification.classificationName(), null, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findResourceItemsAll(C classification, String value, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return findResourceItemsAll(classification.classificationName(), value, originatingSystem.getEnterprise(), latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findResourceItemsAll(C classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findResourceItemsAll(classification.classificationName(), value, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findResourceItemsAll(String classification, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		
		return findResourceItemsAll(classification, null, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findResourceItemsAll(String classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findResourceItemsAll(classification, value, enterprise, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findResourceItemsAll(String classification, String searchValue, IEnterprise<?> enterprise, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findQueryRelationshipTableType());
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
	default List<IRelationshipValue<L, R, ?>> findAllResourceItems(ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findQueryRelationshipTableType());
		return (List<IRelationshipValue<L, R, ?>>) activityMasterIdentity.builder()
		                                                                 .findParentLink((P) this, null)
		                                                                 .inActiveRange(originatingSystem.getEnterpriseID())
		                                                                 .inDateRange()
		                                                                 .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                                 .getAll();
	}
	
	
	default boolean hasBefore(C classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification, enterprise, identityToken);
		}
		return numberOfAll(classificationValue.classificationName(), enterprise, identityToken) > 0;
	}
	
	default boolean hasBefore(C classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOfAll(classificationValue.classificationName(), value, enterprise, identityToken) > 0;
	}
	
	default boolean hasBefore(String classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAll(classificationValue, value, enterprise, identityToken) > 0;
	}
	
	default boolean has(C classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOf(classificationValue.classificationName(), enterprise, identityToken) > 0;
	}
	
	default boolean has(C classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOf(classificationValue, value, enterprise, identityToken) > 0;
	}
	
	default boolean has(C classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification, originatingSystem.getEnterprise(), identityToken);
		}
		return numberOf(classificationValue.classificationName(), originatingSystem.getEnterprise(), identityToken) > 0;
	}
	
	default boolean has(C classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification, originatingSystem.getEnterprise(), identityToken);
		}
		return numberOf(classificationValue, value, originatingSystem.getEnterprise(), identityToken) > 0;
	}
	
	default long numberOf(String classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOf(classificationValue, null, enterprise, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOf(String classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findResourceItemCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, enterprise, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, value)
		                             .inActiveRange(enterprise)
		                             .inDateRange()
		                             .canRead(enterprise, identityToken)
		                             .getCount();
	}
	
	default long numberOf(C classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOf(classificationValue.classificationName(), value, enterprise, identityToken);
	}
	
	default long numberOfAll(C classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = (C) GuiceContext.get(ClassificationService.class)
			                                      .find(Classifications.NoClassification,enterprise, identityToken);
		}
		return numberOfAll(classificationValue.classificationName(), null, enterprise, identityToken);
	}
	
	default long numberOfAll(String classificationValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		return numberOfAll(classificationValue, null, enterprise, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfAll(String classificationValue, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findResourceItemCountableQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (classificationValue == null)
		{
			classificationValue = Classifications.NoClassification.classificationValue();
		}
		Classification classification = (Classification) classificationService.find(classificationValue, enterprise, identityToken);
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
	
	default IRelationshipValue<L, R, ?> addResourceItem(S typeAdd,
	                                        String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        LocalDateTime effectiveToDate,
	                                        ISystems<?> originalSystemID,
	                                        IEnterprise<?> enterprise,
	                                        UUID... identityToken )
	{
		Q tableForClassification = get(findResourceItemAddableTableType());
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
		
		configureResourceItemAddable(tableForClassification, (P) this,
		                 typeAdd,
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
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(C classificationValue,
	                                                            IResourceType<?> type,
	                                                            String searchValue,
	                                                            String storeValue,
	                                                            byte[] data,
	                                                            String mimeType,
	                                                            ISystems<?> originatingSystem,
	                                                            UUID... identityToken)
	{
		Q tableForClassification = get(findQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .withValue(searchValue)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canCreate(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification = (Q) addResourceItem(classificationValue, type, storeValue, data, mimeType, originatingSystem, identityToken);
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
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
			
			Q newTableForClassification = get(findQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(tableForClassification.getSystemID());
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(storeValue == null ? STRING_EMPTY : storeValue);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureResourceItemLinkValue(newTableForClassification, (P) tableForClassification.getPrimary(), (S) tableForClassification.getSecondary(),
			                               classification, storeValue,
			                               originatingSystem.getEnterpriseID());
			newTableForClassification.persist();
			
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				newTableForClassification.createDefaultSecurity(originatingSystem, identityToken);
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
	                                                            @NotNull IEnterprise<?> enterprise,
	                                                            UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemAddableTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.findOrCreate(classificationName, enterprise, identityToken);
		
		
		boolean exists = findResourceItemTypeQuery(value, enterprise, tableForClassification,(S) sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{ return addOrUpdateResourceItem((C)classification,type, value,value, data,mimeType,originalSystemID, identityToken); }
		
		tableForClassification = (Q) findResourceItemTypeQuery(value, enterprise, tableForClassification,(S)  sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return tableForClassification;
		}
		return updateResourceItem(tableForClassification,(S) sType, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addResourceItem(C resourceClassification,
	                                                    IResourceType<?> type,
	                                                    String storeValue,
	                                                    byte[] data,
	                                                    String mimeType,
	                                                    ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = GuiceContext.get(findQueryRelationshipTableType());
		
		IResourceItemService<?> service = GuiceContext.get(IResourceItemService.class);
		ResourceItem item = (ResourceItem) service.create(type, mimeType, originatingSystem, identityToken);
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(resourceClassification, originatingSystem.getEnterpriseID(), identityToken);
		
		boolean async = GuiceContext.get(ActivityMasterConfiguration.class)
		                            .isAsyncEnabled();
		
		if(data != null)
		{
			StoreResourceItemThread storeThread = GuiceContext.get(StoreResourceItemThread.class);
			storeThread.setItem(item);
			storeThread.setData(data);
			storeThread.setIdentifyingToken(identityToken);
			storeThread.setOriginatingSystem(originatingSystem);
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
		
		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setValue(storeValue == null ? STRING_EMPTY : storeValue);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setClassificationID(classification);
		tableForClassification.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                                                .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
		
		configureResourceItemLinkValue(tableForClassification, (P) this, (S) item, classification, tableForClassification.getValue(), originatingSystem.getEnterpriseID());
		
		tableForClassification.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}
		
		return tableForClassification;
	}
	
	void configureResourceItemLinkValue(Q linkTable, P primary, S secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise);
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuseResourceItem(C classificationValue,
	                                                           IResourceType<?> type,
	                                                           String searchValue,
	                                                           String storeValue,
	                                                           byte[] data,
	                                                           String mimeType,
	                                                           ISystems<?> originatingSystem,
	                                                           UUID... identityToken)
	{
		Q tableForClassification = get(findQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .withValue(searchValue)
		                                                         .withEnterprise(originatingSystem.getEnterpriseID())
		                                                         .canCreate(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		
		if (exists.isEmpty())
		{
			tableForClassification = (Q) addResourceItem(classificationValue, type, storeValue, data, mimeType, originatingSystem, identityToken);
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	@NotNull
	default S stringToResourceItemSecondary(String typeName, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IResourceItemService<?> service = GuiceContext.get(IResourceItemService.class);
		try
		{
			return (S) service.findResourceItemType(typeName, enterprise, identityToken);
		}catch (NoSuchElementException e)
		{
			ISystems<?> system = get(ResourceItemSystem.class)
					.getSystem(enterprise);
			return (S) service.createType(typeName, typeName, system, identityToken);
		}
	}
	
	void configureResourceItemAddable(Q linkTable, P primary, S secondary, C classificationValue, String value, IEnterprise<?> enterprise);
	
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
	
	default IRelationshipValue<L, R, ?> addResourceItem(String typeName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addResourceItem(typeName, Classifications.NoClassification.classificationName(), value, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addResourceItem(String typeName, String classificationName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addResourceItem(typeName, classificationName, value, STRING_EMPTY, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addResourceItem(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addResourceItem(typeName, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addResourceItem(String secondaryName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addResourceItem(secondaryName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, SCDEntity.EndOfTime, enterprise, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> addResourceItem(String secondaryName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, LocalDateTime effectiveToDate, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return addResourceItem(secondaryName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addResourceItem(@NotNull String secondaryName,
	                                                    @NotNull String classificationName,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    LocalDateTime effectiveFromDate,
	                                                    LocalDateTime effectiveToDate,
	                                                    ISystems<?> originalSystemID,
	                                                    @NotNull IEnterprise<?> enterprise,
	                                                    UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemAddableTableType());
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
		
		configureResourceItemAddable(tableForClassification, (P) this,
		                             stringToResourceItemSecondary(secondaryName, enterprise, identityToken),
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
	
	default IRelationshipValue<L, R, ?> addOrReuseResourceItem(@NotNull String type,
	                                                           @NotNull String classificationName,
	                                                           @NotNull IEnterprise<?> enterprise,
	                                                           UUID... identityToken)
	{
		return addOrReuseResourceItem(type, classificationName, null, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseResourceItem(@NotNull String type,
	                                                           @NotNull String classificationName,
	                                                           String value,
	                                                           @NotNull IEnterprise<?> enterprise,
	                                                           UUID... identityToken)
	{
		return addOrReuseResourceItem(type, classificationName, value, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseResourceItem(@NotNull String type,
	                                                           @NotNull String classificationName,
	                                                           String value,
	                                                           String originalSourceSystemUniqueID,
	                                                           @NotNull IEnterprise<?> enterprise,
	                                                           UUID... identityToken)
	{
		return addOrReuseResourceItem(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseResourceItem(@NotNull String type,
	                                                           @NotNull String classificationName,
	                                                           String value,
	                                                           String originalSourceSystemUniqueID,
	                                                           LocalDateTime effectiveFromDate,
	                                                           @NotNull IEnterprise<?> enterprise,
	                                                           UUID... identityToken)
	{
		return addOrReuseResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseResourceItem(@NotNull String type,
	                                                           @NotNull String classificationName,
	                                                           String value,
	                                                           String originalSourceSystemUniqueID,
	                                                           LocalDateTime effectiveFromDate,
	                                                           LocalDateTime effectiveToDate,
	                                                           @NotNull IEnterprise<?> enterprise,
	                                                           UUID... identityToken)
	{
		return addOrReuseResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuseResourceItem(@NotNull String type,
	                                                           @NotNull String classificationName,
	                                                           String value,
	                                                           String originalSourceSystemUniqueID,
	                                                           LocalDateTime effectiveFromDate,
	                                                           LocalDateTime effectiveToDate,
	                                                           ISystems<?> originalSystemID,
	                                                           @NotNull IEnterprise<?> enterprise,
	                                                           UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemAddableTableType());
		S sType = stringToResourceItemSecondary(type, enterprise, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, enterprise, identityToken);
		boolean exists = findResourceItemTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return (Q) addResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
		}
		else
		{
			tableForClassification = (Q) findResourceItemTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull String type,
	                                                            @NotNull C classificationName,
	                                                            String value,
	                                                            String originalSourceSystemUniqueID,
	                                                            LocalDateTime effectiveFromDate,
	                                                            LocalDateTime effectiveToDate,
	                                                            @NotNull IEnterprise<?> enterprise,
	                                                            UUID... identityToken)
	{
		return addOrUpdateResourceItem(type, classificationName.classificationName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull String type,
	                                                            @NotNull String classificationName,
	                                                            @NotNull IEnterprise<?> enterprise,
	                                                            UUID... identityToken)
	{
		return addOrUpdateResourceItem(type, classificationName, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull String type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            @NotNull IEnterprise<?> enterprise,
	                                                            UUID... identityToken)
	{
		return addOrUpdateResourceItem(type, classificationName, value, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull String type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            String originalSourceSystemUniqueID,
	                                                            @NotNull IEnterprise<?> enterprise,
	                                                            UUID... identityToken)
	{
		return addOrUpdateResourceItem(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull String type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            String originalSourceSystemUniqueID,
	                                                            LocalDateTime effectiveFromDate,
	                                                            @NotNull IEnterprise<?> enterprise,
	                                                            UUID... identityToken)
	{
		return addOrUpdateResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull String type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            String originalSourceSystemUniqueID,
	                                                            LocalDateTime effectiveFromDate,
	                                                            LocalDateTime effectiveToDate,
	                                                            @NotNull IEnterprise<?> enterprise,
	                                                            UUID... identityToken)
	{
		return addOrUpdateResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateResourceItem(@NotNull String type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            String originalSourceSystemUniqueID,
	                                                            LocalDateTime effectiveFromDate,
	                                                            LocalDateTime effectiveToDate,
	                                                            ISystems<?> originalSystemID,
	                                                            @NotNull IEnterprise<?> enterprise,
	                                                            UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemAddableTableType());
		
		S sType = stringToResourceItemSecondary(type, enterprise, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.findOrCreate(classificationName,enterprise,identityToken);
		boolean exists = findResourceItemTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{ return addResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken); }
		
		tableForClassification = (Q) findResourceItemTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return tableForClassification;
		}
		return updateResourceItem(tableForClassification, type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> updateResourceItem(@NotNull IRelationshipValue<L, R, ?> original,
	                                           @NotNull S type,
	                                           @NotNull String classificationName,
	                                           String value,
	                                           String originalSourceSystemUniqueID,
	                                           LocalDateTime effectiveFromDate,
	                                           LocalDateTime effectiveToDate,
	                                           ISystems<?> originalSystemID,
	                                           @NotNull IEnterprise<?> enterprise,
	                                           UUID... identityToken)
	{
		archiveResourceItem(original, identityToken);
		return addResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> updateResourceItem(@NotNull IRelationshipValue<L, R, ?> original,
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
		archiveResourceItem(original, identityToken);
		return addResourceItem(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
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
	
	private QueryBuilderRelationship<?, ?, ?, ?, ?, ?> findResourceItemTypeQuery(String value, IEnterprise<?> enterprise, Q tableForClassification, S sType, Classification classification, UUID... identityToken)
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
