package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.ISecurityEnabledQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.guicedinjection.GuiceContext;

import java.io.Serializable;

import static com.guicedee.guicedinjection.json.StaticStrings.*;

public abstract class QueryBuilderTable<J extends QueryBuilderTable<J, E, I, S>,
		E extends WarehouseTable<E, J, I, S>,
		I extends Serializable,
		S extends WarehouseSecurityTable>
		extends QueryBuilderSCD<J, E, I, S>
		implements ISecurityEnabledQueryBuilder<J, E, I, S>
{
	@Override
	public boolean onCreate(E entity)
	{
		if (entity.getOriginalSourceSystemUniqueID() == null)
		{ entity.setOriginalSourceSystemUniqueID(STRING_EMPTY); }
		return super.onCreate(entity);
	}
}
