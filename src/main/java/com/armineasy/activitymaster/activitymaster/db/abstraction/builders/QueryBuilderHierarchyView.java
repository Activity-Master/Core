package com.armineasy.activitymaster.activitymaster.db.abstraction.builders;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.SecurityHierarchyView_;
import com.entityassist.querybuilder.QueryBuilder;
import com.guicedee.guicedinjection.GuiceContext;

import javax.persistence.EntityManager;
import java.io.Serializable;

import static com.entityassist.enumerations.Operand.*;

public abstract class QueryBuilderHierarchyView <J extends QueryBuilderHierarchyView<J, E, I>, E extends WarehouseHierarchyView<E, J, I>, I extends Serializable>
	extends QueryBuilder<J,E,I>
{
	@Override
	public EntityManager getEntityManager()
	{
		return GuiceContext.get(EntityManager.class, ActivityMasterDB.class);
	}

	@Override
	protected boolean isIdGenerated()
	{
		return false;
	}

	@SuppressWarnings("unchecked")
	public J findMyHierarchy(Serializable token)
	{
		where(getAttribute("path"), Like, "%" + token);
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	public J findMyChildrenRecursive(Serializable token)
	{
		where(getAttribute("path"), Like, "%" + token + "%");
		where(getAttribute("path"), NotLike, "%" + token);
		return (J) this;
	}


	@SuppressWarnings("unchecked")
	public J findMyChildren(Long securityTokenID)
	{
		where(SecurityHierarchyView_.parentID, Equals, securityTokenID);
		return (J) this;
	}
}
