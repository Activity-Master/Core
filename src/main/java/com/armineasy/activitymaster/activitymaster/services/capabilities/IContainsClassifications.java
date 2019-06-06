package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderDefault;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;

import javax.cache.annotation.CacheKey;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.jwebmp.entityassist.SCDEntity.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

public interface IContainsClassifications<P extends WarehouseCoreTable,
		                                         S extends WarehouseCoreTable<?, ? extends QueryBuilderDefault, ?, ?>,
		                                         Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?>,
		                                         C extends IClassificationValue<?>>
{
	@SuppressWarnings("unchecked")
	default Optional<Q> find(IClassificationValue<?> classificationValue, ISystems<?> requestingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findClassificationQueryRelationshipTableType());
		ClassificationService classificationService = get(ClassificationService.class);
		Classification classification = classificationService.find(classificationValue, requestingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) activityMasterIdentity.builder()
		                                                         .findLink((P) this, (S) classification, requestingSystem.getEnterpriseID())
		                                                         .inActiveRange(requestingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(requestingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		return exists;
	}

	@SuppressWarnings("unchecked")
	default List<Q> findAll(IClassificationValue<?> classificationValue, ISystems<?> requestingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findClassificationQueryRelationshipTableType());
		ClassificationService classificationService = get(ClassificationService.class);
		Classification classification = classificationService.find(classificationValue, requestingSystem.getEnterpriseID(), identityToken);

		List<Q> exists = (List<Q>) activityMasterIdentity.builder()
		                                                 .findLink((P) this, (S) classification, requestingSystem.getEnterpriseID())
		                                                 .inActiveRange(requestingSystem.getEnterpriseID())
		                                                 .inDateRange()
		                                                 .canRead(requestingSystem.getEnterpriseID(), identityToken)
		                                                 .getAll();
		return exists;
	}

	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findClassificationQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsClassifications.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	default boolean has(C classificationValue, ISystems<?> originatingSystem, @CacheKey UUID... identityToken)
	{
		return numberOf(classificationValue, originatingSystem, identityToken) > 0;
	}

	@SuppressWarnings("unchecked")
	default long numberOf(C classificationValue, ISystems<?> originatingSystem, @CacheKey UUID... identityToken)
	{
		Q activityMasterIdentity = get(findClassificationQueryRelationshipTableType());
		ClassificationService classificationService = get(ClassificationService.class);
		Classification classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, originatingSystem.getEnterpriseID())
		                             .inActiveRange(originatingSystem.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                             .getCount();
	}

	@SuppressWarnings("unchecked")
	default Q add(C classificationValue, String value, ISystems originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		ClassificationService classificationService = get(ClassificationService.class);
		Classification classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID(classification);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID("");
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureForClassification(tableForClassification, originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}

		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default Q addOrReuse(C classificationValue, String value, ISystems originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		ClassificationService classificationService = get(ClassificationService.class);
		Classification classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, originatingSystem.getEnterpriseID())
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
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
			tableForClassification.setOriginalSourceSystemUniqueID("");
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureForClassification(tableForClassification, originatingSystem.getEnterpriseID());

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
	default Q addOrUpdate(C classificationValue, String value, ISystems originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		ClassificationService classificationService = get(ClassificationService.class);
		Classification classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, originatingSystem.getEnterpriseID())
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canCreate(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification = addOrReuse(classificationValue, value, originatingSystem, identityToken);
		}
		else
		{
			tableForClassification = exists.get();
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();

			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID(flagService.getDeletedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();

			Q newTableForClassification = get(findClassificationQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(Long.toString(originalSystem.getId()));
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID(flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureForClassification(newTableForClassification, originatingSystem.getEnterpriseID());
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
	default Q add(Classification addy, String value, ISystems originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID(addy);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID("");
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureForClassification(tableForClassification, originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}

		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default Q addOrReuse(Classification addy, String value, ISystems originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) addy, originatingSystem.getEnterpriseID())
		                                                         .inActiveRange(addy.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setClassificationID(addy);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID("");
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureForClassification(tableForClassification, originatingSystem.getEnterpriseID());

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
	default Q addOrUpdate(Classification addy, String value, ISystems originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) addy, originatingSystem.getEnterpriseID())
		                                                         .inActiveRange(addy.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setClassificationID(addy);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID("");
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureForClassification(tableForClassification, originatingSystem.getEnterpriseID());

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
			tableForClassification.setActiveFlagID(flagService.getDeletedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();

			Q newTableForClassification = get(findClassificationQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(Long.toString(originalSystem.getId()));
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID(flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureForClassification(newTableForClassification, originatingSystem.getEnterpriseID());
			newTableForClassification.persist();

			if (get(ActivityMasterConfiguration.class)
					    .isSecurityEnabled())
			{
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			}
		}
		return tableForClassification;
	}

	void configureForClassification(Q classificationLink, IEnterprise<?> enterprise);

}
