package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderDefault;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.ActiveFlagService;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.jwebmp.entityassist.SCDEntity.*;

public interface IContainsClassifications<P extends WarehouseCoreTable,
		                                         S extends WarehouseCoreTable<?,? extends QueryBuilderDefault,?,?>,
		                                         Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?>,
		                                         J extends IClassificationValue>
{
	@SuppressWarnings("unchecked")
	default Optional<Q> findClassification(@CacheKey IClassificationValue classificationValue, @CacheKey ISystems requestingSystem, @CacheKey UUID... identityToken)
	{
		Q activityMasterIdentity = GuiceContext.get(findClassificationQueryRelationshipTableType());
		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
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
	default List<Q> findClassifications(@CacheKey IClassificationValue classificationValue, @CacheKey ISystems requestingSystem, @CacheKey UUID... identityToken)
	{
		Q activityMasterIdentity = GuiceContext.get(findClassificationQueryRelationshipTableType());
		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
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
	default boolean hasClassification(J classificationValue, ISystems<?> originatingSystem, @CacheKey UUID... identityToken)
	{
		Q activityMasterIdentity = GuiceContext.get(findClassificationQueryRelationshipTableType());
		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
		Classification classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, originatingSystem.getEnterpriseID())
		                             .inActiveRange(originatingSystem.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                             .getCount() > 0;
	}

	@SuppressWarnings("unchecked")
	default Q addClassification(J classificationValue, String value, ISystems originatingSystem, UUID... identifyingToken)
	{
		Q tableForClassification = GuiceContext.get(findClassificationQueryRelationshipTableType());
		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
		Classification classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identifyingToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, originatingSystem.getEnterpriseID())
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withEnterprise(originatingSystem.getEnterpriseID())
		                                                         .canCreate(originatingSystem.getEnterpriseID(),identifyingToken)
		                                                         .get();

		if (exists.isEmpty())
		{

			tableForClassification.setEnterpriseID(originatingSystem.getEnterpriseID());
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID("");
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureForClassification(tableForClassification, originatingSystem.getEnterpriseID());

			tableForClassification.persist();
			if(GuiceContext.get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originatingSystem,identifyingToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default Q addOrUpdateClassification(J classificationValue, String value,ISystems originatingSystem,  UUID... identifyingToken)
	{
		Q tableForClassification = GuiceContext.get(findClassificationQueryRelationshipTableType());
		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
		Classification classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identifyingToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, originatingSystem.getEnterpriseID())
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canCreate(originatingSystem.getEnterpriseID(),identifyingToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification = addClassification(classificationValue, value, originatingSystem, identifyingToken);
		}
		else
		{
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();

			tableForClassification.setActiveFlagID(GuiceContext.get(ActiveFlagService.class).getDeletedFlag(originalSystem.getEnterpriseID()));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());

			tableForClassification.update();

			tableForClassification.setId(null);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID(originalSystem);
			tableForClassification.setOriginalSourceSystemUniqueID(Long.toString(originalSystem.getId()));
			tableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			tableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			tableForClassification.setEffectiveFromDate(LocalDateTime.now());
			tableForClassification.setEffectiveToDate(EndOfTime);
			tableForClassification.setActiveFlagID(GuiceContext.get(ActiveFlagService.class).getActiveFlag(originalSystem.getEnterpriseID()));
			tableForClassification.setValue(value);
			configureForClassification(tableForClassification, originatingSystem.getEnterpriseID());
			tableForClassification.persist();

			//TODO security from original
			if(GuiceContext.get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originalSystem,identifyingToken);
			}
		}
		return tableForClassification;
	}


	void configureForClassification(Q classificationLink, Enterprise enterprise);

}
