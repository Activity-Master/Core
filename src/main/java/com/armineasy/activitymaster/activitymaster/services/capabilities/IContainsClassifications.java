package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseBaseTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderDefault;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationship;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IRelationshipValue;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.services.system.IClassificationService;
import com.jwebmp.entityassist.querybuilder.builders.JoinExpression;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.jwebmp.entityassist.SCDEntity.*;
import static com.jwebmp.entityassist.enumerations.Operand.*;
import static com.jwebmp.entityassist.querybuilder.EntityAssistStrings.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;
import static javax.persistence.criteria.JoinType.*;

@SuppressWarnings("Duplicates")
public interface IContainsClassifications<P extends WarehouseCoreTable,
		                                         S extends WarehouseCoreTable<?, ? extends QueryBuilderDefault, ?, ?>,
		                                         Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?>,
		                                         T extends IClassificationValue<?>,
		                                         J extends IContainsClassifications<P, S, Q, T, J>>
{
/*
	@SuppressWarnings("unchecked")
	default Optional<Q> find(IClassificationValue<?> classificationValue, ISystems<?> requestingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findClassificationQueryRelationshipTableType());
		ClassificationService classificationService = get(ClassificationService.class);
		Classification classification = classificationService.find(classificationValue, requestingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) activityMasterIdentity.builder()
		                                                         .findLink((P) this, (S) classification, null)
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
		                                                 .findLink((P) this, (S) classification, null)
		                                                 .inActiveRange(requestingSystem.getEnterpriseID())
		                                                 .inDateRange()
		                                                 .canRead(requestingSystem.getEnterpriseID(), identityToken)
		                                                 .getAll();
		return exists;
	}
*/

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

/*
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
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(originatingSystem.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                             .getCount();
	}

	@SuppressWarnings("unchecked")
	default Q add(C classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
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
	default Q addOrReuse(C classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		ClassificationService classificationService = get(ClassificationService.class);
		Classification classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
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
	default Q addOrUpdate(C classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		ClassificationService classificationService = get(ClassificationService.class);
		Classification classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
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
	default Q add(Classification addy, String value, ISystems<?> originatingSystem, UUID... identityToken)
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
	default Q addOrReuse(Classification addy, String value, ISystems<?> originatingSystem, UUID... identityToken)
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
	default Q addOrUpdate(Classification addy, String value, ISystems<?> originatingSystem, UUID... identityToken)
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
*/

	void configureForClassification(Q classificationLink, IEnterprise<?> enterprise);

	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<?>> find(T classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findClassificationQueryRelationshipTableType());
		IClassificationService classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);
		return (Optional<IRelationshipValue<?>>) activityMasterIdentity.builder()
		                                                               .findLink((P) this, (S) classification, null)
		                                                               .inActiveRange(originatingSystem.getEnterpriseID())
		                                                               .inDateRange()
		                                                               .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                               .get();
	}

	/**
	 * Returns either a List with Strings, or a List with Object[] for each row and values returned
	 * @param value The value to apply
	 * @param originatingSystem The system coming from
	 * @param identityToken The identity token to use
	 * @param values Any additional values to select
	 *
	 * @return The result of List&gt;String&lt; or List&gt;Object[]&lt;
	 */
	default List getValues(T value, ISystems<?> originatingSystem, UUID identityToken, T... values)
	{
		Q activityMasterIdentity = get(findClassificationQueryRelationshipTableType());
		IClassificationService classificationService = get(IClassificationService.class);
		List<T> fetching = new ArrayList<>();
		fetching.add(value);
		if (values != null)
		{
			fetching.addAll(Arrays.asList(values));
		}

		WarehouseBaseTable base = (WarehouseBaseTable) this;
		QueryBuilder baseTableBuilder = (QueryBuilder) base.builder();

		QueryBuilderRelationship builder = activityMasterIdentity.builder();
		List<JoinExpression> joins = new ArrayList<>();

		for (T classificationValue : fetching)
		{
			IClassification<?> classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

			JoinExpression newExpression = new JoinExpression();
			baseTableBuilder.join(baseTableBuilder.getAttribute("classifications"), LEFT, newExpression);
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("classificationID")), Equals, classification);

			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("activeFlagID")), InList, get(IActiveFlagService.class).findActiveRange(originatingSystem.getEnterpriseID(),identityToken));
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("enterpriseID")), Equals, originatingSystem.getEnterpriseID());
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("effectiveFromDate")), LessThanEqualTo, LocalDateTime.now());
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("effectiveToDate")), GreaterThanEqualTo, LocalDateTime.now());

			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get(builder.getPrimaryAttribute().getName())), Equals, base.getId());

			baseTableBuilder.selectColumn(newExpression.getGeneratedRoot()
			                                           .get("value"));
			joins.add(newExpression);
		}
		List list = baseTableBuilder.getAll();
		return list;
	}

	default double sumAll(T value, ISystems<?> originatingSystem, UUID identityToken)
	{
		List<String> results = getValues(value, originatingSystem, identityToken);
		double d = 0.0d;
		for (String result : results)
		{
			Double D = Double.parseDouble(result);
			d += D;
		}
		return d;
	}

	@SuppressWarnings("unchecked")
	default Optional<Q> findFirst(T classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findClassificationQueryRelationshipTableType());
		IClassificationService classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		return (Optional<Q>) activityMasterIdentity.builder()
		                                           .findLink((P) this, (S) classification, null)
		                                           .inActiveRange(originatingSystem.getEnterpriseID())
		                                           .inDateRange()
		                                           .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                           .setReturnFirst(true)
		                                           .get();
	}

	@SuppressWarnings("unchecked")
	default List<Q> findAll(T classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findClassificationQueryRelationshipTableType());
		IClassificationService classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);
		return (List<Q>) activityMasterIdentity.builder()
		                                       .findLink((P) this, (S) classification, null)
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
		Q activityMasterIdentity = get(findClassificationQueryRelationshipTableType());
		IClassificationService classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(originatingSystem.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                             .getCount();
	}

	@SuppressWarnings("unchecked")
	default Q add(T classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());

		IClassificationService classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID((Classification) classification);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
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
	default Q addOrUpdate(T classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());

		IClassificationService classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
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
			tableForClassification.setActiveFlagID(flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();

			Q newTableForClassification = get(findClassificationQueryRelationshipTableType());

			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
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
	default Q addOrReuse(T classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());

		IClassificationService classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withEnterprise(originatingSystem.getEnterpriseID())
		                                                         .canCreate(originatingSystem.getEnterpriseID(), identityToken)
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
	default Q add(IClassification<?> classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID((Classification) classificationValue);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
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
	default Q addOrReuse(IClassification<?> classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
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
	default Q addOrUpdate(IClassification<?> classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
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
			tableForClassification.setActiveFlagID(flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();

			Q newTableForClassification = get(findClassificationQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
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
	default Q update(T classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());

		IClassificationService classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
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
			tableForClassification.setActiveFlagID(flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();

			Q newTableForClassification = get(findClassificationQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
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
	default Q expire(T classificationValue, Duration duration, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());

		IClassificationService classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
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
	default Q archive(T classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());

		IClassificationService classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
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
			tableForClassification.setActiveFlagID(flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default Q remove(T classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());

		IClassificationService classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
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
			tableForClassification.setActiveFlagID(flagService.getDeletedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}

}
