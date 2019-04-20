package com.armineasy.activitymaster.activitymaster.db.abstraction.assists;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @param <S>
 * @param <J>
 *
 * @author GedMarc
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
	private static final long serialVersionUID = 1L;

	public WarehouseSCDNameDescriptionTable()
	{
	}

}
