package com.guicedee.activitymaster.core.services.capabilities.bases;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.events.EventThread;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.entityassist.SCDEntity.*;
import static com.guicedee.activitymaster.core.services.classifications.events.EventInvolvedPartiesClassifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

@SuppressWarnings("unchecked")
public interface IAddableType<P extends WarehouseSCDTable,
		S extends WarehouseSCDTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ?, ?, ?, L, R>,
		C extends IClassification<?>,
		T extends ITypeValue<?>,
		L, R,
		J extends IAddableType<P, S, Q, C, T, L, R, J>>
{
	T stringToType(String typeName, ISystems<?> system, UUID... identityToken);
	
	default S stringToSecondary(String typeName, ISystems<?> system, UUID... identityToken)
	{
		return null;
	}
	
	void configureAddable(Q linkTable, P primary, S secondary, C classificationValue, T type, String value, ISystems<?> system);
	
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
	                                        ISystems<?> system,
	                                        UUID... identityToken)
	{
		Q tableForClassification = get(findAddableTableType());
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
		
		configureAddable(tableForClassification, (P) this,
				(S) typeAdd,
				(C) classification, (T) typeAdd, value, system);
		
		tableForClassification.persist();
	
			tableForClassification.createDefaultSecurity(system, identityToken);
		
		if (EventThread.event.get() != null)
		{
			EventThread.event.get()
			                 .add((IEventClassification<?>) Created, " - " + classificationName + " - " + value, system, identityToken);
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> add(T typeName, C classificationName, ISystems<?> system, UUID... identityToken)
	{
		return add(typeName.classificationValue(), classificationName.getName(), null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(T typeName, C classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		return add(typeName.classificationValue(), classificationName.getName(), value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(T typeName, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		return add(typeName.classificationValue(), classificationName, value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(T typeName, String value, ISystems<?> system, UUID... identityToken)
	{
		return add(typeName.classificationValue(), Classifications.NoClassification.classificationName(), value, system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> add(String typeName, String value, ISystems<?> system, UUID... identityToken)
	{
		return add(typeName, Classifications.NoClassification.classificationName(), value, system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> add(String typeName, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		return add(typeName, classificationName, value, STRING_EMPTY, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, ISystems<?> system, UUID... identityToken)
	{
		return add(typeName, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> add(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, ISystems<?> system, UUID... identityToken)
	{
		return add(typeName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	
	default IRelationshipValue<L, R, ?> add(String typeName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, LocalDateTime effectiveToDate, ISystems<?> system, UUID... identityToken)
	{
		return add(typeName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(@NotNull String type,
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
		T typeToUse = stringToType(type, system, identityToken);
		
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
		
		configureAddable(tableForClassification, (P) this, stringToSecondary(type, system, identityToken),
				(C) classification, typeToUse, value, system);
		
		tableForClassification.persist();
	
			tableForClassification.createDefaultSecurity(system, identityToken);
		
		if (EventThread.event.get() != null)
		{
			EventThread.event.get()
			                 .add((IEventClassification<?>) Created, type + " - " + classificationName + " - " + value, system, identityToken);
		}
		return (IRelationshipValue<L, R, ?>) tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull T type,
	                                               @NotNull C classificationName,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull T type,
	                                               @NotNull C classificationName,
	                                               String value,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull T type,
	                                               @NotNull C classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull T type,
	                                               @NotNull C classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               LocalDateTime effectiveFromDate,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull T type,
	                                               @NotNull C classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               LocalDateTime effectiveFromDate,
	                                               LocalDateTime effectiveToDate,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull T type,
	                                               @NotNull C classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               LocalDateTime effectiveFromDate,
	                                               LocalDateTime effectiveToDate,
	                                               ISystems<?> originalSystemID,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull T type,
	                                               @NotNull String classificationName,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull T type,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull T type,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull T type,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               LocalDateTime effectiveFromDate,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull T type,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               LocalDateTime effectiveFromDate,
	                                               LocalDateTime effectiveToDate,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull T type,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               LocalDateTime effectiveFromDate,
	                                               LocalDateTime effectiveToDate,
	                                               ISystems<?> originalSystemID,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull String type,
	                                               @NotNull String classificationName,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull String type,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull String type,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull String type,
	                                               @NotNull String classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               LocalDateTime effectiveFromDate,
	                                               @NotNull ISystems<?> system,
	                                               UUID... identityToken)
	{
		return addOrReuse(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull String type,
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
	
	default IRelationshipValue<L, R, ?> addOrReuse(R sType,
	                                               String classificationName,
	                                               String value,
	                                               String originalSourceSystemUniqueID,
	                                               LocalDateTime effectiveFromDate,
	                                               LocalDateTime effectiveToDate,
	                                               ISystems<?> originalSystemID,
	                                               ISystems<?> system,
	                                               UUID... identityToken)
	{
		Q tableForClassification = get(findAddableTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		boolean exists = findTypeQuery(value, system, tableForClassification, (S)sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return add(sType, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		else
		{
			tableForClassification = (Q) findTypeQuery(value, system, tableForClassification, (S)sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuse(@NotNull String type,
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
		S sType = stringToSecondary(type, system, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		boolean exists = findTypeQuery(value, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return (Q) add(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		else
		{
			tableForClassification = (Q) findTypeQuery(value, system, tableForClassification, sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull T type,
	                                                @NotNull String classificationName,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull T type,
	                                                @NotNull String classificationName,
	                                                String value,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, value, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull T type,
	                                                @NotNull String classificationName,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull T type,
	                                                @NotNull String classificationName,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                LocalDateTime effectiveFromDate,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type.classificationValue(), classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull T type,
	                                                @NotNull C classificationName,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                LocalDateTime effectiveFromDate,
	                                                LocalDateTime effectiveToDate,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type.classificationValue(), classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull T type,
	                                                @NotNull C classificationName,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull T type,
	                                                @NotNull C classificationName,
	                                                String value,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, value, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull T type,
	                                                @NotNull C classificationName,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull T type,
	                                                @NotNull C classificationName,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                LocalDateTime effectiveFromDate,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull String type,
	                                                @NotNull C classificationName,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                LocalDateTime effectiveFromDate,
	                                                LocalDateTime effectiveToDate,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName.getName(), value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull String type,
	                                                @NotNull String classificationName,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull String type,
	                                                @NotNull String classificationName,
	                                                String value,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, value, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull String type,
	                                                @NotNull String classificationName,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull String type,
	                                                @NotNull String classificationName,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                LocalDateTime effectiveFromDate,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull String type,
	                                                @NotNull String classificationName,
	                                                String value,
	                                                String originalSourceSystemUniqueID,
	                                                LocalDateTime effectiveFromDate,
	                                                LocalDateTime effectiveToDate,
	                                                @NotNull ISystems<?> system,
	                                                UUID... identityToken)
	{
		return addOrUpdate(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull String type,
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
		S sType = stringToSecondary(type, system, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		boolean exists = findTypeQuery(value, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
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
	
	
	default IRelationshipValue<L, R, ?> addOrUpdate(@NotNull R sType,
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
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		boolean exists = findTypeQuery(value, system, tableForClassification, (S)sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return add(sType, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		
		tableForClassification = (Q) findTypeQuery(value, system, tableForClassification, (S)sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return tableForClassification;
		}
		return update(tableForClassification, sType, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
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
	
	default IRelationshipValue<L, R, ?> update(@NotNull IRelationshipValue<L, R, ?> original,
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
	
	private QueryBuilderRelationship<?, ?, ?, ?, ?, ?> findTypeQuery(String value, ISystems<?> system, Q tableForClassification, S sType, Classification classification, UUID... identityToken)
	{
		return tableForClassification.builder()
		                             .findLink((P) this, sType, value)
		                             .inActiveRange(system)
		                             .inDateRange()
		                             .withClassification(classification)
		                             .withEnterprise(system)
		                             .canCreate(system, identityToken);
	}
	
	
}
