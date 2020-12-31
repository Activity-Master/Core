package com.guicedee.activitymaster.core.db.abstraction.assists;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;

import jakarta.persistence.MappedSuperclass;

import java.io.Serial;
import java.io.Serializable;

/**
 * @param <S>
 * @param <J>
 *
 * @author Marc Magon
 * @version 1.0
 * @since 06 Dec 2016
 */
@MappedSuperclass()
public abstract class WarehouseSCDNameDescriptionTable<J extends WarehouseSCDNameDescriptionTable<J, Q, I, S>,
		                                                      Q extends QueryBuilderSCDNameDescription<Q, J, I, S>,
		                                                      I extends Serializable,
		                                                      S extends WarehouseSecurityTable>
		extends WarehouseTable<J, Q, I, S>
{
	@Serial
	private static final long serialVersionUID = 1L;

	public WarehouseSCDNameDescriptionTable()
	{
	}

}
