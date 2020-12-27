package com.guicedee.activitymaster.core.services.capabilities;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IArrangementTypes;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IArrangementsService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;

import jakarta.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.entityassist.SCDEntity.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

@SuppressWarnings("Duplicates")
public interface IContainsArrangementTypes<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?, ?, ?>,
		T extends IArrangementTypes<?>,
		L, R,
		J extends IContainsArrangementTypes<P, S, Q, T, L, R, J>>
{
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(T addressClassification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return findArrangementTypes(addressClassification, null, originatingSystem, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(T classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return findArrangementTypes(classification, null, enterprise, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(T classification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypes(iClassification, value, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(T classification, String searchValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		
		return findArrangementTypes(iClassification, searchValue, iClassification.getEnterprise(), false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(String classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypes(iClassification, null, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(String classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypes(iClassification, searchValue, enterprise, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(T classification, String value, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypes(iClassification, value, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(String classification, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypes(iClassification, null, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(String classification, String searchValue, boolean first, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypes(iClassification, searchValue, enterprise, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(T classification, String value, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypes(iClassification, value, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(String classification, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypes(iClassification, null, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(String classification, String searchValue, boolean first, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypes(iClassification, searchValue, enterprise, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypesFirst(T classification, String searchValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findArrangementTypes(iClassification, searchValue, originatingSystem.getEnterprise(), true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypesFirst(T classification, String searchValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypes(iClassification, searchValue, enterprise, true, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypesFirst(T classification, String searchValue, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findArrangementTypes(iClassification, searchValue, originatingSystem.getEnterprise(), true, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypesFirst(T classification, String searchValue, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypes(iClassification, searchValue, enterprise, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(IClassification<?> classification, String searchValue, IEnterprise<?> enterprise, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findArrangementTypesQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(enterprise, identityToken)
				                   .withClassification(classification)
				                   .withValue(searchValue)
				                   .inDateRange()
				                   .withEnterprise(enterprise)
				                   .canRead(enterprise, identityToken);
		if (first)
		{ queryBuilderRelationshipClassification.setMaxResults(1); }
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findArrangementTypes(T arrangementType, IClassification<?> classification, String searchValue, IEnterprise<?> enterprise, boolean first, boolean latest, UUID... identityToken)
	{
		IArrangementsService<?> arrangementsService = get(IArrangementsService.class);
		S sType = (S) arrangementsService.find(arrangementType, enterprise, identityToken);
		Q relationshipTable = get(findArrangementTypesQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .findChildLink((S)sType)
				                   .inActiveRange(enterprise, identityToken)
				                   .withClassification(classification)
				                   .withValue(searchValue)
				                   .inDateRange()
				                   .withEnterprise(enterprise)
				                   .canRead(enterprise, identityToken);
		if (first)
		{ queryBuilderRelationshipClassification.setMaxResults(1); }
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(T classification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findArrangementTypesAll(iClassification, null, originatingSystem.getEnterprise(), false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(T classification, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findArrangementTypesAll(iClassification, null, originatingSystem.getEnterprise(), latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(T classification, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypesAll(iClassification, null, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(T classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findArrangementTypesAll(iClassification, value, originatingSystem.getEnterprise(), false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(T classification, String value, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findArrangementTypesAll(iClassification, value, originatingSystem.getEnterprise(), latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(T classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypesAll(iClassification, value, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(String classification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findArrangementTypesAll(iClassification, null, originatingSystem.getEnterprise(), false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(String classification, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findArrangementTypesAll(iClassification, null, originatingSystem.getEnterprise(), latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(String classification, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypesAll(iClassification, null, enterprise, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(String classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findArrangementTypesAll(iClassification, value, originatingSystem.getEnterprise(), false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(String classification, String value, boolean latest, ISystems<?> originatingSystem, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, originatingSystem.getEnterprise(), identityToken);
		return findArrangementTypesAll(iClassification, value, originatingSystem.getEnterprise(), latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(String classification, String value, boolean latest, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, enterprise, identityToken);
		return findArrangementTypesAll(iClassification, value, enterprise, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findArrangementTypesAll(IClassification<?> classification, String searchValue, IEnterprise<?> enterprise, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findArrangementTypesQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(enterprise, identityToken)
				                   .withValue(searchValue)
				                   .withClassification(classification)
				                   .inDateRange()
				                   .canRead(enterprise, identityToken);
		if (latest)
		{ queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate")); }
		return (List) queryBuilderRelationshipClassification.getAll();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findArrangementTypesQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsArrangementTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findArrangementTypesPrimaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsArrangementTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}
	
	default boolean has(T classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return numberOf(classificationValue, originatingSystem, identityToken) > 0;
	}
	
	@SuppressWarnings("unchecked")
	default long numberOf(T classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> arrangementTypeService = get(IArrangementsService.class);
		IArrangementType<?> classification = arrangementTypeService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);
		
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(originatingSystem.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                             .getCount();
	}
	
	@SuppressWarnings("unchecked")
	default Q add(IClassificationValue<?> classificationValue, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, originatingSystem.getEnterpriseID(), identityToken);
		
		IClassificationService<?> arrangementTypeService = get(IClassificationService.class);
		Classification classification = (Classification) arrangementTypeService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID(classification);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureArrangementType(tableForClassification, (P) this, (S) iArrangementType, classification, value, originatingSystem.getEnterpriseID());
		
		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				.isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}
		
		return tableForClassification;
	}
	
	void configureArrangementType(Q linkTable, P primary, S secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise);
	
	@SuppressWarnings("unchecked")
	default Q addOrUpdate(IClassificationValue<?> classificationValue, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, originatingSystem.getEnterpriseID(), identityToken);
		
		IClassificationService<?> arrangementTypeService = get(IClassificationService.class);
		Classification classification = (Classification) arrangementTypeService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canCreate(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification = addOrReuse(classificationValue, arrangementType, value, originatingSystem, identityToken);
		}
		else
		{
			tableForClassification = exists.get();
			if (Strings.nullToEmpty(value)
			           .equals(exists.get()
			                         .getValue()))
			{
				return exists.get();
			}
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();
			
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
			
			Q newTableForClassification = get(findArrangementTypesQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureArrangementType(newTableForClassification, (P) this, (S) iArrangementType, classification, value, originatingSystem.getEnterpriseID());
			newTableForClassification.persist();
			
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			}
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addOrReuse(IClassificationValue<?> classificationValue, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, originatingSystem.getEnterpriseID(), identityToken);
		
		IClassificationService<?> arrangementTypeService = get(IClassificationService.class);
		Classification classification = (Classification) arrangementTypeService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .withEnterprise(originatingSystem.getEnterpriseID())
		                                                         .canCreate(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureArrangementType(tableForClassification, (P) this, (S) iArrangementType, classification, value, originatingSystem.getEnterpriseID());
			
			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q add(IClassification<?> classificationValue, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, originatingSystem.getEnterpriseID(), identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID((Classification) classificationValue);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureArrangementType(tableForClassification, (P) this, (S) iArrangementType, classificationValue, value, originatingSystem.getEnterpriseID());
		
		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				.isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}
		
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addOrReuse(IClassification<?> classification, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, originatingSystem.getEnterpriseID(), identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setClassificationID((Classification) classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureArrangementType(tableForClassification, (P) this, (S) iArrangementType, classification, value, originatingSystem.getEnterpriseID());
			
			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addOrUpdate(IClassification<?> classification, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, originatingSystem.getEnterpriseID(), identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setClassificationID((Classification) classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureArrangementType(tableForClassification, (P) this, (S) iArrangementType, classification, value, originatingSystem.getEnterpriseID());
			
			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
			}
		}
		else
		{
			if (Strings.nullToEmpty(value)
			           .equals(exists.get()
			                         .getValue()))
			{
				return exists.get();
			}
			tableForClassification = exists.get();
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();
			
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
			
			Q newTableForClassification = get(findArrangementTypesQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureArrangementType(newTableForClassification, (P) this, (S) iArrangementType, classification, value, originatingSystem.getEnterpriseID());
			newTableForClassification.persist();
			
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			}
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q update(IClassification<?> classification, T arrangementType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, originatingSystem.getEnterpriseID(), identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			//do nothing to the object
		}
		else
		{
			tableForClassification = exists.get();
			if (Strings.nullToEmpty(value)
			           .equals(exists.get()
			                         .getValue()))
			{
				return exists.get();
			}
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();
			
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
			
			Q newTableForClassification = get(findArrangementTypesQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureArrangementType(newTableForClassification, (P) this, (S) iArrangementType, classification, value, originatingSystem.getEnterpriseID());
			newTableForClassification.persist();
			
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			}
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q expire(T arrangementType, Duration duration, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, originatingSystem.getEnterpriseID(), identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			//do nothing to the object
		}
		else
		{
			tableForClassification = exists.get();
			tableForClassification.setEffectiveToDate(LocalDateTime.now()
			                                                       .plus(duration));
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q archive(IClassification<?> classification, T arrangementType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		IArrangementsService<?> classificationDataConceptService = get(IArrangementsService.class);
		IArrangementType<?> iArrangementType = classificationDataConceptService.find(
				arrangementType, originatingSystem.getEnterpriseID(), identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) iArrangementType, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .withClassification(classification)
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			//do nothing to the object
		}
		else
		{
			tableForClassification = exists.get();
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();
			
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q remove(IClassification<?> classification, T arrangementType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findArrangementTypesQueryRelationshipTableType());
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) arrangementType, null)
		                                                         .withClassification(classification)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .withClassification(classification)
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
		}
		else
		{
			tableForClassification = exists.get();
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getDeletedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}
}
