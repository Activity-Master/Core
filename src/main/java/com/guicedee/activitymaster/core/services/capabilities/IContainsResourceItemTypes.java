package com.guicedee.activitymaster.core.services.capabilities;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;
import com.guicedee.activitymaster.core.services.system.*;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.entityassist.SCDEntity.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

@SuppressWarnings("Duplicates")
public interface IContainsResourceItemTypes<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTypesTable<P, S, ?, ? extends QueryBuilderRelationshipClassificationTypes, T, ?, ?, ?, ?>,
		T extends IResourceType<?>,
		J extends IContainsResourceItemTypes<P, S, Q, T, J>>
{
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<P, S, ?>> findResourceItemTypes(T resourceType, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService<?> involvedPartyIdentificationTypeService = get(IResourceItemService.class);
		IResourceItemType<?> classification = involvedPartyIdentificationTypeService.findResourceItemType(resourceType, system, identityToken);
		return (Optional<IRelationshipValue<P, S, ?>>) activityMasterIdentity.builder()
		                                                                     .findLink((P) this, (S) classification, null)
		                                                                     .inActiveRange(system.getEnterpriseID())
		                                                                     .inDateRange()
		                                                                     .canRead(system, identityToken)
		                                                                     .get();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findResourceItemTypeQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsResourceItemTypes.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	default Optional<Q> findResourceItemTypesFirst(T resourceType, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService<?> involvedPartyIdentificationTypeService = get(IResourceItemService.class);
		IResourceItemType<?> classification = involvedPartyIdentificationTypeService.findResourceItemType(resourceType, system, identityToken);
		
		return (Optional<Q>) activityMasterIdentity.builder()
		                                           .findLink((P) this, (S) classification, null)
		                                           .inActiveRange(system.getEnterpriseID())
		                                           .inDateRange()
		                                           .canRead(system, identityToken)
		                                           .setReturnFirst(true)
		                                           .get();
	}
	
	@SuppressWarnings("unchecked")
	default List<Q> findResourceItemTypesAll(T resourceType, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService<?> involvedPartyIdentificationTypeService = get(IResourceItemService.class);
		IResourceItemType<?> classification = involvedPartyIdentificationTypeService.findResourceItemType(resourceType, system, identityToken);
		return (List<Q>) activityMasterIdentity.builder()
		                                       .findLink((P) this, (S) classification, null)
		                                       .inActiveRange(system.getEnterpriseID())
		                                       .inDateRange()
		                                       .canRead(system, identityToken)
		                                       .getAll();
	}
	
	default boolean hasResourceItemTypes(T classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfResourceItemTypes(classificationValue, system, identityToken) > 0;
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfResourceItemTypes(T classificationValue, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService<?> involvedPartyIdentificationTypeService = get(IResourceItemService.class);
		IResourceItemType<?> classification = involvedPartyIdentificationTypeService.findResourceItemType(classificationValue, system, identityToken);
		
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(system.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(system, identityToken)
		                             .getCount();
	}
	
	@SuppressWarnings("unchecked")
	default Q addResourceItemTypes(T resourceType, String value, String classificationName, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
		
		IResourceItemService<?> classificationDataConceptService = get(IResourceItemService.class);
		IResourceItemType<?> resourceItemType = classificationDataConceptService.findResourceItemType(resourceType, system, identityToken);
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = Classifications.NoClassification.classificationName();
		}
		IClassification<?> classification = classificationService.find(classificationName, system, identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setClassificationID((Classification) classification);
		tableForClassification.setOriginalSourceSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
		configureResourceItemTypeLinkValue(tableForClassification, (P) this, (S) resourceItemType, classification, value, system.getEnterpriseID());
		
		tableForClassification.persist();
	
			tableForClassification.createDefaultSecurity(system, identityToken);
		
		
		return tableForClassification;
	}
	
	void configureResourceItemTypeLinkValue(Q linkTable, P primary, S secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise);
	
	@SuppressWarnings("unchecked")
	default Q addOrReuseResourceItemTypes(T resourceType, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService<?> classificationDataConceptService = get(IResourceItemService.class);
		IResourceItemType<?> classificationDataConcept = classificationDataConceptService.findResourceItemType(resourceType, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(system, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
			configureResourceItemTypeLinkValue(tableForClassification, (P) this, (S) classificationDataConcept, null, value, system.getEnterpriseID());
			
			tableForClassification.persist();
		
				tableForClassification.createDefaultSecurity(system, identityToken);
			
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q addOrUpdateResourceItemTypes(T resourceType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService<?> classificationDataConceptService = get(IResourceItemService.class);
		IResourceItemType<?> classificationDataConcept = classificationDataConceptService.findResourceItemType(resourceType, originatingSystem, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureResourceItemTypeLinkValue(tableForClassification, (P) this, (S) classificationDataConcept, null, value, originatingSystem.getEnterpriseID());
			
			tableForClassification.persist();
		
				tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
			
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
			
			Q newTableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(StartOfTime);
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureResourceItemTypeLinkValue(newTableForClassification, (P) this, (S) classificationDataConcept, null, value, originatingSystem.getEnterpriseID());
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
	default Q updateResourceItemTypes(T resourceType, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService<?> classificationDataConceptService = get(IResourceItemService.class);
		IResourceItemType<?> classificationDataConcept = classificationDataConceptService.findResourceItemType(resourceType, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(system, identityToken)
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
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(system.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
			
			Q newTableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setSystemID((Systems) system);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(StartOfTime);
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			configureResourceItemTypeLinkValue(newTableForClassification, (P) this, (S) classificationDataConcept, null, value, system.getEnterpriseID());
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
	default Q expireResourceItemTypes(T resourceType, Duration duration, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService<?> classificationDataConceptService = get(IResourceItemService.class);
		IResourceItemType<?> classificationDataConcept = classificationDataConceptService.findResourceItemType(resourceType, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(system, identityToken)
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
	default Q archiveResourceItemTypes(T resourceType, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService<?> classificationDataConceptService = get(IResourceItemService.class);
		IResourceItemType<?> classificationDataConcept = classificationDataConceptService.findResourceItemType(resourceType, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(system, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			//do nothing to the object
		}
		else
		{
			tableForClassification = exists.get();
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(system.getEnterpriseID(), identityToken));
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Q removeResourceItemTypes(T resourceType, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemTypeQueryRelationshipTableType());
		IResourceItemService<?> classificationDataConceptService = get(IResourceItemService.class);
		IResourceItemType<?> classificationDataConcept = classificationDataConceptService.findResourceItemType(resourceType, system, identityToken);
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(system, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
		}
		else
		{
			tableForClassification = exists.get();
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getDeletedFlag(system.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}
}
