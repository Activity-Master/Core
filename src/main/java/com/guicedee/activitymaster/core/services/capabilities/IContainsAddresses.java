package com.guicedee.activitymaster.core.services.capabilities;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IAddressService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.guicedinjection.GuiceContext;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

import static com.entityassist.SCDEntity.*;
import static com.entityassist.enumerations.Operand.*;
import static com.entityassist.querybuilder.EntityAssistStrings.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static javax.persistence.criteria.JoinType.*;

public interface IContainsAddresses<P extends WarehouseCoreTable,
		                                   S extends WarehouseCoreTable,
		                                   Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?, L, R>,
		                                   T extends IAddressClassification<?>,
		                                   L, R,
		                                   J extends IContainsAddresses<P, S, Q, T, L, R, J>>
{
	void configureAddressLinkValue(Q linkTable, P primary, S secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise);

	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findAddressQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsAddresses.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}

	/**
	 * Returns either a List with Strings, or a List with Object[] for each row and values returned
	 *
	 * @param addressType
	 * 		The value to apply
	 * @param originatingSystem
	 * 		The system coming from
	 * @param identityToken
	 * 		The identity token to use
	 * @param values
	 * 		Any additional values to select
	 *
	 * @return The result of List&gt;String&lt; or List&gt;Object[]&lt;
	 */
	default List getValues(T addressType, String searchValue, ISystems<?> originatingSystem, UUID identityToken, T... values)
	{
		Q activityMasterIdentity = get(findAddressQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		List<T> fetching = new ArrayList<>();
		fetching.add(addressType);
		if (values != null)
		{
			fetching.addAll(Arrays.asList(values));
		}

		WarehouseBaseTable base = (WarehouseBaseTable) this;
		QueryBuilder baseTableBuilder = (QueryBuilder) base.builder();

		QueryBuilderRelationship builder = activityMasterIdentity.builder();
		List<JoinExpression> joins = new ArrayList<>();

		for (T valuesToGet : fetching)
		{
			IClassification<?> classification = classificationService.find(valuesToGet, originatingSystem.getEnterpriseID(), identityToken);

			JoinExpression newExpression = new JoinExpression();
			baseTableBuilder.join(baseTableBuilder.getAttribute("classifications"), LEFT, newExpression);
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("classificationID")), Equals, classification);

			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("activeFlagID")), InList, get(IActiveFlagService.class).findActiveRange(originatingSystem.getEnterpriseID(), identityToken));
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("enterpriseID")), Equals, originatingSystem.getEnterpriseID());
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("effectiveFromDate")), LessThanEqualTo, LocalDateTime.now());
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("effectiveToDate")), GreaterThanEqualTo, LocalDateTime.now());

			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get(builder.getPrimaryAttribute()
			                                                 .getName())), Equals, base.getId());

			baseTableBuilder.selectColumn(newExpression.getGeneratedRoot()
			                                           .get("value"));
			joins.add(newExpression);
		}
		if (searchValue != null && !searchValue.isEmpty())
		{
			baseTableBuilder.where(baseTableBuilder.getRoot()
			                                       .get("value"), Equals, searchValue);
		}
		List list = baseTableBuilder.getAll();
		return list;
	}

	default double sumAll(T reesourceItemType, ISystems<?> originatingSystem, UUID identityToken)
	{
		List<String> results = getValues(reesourceItemType, null, originatingSystem, identityToken);
		double d = 0.0d;
		for (String result : results)
		{
			Double D = Double.parseDouble(result);
			d += D;
		}
		return d;
	}

	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> find(T addressClassification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return find(addressClassification, null, originatingSystem, identityToken);
	}

	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> find(T addressClassification, String searchValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findAddressQueryRelationshipTableType());
		IClassificationService<?> addressService = get(IClassificationService.class);
		IClassification<?> classification = addressService.find(addressClassification, originatingSystem.getEnterpriseID(), identityToken);

		return (Optional<IRelationshipValue<L, R, ?>>) activityMasterIdentity.builder()
		                                                                     .findParentLink((P) this)
		                                                                     .inActiveRange(originatingSystem.getEnterpriseID())
		                                                                     .withClassification(classification)
		                                                                     .withValue(searchValue)
		                                                                     .inDateRange()
		                                                                     .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                                     .get();
	}

	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findFirst(T addressClassification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return findFirst(addressClassification, null, originatingSystem, identityToken);
	}

	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findFirst(T addressClassification, String searchValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findAddressQueryRelationshipTableType());
		IClassificationService<?> addressService = get(IClassificationService.class);
		IClassification<?> classification = addressService.find(addressClassification, originatingSystem.getEnterpriseID(), identityToken);

		return (Optional<IRelationshipValue<L, R, ?>>) activityMasterIdentity.builder()
		                                                                     .findParentLink((P) this)
		                                                                     .inActiveRange(originatingSystem.getEnterpriseID())
		                                                                     .inDateRange()
		                                                                     .withClassification(classification)
		                                                                     .withValue(searchValue)
		                                                                     .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                                     .setReturnFirst(true)
		                                                                     .get();
	}

	@SuppressWarnings("unchecked")
	default List<IRelationshipValue<L, R, ?>> findAll(T addressClassification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return findAll(addressClassification, null, originatingSystem, identityToken);
	}

	@SuppressWarnings("unchecked")
	default List<IRelationshipValue<L, R, ?>> findAll(T addressClassification, String searchValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findAddressQueryRelationshipTableType());
		IClassificationService<?> addressService = get(IClassificationService.class);
		IClassification<?> classification = addressService.find(addressClassification, originatingSystem.getEnterpriseID(), identityToken);

		return (List<IRelationshipValue<L, R, ?>>) activityMasterIdentity.builder()
		                                                                 .findParentLink((P) this)
		                                                                 .inActiveRange(originatingSystem.getEnterpriseID())
		                                                                 .withValue(searchValue)
		                                                                 .withClassification(classification)
		                                                                 .inDateRange()
		                                                                 .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                                 .getAll();
	}

	@SuppressWarnings("unchecked")
	default List<IRelationshipValue<L, R, ?>> findAllAddresses(ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findAddressQueryRelationshipTableType());
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

	default boolean has(T classificationValue, String searchValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return numberOf(classificationValue, searchValue, originatingSystem, identityToken) > 0;
	}

	@SuppressWarnings("unchecked")
	default long numberOf(T classificationValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return numberOf(classificationValue, null, originatingSystem, identityToken);
	}

	@SuppressWarnings("unchecked")
	default long numberOf(T classificationValue, String searchValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findAddressQueryRelationshipTableType());
		IClassificationService<?> addressService = get(IClassificationService.class);
		IClassification<?> classification = addressService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		return activityMasterIdentity.builder()
		                             .findParentLink((P) this)
		                             .withValue(searchValue)
		                             .withClassification(classification)
		                             .inActiveRange(originatingSystem.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                             .getCount();
	}

	default IRelationshipValue<L, R, ?> add(R secondary, T addressClassification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = GuiceContext.get(findAddressQueryRelationshipTableType());
		Address item = (Address) secondary;

		IClassificationService<?> addressService = get(IClassificationService.class);
		Classification classification = (Classification) addressService.find(addressClassification, originatingSystem.getEnterpriseID(), identityToken);

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setValue(STRING_EMPTY);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setClassificationID(classification);
		tableForClassification.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                                                .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
		configureAddressLinkValue(tableForClassification, (P) this, (S) item, classification, tableForClassification.getValue(), originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}

		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> add(T addressClassification,
	                                        String storeValue,
	                                        ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = GuiceContext.get(findAddressQueryRelationshipTableType());

		IAddressService<?> service = GuiceContext.get(IAddressService.class);
		Address item = (Address) service.create(addressClassification, originatingSystem, storeValue, identityToken);

		IClassificationService<?> addressService = get(IClassificationService.class);
		Classification classification = (Classification) addressService.find(addressClassification, originatingSystem.getEnterpriseID(), identityToken);

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setValue(storeValue == null ? STRING_EMPTY : storeValue);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setClassificationID(classification);
		tableForClassification.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                                                .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));

		configureAddressLinkValue(tableForClassification, (P) this, (S) item, classification, tableForClassification.getValue(), originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}

		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrUpdate(T classificationValue,
	                                                String searchValue,
	                                                String storeValue,
	                                                ISystems<?> originatingSystem,
	                                                UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());

		IClassificationService addressService = get(IClassificationService.class);
		Classification classification = (Classification) addressService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .withValue(searchValue)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canCreate(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification = (Q) add(classificationValue, storeValue, originatingSystem, identityToken);
		}
		else
		{
			tableForClassification = exists.get();

			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();

			Q newTableForClassification = get(findAddressQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(tableForClassification.getSystemID());
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(storeValue == null ? STRING_EMPTY : storeValue);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureAddressLinkValue(newTableForClassification, (P) tableForClassification.getPrimary(), (S) tableForClassification.getSecondary(),
			                          classification, storeValue,
			                          originatingSystem.getEnterpriseID());
			newTableForClassification.persist();

			if (get(ActivityMasterConfiguration.class)
					    .isSecurityEnabled())
			{
				newTableForClassification.createDefaultSecurity(originatingSystem, identityToken);
			}
		}
		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuse(T classificationValue,
	                                               IResourceType<?> type,
	                                               String searchValue,
	                                               String storeValue,
	                                               ISystems<?> originatingSystem,
	                                               UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());

		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .withValue(searchValue)
		                                                         .withEnterprise(originatingSystem.getEnterpriseID())
		                                                         .canCreate(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();

		if (exists.isEmpty())
		{
			tableForClassification = (Q) add(classificationValue, storeValue, originatingSystem, identityToken);
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> add(T classificationValue, IAddress<?> item, String storeValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());

		IClassificationService classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID(classification);
		tableForClassification.setValue(storeValue == null ? STRING_EMPTY : storeValue);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureAddressLinkValue(tableForClassification, (P) this, (S) item, classification, storeValue,
		                          originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}

		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuse(T classificationValue, IAddress<?> item, String searchValue, String storeValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());

		IClassificationService classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .withValue(searchValue)
		                                                         .withEnterprise(originatingSystem.getEnterpriseID())
		                                                         .canCreate(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue(storeValue);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureAddressLinkValue(tableForClassification, (P) this, (S) item, classification, storeValue,
			                          originatingSystem.getEnterpriseID());

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
	default IRelationshipValue<L, R, ?> addOrUpdate(T classificationValue, IAddress<?> item, String searchValue, String storeValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());
		IClassificationService classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .withValue(searchValue)
		                                                         .withEnterprise(originatingSystem.getEnterpriseID())
		                                                         .canCreate(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue(storeValue);
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
			configureAddressLinkValue(tableForClassification, (P) this, (S) item, classification, storeValue,
			                          originatingSystem.getEnterpriseID());

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

			Q newTableForClassification = get(findAddressQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId()
			                                                                                .toString());
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(storeValue);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureAddressLinkValue(newTableForClassification, (P) this, (S) tableForClassification.getSecondary(), classification, storeValue,
			                          originatingSystem.getEnterpriseID());
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
	default IRelationshipValue<L, R, ?> update(T classificationValue, String searchValue, String storeValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());
		IClassificationService classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withValue(searchValue)
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
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();

			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();

			Q newTableForClassification = get(findAddressQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) originatingSystem);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId()
			                                                                                .toString());
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(storeValue == null ? STRING_EMPTY : storeValue);
			newTableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			configureAddressLinkValue(newTableForClassification, (P) this, (S) tableForClassification.getSecondary(), classification, storeValue,
			                          originatingSystem.getEnterpriseID());
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
	default IRelationshipValue<L, R, ?> archive(T classificationValue, String searchValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());

		IClassificationService addressService = get(IClassificationService.class);
		Classification classification = (Classification) addressService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withValue(searchValue)
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
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> remove(T classificationValue, String searchValue, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());

		IClassificationService<?> addressService = get(IClassificationService.class);
		Classification classification = (Classification) addressService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .withClassification(classification)
		                                                         .withValue(searchValue)
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
