package com.guicedee.activitymaster.core.db.hierarchies.builders;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.hierarchies.SecurityHierarchyParents;
import com.entityassist.querybuilder.QueryBuilder;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.persistence.EntityManager;

import java.util.UUID;

public class SecurityHierarchyParentsQueryBuilder
		extends QueryBuilder<SecurityHierarchyParentsQueryBuilder, SecurityHierarchyParents, UUID>
{
	@Override
	public EntityManager getEntityManager()
	{
		return GuiceContext.get(EntityManager.class, ActivityMasterDB.class);
	}

	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
