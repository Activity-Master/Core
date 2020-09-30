package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.ISecurityEnabledQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.ActiveFlagService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.guicedinjection.GuiceContext;

import java.io.Serializable;

public abstract class QueryBuilderSCD<J extends QueryBuilderSCD<J, E, I, S>,
		E extends WarehouseSCDTable<E, J, I, S>,
		I extends Serializable,
		S extends WarehouseSecurityTable>
		extends QueryBuilderCore<J, E, I, S>
		implements ISecurityEnabledQueryBuilder<J, E, I, S>
{
	@Override
	public boolean onCreate(E entity)
	{
		if (entity.getSystemID() != null && entity.getEnterpriseID() == null)
		{
			entity.setEnterpriseID(entity.getSystemID()
			                             .getEnterpriseID());
		}
		if (entity.getEnterpriseID() != null)
		{
			IEnterprise<?> enterprise = entity.getEnterpriseID();
			if (entity.getSystemID() == null)
			{
				entity.setSystemID((Systems) GuiceContext.get(SystemsService.class)
				                                         .getActivityMaster(enterprise));
			}
			if (entity.getActiveFlagID() == null)
			{
				entity.setActiveFlagID((ActiveFlag) GuiceContext.get(ActiveFlagService.class)
				                                                .getActiveFlag(enterprise));
			}
		}
		return super.onCreate(entity);
	}
	
	
}
