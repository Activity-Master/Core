package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseBaseTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationship;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemData;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemType;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.*;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IResourceType;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.services.system.IClassificationService;
import com.armineasy.activitymaster.activitymaster.services.system.IResourceItemService;
import com.jwebmp.entityassist.querybuilder.builders.JoinExpression;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.interfaces.JobService;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

import static com.jwebmp.entityassist.SCDEntity.*;
import static com.jwebmp.entityassist.enumerations.Operand.*;
import static com.jwebmp.entityassist.querybuilder.EntityAssistStrings.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;
import static javax.persistence.criteria.JoinType.*;

@SuppressWarnings("Duplicates")
public interface IContainsResourceItems<P extends WarehouseCoreTable,
		                                       S extends WarehouseCoreTable,
		                                       Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?>,
		                                       T extends IResourceType<?>,
											   C extends IResourceItemClassification<?>,
		                                       J extends IContainsResourceItems<P, S, Q, T,C, J>>
{
	void configureResourceItemLinkValue(Q linkTable, P primary, S secondary, Classification classificationValue, String value, IEnterprise<?> enterprise);

	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findResourceItemQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsResourceItems.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	default void storeResourceItemData(ResourceItem item, byte[] data, ISystems<?> originatingSystem, UUID... identifyingToken)
	{
		ResourceItemData itemData = new ResourceItemData();
		itemData.setResource(item);
		itemData.setResourceItemData(data);

		itemData.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		itemData.setSystemID((Systems)originatingSystem);
		itemData.setOriginalSourceSystemID((Systems) originatingSystem);

		itemData.setActiveFlagID(((Systems)originatingSystem).getActiveFlagID());
		itemData.persist();

		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			itemData.createDefaultSecurity(originatingSystem, identifyingToken);
		}
	}


	/**
	 * Returns either a List with Strings, or a List with Object[] for each row and values returned
	 * @param resourceItemType The value to apply
	 * @param originatingSystem The system coming from
	 * @param identityToken The identity token to use
	 * @param values Any additional values to select
	 *
	 * @return The result of List&gt;String&lt; or List&gt;Object[]&lt;
	 */
	default List getValues(T resourceItemType, ISystems<?> originatingSystem, UUID identityToken, T... values)
	{
		Q activityMasterIdentity = get(findResourceItemQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		List<T> fetching = new ArrayList<>();
		fetching.add(resourceItemType);
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
			IClassification<?> classification = classificationService.find((IClassificationValue<?>) valuesToGet, originatingSystem.getEnterpriseID(), identityToken);

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

	default double sumAll(T reesourceItemType, ISystems<?> originatingSystem, UUID identityToken)
	{
		List<String> results = getValues(reesourceItemType, originatingSystem, identityToken);
		double d = 0.0d;
		for (String result : results)
		{
			Double D = Double.parseDouble(result);
			d += D;
		}
		return d;
	}


	default Q add(C iClassificationValue,IResourceItem<?> resourceItem, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemQueryRelationshipTableType());

		IClassificationService resourceItemService = get(IClassificationService.class);
		Classification classification = (Classification) resourceItemService.find(iClassificationValue, originatingSystem.getEnterpriseID(), identityToken);


		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID(classification);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureResourceItemLinkValue(tableForClassification, (P)this, (S) resourceItem, classification, value, originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}

		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<?>> find(T resourceItemType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findResourceItemQueryRelationshipTableType());
		IResourceItemService resourceItemService = get(IResourceItemService.class);
		ResourceItemType classification = (ResourceItemType) resourceItemService.findResourceItemType(resourceItemType, originatingSystem, identityToken);
		return (Optional<IRelationshipValue<?>>) activityMasterIdentity.builder()
		                                                               .findLink((P) this, (S) classification, null)
		                                                               .inActiveRange(originatingSystem.getEnterpriseID())
		                                                               .inDateRange()
		                                                               .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                                               .get();
	}

	@SuppressWarnings("unchecked")
	default Optional<Q> findFirst(T resourceItemType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findResourceItemQueryRelationshipTableType());
		IResourceItemService resourceItemService = get(IResourceItemService.class);
		ResourceItemType classification = (ResourceItemType) resourceItemService.findResourceItemType(resourceItemType, originatingSystem, identityToken);

		return (Optional<Q>) activityMasterIdentity.builder()
		                                           .findLink((P) this, (S) classification, null)
		                                           .inActiveRange(originatingSystem.getEnterpriseID())
		                                           .inDateRange()
		                                           .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                                           .setReturnFirst(true)
		                                           .get();
	}

	@SuppressWarnings("unchecked")
	default List<Q> findAll(T resourceItemType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findResourceItemQueryRelationshipTableType());
		IResourceItemService resourceItemService = get(IResourceItemService.class);
		ResourceItemType classification = (ResourceItemType) resourceItemService.findResourceItemType(resourceItemType, originatingSystem, identityToken);
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
		Q activityMasterIdentity = get(findResourceItemQueryRelationshipTableType());
		IResourceItemService resourceItemService = get(IResourceItemService.class);
		ResourceItemType classification = (ResourceItemType) resourceItemService.findResourceItemType(classificationValue, originatingSystem, identityToken);

		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(originatingSystem.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(originatingSystem.getEnterpriseID(), identityToken)
		                             .getCount();
	}


	@SuppressWarnings("unchecked")
	default ResourceItem add(C classificationValue,T resourceTypeValue,
	                          byte[] data,
	                          String mimeType,
	                          ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = GuiceContext.get(findResourceItemQueryRelationshipTableType());

		IResourceItemService service = GuiceContext.get(IResourceItemService.class);

		ResourceItem item = (ResourceItem) service.create(resourceTypeValue, mimeType, originatingSystem, identityToken);



		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		ResourceItemType classificationDataConcept = (ResourceItemType) classificationDataConceptService.findResourceItemType(resourceTypeValue, originatingSystem, identityToken);

		IClassificationService resourceItemService = get(IClassificationService.class);
		Classification classification = (Classification) resourceItemService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		boolean async = GuiceContext.get(ActivityMasterConfiguration.class)
		                            .isAsyncEnabled();
		if (async)
		{
			JobService.getInstance()
			          .addJob("ResourceItemDataStore", () -> storeResourceItemData(item, data, originatingSystem, identityToken));
		}
		else
		{
			storeResourceItemData(item, data, originatingSystem, identityToken);
		}

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setValue(STRING_EMPTY);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setActiveFlagID(((Systems)originatingSystem).getActiveFlagID());
		tableForClassification.setClassificationID(classification);
		configureResourceItemLinkValue(tableForClassification, (P)this, (S)item, classification, tableForClassification.getValue(), originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}

		return item;
	}

	@SuppressWarnings("unchecked")
	default Q add(C classificationValue, T resourceItemType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemQueryRelationshipTableType());

		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		ResourceItemType classificationDataConcept = (ResourceItemType) classificationDataConceptService.findResourceItemType(resourceItemType, originatingSystem, identityToken);

		IClassificationService resourceItemService = get(IClassificationService.class);
		Classification classification = (Classification) resourceItemService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID(classification);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureResourceItemLinkValue(tableForClassification, (P)this, (S)classificationDataConcept, classification, value, originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}

		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default Q addOrUpdate(C classificationValue, T resourceItemType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemQueryRelationshipTableType());
		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		ResourceItemType classificationDataConcept = (ResourceItemType) classificationDataConceptService.findResourceItemType(resourceItemType, originatingSystem, identityToken);

		IClassificationService resourceItemService = get(IClassificationService.class);
		Classification classification = (Classification) resourceItemService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification(classification)
		                                                         .canCreate(originatingSystem.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification = addOrReuse(classificationValue, resourceItemType, value, originatingSystem, identityToken);
		}
		else
		{
			tableForClassification = exists.get();
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();

			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID(flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();

			Q newTableForClassification = get(findResourceItemQueryRelationshipTableType());
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
			configureResourceItemLinkValue(newTableForClassification, (P)this, (S)classificationDataConcept, classification, value, originatingSystem.getEnterpriseID());
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
	default Q addOrReuse(C classificationValue, T resourceItemType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemQueryRelationshipTableType());
		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		ResourceItemType classificationDataConcept = (ResourceItemType) classificationDataConceptService.findResourceItemType(resourceItemType, originatingSystem, identityToken);

		IClassificationService resourceItemService = get(IClassificationService.class);
		Classification classification = (Classification) resourceItemService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			//			configureInvolvedPartyIdentificationType(tableForClassification, classification, (Q) classificationDataConcept, originatingSystem.getEnterpriseID());
			configureResourceItemLinkValue(tableForClassification, (P)this, (S)classificationDataConcept, classification, value, originatingSystem.getEnterpriseID());

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
	default Q add(IClassification<?> iClassification, T resourceItemType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemQueryRelationshipTableType());
		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		ResourceItemType classificationDataConcept =(ResourceItemType)  classificationDataConceptService.findResourceItemType(resourceItemType, originatingSystem, identityToken);


		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setClassificationID((Classification) iClassification);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		configureResourceItemLinkValue(tableForClassification, (P)this, (S)classificationDataConcept, (Classification) iClassification, value, originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identityToken);
		}

		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default Q addOrReuse(IClassification<?> classification, T resourceItemType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemQueryRelationshipTableType());
		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		ResourceItemType classificationDataConcept = (ResourceItemType) classificationDataConceptService.findResourceItemType(resourceItemType, originatingSystem, identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			configureResourceItemLinkValue(tableForClassification, (P)this, (S)classificationDataConcept, (Classification) classification, value, originatingSystem.getEnterpriseID());

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
	default Q addOrUpdate(IClassification<?> classification, T resourceItemType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemQueryRelationshipTableType());
		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		ResourceItemType classificationDataConcept = (ResourceItemType) classificationDataConceptService.findResourceItemType(resourceItemType, originatingSystem, identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			configureResourceItemLinkValue(tableForClassification, (P)this, (S)classificationDataConcept, (Classification) classification, value, originatingSystem.getEnterpriseID());

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

			Q newTableForClassification = get(findResourceItemQueryRelationshipTableType());
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
			configureResourceItemLinkValue(newTableForClassification, (P)this, (S)classificationDataConcept, (Classification) classification, value, originatingSystem.getEnterpriseID());
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
	default Q update(C classificationValue, T resourceItemType, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemQueryRelationshipTableType());
		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		ResourceItemType classificationDataConcept =(ResourceItemType)  classificationDataConceptService.findResourceItemType(resourceItemType, originatingSystem, identityToken);

		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withClassification((IClassification<?>) classificationValue)
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

			IClassificationService resourceItemService = get(IClassificationService.class);
			Classification classification = (Classification) resourceItemService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);

			Q newTableForClassification = get(findResourceItemQueryRelationshipTableType());
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
			configureResourceItemLinkValue(newTableForClassification, (P)this, (S)classificationDataConcept, classification, value, originatingSystem.getEnterpriseID());
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
	default Q archive(C classificationValue, T resourceItemType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemQueryRelationshipTableType());
		IResourceItemService classificationDataConceptService = get(IResourceItemService.class);
		ResourceItemType classificationDataConcept = (ResourceItemType) classificationDataConceptService.findResourceItemType(resourceItemType, originatingSystem, identityToken);


		IClassificationService resourceItemService = get(IClassificationService.class);
		Classification classification = (Classification) resourceItemService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);


		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classificationDataConcept, null)
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
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();

			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID(flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}

	@SuppressWarnings("unchecked")
	default Q remove(C classificationValue,T identificationType,ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findResourceItemQueryRelationshipTableType());

		IClassificationService resourceItemService = get(IClassificationService.class);
		Classification classification = (Classification) resourceItemService.find(classificationValue, originatingSystem.getEnterpriseID(), identityToken);


		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) identificationType, null)
		                                                         .withClassification(classification)
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






	
	
	
	
