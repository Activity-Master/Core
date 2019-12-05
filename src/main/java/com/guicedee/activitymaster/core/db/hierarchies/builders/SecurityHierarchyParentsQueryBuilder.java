package com.guicedee.activitymaster.core.db.hierarchies.builders;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.hierarchies.SecurityHierarchyParents;
import com.entityassist.querybuilder.QueryBuilder;
import com.guicedee.guicedinjection.GuiceContext;

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
