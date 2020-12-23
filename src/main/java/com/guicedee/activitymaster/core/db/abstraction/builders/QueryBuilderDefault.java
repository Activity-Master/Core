package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.entityassist.querybuilder.QueryBuilderSCD;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IQueryBuilderDefault;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.ISecurityEnabledQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.persistence.EntityManager;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public abstract class QueryBuilderDefault<J extends QueryBuilderDefault<J, E, I>,
		                                         E extends WarehouseBaseTable<E, J, I>,
		                                         I extends Serializable>
		extends QueryBuilderSCD<J, E, I>
		implements IQueryBuilderDefault<J, E, I>
{
	public QueryBuilderDefault()
	{
		setRunDetached(true);
		setReturnFirst(true);
		setDetach(true);
	}

	@Override
	public boolean onCreate(E entity) {
		if (entity.getId() == null) {
			//noinspection unchecked
			entity.setId((I) UUID.randomUUID());
		}
		return super.onCreate(entity);
	}

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
