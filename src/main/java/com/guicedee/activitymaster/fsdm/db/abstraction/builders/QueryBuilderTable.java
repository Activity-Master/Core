package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseTable;

import static com.guicedee.guicedinjection.json.StaticStrings.*;

public abstract class QueryBuilderTable<J extends QueryBuilderTable<J, E, I>,
		E extends WarehouseTable<E, J, I>,
		I extends java.util.UUID>
		extends QueryBuilderSCD<J, E, I>
		implements IQueryBuilderDefault<J, E, I>,
		           IQueryBuilderEnterprise<J, E, I>,
		           IQueryBuilderFlags<J, E, I>
{
	@Override
	public boolean onCreate(E entity)
	{
		if (entity.getOriginalSourceSystemUniqueID() == null)
		{
			entity.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		}
		/*if (entity.getOriginalSourceSystemID() == null)
		{
			ISystems<?, ?> system = getISystem(ActivityMasterSystemName);
			entity.setSystemID(system);
			if (entity.getEnterpriseID() == null)
			{
				entity.setEnterpriseID(system.getEnterpriseID());
			}
		}
		if (entity.getSystemID() == null)
		{
			ISystems<?, ?> system = getISystem(ActivityMasterSystemName);
			entity.setSystemID(system);
		}*/
		return super.onCreate(entity);
	}
}
