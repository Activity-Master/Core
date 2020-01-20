package com.guicedee.activitymaster.core.services.capabilities;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderDefault;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IIdentificationType;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IInvolvedPartyService;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.entityassist.SCDEntity.*;
import static com.entityassist.querybuilder.EntityAssistStrings.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

@SuppressWarnings("Duplicates")
public interface IContainsInvolvedPartyIdentificationTypes<P extends WarehouseCoreTable,
		                                                          S extends WarehouseCoreTable<?, ? extends QueryBuilderDefault, ?, ?>,
		                                                          Q extends WarehouseRelationshipTable<P, S, ?, ? extends QueryBuilderRelationship, ?, ?, L, R>,
		                                                          T extends IIdentificationType<?>,
		                                                          L, R,
		                                                          J extends IContainsInvolvedPartyIdentificationTypes<P, S, Q, T, L, R, J>>
{
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> find(T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		IInvolvedPartyService involvedPartyIdentificationTypeService = get(IInvolvedPartyService.class);
		IInvolvedPartyIdentificationType<?> classification = involvedPartyIdentificationTypeService.findIdentificationType(identificationType, originatingSystem.getEnterpriseID(),
		                                                                                                                   identityToken);

		return (Optional<IRelationshipValue<L, R, ?>>) activityMasterIdentity.builder()
		                                                                     .findLink((P) this, (S) classification, null)
		                                                                     .inActiveRange(originatingSystem.getEnterpriseID())
		                                                                     .inDateRange()
		                                                                     .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                                     .get();
	}

	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findInvolvedPartyIdentificationTypeQueryRelationshipTableType()
	{
		Type[] genericInterfaces;
		Class currentClass = getClass();
		while (currentClass != Object.class)
		{
			genericInterfaces = currentClass.getGenericInterfaces();
			for (Type genericInterface : genericInterfaces)
			{
				String clazz = genericInterface.getTypeName();
				if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsInvolvedPartyIdentificationTypes.class.getCanonicalName()))
				{
					Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
					return (Class<Q>) genericTypes[2];
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findFirst(T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		IInvolvedPartyService involvedPartyIdentificationTypeService = get(IInvolvedPartyService.class);
		IInvolvedPartyIdentificationType<?> classification = involvedPartyIdentificationTypeService.findIdentificationType(identificationType, originatingSystem.getEnterpriseID(),
		                                                                                                                   identityToken);

		return (Optional<IRelationshipValue<L, R, ?>>) activityMasterIdentity.builder()
		                                                                     .findLink((P) this, (S) classification, null)
		                                                                     .inActiveRange(originatingSystem.getEnterpriseID())
		                                                                     .inDateRange()
		                                                                     .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                                     .setReturnFirst(true)
		                                                                     .get();
	}

	@SuppressWarnings("unchecked")
	default List<IRelationshipValue<L, R, ?>> findAll(T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		IInvolvedPartyService involvedPartyIdentificationTypeService = get(IInvolvedPartyService.class);
		IInvolvedPartyIdentificationType<?> classification = involvedPartyIdentificationTypeService.findIdentificationType(identificationType, originatingSystem.getEnterpriseID(),
		                                                                                                                   identityToken);
		return (List<IRelationshipValue<L, R, ?>>) activityMasterIdentity.builder()
		                                                                 .findLink((P) this, (S) classification, null)
		                                                                 .inActiveRange(originatingSystem.getEnterpriseID())
		                                                                 .inDateRange()
		                                                                 .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                                 .getAll();
	}

	@SuppressWarnings("unchecked")
	default List<IRelationshipValue<L, R, ?>> findAllIdentificationTypes(ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		return (List<IRelationshipValue<L, R, ?>>) activityMasterIdentity.builder()
		                                                                 .findParentLink((P) this, null)
		                                                                 .inActiveRange(originatingSystem.getEnterpriseID())
		                                                                 .inDateRange()
		                                                                 .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                                 .getAll();
	}

	default boolean has(T classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return numberOf(classificationValue, originatingSystem, identityToken) > 0;
	}

	@SuppressWarnings("unchecked")
	default long numberOf(T classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		IInvolvedPartyService involvedPartyIdentificationTypeService = get(IInvolvedPartyService.class);
		IInvolvedPartyIdentificationType<?> classification = involvedPartyIdentificationTypeService.findIdentificationType(classificationValue, originatingSystem.getEnterpriseID(),
		                                                                                                                   identityToken);

		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(originatingSystem.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                             .getCount();
	}

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> add(T identificationType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());

		IInvolvedPartyService classificationDataConceptService = get(IInvolvedPartyService.class);
		IInvolvedPartyIdentificationType<?> classificationDataConcept = classificationDataConceptService.findIdentificationType(identificationType,
		                                                                                                                        originatingSystem.getEnterpriseID(), identityToken);

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureInvolvedPartyIdentificationType(tableForClassification, (P) this, (S) classificationDataConcept, null, value, originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}

		return tableForClassification;
	}

	void configureInvolvedPartyIdentificationType(Q linkTable, P primary, S secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise);

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuse(T identificationType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		IInvolvedPartyService classificationDataConceptService = get(IInvolvedPartyService.class);
		IInvolvedPartyIdentificationType<?> classificationDataConcept = classificationDataConceptService.findIdentificationType(identificationType,
		                                                                                                                        originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureInvolvedPartyIdentificationType(tableForClassification, (P) this, (S) classificationDataConcept, null, value, originatingSystem.getEnterpriseID());

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
	default IRelationshipValue<L, R, ?> addOrUpdate(T identificationType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		IInvolvedPartyService classificationDataConceptService = get(IInvolvedPartyService.class);
		IInvolvedPartyIdentificationType<?> classificationDataConcept = classificationDataConceptService.findIdentificationType(identificationType,
		                                                                                                                        originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureInvolvedPartyIdentificationType(tableForClassification, (P) this, (S) classificationDataConcept, null, value, originatingSystem.getEnterpriseID());

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
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();

			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();

			Q newTableForClassification = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
			newTableForClassification.setId(null);
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
			configureInvolvedPartyIdentificationType(newTableForClassification, (P) this, (S) classificationDataConcept, null, value, originatingSystem.getEnterpriseID());
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
	default IRelationshipValue<L, R, ?> update(T identificationType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		IInvolvedPartyService classificationDataConceptService = get(IInvolvedPartyService.class);
		IInvolvedPartyIdentificationType<?> classificationDataConcept = classificationDataConceptService.findIdentificationType(identificationType,
		                                                                                                                        originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();

			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();

			Q newTableForClassification = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
			newTableForClassification.setId(null);
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
			configureInvolvedPartyIdentificationType(newTableForClassification, (P) this, (S) classificationDataConcept, null, value, originatingSystem.getEnterpriseID());
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
	default IRelationshipValue<L, R, ?> expire(T identificationType, Duration duration, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		IInvolvedPartyService classificationDataConceptService = get(IInvolvedPartyService.class);
		IInvolvedPartyIdentificationType<?> classificationDataConcept = classificationDataConceptService.findIdentificationType(identificationType,
		                                                                                                                        originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			if (duration != null)
			{
				tableForClassification.setEffectiveToDate(LocalDateTime.now()
				                                                       .plus(duration));
			}
			else
			{
				InvolvedParty ip = (InvolvedParty) this;
				tableForClassification.setEffectiveToDate(LocalDateTime.now());
				tableForClassification.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class).getDeletedFlag(ip.getEnterpriseID(), identityToken));
			}
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> archive(T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		IInvolvedPartyService classificationDataConceptService = get(IInvolvedPartyService.class);
		IInvolvedPartyIdentificationType<?> classificationDataConcept = classificationDataConceptService.findIdentificationType(identificationType,
		                                                                                                                        originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> remove(T identificationType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findInvolvedPartyIdentificationTypeQueryRelationshipTableType());
		IInvolvedPartyService classificationDataConceptService = get(IInvolvedPartyService.class);
		IInvolvedPartyIdentificationType<?> classificationDataConcept = classificationDataConceptService.findIdentificationType(identificationType,
		                                                                                                                        originatingSystem.getEnterpriseID(), identityToken);
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
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
