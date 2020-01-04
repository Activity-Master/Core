package com.guicedee.activitymaster.core.services.capabilities;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseHierarchyView;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilder;
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
import com.guicedee.activitymaster.core.services.exceptions.SecurityAccessException;
import com.guicedee.activitymaster.core.services.system.IEnterpriseService;
import com.guicedee.guicedinjection.GuiceContext;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static com.entityassist.querybuilder.EntityAssistStrings.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

/**
 * If this entity has hierarchy capabilities
 *
 * @param <Q>
 * 		The type containing the hierarchy
 */
public interface IContainsHierarchy<J extends WarehouseCoreTable,
		                                   Q extends WarehouseClassificationRelationshipTable<J, J, Q, ? extends QueryBuilderRelationshipClassification, ?, ?, ?, ?>,
		                                   W extends WarehouseHierarchyView<?, ? extends QueryBuilderHierarchyView, ?>,
		                                   T>
{
	default J addChild(T child, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		return addChild(child, STRING_EMPTY, enterprise, identifyingToken);
	}
	//	Optional<IRelationshipValue<T, T, ?>>

	@SuppressWarnings({"unchecked", "Duplicates"})
	@NotNull
	default J addChild(T child, String value, IEnterprise<?> enterprise, UUID... identifyingToken)
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
			             .findChildLink((J) child, value)
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
				return me;
			}

			ClassificationService service = get(ClassificationService.class);
			Classification hierarchyType = (Classification) service.getHierarchyType(enterprise);
			ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
			                                               .getActivityMaster(enterprise, identifyingToken);
			linkTable.setSystemID((Systems) activityMasterSystem);
			linkTable.setActiveFlagID((ActiveFlag) GuiceContext.get(ActiveFlagService.class)
			                                                   .getActiveFlag(enterprise));
			linkTable.setOriginalSourceSystemID((Systems) activityMasterSystem);
			linkTable.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			linkTable.setEnterpriseID((Enterprise) enterprise);
			linkTable.setClassificationID(hierarchyType);
			linkTable.setValue(Strings.nullToEmpty(value));
			configureNewHierarchyItem(linkTable, (T) me, (T) child, value);
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

	@SuppressWarnings({"unchecked", "Duplicates"})
	@NotNull
	default J removeChild(T child, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		J me = (J) this;
		Class<Q> hierarchyTable = findHierarchyTableType();
		Q linkTable = get(hierarchyTable);
		Optional<Q> exists = linkTable.builder()
		                              .findLink(me, (J) child, null)
		                              .inActiveRange(enterprise, identifyingToken)
		                              .inDateRange()
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
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	default J findParent(UUID... identifyingToken)
	{
		Class<W> hierarchyView = findHierarchyViewType();
		W hierarchy = get(hierarchyView);
		J me = (J) this;
		hierarchy = (W) hierarchy.builder()
		                         .findMyHierarchy(me.getId())
		                         .get()
		                         .orElseThrow();

		Long parent = hierarchy.getParentID();
		if (parent != null)
		{
			QueryBuilder qb = (QueryBuilder) me.builder()
			                                   .find(parent);
			qb.inDateRange();

			J returnedEntity = (J) qb.get()
			                         .orElseThrow();
			return returnedEntity;
		}
		return null;
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
	default List<J> findChildren(UUID... identifyingToken)
	{
		Class<W> hierarchyView = findHierarchyViewType();
		W hierarchy = get(hierarchyView);

		J me = (J) this;
		List<W> hierarchies = (List<W>) hierarchy.builder()
		                                         .findMyChildren((Long) me.getId())
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
		QueryBuilder qb = (QueryBuilder) me.builder()
		                                   .find(search);
		qb.inDateRange();
		List<J> returnedEntity = qb.getAll();
		return returnedEntity;
	}

	@SuppressWarnings("unchecked")
	default Set<Long> findSecurityChildren(IClassification<?> filter, IEnterpriseName<?> enterpriseName, UUID... identifyingToken)
	{
		Class<W> hierarchyView = findHierarchyViewType();
		W hierarchy = get(hierarchyView);

		J me = (J) this;
		List<W> hierarchies = (List<W>) hierarchy.builder()
		                                         .findMyChildren((Long) me.getId())
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
	default List<IRelationshipValue<T, T, ?>> findChildren(IClassification<?> filter, IEnterpriseName<?> enterpriseName, UUID... identifyingToken)
	{
		Class<Q> relationshipTableClass = findHierarchyTableType();
		try
		{
			IEnterprise<?> enterprise = get(IEnterpriseService.class).getEnterprise(enterpriseName);
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

	default Q findLink(T child, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		return findLink(child, enterprise, null, identifyingToken);
	}

	@SuppressWarnings("unchecked")
	default Q findLink(T child, IEnterprise<?> enterprise, String value, UUID... identifyingToken)
	{
		Class<Q> hierarchyView = findHierarchyTableType();
		Q linkTable = get(hierarchyView);
		return linkTable.builder()
		                .findLink((J) this, (J) child, value)
		                .inActiveRange(enterprise, identifyingToken)
		                .canRead(enterprise, identifyingToken)
		                .inDateRange()
		                .get()
		                .orElse(null);
	}

	default J setParent(T parent, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
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
