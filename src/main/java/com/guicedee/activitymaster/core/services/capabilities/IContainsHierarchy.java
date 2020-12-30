package com.guicedee.activitymaster.core.services.capabilities;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseHierarchyView;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderCore;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderHierarchyView;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.ActiveFlagService;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRelationshipValue;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.exceptions.SecurityAccessException;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.IEnterpriseService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static com.entityassist.enumerations.Operand.Equals;
import static com.entityassist.enumerations.Operand.InList;
import static com.guicedee.guicedinjection.GuiceContext.get;
import static com.guicedee.guicedinjection.json.StaticStrings.STRING_EMPTY;

/**
 * If this entity has hierarchy capabilities
 *
 * @param <Q> The type containing the hierarchy
 */
@SuppressWarnings({"rawtypes"})
public interface IContainsHierarchy<J extends WarehouseCoreTable<J, ?, UUID, ?>,
		Q extends WarehouseClassificationRelationshipTable<J, J, Q, ? extends QueryBuilderRelationshipClassification, ?, ?, ?, ?>,
		W extends WarehouseHierarchyView<?, ? extends QueryBuilderHierarchyView, ?>,
		L,
		T>
{
	default J addChild(T child, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		return addChild(child, STRING_EMPTY, enterprise, identifyingToken);
	}
	
	/**
	 * Adds a child with the default hierarchy type classification
	 *
	 * @param child
	 * @param hierarchyName
	 * @param enterprise
	 * @param identifyingToken
	 * @return
	 */
	@SuppressWarnings({"unchecked", "Duplicates"})
	@NotNull
	default J addChild(T child, String hierarchyName, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		J me = (J) this;
		Class<Q> hierarchyTable = findHierarchyTableType();
		Q linkTable = get(hierarchyTable);
		
		ClassificationService service = get(ClassificationService.class);
		Classification hierarchyType = (Classification) service.find(service.getHierarchyType(enterprise, identifyingToken)
		                                                                    .getName(), enterprise, identifyingToken);
		
		Optional<Q> exists = linkTable.builder()
		                              .findLink(me, (J) child, null)
		                              .inActiveRange(enterprise, identifyingToken)
		                              .inDateRange()
		                              .canCreate(enterprise, identifyingToken)
		                              .withClassification(hierarchyType)
		                              .withEnterprise(enterprise)
		                              .withValue(hierarchyName)
		                              .get();
		if (exists.isEmpty())
		{
			if (linkTable.builder()
			             .findChildLink((J) child, hierarchyName)
			             .inActiveRange(enterprise, identifyingToken)
			             .inDateRange()
			             .getCount() > 0
			)
			{
				Q existingLink = linkTable.builder()
				                          .findChildLink((J) child)
				                          .inActiveRange(enterprise, identifyingToken)
				                          .inDateRange()
				                          .get()
				                          .orElseThrow();
				configureNewHierarchyItem(existingLink, (T) me, child, hierarchyName);
				existingLink.update();
				return me;
			}
			ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
			                                               .getActivityMaster(enterprise, identifyingToken);
			linkTable.setSystemID((Systems) activityMasterSystem);
			linkTable.setActiveFlagID((ActiveFlag) GuiceContext.get(ActiveFlagService.class)
			                                                   .getActiveFlag(enterprise));
			linkTable.setOriginalSourceSystemID((Systems) activityMasterSystem);
			linkTable.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			linkTable.setEnterpriseID((Enterprise) enterprise);
			
			linkTable.setClassificationID(hierarchyType);
			
			linkTable.setValue(Strings.nullToEmpty(hierarchyName));
			configureNewHierarchyItem(linkTable, (T) me, child, hierarchyName);
			linkTable.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				linkTable.createDefaultSecurity(activityMasterSystem, identifyingToken);
			}
		}
		return (J) child;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findHierarchyTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsHierarchy.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[1];
			}
		}
		return null;
	}
	
	void configureNewHierarchyItem(Q newLink, T parent, T child, String value);
	
	/**
	 * Adds a child to the hierarchies table with the given classification value
	 *
	 * @param child               The child element
	 * @param hierarchy           The hierarchy name
	 * @param classificationValue A custom hierarchy type - try not to specify
	 * @param enterprise
	 * @param identifyingToken
	 * @return
	 */
	@SuppressWarnings({"unchecked", "Duplicates"})
	@NotNull
	default L addChild(T child, String hierarchy, IClassificationValue<?> classificationValue, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		J me = (J) this;
		Class<Q> hierarchyTable = findHierarchyTableType();
		Q linkTable = get(hierarchyTable);
		
		Optional<Q> exists = linkTable.builder()
		                              .findLink(me, (J) child, null)
		                              .inActiveRange(enterprise, identifyingToken)
		                              .inDateRange()
		                              .canCreate(enterprise, identifyingToken)
		                              .withEnterprise(enterprise)
		                              .get();
		if (exists.isEmpty())
		{
			if (linkTable.builder()
			             .findChildLink((J) child, null)
			             .inActiveRange(enterprise, identifyingToken)
			             .inDateRange()
			             .getCount() > 0
			)
			{
				Q existingLink = linkTable.builder()
				                          .findChildLink((J) child)
				                          .inActiveRange(enterprise, identifyingToken)
				                          .inDateRange()
				                          .get()
				                          .orElseThrow();
				configureNewHierarchyItem(existingLink, (T) me, child, null);
				existingLink.update();
				return (L) me;
			}
			ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
			                                               .getActivityMaster(enterprise, identifyingToken);
			linkTable.setSystemID((Systems) activityMasterSystem);
			linkTable.setActiveFlagID((ActiveFlag) GuiceContext.get(ActiveFlagService.class)
			                                                   .getActiveFlag(enterprise));
			linkTable.setOriginalSourceSystemID((Systems) activityMasterSystem);
			linkTable.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			linkTable.setEnterpriseID((Enterprise) enterprise);
			IClassification<?> classification = get(IClassificationService.class).find(classificationValue.classificationName(), enterprise, identifyingToken);
			linkTable.setClassificationID((Classification) classification);
			
			linkTable.setValue(Strings.nullToEmpty(hierarchy));
			configureNewHierarchyItem(linkTable, (T) me, child, hierarchy);
			linkTable.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				linkTable.createDefaultSecurity(activityMasterSystem, identifyingToken);
			}
		}
		return (L) child;
	}
	
	/**
	 * Removes a child with the default hierarchy type classification
	 *
	 * @param child
	 * @param enterprise
	 * @param identifyingToken
	 * @return
	 */
	@SuppressWarnings({"unchecked", "Duplicates"})
	@NotNull
	default L removeChild(T child, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		J me = (J) this;
		Class<Q> hierarchyTable = findHierarchyTableType();
		Q linkTable = get(hierarchyTable);
		ClassificationService service = get(ClassificationService.class);
		Classification hierarchyType = (Classification) service.find(service.getHierarchyType(enterprise, identifyingToken)
		                                                                    .getName(), enterprise, identifyingToken);
		Optional<Q> exists = linkTable.builder()
		                              .findLink(me, (J) child, null)
		                              .inActiveRange(enterprise, identifyingToken)
		                              .inDateRange()
		                              .canDelete(enterprise, identifyingToken)
		                              .withClassification(hierarchyType)
		                              .withEnterprise(enterprise)
		                              .withValue(STRING_EMPTY)
		                              .get();
		if (exists.isPresent())
		{
			exists.get()
			      .delete();
		}
		else
		{
			throw new SecurityAccessException("Cannot delete requested item");
		}
		return (L) this;
	}
	
	/**
	 * Removes a child with the given classification value
	 *
	 * @param child
	 * @param classificationValue
	 * @param enterprise
	 * @param identifyingToken
	 * @return
	 */
	@SuppressWarnings({"Duplicates"})
	@NotNull
	default L removeChild(T child, IClassificationValue<?> classificationValue, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		return removeChild(child, STRING_EMPTY, classificationValue, enterprise, identifyingToken);
	}
	
	/**
	 * Removes a child with the given classification value
	 *
	 * @param child
	 * @param classificationValue
	 * @param enterprise
	 * @param identifyingToken
	 * @return
	 */
	@SuppressWarnings({"unchecked", "Duplicates"})
	@NotNull
	default L removeChild(T child, String hierarchy, IClassificationValue<?> classificationValue, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		J me = (J) this;
		Class<Q> hierarchyTable = findHierarchyTableType();
		ClassificationService service = get(ClassificationService.class);
		Classification hierarchyType = (Classification) service.find(classificationValue, enterprise, identifyingToken);
		Q linkTable = get(hierarchyTable);
		Optional<Q> exists = linkTable.builder()
		                              .findLink(me, (J) child, null)
		                              .inActiveRange(enterprise, identifyingToken)
		                              .withClassification(hierarchyType)
		                              .inDateRange()
		                              .withValue(hierarchy)
		                              .canDelete(enterprise, identifyingToken)
		                              .withEnterprise(enterprise)
		                              .get();
		if (exists.isPresent())
		{
			exists.get()
			      .delete();
		}
		else
		{
			throw new SecurityAccessException("Cannot delete requested item");
		}
		return (L) this;
	}
	
	/**
	 * Finds the direct parent on the default Hierarchy Type
	 *
	 * @param identifyingToken
	 * @return
	 */
	default L findParent(UUID... identifyingToken)
	{
		return findParent(STRING_EMPTY, identifyingToken);
	}
	
	/**
	 * Finds the direct parent on A Hierarchy Type
	 *
	 * @param identifyingToken
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default L findParent(String hierarchyType,String classificationName,ISystems<?> system, UUID... identifyingToken)
	{
		J me = (J) this;
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationName, system.getEnterprise(), identifyingToken);
		Class<Q> hierarchyTable = findHierarchyTableType();
		Q linkTable = get(hierarchyTable);
		if(Strings.isNullOrEmpty(hierarchyType))
			hierarchyType = null;
		Optional<Q> exists = linkTable.builder()
		                              .findLink(null, (J) this)
		                              .inActiveRange(system.getEnterprise(), identifyingToken)
		                              .withClassification(classification)
		                              .inDateRange()
		                              .withValue(hierarchyType)
		                              .canRead(system.getEnterprise(), identifyingToken)
		                              .withEnterprise(system.getEnterprise())
		                              .get();
		if (exists.isPresent())
		{
			Q q = exists.get();
			return (L) q.getPrimary();
		}
		return null;
	}
	
	
	/**
	 * Finds the direct parent on A Hierarchy Type
	 *
	 * @param identifyingToken
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default L findParent(String hierarchyType, UUID... identifyingToken)
	{
		Class<W> hierarchyView = findHierarchyViewType();
		W hierarchy = get(hierarchyView);
		J me = (J) this;
		QueryBuilderHierarchyView<? extends QueryBuilderHierarchyView, ?, ?> builder = hierarchy.builder();
		if (!Strings.isNullOrEmpty(hierarchyType))
		{
			builder.where(builder.getAttribute("value"), Equals, hierarchyType);
		}
		UUID iid = builder.where(builder.getAttribute("id"), Equals, me.getId())
				.selectColumn(builder.getAttribute("parentID"))
				.get(UUID.class)
				.orElse(null);

		return (L) me.builder().find(iid).get().orElseThrow();
	}
	
	/**
	 * Returns if this type has a parent in the given hierarchy
	 *
	 * @return if a parent was found
	 */
	default boolean hasParent()
	{
		return hasParent(STRING_EMPTY);
	}
	
	/**
	 * Returns if this type has a parent in the given hierarchy
	 *
	 * @param hierarchyType The name of the hierarchy
	 * @return if a parent was found
	 */
	default boolean hasParent(String hierarchyType)
	{
		Class<W> hierarchyView = findHierarchyViewType();
		W hierarchy = get(hierarchyView);
		@SuppressWarnings("unchecked")
		J me = (J) this;
		return hierarchy.builder()
		                .findMyHierarchy(me.getId())
		                .withValue(hierarchyType)
		                .getCount() > 0;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<W> findHierarchyViewType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsHierarchy.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<W>) genericTypes[2];
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	default List<L> findChildren(UUID... identifyingToken)
	{
		Class<W> hierarchyView = findHierarchyViewType();
		W hierarchy = get(hierarchyView);
		J me = (J) this;

		QueryBuilderHierarchyView<? extends QueryBuilderHierarchyView, ?, ?> builder = hierarchy.builder();
		Collection<UUID> iids = builder.where(builder.getAttribute("parentID"), Equals, me.getId())
				.selectColumn(builder.getAttribute("id"))
				.getAll(UUID.class);
		QueryBuilderCore<?, J, UUID, ?> builder1 = me.builder();
		return (List<L>) builder1.where(builder1.<J,UUID>getAttribute("id"),InList,iids).getAll();
	}
	
	@SuppressWarnings("unchecked")
	default Set<Long> findSecurityChildren(IClassification<?> filter, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		Class<W> hierarchyView = findHierarchyViewType();
		W hierarchy = get(hierarchyView);
		
		J me = (J) this;
		List<W> hierarchies = (List<W>) hierarchy.builder()
		                                         .findMyChildren(me.getId())
		                                         .getAll();
		Set<Long> search = new LinkedHashSet<>();
		for (W w : hierarchies)
		{
			String[] split = w.getPath()
			                  .split("/");
			boolean foundMe = false;
			for (String s : split)
			{
				if (!foundMe)
				{
					if (s.equals(me.getId()
					               .toString()))
					{
						foundMe = true;
					}
				}
				else
				{
					search.add(Long.valueOf(s));
				}
			}
		}
		return search;
	}
	
	@SuppressWarnings("unchecked")
	default List<IRelationshipValue<T, T, ?>> findChildren(IClassification<?> filter, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		Class<Q> relationshipTableClass = findHierarchyTableType();
		try
		{
			Q relationshipTable = relationshipTableClass.getDeclaredConstructor()
			                                            .newInstance();
			QueryBuilderRelationshipClassification qb = relationshipTable.builder()
			                                                             .findParentLink((J) this)
			
			                                                             .inActiveRange(enterprise, identifyingToken)
			                                                             .inDateRange();
			if (filter != null)
			{
				qb.withClassification(filter);
			}
			return (List<IRelationshipValue<T, T, ?>>) qb.getAll();
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	default List<IRelationshipValue<T, T, ?>> findChildren(IClassification<?> filter, String value, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		Class<Q> relationshipTableClass = findHierarchyTableType();
		try
		{
			Q relationshipTable = relationshipTableClass.getDeclaredConstructor()
			                                            .newInstance();
			QueryBuilderRelationshipClassification qb = relationshipTable.builder()
			                                                             .findParentLink((J) this)
			                                                             .inActiveRange(enterprise, identifyingToken)
			                                                             .withValue(value)
			                                                             .inDateRange();
			if (filter != null)
			{
				qb.withClassification(filter);
			}
			return (List<IRelationshipValue<T, T, ?>>) qb.getAll();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	default Q findLink(T child, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		return findLink(child, enterprise, null, identifyingToken);
	}
	
	/**
	 * Finds a link on the default Hierarchy Classification Type
	 *
	 * @param child
	 * @param enterprise
	 * @param value
	 * @param identifyingToken
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default Q findLink(T child, IEnterprise<?> enterprise, String value, UUID... identifyingToken)
	{
		Class<Q> hierarchyView = findHierarchyTableType();
		Q linkTable = get(hierarchyView);
		ClassificationService service = get(ClassificationService.class);
		Classification hierarchyType = (Classification) service.find(service.getHierarchyType(enterprise, identifyingToken)
		                                                                    .getName(), enterprise, identifyingToken);
		return linkTable.builder()
		                .findLink((J) this, (J) child, value)
		                .inActiveRange(enterprise, identifyingToken)
		                .canRead(enterprise, identifyingToken)
		                .withClassification(hierarchyType)
		                .inDateRange()
		                .get()
		                .orElse(null);
	}
	
	default J setParent(T parent, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		System.out.println("SET PARENT NOT DONE");
		return null;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<J> findTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsHierarchy.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<J>) genericTypes[0];
			}
		}
		return null;
	}
}
