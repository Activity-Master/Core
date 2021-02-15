package com.guicedee.activitymaster.core.services.capabilities;

import com.entityassist.SCDEntity;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.ClassificationService;
import com.guicedee.activitymaster.core.db.abstraction.*;
import com.guicedee.activitymaster.core.db.abstraction.builders.*;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.events.EventThread;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.classifications.involvedparty.IInvolvedPartyClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
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

public interface IContainsInvolvedParties<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?, ?, ?>,
		C extends IClassificationValue<?>,
		L, R,
		J extends IContainsInvolvedParties<P, S, Q, C, L, R, J>>
{
	
	default List<IRelationshipValue<L, R, ?>> findByInvolvedParty(IInvolvedParty<?> involvedParty, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationName, system, identityToken);
		Q relationshipTable = get(this.findInvolvedPartyQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findChildLink((S) involvedParty)
				                   .inActiveRange(system, identityToken)
				                   .withValue(value)
				                   .withClassification(classification)
				                   .inDateRange()
				                   .canRead(system, identityToken);
		return (List) queryBuilderRelationshipClassification.getAll();
		
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(C classification, ISystems<?> system, UUID... identityToken)
	{
		return findInvolvedParty(classification, null, system, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(C classification, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedParty(iClassification, value, system, false, false, identityToken);
	}
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(String classification, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedParty(iClassification, null, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedParty(iClassification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(C classification, String value, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedParty(iClassification, value, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(String classification, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedParty(iClassification, null, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(String classification, String searchValue, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedParty(iClassification, searchValue, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(C classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedParty(iClassification, value, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(String classification, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedParty(iClassification, null, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(String classification, String searchValue, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedParty(iClassification, searchValue, system, first, latest, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedPartyFirst(C classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedParty(iClassification, searchValue, system, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedPartyFirst(C classification, String searchValue, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedParty(iClassification, searchValue, system, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findInvolvedParty(IClassification<?> classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(this.findInvolvedPartyQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system, identityToken)
				                   .withClassification(classification)
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
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(C classification, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedPartyAll(iClassification, null, system, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(C classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedPartyAll(iClassification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(C classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem, identityToken);
		return findInvolvedPartyAll(iClassification, value, originatingSystem, false, identityToken);
	}

	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(C classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedPartyAll(iClassification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(String classification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem, identityToken);
		return findInvolvedPartyAll(iClassification, null, originatingSystem, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(String classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedPartyAll(iClassification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(String classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem, identityToken);
		return findInvolvedPartyAll(iClassification, value, originatingSystem, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findInvolvedPartyAll(iClassification, value, system, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findInvolvedPartyAll(IClassification<?> classification, String searchValue, ISystems<?> system, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(this.findInvolvedPartyQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system, identityToken)
				                   .withValue(searchValue)
				                   .withClassification(classification)
				                   .inDateRange()
				                   .canRead(system, identityToken);
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		return (List) queryBuilderRelationshipClassification.getAll();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findInvolvedPartyQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedParties.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findInvolvedPartyPrimaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedParties.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	default boolean hasInvolvedParty(IInvolvedPartyClassification<?> InvolvedPartyClassification, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyQueryRelationshipTableType());
		Classification classification = (Classification) GuiceContext.get(ClassificationService.class)
		                                                             .find(InvolvedPartyClassification,
				                                                             system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, value)
		                             .inActiveRange(classification.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(system, identityToken)
		                             .getCount() > 0;
	}
	
	default IRelationshipValue<L, R, ?> addInvolvedParty(R typeAdd,
	                                                     String classificationName,
	                                                     String value,
	                                                     String originalSourceSystemUniqueID,
	                                                     LocalDateTime effectiveFromDate,
	                                                     LocalDateTime effectiveToDate,
	                                                     ISystems<?> originalSystemID,
	                                                     @NotNull ISystems<?> system,
	                                                     UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyAddableTableType());
		IClassificationService<?> classificationService = GuiceContext.get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationName, system, identityToken);
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setValue(Strings.nullToEmpty(value));
		tableForClassification.setSystemID((Systems) system);
		if (originalSystemID == null)
		{
			originalSystemID = system;
		}
		tableForClassification.setOriginalSourceSystemID((Systems) originalSystemID);
		tableForClassification.setOriginalSourceSystemUniqueID(originalSourceSystemUniqueID);
		tableForClassification.setEffectiveFromDate(effectiveFromDate);
		tableForClassification.setEffectiveToDate(effectiveToDate);
		tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
		tableForClassification.setClassificationID((Classification) classification);
		
		configureAddable(tableForClassification, (P) this,
				(S)typeAdd,
				(C) classification, value, system.getEnterpriseID());
		
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
		return (IRelationshipValue<L, R, ?> )tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addInvolvedParty(R typeName, String value, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return addInvolvedParty(typeName, Classifications.NoClassification.classificationName(), value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addInvolvedParty(R typeName, String classificationName, String value, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return addInvolvedParty(typeName, classificationName, value, STRING_EMPTY, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addInvolvedParty(R typeName, String classificationName, String value, String originalSourceSystemUniqueID, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return addInvolvedParty(typeName, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addInvolvedParty(R secondaryName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return addInvolvedParty(secondaryName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, SCDEntity.EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addInvolvedParty(R secondaryName, String classificationName, String value, String originalSourceSystemUniqueID, LocalDateTime effectiveFromDate, LocalDateTime effectiveToDate, @NotNull ISystems<?> system, UUID... identityToken)
	{
		return addInvolvedParty(secondaryName, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	
	
	@SuppressWarnings("unchecked")
	default Q addInvolvedParty(IInvolvedPartyClassification<?> involvedPartyClassification, ISystems<?> system, String value, UUID... identifyingToken)
	{
		Classification classification = (Classification) get(ClassificationService.class).find(involvedPartyClassification,
		                                                                                       system, identifyingToken);
		
		InvolvedParty addy = new InvolvedParty();
		Optional<InvolvedParty> InvolvedPartyExists = addy.builder()
		                                                  .hasClassification(classification, value)
		                                                  .withEnterprise(system.getEnterpriseID())
		                                                  .inDateRange()
		                                                  .get();
		if (InvolvedPartyExists.isEmpty())
		{
			addy.setEnterpriseID(classification.getEnterpriseID());
			addy.setSystemID((Systems) system);
			addy.setOriginalSourceSystemID((Systems) system);
			addy.setActiveFlagID(classification.getActiveFlagID());
			addy.persist();
			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				addy.createDefaultSecurity(system, identifyingToken);
			}
			addy.add(involvedPartyClassification, value, system, identifyingToken);
		}
		else
		{
			addy = InvolvedPartyExists.get();
		}
		
		Q tableForClassification = get(findInvolvedPartyQueryRelationshipTableType());
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) addy, value)
		                                                         .inActiveRange(classification.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID(classification.getEnterpriseID());
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemID((Systems) system);
			tableForClassification.setActiveFlagID(classification.getActiveFlagID());
			
			setMyInvolvedPartyLinkValue(tableForClassification, (P) this, (S) addy, system);
			
			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(system, identifyingToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	
	@SuppressWarnings("unchecked")
	default Q addInvolvedParty(IInvolvedParty<?> addy, IInvolvedPartyClassification<?> iclassification, String value, ISystems<?> system, UUID... identifyingToken)
	{
		Q tableForClassification = get(findInvolvedPartyQueryRelationshipTableType());
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) addy, value)
		                                                         .inActiveRange(addy.getEnterpriseID())
		                                                         .withClassification(iclassification,system)
		                                                         .withValue(value)
		                                                         .inDateRange()
		                                                         .canRead(system, identifyingToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			Classification classification = (Classification) get(ClassificationService.class).find(iclassification,
			                                                                                       system, identifyingToken);
			
			tableForClassification.setEnterpriseID((Enterprise) addy.getEnterpriseID());
			tableForClassification.setSystemID((Systems) system);
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue(value);
			tableForClassification.setOriginalSourceSystemID((Systems) system);
			tableForClassification.setActiveFlagID((ActiveFlag) addy.getActiveFlagID());
			setMyInvolvedPartyLinkValue(tableForClassification, (P) this, (S) addy, system);
			tableForClassification.persist();
			
			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(system, identifyingToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	
	void setMyInvolvedPartyLinkValue(Q classificationLink, P first, S involvedParty, ISystems<?> system);
	
	default IRelationshipValue<L, R, ?> addOrReuseInvolvedParty(@NotNull R type,
	                                                            @NotNull String classificationName,
	                                                            @NotNull ISystems<?> system,
	                                                            UUID... identityToken)
	{
		return addOrReuseInvolvedParty(type, classificationName, null, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseInvolvedParty(@NotNull R type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            @NotNull ISystems<?> system,
	                                                            UUID... identityToken)
	{
		return addOrReuseInvolvedParty(type, classificationName, value, null, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseInvolvedParty(@NotNull R type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            String originalSourceSystemUniqueID,
	                                                            @NotNull ISystems<?> system,
	                                                            UUID... identityToken)
	{
		return addOrReuseInvolvedParty(type, classificationName, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseInvolvedParty(@NotNull R type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            String originalSourceSystemUniqueID,
	                                                            LocalDateTime effectiveFromDate,
	                                                            @NotNull ISystems<?> system,
	                                                            UUID... identityToken)
	{
		return addOrReuseInvolvedParty(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrReuseInvolvedParty(@NotNull R type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            String originalSourceSystemUniqueID,
	                                                            LocalDateTime effectiveFromDate,
	                                                            LocalDateTime effectiveToDate,
	                                                            @NotNull ISystems<?> system,
	                                                            UUID... identityToken)
	{
		return addOrReuseInvolvedParty(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuseInvolvedParty(@NotNull R type,
	                                                            @NotNull String classificationName,
	                                                            String value,
	                                                            String originalSourceSystemUniqueID,
	                                                            LocalDateTime effectiveFromDate,
	                                                            LocalDateTime effectiveToDate,
	                                                            ISystems<?> originalSystemID,
	                                                            @NotNull ISystems<?> system,
	                                                            UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyAddableTableType());
		S sType = (S) type;
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		boolean exists = findTypeQueryInvolvedParties(value, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return addInvolvedParty(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		else
		{
			tableForClassification = (Q) findTypeQueryInvolvedParties(value, system, tableForClassification, sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return (IRelationshipValue<L, R, ?>)tableForClassification;
	}
	
	void configureAddable(Q linkTable, P primary, S secondary, C classificationValue, String value, IEnterprise<?> enterprise);
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findInvolvedPartyAddableTableType()
	{
		Type[] genericInterfaces;
		Class<?> currentClass = getClass();
		while (currentClass != Object.class)
		{
			genericInterfaces = currentClass.getGenericInterfaces();
			for (Type genericInterface : genericInterfaces)
			{
				String clazz = genericInterface.getTypeName();
				if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedParties.class.getCanonicalName()))
				{
					Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
					return (Class<Q>) genericTypes[2];
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return null;
	}
	
	private QueryBuilderRelationship<?, ?, ?, ?, ?, ?> findTypeQueryInvolvedParties(String value, @NotNull ISystems<?> system, Q tableForClassification, S sType, Classification classification, UUID... identityToken)
	{
		return tableForClassification.builder()
		                             .findLink((P) this, sType, value)
		                             .inActiveRange(system.getEnterprise())
		                             .inDateRange()
		                             .withClassification(classification)
		                             .withEnterprise(system.getEnterprise())
		                             .canCreate(system.getEnterprise(), identityToken);
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
		Q tableForClassification = get(findInvolvedPartyAddableTableType());
		S sType = (S) type;
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		boolean exists = findTypeQueryInvolvedParties(value, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return addInvolvedParty(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		else
		{
			tableForClassification = (Q) findTypeQueryInvolvedParties(value, system, tableForClassification, sType, classification, identityToken)
					.get()
					.orElseThrow();
		}
		return (IRelationshipValue<L, R, ?>)tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateInvolvedParty(@NotNull R type,
	                                                             @NotNull C classificationName,
	                                                             String searchValue,
	                                                             String value,
	                                                             String originalSourceSystemUniqueID,
	                                                             LocalDateTime effectiveFromDate,
	                                                             LocalDateTime effectiveToDate,
	                                                             @NotNull ISystems<?> system,
	                                                             UUID... identityToken)
	{
		return addOrUpdateInvolvedParty(type, classificationName.classificationName(),searchValue, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, system, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateInvolvedParty(@NotNull R type,
	                                                             @NotNull String classificationName,
	                                                             @NotNull ISystems<?> system,
	                                                             UUID... identityToken)
	{
		return addOrUpdateInvolvedParty(type, classificationName,null, null, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateInvolvedParty(@NotNull R type,
	                                                             @NotNull String classificationName,
	                                                             String searchValue,
	                                                             String value,
	                                                             @NotNull ISystems<?> system,
	                                                             UUID... identityToken)
	{
		return addOrUpdateInvolvedParty(type, classificationName, searchValue, value,"", system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateInvolvedParty(@NotNull R type,
	                                                             @NotNull String classificationName,
	                                                             String searchValue,
	                                                             String value,
	                                                             String originalSourceSystemUniqueID,
	                                                             @NotNull ISystems<?> system,
	                                                             UUID... identityToken)
	{
		return addOrUpdateInvolvedParty(type, classificationName,searchValue, value, originalSourceSystemUniqueID, LocalDateTime.now(), system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateInvolvedParty(@NotNull R type,
	                                                             @NotNull String classificationName,
	                                                             String searchValue,
	                                                             String value,
	                                                             String originalSourceSystemUniqueID,
	                                                             LocalDateTime effectiveFromDate,
	                                                             @NotNull ISystems<?> system,
	                                                             UUID... identityToken)
	{
		return addOrUpdateInvolvedParty(type, classificationName,searchValue, value, originalSourceSystemUniqueID, effectiveFromDate, EndOfTime, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateInvolvedParty(@NotNull R type,
	                                                             @NotNull String classificationName,
	                                                             String searchValue,
	                                                             String value,
	                                                             String originalSourceSystemUniqueID,
	                                                             LocalDateTime effectiveFromDate,
	                                                             LocalDateTime effectiveToDate,
	                                                             @NotNull ISystems<?> system,
	                                                             UUID... identityToken)
	{
		return addOrUpdateInvolvedParty(type, classificationName,searchValue, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, system, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdateInvolvedParty(@NotNull R type,
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
		Q tableForClassification = get(findInvolvedPartyAddableTableType());
		S sType = (S) type;
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationName, system, identityToken);
		boolean exists = findTypeQueryInvolvedParties(searchValue, system, tableForClassification, sType, classification, identityToken).getCount() > 0;
		if (!exists)
		{
			return addInvolvedParty(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
		}
		
		tableForClassification = (Q) findTypeQueryInvolvedParties(value, system, tableForClassification, sType, classification, identityToken)
				.get()
				.orElseThrow();
		
		if (tableForClassification.getValue()
		                          .equals(value))
		{
			return (IRelationshipValue<L, R, ?>)tableForClassification;
		}
		return (IRelationshipValue<L, R, ?>)update((IRelationshipValue<L, R, ?>)tableForClassification, type, classificationName, value, originalSourceSystemUniqueID,
				effectiveFromDate, effectiveToDate, originalSystemID,
				system, identityToken);
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
		return addInvolvedParty(type, classificationName, value, originalSourceSystemUniqueID, effectiveFromDate, effectiveToDate, originalSystemID, system, identityToken);
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
}
