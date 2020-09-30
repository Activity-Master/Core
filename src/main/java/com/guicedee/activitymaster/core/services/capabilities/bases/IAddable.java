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

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.entityassist.SCDEntity.*;
import static com.guicedee.activitymaster.core.services.classifications.events.EventInvolvedPartiesClassifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

public interface IAddable<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationship, ?, ?, L, R>,
		C extends IClassification<?>,
		L, R>

{
	default S stringToSecondary(String typeName, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return null;
	}
	
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
	
	default IRelationshipValue<L, R, ?> add(S typeAdd,
	                                        String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        LocalDateTime effectiveToDate,
	                                        ISystems<?> originalSystemID,
	                                        IEnterprise<?> enterprise,
	                                        UUID... identityToken )
	{
		Q tableForClassification = get(findAddableTableType());
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
		
		configureAddable(tableForClassification, (P) this,
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
	
	default IRelationshipValue<L, R, ?> add(String typeName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return add(typeName, Classifications.NoClassification.classificationName(), value, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(String typeName, String classificationName, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return add(typeName, classificationName, value, STRING_EMPTY, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return add(typeName, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(String secondaryName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return add(secondaryName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, SCDEntity.EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(String secondaryName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, LocalDateTime effectiveToDate, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return add(secondaryName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(@NotNull String secondaryName,
	                                        @NotNull String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        LocalDateTime effectiveToDate,
	                                        ISystems<?> originalSystemID,
	                                        @NotNull IEnterprise<?> enterprise,
	                                        UUID... identityToken)
	{
		Q tableForClassification = get(findAddableTableType());
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
		
		configureAddable(tableForClassification, (P) this,
		                 stringToSecondary(secondaryName, enterprise, identityToken),
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
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull String type,
	                     @NotNull String classificationName,
	                     @NotNull IEnterprise<?> enterprise,
	                     UUID... identityToken)
	{
		return addOrReuse(type, classificationName, null, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull String type,
	                     @NotNull String classificationName,
	                     String value,
	                     @NotNull IEnterprise<?> enterprise,
	                     UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, null, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull String type,
	                     @NotNull String classificationName,
	                     String value,
	                     String originalSourceSystemUniqueID,
	                     @NotNull IEnterprise<?> enterprise,
	                     UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull String type,
	                     @NotNull String classificationName,
	                     String value,
	                     String originalSourceSystemUniqueID,
	                     LocalDateTime effectiveFromDate,
	                     @NotNull IEnterprise<?> enterprise,
	                     UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull String type,
	                     @NotNull String classificationName,
	                     String value,
	                     String originalSourceSystemUniqueID,
	                     LocalDateTime effectiveFromDate,
	                     LocalDateTime effectiveToDate,
	                     @NotNull IEnterprise<?> enterprise,
	                     UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull String type,
	                     @NotNull String classificationName,
	                     String value,
	                     String originalSourceSystemUniqueID,
	                     LocalDateTime effectiveFromDate,
	                     LocalDateTime effectiveToDate,
	                     ISystems<?> originalSystemID,
	                     @NotNull IEnterprise<?> enterprise,
	                     UUID... identityToken)
	{
		Q tableForClassification = get(findAddableTableType());
		S sType = stringToSecondary(type, enterprise, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, enterprise, identityToken);
		boolean exists = findTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return add(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
		}
		else
		{
			tableForClassification = (Q) findTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(S sType,
	                                        String classificationName,
	                                        String value,
	                                        String originalSourceSystemUniqueID,
	                                        LocalDateTime effectiveFromDate,
	                                        LocalDateTime effectiveToDate,
	                                        ISystems<?> originalSystemID,
	                                        IEnterprise<?> enterprise,
	                                        UUID... identityToken )
	{
		Q tableForClassification = get(findAddableTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.findOrCreate(classificationName, enterprise, identityToken);
		boolean exists = findTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return add(sType, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
		}
		else
		{
			tableForClassification = (Q) findTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull String type,
	                      @NotNull C classificationName,
	                      String value,
	                      String originalSourceSystemUniqueID,
	                      LocalDateTime effectiveFromDate,
	                      LocalDateTime effectiveToDate,
	                      @NotNull IEnterprise<?> enterprise,
	                      UUID... identityToken)
	{
		return addOrUpdate(type, classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull String type,
	                      @NotNull String classificationName,
	                      @NotNull IEnterprise<?> enterprise,
	                      UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull String type,
	                      @NotNull String classificationName,
	                      String value,
	                      @NotNull IEnterprise<?> enterprise,
	                      UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, value, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull String type,
	                      @NotNull String classificationName,
	                      String value,
	                      String originalSourceSystemUniqueID,
	                      @NotNull IEnterprise<?> enterprise,
	                      UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull String type,
	                      @NotNull String classificationName,
	                      String value,
	                      String originalSourceSystemUniqueID,
	                      LocalDateTime effectiveFromDate,
	                      @NotNull IEnterprise<?> enterprise,
	                      UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull String type,
	                      @NotNull String classificationName,
	                      String value,
	                      String originalSourceSystemUniqueID,
	                      LocalDateTime effectiveFromDate,
	                      LocalDateTime effectiveToDate,
	                      @NotNull IEnterprise<?> enterprise,
	                      UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull String type,
	                      @NotNull String classificationName,
	                      String value,
	                      String originalSourceSystemUniqueID,
	                      LocalDateTime effectiveFromDate,
	                      LocalDateTime effectiveToDate,
	                      ISystems<?> originalSystemID,
	                      @NotNull IEnterprise<?> enterprise,
	                      UUID... identityToken)
	{
		Q tableForClassification = get(findAddableTableType());
		S sType = stringToSecondary(type, enterprise, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.findOrCreate(classificationName, enterprise, identityToken);
		boolean exists = findTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{ return add(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken); }
		
		tableForClassification = (Q) findTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return tableForClassification;
		}
		return update(tableForClassification, type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
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
		archive(original, identityToken);
		return add(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull S sType,
	                                                @NotNull String classificationName,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                LocalDateTime effectiveFromDate,
	                                                LocalDateTime effectiveToDate,
	                                                ISystems<?> originalSystemID,
	                                                @NotNull IEnterprise<?> enterprise,
	                                                UUID... identityToken)
	{
		Q tableForClassification = get(findAddableTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, enterprise, identityToken);
		boolean exists = findTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{ return add(sType, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken); }
		
		tableForClassification = (Q) findTypeQuery(value, enterprise, tableForClassification, sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return tableForClassification;
		}
		return update(tableForClassification, sType, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> update(@NotNull IRelationshipValue<L, R, ?> original,
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
		archive(original, identityToken);
		return add(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, enterprise, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> archive(@NotNull IRelationshipValue<L, R, ?>  original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	default IRelationshipValue<L, R, ?> remove(@NotNull IRelationshipValue<L, R, ?>  original, UUID... identityToken)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		IActiveFlagService flagService = get(IActiveFlagService.class);
		entity.setActiveFlagID((ActiveFlag) flagService.getDeletedFlag(entity.getEnterpriseID(), identityToken));
		entity.update();
		return original;
	}
	
	@SuppressWarnings("rawtypes")
	default IRelationshipValue<L, R, ?> expire(@NotNull  IRelationshipValue<L, R, ?>  original, Duration when)
	{
		WarehouseSCDTable entity = (WarehouseSCDTable) original;
		entity.setEffectiveToDate(entity.getEffectiveToDate()
		                                .plus(when));
		QueryBuilderSCD scd = (QueryBuilderSCD) entity.builder();
		scd.update(entity, when);
		return original;
	}
	
	private QueryBuilderRelationship<?, ?, ?, ?, ?, ?> findTypeQuery(String value, IEnterprise<?> enterprise, Q tableForClassification, S sType, Classification classification, UUID... identityToken)
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
