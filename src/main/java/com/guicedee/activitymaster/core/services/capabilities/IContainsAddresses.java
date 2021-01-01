package com.guicedee.activitymaster.core.services.capabilities;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.capabilities.bases.ISearchable;
import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IAddressService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.validation.constraints.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

import static com.entityassist.SCDEntity.*;
import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;
import static jakarta.persistence.criteria.JoinType.*;

public interface IContainsAddresses<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?, L, R>,
		T extends IAddressClassification<?>,
		L, R,
		J extends IContainsAddresses<P, S, Q, T, L, R, J>>
{
	default double sumAll(T reesourceItemType, ISystems<?> system, UUID identityToken)
	{
		List<String> results = getValues(reesourceItemType, null, system, identityToken);
		double d = 0.0d;
		for (String result : results)
		{
			Double D = Double.parseDouble(result);
			d += D;
		}
		return d;
	}
	
	/**
	 * Returns either a List with Strings, or a List with Object[] for each row and values returned
	 *
	 * @param addressType   The value to apply
	 * @param system        The system coming from
	 * @param identityToken The identity token to use
	 * @param values        Any additional values to select
	 *
	 * @return The result of List&gt;String&lt; or List&gt;Object[]&lt;
	 */
	default List getValues(T addressType, String searchValue, ISystems<?> system, UUID identityToken, T... values)
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
		QueryBuilderTable baseTableBuilder = (QueryBuilderTable) base.builder();
		
		QueryBuilderRelationship builder = activityMasterIdentity.builder();
		List<JoinExpression> joins = new ArrayList<>();
		
		for (T valuesToGet : fetching)
		{
			IClassification<?> classification = classificationService.find(valuesToGet, system, identityToken);
			
			JoinExpression newExpression = new JoinExpression();
			baseTableBuilder.join(baseTableBuilder.getAttribute("classifications"), LEFT, newExpression);
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("classificationID")), Equals, classification);
			
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("activeFlagID")), InList, get(IActiveFlagService.class).findActiveRange(system.getEnterpriseID(), identityToken));
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("enterpriseID")), Equals, system.getEnterpriseID());
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
	
	default Optional<IRelationshipValue<L, R, ?>> findAddresses(T classification, ISystems<?> system, UUID... identityToken)
	{
		return findAddresses(classification, null, system, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findAddresses(T classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		
		return findAddresses(iClassification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findAddresses(String classification, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddresses(iClassification, null, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findAddresses(String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddresses(iClassification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findAddresses(T classification, String value, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddresses(iClassification, value, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findAddresses(String classification, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddresses(iClassification, null, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findAddresses(String classification, String searchValue, boolean first, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddresses(iClassification, searchValue, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findAddresses(T classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddresses(iClassification, value, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findAddresses(String classification, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddresses(iClassification, null, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findAddresses(String classification, String searchValue, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddresses(iClassification, searchValue, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findAddressesFirst(T classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddresses(iClassification, searchValue, system, true, false, identityToken);
	}
	
	
	default Optional<IRelationshipValue<L, R, ?>> findAddressesFirst(T classification, String searchValue, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddresses(iClassification, searchValue, system, true, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findAddresses(IClassification<?> classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findAddressQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system, identityToken)
				                   .withClassification(classification)
				                   .withValue(searchValue)
				                   .inDateRange()
				                   .withEnterprise(system.getEnterprise())
				                   .canRead(system, identityToken);
		if (first)
		{
			queryBuilderRelationshipClassification.setMaxResults(1);
		}
		if (latest)
		{
			queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate"));
		}
		
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	
	default List<IRelationshipValue<L, R, ?>> findAddressesAll(T classification, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddressesAll(iClassification, null, system, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAddressesAll(T classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddressesAll(iClassification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAddressesAll(T classification, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddressesAll(iClassification, value, system, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAddressesAll(T classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddressesAll(iClassification, value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAddressesAll(String classification, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddressesAll(iClassification, null, system, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAddressesAll(String classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddressesAll(iClassification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAddressesAll(String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddressesAll(iClassification, value, system, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findAddressesAll(String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		return findAddressesAll(iClassification, value, system, latest, identityToken);
	}
	
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findAddressesAll(IClassification<?> classification, String searchValue, ISystems<?> system, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findAddressQueryRelationshipTableType());
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system, identityToken)
				                   .withValue(searchValue)
				                   .withClassification(classification)
				                   .inDateRange()
				                   .canRead(system, identityToken);
		if (latest)
		{
			queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate"));
		}
		return (List) queryBuilderRelationshipClassification.getAll();
	}
	
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
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findAddressPrimaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(ISearchable.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	default List<IRelationshipValue<L, R, ?>> findAllAddresses(ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findAddressQueryRelationshipTableType());
		return (List<IRelationshipValue<L, R, ?>>) activityMasterIdentity.builder()
		                                                                 .findParentLink((P) this, null)
		                                                                 .inActiveRange(system.getEnterpriseID())
		                                                                 .inDateRange()
		                                                                 .canRead(system, identityToken)
		                                                                 .getAll();
	}
	
	default boolean has(T classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOf(classificationValue, system, identityToken) > 0;
	}
	
	@SuppressWarnings("unchecked")
	default long numberOf(T classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOf(classificationValue, null, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default long numberOf(T classificationValue, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findAddressQueryRelationshipTableType());
		IClassificationService<?> addressService = get(IClassificationService.class);
		IClassification<?> classification = addressService.find(classificationValue, system, identityToken);
		
		return activityMasterIdentity.builder()
		                             .findParentLink((P) this)
		                             .withValue(searchValue)
		                             .withClassification(classification)
		                             .inActiveRange(system.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(system, identityToken)
		                             .getCount();
	}
	
	default boolean has(T classificationValue, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOf(classificationValue, searchValue, system, identityToken) > 0;
	}
	
	default IRelationshipValue<L, R, ?> add(R secondary, T addressClassification, ISystems<?> system, UUID... identityToken)
	{
		return add(secondary, addressClassification, STRING_EMPTY, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> add(R secondary, T addressClassification, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = GuiceContext.get(findAddressQueryRelationshipTableType());
		Address item = (Address) secondary;
		
		IClassificationService<?> addressService = get(IClassificationService.class);
		Classification classification = (Classification) addressService.find(addressClassification, system, identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setClassificationID(classification);
		tableForClassification.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                                                .getActiveFlag(system.getEnterpriseID(), identityToken));
		configureAddressLinkValue(tableForClassification, (P) this, (S) item, classification, tableForClassification.getValue(), system);
		
		tableForClassification.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(system, identityToken);
		}
		
		return tableForClassification;
	}
	
	void configureAddressLinkValue(Q linkTable, P primary, S secondary, IClassification<?> classificationValue, String value, ISystems<?> system);
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrUpdate(T classificationValue,
	                                                String searchValue,
	                                                String storeValue,
	                                                ISystems<?> system,
	                                                UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());
		
		IClassificationService addressService = get(IClassificationService.class);
		Classification classification = (Classification) addressService.find(classificationValue, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .withValue(searchValue)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canCreate(system.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification = (Q) add(classificationValue, storeValue, system, identityToken);
		}
		else
		{
			tableForClassification = exists.get();
			if (Strings.nullToEmpty(storeValue)
			           .equals(tableForClassification.getValue()))
			{
				return tableForClassification;
			}
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(system.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
			
			Q newTableForClassification = get(findAddressQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) system);
			newTableForClassification.setOriginalSourceSystemID(tableForClassification.getSystemID());
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(system.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(Strings.nullToEmpty(storeValue));
			newTableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			configureAddressLinkValue(newTableForClassification, (P) tableForClassification.getPrimary(), (S) tableForClassification.getSecondary(),
					classification, storeValue,
					system);
			newTableForClassification.persist();
			
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				newTableForClassification.createDefaultSecurity(system, identityToken);
			}
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> add(T addressClassification,
	                                        String storeValue,
	                                        ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = GuiceContext.get(findAddressQueryRelationshipTableType());
		
		IAddressService<?> service = GuiceContext.get(IAddressService.class);
		Address item = (Address) service.create(addressClassification, system, storeValue, identityToken);
		
		IClassificationService<?> addressService = get(IClassificationService.class);
		Classification classification = (Classification) addressService.find(addressClassification, system, identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setValue(storeValue == null ? STRING_EMPTY : storeValue);
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setClassificationID(classification);
		tableForClassification.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                                                .getActiveFlag(system.getEnterpriseID(), identityToken));
		
		configureAddressLinkValue(tableForClassification, (P) this, (S) item, classification, tableForClassification.getValue(), system);
		
		tableForClassification.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(system, identityToken);
		}
		
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuse(T classificationValue,
	                                               IResourceType<?> type,
	                                               String searchValue,
	                                               String storeValue,
	                                               ISystems<?> system,
	                                               UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .withValue(searchValue)
		                                                         .withEnterprise(system.getEnterpriseID())
		                                                         .canCreate(system.getEnterpriseID(), identityToken)
		                                                         .get();
		
		if (exists.isEmpty())
		{
			tableForClassification = (Q) add(classificationValue, storeValue, system, identityToken);
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> add(T classificationValue, IAddress<?> item, String storeValue, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());
		
		IClassificationService classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setClassificationID(classification);
		tableForClassification.setValue(storeValue == null ? STRING_EMPTY : storeValue);
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
		configureAddressLinkValue(tableForClassification, (P) this, (S) item, classification, storeValue,
				system);
		
		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				.isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(system, identityToken);
		}
		
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuse(T classificationValue, IAddress<?> item, String searchValue, String storeValue, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());
		
		IClassificationService classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .withValue(searchValue)
		                                                         .withEnterprise(system.getEnterpriseID())
		                                                         .canCreate(system.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue(storeValue);
			tableForClassification.setSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
			configureAddressLinkValue(tableForClassification, (P) this, (S) item, classification, storeValue,
					system);
			
			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(system, identityToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrUpdate(T classificationValue, IAddress<?> item, String searchValue, String storeValue, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());
		IClassificationService classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .withValue(searchValue)
		                                                         .withEnterprise(system.getEnterpriseID())
		                                                         .canCreate(system.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue(storeValue);
			tableForClassification.setSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
			configureAddressLinkValue(tableForClassification, (P) this, (S) item, classification, storeValue,
					system);
			
			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(system, identityToken);
			}
		}
		else
		{
			if (Strings.nullToEmpty(storeValue)
			           .equals(exists.get()
			                         .getValue()))
			{
				return exists.get();
			}
			tableForClassification = exists.get();
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();
			
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(system.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
			
			Q newTableForClassification = get(findAddressQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) system);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId()
			                                                                                .toString());
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(storeValue);
			newTableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			configureAddressLinkValue(newTableForClassification, (P) this, (S) tableForClassification.getSecondary(), classification, storeValue,
					system);
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
	default IRelationshipValue<L, R, ?> update(T classificationValue, String searchValue, String storeValue, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());
		IClassificationService classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withValue(searchValue)
		                                                         .withClassification(classification)
		                                                         .canRead(system, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			//do nothing to the object
		}
		else
		{
			tableForClassification = exists.get();
			if (Strings.nullToEmpty(storeValue)
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
			
			Q newTableForClassification = get(findAddressQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) system);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId()
			                                                                                .toString());
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(storeValue == null ? STRING_EMPTY : storeValue);
			newTableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			configureAddressLinkValue(newTableForClassification, (P) this, (S) tableForClassification.getSecondary(), classification, storeValue,
					system);
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
	default IRelationshipValue<L, R, ?> archive(T classificationValue, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());
		
		IClassificationService addressService = get(IClassificationService.class);
		Classification classification = (Classification) addressService.find(classificationValue, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withValue(searchValue)
		                                                         .withClassification(classification)
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
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> remove(T classificationValue, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findAddressQueryRelationshipTableType());
		
		IClassificationService<?> addressService = get(IClassificationService.class);
		Classification classification = (Classification) addressService.find(classificationValue, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findParentLink((P) this)
		                                                         .withClassification(classification)
		                                                         .withValue(searchValue)
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
