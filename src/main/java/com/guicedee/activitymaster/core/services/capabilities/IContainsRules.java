package com.guicedee.activitymaster.core.services.capabilities;

import com.entityassist.SCDEntity;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.events.EventThread;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRelationshipValue;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.entityassist.SCDEntity.EndOfTime;
import static com.guicedee.activitymaster.core.services.classifications.events.EventInvolvedPartiesClassifications.Created;
import static com.guicedee.guicedinjection.GuiceContext.get;
import static com.guicedee.guicedinjection.json.StaticStrings.STRING_EMPTY;

public interface IContainsRules<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationship, ?, ?, L, R>,
		C extends IClassification<?>,
		L, R>

{
	void configureAddableRule(Q linkTable, P primary, S secondary, C classificationValue, String value, ISystems<?> system);
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findAddableTableTypeRule()
	{
		Type[] genericInterfaces;
		Class<?> currentClass = getClass();
		while (currentClass != Object.class)
		{
			genericInterfaces = currentClass.getGenericInterfaces();
			for (Type genericInterface : genericInterfaces)
			{
				String clazz = genericInterface.getTypeName();
				if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsRules.class.getCanonicalName()))
				{
					Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
					return (Class<Q>) genericTypes[2];
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return null;
	}
	
	default IRelationshipValue<L, R, ?> addRule(R typeAdd,
	                                            String classificationName,
	                                            String value,
	                                            String originalSourceSystemUniqueID,
	                                            LocalDateTime effectiveFromDate,
	                                            LocalDateTime effectiveToDate,
	                                            ISystems<?> originalSystemID,
	                                            @NotNull ISystems<?> system,
	                                            UUID... identityToken)
	{
		Q tableForClassification = get(findAddableTableTypeRule());
		IClassificationService<?> classificationService = GuiceContext.get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationName, system, identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setValue(Strings.nullToEmpty(value));
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemUniqueID(originalSourceSystemUniqueID);
		tableForClassification.setEffectiveFromDate(effectiveFromDate);
		tableForClassification.setEffectiveToDate(effectiveToDate);
		tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
		tableForClassification.setClassificationID((Classification) classification);
		
		configureAddableRule(tableForClassification, (P) this,
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
	
	default IRelationshipValue<L, R, ?> addRule(R typeName, String value, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return addRule(typeName, Classifications.NoClassification.classificationName(), value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addRule(R typeName, String classificationName, String value, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return addRule(typeName, classificationName, value, STRING_EMPTY, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addRule(R typeName, String classificationName, String value, String originalSourceSystemUniqueID, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return addRule(typeName, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addRule(R secondaryName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return addRule(secondaryName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, SCDEntity.EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addRule(R secondaryName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, LocalDateTime effectiveToDate, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return addRule(secondaryName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseRule(@NotNull R type,
	                                                   @NotNull String classificationName,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseRule(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseRule(@NotNull R type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseRule(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseRule(@NotNull R type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseRule(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseRule(@NotNull R type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   LocalDateTime effectiveFromDate,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseRule(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseRule(@NotNull R type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   LocalDateTime effectiveFromDate,
	                                                   LocalDateTime effectiveToDate,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		return addOrReuseRule(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuseRule(@NotNull R type,
	                                                   @NotNull String classificationName,
	                                                   String value,
	                                                   String originalSourceSystemUniqueID,
	                                                   LocalDateTime effectiveFromDate,
	                                                   LocalDateTime effectiveToDate,
	                                                   ISystems<?> originalSystemID,
	                                                   @NotNull ISystems<?> system,
	                                                   UUID... identityToken)
	{
		Q tableForClassification = get(findAddableTableTypeRule());
		S sType = (S) type;
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		boolean exists = findTypeQueryRule(value, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return addRule(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		else
		{
			tableForClassification = (Q) findTypeQueryRule(value, system, tableForClassification, sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateRule(@NotNull R type,
	                                                    @NotNull C classificationName,
	                                                    String searchValue,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    LocalDateTime effectiveFromDate,
	                                                    LocalDateTime effectiveToDate,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateRule(type, classificationName.getName(),searchValue, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, system, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateRule(@NotNull R type,
	                                                    @NotNull String classificationName,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateRule(type, classificationName,null, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateRule(@NotNull R type,
	                                                    @NotNull String classificationName,
	                                                    String searchValue,
	                                                    String value,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateRule(type, classificationName, searchValue, value,"", system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateRule(@NotNull R type,
	                                                    @NotNull String classificationName,
	                                                    String searchValue,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateRule(type, classificationName,searchValue, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateRule(@NotNull R type,
	                                                    @NotNull String classificationName,
	                                                    String searchValue,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    LocalDateTime effectiveFromDate,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateRule(type, classificationName,searchValue, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateRule(@NotNull R type,
	                                                    @NotNull String classificationName,
	                                                    String searchValue,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    LocalDateTime effectiveFromDate,
	                                                    LocalDateTime effectiveToDate,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		return addOrUpdateRule(type, classificationName,searchValue, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, system, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateRule(@NotNull R type,
	                                                    @NotNull String classificationName,
	                                                    String searchValue,
	                                                    String value,
	                                                    String originalSourceSystemUniqueID,
	                                                    LocalDateTime effectiveFromDate,
	                                                    LocalDateTime effectiveToDate,
	                                                    ISystems<?> originalSystemID,
	                                                    @NotNull ISystems<?> system,
	                                                    UUID... identityToken)
	{
		Q tableForClassification = get(findAddableTableTypeRule());
		S sType = (S) type;
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		boolean exists = findTypeQueryRule(searchValue, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return addRule(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		
		tableForClassification = (Q) findTypeQueryRule(value, system, tableForClassification, sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return tableForClassification;
		}
		return updateRule(tableForClassification, type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> updateRule(@NotNull IRelationshipValue<L, R, ?> original,
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
		archiveRule(original, identityToken);
		return addRule(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> archiveRule(@NotNull IRelationshipValue<L, R, ?> original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	default IRelationshipValue<L, R, ?> removeRule(@NotNull IRelationshipValue<L, R, ?> original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getDeletedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	@SuppressWarnings("rawtypes")
	default IRelationshipValue<L, R, ?> expireRule(@NotNull IRelationshipValue<L, R, ?> original, Duration when)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		entity.setEffectiveToDate(entity.getEffectiveToDate()
		                                .plus(when));
		QueryBuilderSCD scd = (QueryBuilderSCD) entity.builder();
		scd.update(entity, when);
		return original;
	}
	
	private QueryBuilderRelationship<?, ?, ?, ?, ?, ?> findTypeQueryRule(String value, @NotNull ISystems<?> system, Q tableForClassification, S sType, Classification classification, UUID... identityToken)
	{
		return tableForClassification.builder()
		                             .findLink((P) this, sType, value)
		                             .inActiveRange(system.getEnterprise())
		                             .inDateRange()
		                             .withClassification(classification)
		                             .withEnterprise(system.getEnterprise())
		                             .canCreate(system.getEnterprise(), identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findRule(C classification, ISystems<?> system, UUID... identityToken)
	{
		return findRule(classification, null, system, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findRule(C classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return findRule(classification.getName(), value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findRule(String classification, ISystems<?> system, UUID... identityToken)
	{
		
		return findRule(classification, null, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findRule(String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		
		return findRule(classification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findRule(C classification, String value, boolean first, ISystems<?> system, UUID... identityToken)
	{
		
		return findRule(classification.getName(), value, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findRule(String classification, boolean first, ISystems<?> system, UUID... identityToken)
	{
		
		return findRule(classification, null, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findRule(String classification, String searchValue, boolean first, ISystems<?> system, UUID... identityToken)
	{
		
		return findRule(classification, searchValue, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findRule(C classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findRule(classification.getName(), value, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findRule(String classification, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findRule(classification, null, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findRule(String classification, String searchValue, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findRule(classification, searchValue, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findRuleFirst(C classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		
		return findRule(classification.getName(), searchValue, system, true, false, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findRuleFirst(C classification, String searchValue, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findRule(classification.getName(), searchValue, system, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findRule(String classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findQueryRelationshipTableTypeRules());
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
	
	
	default List<IRelationshipValue<L, R, ?>> findRulesAll(C classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findRulesAll(classification.getName(), null, system, latest, identityToken);
	}

	
	default List<IRelationshipValue<L, R, ?>> findRulesAll(C classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findRulesAll(classification.getName(), value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findRulesAll(String classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		
		return findRulesAll(classification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findRulesAll(String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findRulesAll(classification, value, system, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findRulesAll(String classification, String searchValue, ISystems<?> system, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findQueryRelationshipTableTypeRules());
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
	default Class<Q> findQueryRelationshipTableTypeRules()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsRules.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findPrimaryTableTypeRules()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsRules.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<S> findSecondaryTableTypeRules()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsRules.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<S>) genericTypes[1];
			}
		}
		return null;
	}
}

