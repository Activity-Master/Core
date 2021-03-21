package com.guicedee.activitymaster.core.db.hierarchies.builders;

import com.entityassist.querybuilder.QueryBuilder;
import com.guicedee.activitymaster.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.hierarchies.SecurityHierarchyParents;
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
