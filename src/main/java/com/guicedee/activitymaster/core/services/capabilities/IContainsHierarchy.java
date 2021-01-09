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
import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
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
	default L addChild(L child, ISystems<?> system, UUID... identifyingToken)
	{
		return addChild(child, STRING_EMPTY, system, identifyingToken);
	}
	
	@NotNull
	default L addChild(L child, String classificationName, ISystems<?> system, UUID... identifyingToken)
	{
		return addChild(child, classificationName, (String)null,system, identifyingToken);
	}
	
	/**
	 * Adds a child with the default hierarchy type classification
	 *
	 * @param child
	 * @param classificationName
	 * @param system
	 * @param identifyingToken
	 *
	 * @return
	 */
	@SuppressWarnings({"unchecked", "Duplicates"})
	@NotNull
	default L addChild(L child, String classificationName, String value, ISystems<?> system, UUID... identifyingToken)
	{
		J me = (J) this;
		Class<Q> hierarchyTable = findHierarchyTableType();
		Q linkTable = get(hierarchyTable);
		
		ClassificationService service = get(ClassificationService.class);
		if(Strings.isNullOrEmpty(classificationName))
			classificationName = HierarchyTypeClassification.classificationName();
		
		IEnterprise<?> enterprise = system.getEnterprise();
		
		Classification classification = (Classification) service.find(classificationName, system, identifyingToken);
		
		Optional<Q> exists = linkTable.builder()
		                              .findLink(me, (J) child, value)
		                              .inActiveRange(enterprise, identifyingToken)
		                              .inDateRange()
		                              .canCreate(enterprise, identifyingToken)
		                              .withClassification(classification)
		                              .withEnterprise(enterprise)
		                              .get();
		if (exists.isEmpty())
		{
			if (linkTable.builder()
			             .findChildLink((J) child, classificationName)
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
				configureNewHierarchyItem(existingLink, (T) me, (T) child, value);
				existingLink.update();
				return (L) me;
			}
			linkTable.setSystemID((Systems) system);
			linkTable.setActiveFlagID((ActiveFlag) GuiceContext.get(ActiveFlagService.class)
			                                                   .getActiveFlag(enterprise));
			linkTable.setOriginalSourceSystemID((Systems) system);
			linkTable.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			linkTable.setEnterpriseID((Enterprise) enterprise);
			
			linkTable.setClassificationID(classification);
			
			linkTable.setValue(Strings.nullToEmpty(value));
			configureNewHierarchyItem(linkTable, (T) me, (T) child, value);
			linkTable.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				linkTable.createDefaultSecurity(system, identifyingToken);
			}
		}
		return (L) me;
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
	 * @param system
	 * @param identifyingToken
	 *
	 * @return
	 */
	@NotNull
	default L addChild(L child, String hierarchy, IClassificationValue<?> classificationValue,  ISystems<?> system, UUID... identifyingToken)
	{
		return addChild(child, classificationValue.classificationName(), hierarchy, system, identifyingToken);
	}
	
	/**
	 * Removes a child with the default hierarchy type classification
	 *
	 * @param child
	 * @param system
	 * @param identifyingToken
	 *
	 * @return
	 */
	@SuppressWarnings({"unchecked", "Duplicates"})
	@NotNull
	default L removeChild(L child, String classificationName, String hierarchyValue, ISystems<?> system, UUID... identifyingToken)
	{
		J me = (J) this;
		Class<Q> hierarchyTable = findHierarchyTableType();
		Q linkTable = get(hierarchyTable);
		ClassificationService service = get(ClassificationService.class);
		String hierarchyClassification;
		IEnterprise<?> enterprise = system.getEnterprise();
		if (Strings.isNullOrEmpty(classificationName))
		{
			hierarchyClassification = HierarchyTypeClassification.classificationName();
		}
		else
		{
			hierarchyClassification = classificationName;
		}
		Classification hierarchyType = (Classification) service.find(hierarchyClassification, system, identifyingToken);
		
		Optional<Q> exists = linkTable.builder()
		                              .findLink(me, (J) child, hierarchyValue)
		                              .inActiveRange(enterprise, identifyingToken)
		                              .inDateRange()
		                              .canDelete(system, identifyingToken)
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
	 * @param system
	 * @param identityToken
	 *
	 * @return
	 */
	@NotNull
	default L removeChild(L child, String hierarchy, IClassificationValue<?> classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return removeChild(child, classificationValue.classificationName(), hierarchy, system, identityToken);
	}

	/**
	 * Finds the direct parent on A Hierarchy Type
	 *
	 * @param identifyingToken
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default L findParent(String hierarchyValue, String classificationName, ISystems<?> system, UUID... identifyingToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationName, system, identifyingToken);
		Class<Q> hierarchyTable = findHierarchyTableType();
		Q linkTable = get(hierarchyTable);
		if (Strings.isNullOrEmpty(hierarchyValue))
		{
			hierarchyValue = null;
		}
		Optional<Q> exists = linkTable.builder()
		                              .findLink(null, (J) this)
		                              .inActiveRange(system.getEnterprise(), identifyingToken)
		                              .withClassification(classification)
		                              .inDateRange()
		                              .withValue(hierarchyValue)
		                              .canRead(system, identifyingToken)
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
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default List<L> findParents(String hierarchyValue, String classificationName, ISystems<?> system, UUID... identifyingToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationName, system, identifyingToken);
		Class<Q> hierarchyTable = findHierarchyTableType();
		Q linkTable = get(hierarchyTable);
		if (Strings.isNullOrEmpty(hierarchyValue))
		{
			hierarchyValue = null;
		}
		List exists = linkTable.builder()
		                       .findLink(null, (J) this)
		                       .inActiveRange(system.getEnterprise(), identifyingToken)
		                       .withClassification(classification)
		                       .inDateRange()
		                       .withValue(hierarchyValue)
		                       .canRead(system, identifyingToken)
		                       .withEnterprise(system.getEnterprise())
		                       .getAll();
		return exists;
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
	 *
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
	
	default List<IRelationshipValue<L, L, ?>> findChildren(IClassificationValue<?> classificationFilter, ISystems<?> system, UUID... identifyingToken)
	{
		return findChildren(classificationFilter.toString(),null, system, identifyingToken);
	}
	
	@SuppressWarnings("unchecked")
	default List<IRelationshipValue<L, L, ?>> findChildren(String classificationFilter, String value, ISystems<?> system, UUID... identifyingToken)
	{
		Class<Q> relationshipTableClass = findHierarchyTableType();
		
		String hierarchyClassification;
		if (Strings.isNullOrEmpty(classificationFilter))
		{
			hierarchyClassification = HierarchyTypeClassification.classificationName();
		}
		else
		{
			hierarchyClassification = classificationFilter;
		}
		try
		{
			Q relationshipTable = relationshipTableClass.getDeclaredConstructor()
			                                            .newInstance();
			QueryBuilderRelationshipClassification qb = relationshipTable.builder()
			                                                             .findLink((J)this,null,value)
			                                                             .inActiveRange(system, identifyingToken)
			                                                             .inDateRange();
			if (hierarchyClassification != null)
			{
				IClassificationService<?> service = get(IClassificationService.class);
				IClassification<?> classification = service.find(hierarchyClassification, system, identifyingToken);
				qb.withClassification(classification);
			}
			return (List<IRelationshipValue<L, L, ?>>) qb.getAll();
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	default List<IRelationshipValue<L, L, ?>> findChildren(IClassification<?> filter, String value, ISystems<?> system, UUID... identifyingToken)
	{
		return findChildren(filter.getName(), value, system, identifyingToken);
	}
	
	default Q findLink(L child,  ISystems<?> system, UUID... identifyingToken)
	{
		return findLink(child, HierarchyTypeClassification.classificationName(),null,system,identifyingToken);
	}
	
	/**
	 * Finds a link on the default Hierarchy Classification Type
	 *
	 * @param child
	 * @param system
	 * @param value
	 * @param identifyingToken
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default Q findLink(L child,String classificationName, String value, ISystems<?> system,UUID... identifyingToken)
	{
		Class<Q> hierarchyView = findHierarchyTableType();
		Q linkTable = get(hierarchyView);
		ClassificationService service = get(ClassificationService.class);
		
		IEnterprise<?> enterprise = system.getEnterprise();
		String hierarchyClassification;
		if (Strings.isNullOrEmpty(classificationName))
		{
			hierarchyClassification = HierarchyTypeClassification.classificationName();
		}
		else
		{
			hierarchyClassification = classificationName;
		}
		Classification hierarchyType = (Classification) service.find(hierarchyClassification, system, identifyingToken);
		return linkTable.builder()
		                .findLink((J) this, (J) child, value)
		                .inActiveRange(enterprise, identifyingToken)
		                .canRead(system, identifyingToken)
		                .withClassification(hierarchyType)
		                .inDateRange()
		                .get()
		                .orElse(null);
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
