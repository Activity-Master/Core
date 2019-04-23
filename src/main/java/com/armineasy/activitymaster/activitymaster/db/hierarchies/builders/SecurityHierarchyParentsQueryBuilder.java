package com.armineasy.activitymaster.activitymaster.db.hierarchies.builders;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderDefault;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.SecurityHierarchyParents;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.SecurityHierarchyView;
import com.jwebmp.entityassist.querybuilder.QueryBuilder;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.persistence.EntityManager;

public class SecurityHierarchyParentsQueryBuilder
		extends QueryBuilder<SecurityHierarchyParentsQueryBuilder, SecurityHierarchyParents, Long>
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
}
