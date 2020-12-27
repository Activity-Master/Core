package com.guicedee.activitymaster.core.services.capabilities.bases;

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
import java.util.UUID;

import static com.entityassist.SCDEntity.EndOfTime;
import static com.guicedee.activitymaster.core.services.classifications.events.EventInvolvedPartiesClassifications.Created;
import static com.guicedee.guicedinjection.GuiceContext.get;
import static com.guicedee.guicedinjection.json.StaticStrings.STRING_EMPTY;

public interface IAddable<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationship, ?, ?, L, R>,
		C extends IClassification<?>,
		L, R>

{
	void configureAddable(Q linkTable, P primary, S secondary, C classificationValue, String value, IEnterprise<?> enterprise);
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findAddableTableType()
	{
		Type[] genericInterfaces;
		Class<?> currentClass = getClass();
		while (currentClass != Object.class)
		{
			genericInterfaces = currentClass.getGenericInterfaces();
			for (Type genericInterface : genericInterfaces)
			{
				String clazz = genericInterface.getTypeName();
				if (genericInterface instanceof ParameterizedType && clazz.contains(IAddableType.class.getCanonicalName()))
				{
					Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
					return (Class<Q>) genericTypes[2];
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return null;
	}
	
	default IRelationshipValue<L, R, ?> add(R typeAdd,
	                                        String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        LocalDateTime effectiveToDate,
	                                        ISystems<?> originalSystemID,
	                                        @NotNull ISystems<?> system,
	                                        UUID... identityToken)
	{
		Q tableForClassification = get(findAddableTableType());
		IClassificationService<?> classificationService = GuiceContext.get(IClassificationService.class);
		IClassification<?> classification = classificationService.findOrCreate(classificationName, system.getEnterprise(), identityToken);
		
		ISystems<?> originatingSystem = GuiceContext.get(SystemsService.class)
		                                            .getActivityMaster(system.getEnterprise());
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
		
		configureAddable(tableForClassification, (P) this,
				(S)typeAdd,
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
	
	default IRelationshipValue<L, R, ?> add(R typeName, String value, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return add(typeName, Classifications.NoClassification.classificationName(), value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(R typeName, String classificationName, String value, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return add(typeName, classificationName, value, STRING_EMPTY, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(R typeName, String classificationName, String value, String originalSourceSystemUniqueID, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return add(typeName, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(R secondaryName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return add(secondaryName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, SCDEntity.EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(R secondaryName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, LocalDateTime effectiveToDate, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return add(secondaryName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull R type,
	                                               @NotNull String classificationName,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull R type,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull R type,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull R type,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               LocalDateTime effectiveFromDate,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull R type,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               LocalDateTime effectiveFromDate,
	                                               LocalDateTime effectiveToDate,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull R type,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               LocalDateTime effectiveFromDate,
	                                               LocalDateTime effectiveToDate,
	                                               ISystems<?> originalSystemID,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		Q tableForClassification = get(findAddableTableType());
		S sType = (S) type;
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, system.getEnterprise(), identityToken);
		boolean exists = findTypeQuery(value, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return add(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		else
		{
			tableForClassification = (Q) findTypeQuery(value, system, tableForClassification, sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull R type,
	                                                @NotNull C classificationName,
	                                                String searchValue,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                LocalDateTime effectiveFromDate,
	                                                LocalDateTime effectiveToDate,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName.getName(),searchValue, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, system, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull R type,
	                                                @NotNull String classificationName,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName,null, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull R type,
	                                                @NotNull String classificationName,
	                                                String searchValue,
	                                                String value,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, searchValue, value,"", system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull R type,
	                                                @NotNull String classificationName,
	                                                String searchValue,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName,searchValue, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull R type,
	                                                @NotNull String classificationName,
	                                                String searchValue,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                LocalDateTime effectiveFromDate,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName,searchValue, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull R type,
	                                                @NotNull String classificationName,
	                                                String searchValue,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                LocalDateTime effectiveFromDate,
	                                                LocalDateTime effectiveToDate,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName,searchValue, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, system, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull R type,
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
		Q tableForClassification = get(findAddableTableType());
		S sType = (S) type;
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.findOrCreate(classificationName, system.getEnterprise(), identityToken);
		boolean exists = findTypeQuery(searchValue, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return add(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		
		tableForClassification = (Q) findTypeQuery(value, system, tableForClassification, sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return tableForClassification;
		}
		return update(tableForClassification, type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> update(@NotNull IRelationshipValue<L, R, ?> original,
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
		archive(original, identityToken);
		return add(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
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
	
	private QueryBuilderRelationship<?, ?, ?, ?, ?, ?> findTypeQuery(String value, @NotNull ISystems<?> system, Q tableForClassification, S sType, Classification classification, UUID... identityToken)
	{
		return tableForClassification.builder()
		                             .findLink((P) this, sType, value)
		                             .inActiveRange(system.getEnterprise())
		                             .inDateRange()
		                             .withClassification(classification)
		                             .withEnterprise(system.getEnterprise())
		                             .canCreate(system.getEnterprise(), identityToken);
	}
	
	
}
