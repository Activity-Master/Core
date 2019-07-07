package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.ActiveFlagService;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.exceptions.SecurityAccessException;
import com.google.common.base.Strings;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static com.jwebmp.entityassist.querybuilder.EntityAssistStrings.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

/**
 * If this entity has hierarchy capabilities
 *
 * @param <Q>
 * 		The type containing the hierarchy
 */
public interface IContainsHierarchy<J extends WarehouseCoreTable,
		                                   Q extends WarehouseClassificationRelationshipTable<J, J, Q, ? extends QueryBuilderRelationshipClassification, ?, ?,?,?>,
		                                   W extends WarehouseHierarchyView<?, ? extends QueryBuilderHierarchyView, ?>>
{
	default J addChild(J child, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		return addChild(child, "", enterprise, identifyingToken);
	}

	@SuppressWarnings({"unchecked", "Duplicates"})
	@NotNull
	default J addChild(J child, String value, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		J me = (J) this;
		Class<Q> hierarchyTable = findHierarchyTableType();
		Q linkTable = get(hierarchyTable);

		Optional<Q> exists = linkTable.builder()
		                              .findLink(me, child, null)
		                              .inActiveRange(enterprise, identifyingToken)
		                              .inDateRange()
		                              .canCreate(enterprise, identifyingToken)
		                              .withEnterprise(enterprise)
		                              .get();
		if (exists.isEmpty())
		{
			if (linkTable.builder()
			             .findChildLink(child)
			             .inActiveRange(enterprise, identifyingToken)
			             .inDateRange()
			             .getCount() > 0
			)
			{
				Q existingLink = linkTable.builder()
				                          .findChildLink(child)
				                          .inActiveRange(enterprise, identifyingToken)
				                          .inDateRange()
				                          .get()
				                          .orElseThrow();
				configureNewHierarchyItem(existingLink, me, child, value);
				existingLink.update();
				return me;
			}

			ClassificationService service = get(ClassificationService.class);
			Classification hierarchyType = (Classification) service.getHierarchyType(enterprise);
			ISystems<?> activityMasterSystem = get(SystemsService.class)
			                                               .getActivityMaster(enterprise, identifyingToken);
			linkTable.setSystemID((Systems) activityMasterSystem);
			linkTable.setActiveFlagID(get(ActiveFlagService.class).getActiveFlag(enterprise));
			linkTable.setOriginalSourceSystemID((Systems) activityMasterSystem);
			linkTable.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			linkTable.setEnterpriseID((Enterprise) enterprise);
			linkTable.setClassificationID(hierarchyType);
			linkTable.setValue(Strings.nullToEmpty(value));
			configureNewHierarchyItem(linkTable, me, child, value);
			linkTable.persist();
			if (get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				linkTable.createDefaultSecurity(activityMasterSystem, identifyingToken);
			}
		}
		return child;
	}

	@SuppressWarnings({"unchecked", "Duplicates"})
	@NotNull
	default J removeChild(J child, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		J me = (J) this;
		Class<Q> hierarchyTable = findHierarchyTableType();
		Q linkTable = get(hierarchyTable);
		Optional<Q> exists = linkTable.builder()
		                              .findLink(me, child, null)
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

	default Q findLink(J child, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		return findLink(child, enterprise, null, identifyingToken);
	}

	@SuppressWarnings("unchecked")
	default Q findLink(J child, IEnterprise<?> enterprise, String value, UUID... identifyingToken)
	{
		Class<Q> hierarchyView = findHierarchyTableType();
		Q linkTable = get(hierarchyView);
		return linkTable.builder()
		                .findLink((J) this, child, value)
		                .inActiveRange(enterprise, identifyingToken)
		                .canRead(enterprise, identifyingToken)
		                .inDateRange()
		                .get()
		                .orElse(null);
	}

	default J setParent(J parent, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		return null;
	}

	void configureNewHierarchyItem(Q newLink, J parent, J child, String value);

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
}
