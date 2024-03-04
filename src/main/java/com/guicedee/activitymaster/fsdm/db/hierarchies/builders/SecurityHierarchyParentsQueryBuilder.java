package com.guicedee.activitymaster.fsdm.db.hierarchies.builders;

import com.entityassist.querybuilder.QueryBuilder;
import com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.fsdm.db.hierarchies.SecurityHierarchyParents;
import jakarta.persistence.EntityManager;

public class SecurityHierarchyParentsQueryBuilder
		extends QueryBuilder<SecurityHierarchyParentsQueryBuilder, SecurityHierarchyParents, java.lang.String>
{
	@Override
	public EntityManager getEntityManager()
	{
		return com.guicedee.client.IGuiceContext.get(EntityManager.class, ActivityMasterDB.class);
	}
	
	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