/*




	@SuppressWarnings("unchecked")
	@CacheResult
	default Optional<Q> findResourceItem(@CacheKey ResourceItem classification, @CacheKey UUID... identityToken)
	{
		Q activityMasterIdentity = GuiceContext.get(findResourceItemQueryRelationshipTableType());
		Optional<Q> exists = (Optional<Q>) activityMasterIdentity.builder()
		                                                         .findLink((P) this, (S) classification, null)
		                                                         .inActiveRange(classification.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(classification.getEnterpriseID(), identityToken)
		                                                         .get();
		return exists;
	}


	@SuppressWarnings("unchecked")
	default boolean hasResourceItem(ResourceItem classification, @CacheKey UUID... identityToken)
	{
		Q activityMasterIdentity = GuiceContext.get(findResourceItemQueryRelationshipTableType());
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(classification.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(classification.getEnterpriseID(), identityToken)
		                             .getCount() > 0;
	}
*/








/*

	@SuppressWarnings("unchecked")
	default ResourceItem addOrUseResourceItem(IResourceType<?> resourceTypeValue, IResourceItemClassification classification,
	                                          byte[] data,
	                                          String mimeType,
	                                          ISystems<?> originatingSystem, UUID... identifyingToken)
	{
		Q tableForClassification = GuiceContext.get(findResourceItemQueryRelationshipTableType());

		IResourceItemService service = GuiceContext.get(IResourceItemService.class);
		ResourceItem item = service.create(resourceTypeValue, mimeType, originatingSystem, identifyingToken);



		//TODO YOU ARE HERE!!!!

		boolean async = GuiceContext.get(ActivityMasterConfiguration.class)
		                            .isAsyncEnabled();
		if (async)
		{
			JobService.getInstance()
			          .addJob("ResourceItemDataStore", () -> storeResourceItemData(item, data, originatingSystem, identifyingToken));
		}
		else
		{
			storeResourceItemData(item, data, originatingSystem, identifyingToken);
		}

		tableForClassification.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		tableForClassification.setValue("");
		tableForClassification.setSystemID((Systems) originatingSystem);
		tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
		tableForClassification.setActiveFlagID(((Systems)originatingSystem).getActiveFlagID());
		tableForClassification.setClassificationID((Classification) GuiceContext.get(IClassificationService.class)
		                                                                        .find(classification, originatingSystem, identifyingToken));
		setMyResourceItemLinkValue(tableForClassification, (S) item, originatingSystem.getEnterpriseID());

		tableForClassification.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(originatingSystem, identifyingToken);
		}

		return item;
	}


	@SuppressWarnings("unchecked")
	default Q add(ResourceItem resourceItem, IResourceItemClassification<?> iclassification, ISystems<?> originatingSystem, UUID... identifyingToken)
	{
		Q tableForClassification = get(findResourceItemQueryRelationshipTableType());
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) resourceItem, null)
		                                                         .inActiveRange(resourceItem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identifyingToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			Classification classification = (Classification) get(ClassificationService.class).find(iclassification,
			                                                                                       originatingSystem, identifyingToken);

			tableForClassification.setEnterpriseID(resourceItem.getEnterpriseID());
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue("");
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setActiveFlagID(resourceItem.getActiveFlagID());
			setMyResourceItemLinkValue(tableForClassification, (S) resourceItem, resourceItem.getEnterpriseID());
			tableForClassification.persist();

			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originatingSystem, identifyingToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}


*/


}
