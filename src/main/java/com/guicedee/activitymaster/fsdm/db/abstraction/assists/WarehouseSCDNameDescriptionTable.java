package com.guicedee.activitymaster.fsdm.db.abstraction.assists;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseNameAndDescriptionTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import jakarta.persistence.MappedSuperclass;

import java.io.Serial;

/**
 * @param <J>
 * @author Marc Magon
 * @version 1.0
 * @since 06 Dec 2016
 */
@MappedSuperclass()
public abstract class WarehouseSCDNameDescriptionTable<J extends WarehouseSCDNameDescriptionTable<J, Q, I>,
		Q extends QueryBuilderSCDNameDescription<Q, J, I>,
		I extends java.lang.String>
		extends WarehouseTable<J, Q, I>
		implements IWarehouseNameAndDescriptionTable<J, Q, I>
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	public WarehouseSCDNameDescriptionTable()
	{
	}
	
}
