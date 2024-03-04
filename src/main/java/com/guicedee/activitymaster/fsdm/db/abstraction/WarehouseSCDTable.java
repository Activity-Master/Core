package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.capabilities.contains.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import jakarta.persistence.*;

import java.io.Serial;

/**
 * @param <J>
 * @author Marc Magon
 * @version 1.0
 * @since 06 Dec 2016
 */
@MappedSuperclass

public abstract class WarehouseSCDTable<J extends WarehouseSCDTable<J, Q, I>,
		Q extends QueryBuilderSCD<Q, J, I>,
		I extends java.lang.String>
		extends WarehouseCoreTable<J, Q, I>
		implements IWarehouseTable<J, Q, I>,
		           IContainsActiveFlags<J>,
		           IContainsEnterprise<J>,
		           IContainsSystem<J>
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@JoinColumn(name = "ActiveFlagID",
	            referencedColumnName = "ActiveFlagID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ActiveFlag activeFlagID;
	
	@JoinColumn(name = "EnterpriseID",
	            referencedColumnName = "EnterpriseID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Enterprise enterpriseID;
	
	@JoinColumn(name = "SystemID",
	            referencedColumnName = "SystemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Systems systemID;
	
	
	public WarehouseSCDTable()
	{
	}
	
	protected J configureDefaultsSystemValues(Systems requestingSystem)
	{
		setSystemID(requestingSystem);
		setActiveFlagID(com.guicedee.client.IGuiceContext.get(IActiveFlagService.class)
		                            .getActiveFlag(requestingSystem.getEnterpriseID()));
		setEnterpriseID(requestingSystem.getEnterpriseID());
		return (J) this;
	}
	
	public ActiveFlag getActiveFlagID()
	{
		return this.activeFlagID;
	}
	
	public J setActiveFlagID(IActiveFlag<?, ?> activeFlagID)
	{
		this.activeFlagID = (ActiveFlag) activeFlagID;
		return (J) this;
	}
	
	public Enterprise getEnterpriseID()
	{
		return this.enterpriseID;
	}
	
	public J setEnterpriseID(IEnterprise<?, ?> enterpriseID)
	{
		this.enterpriseID = (Enterprise) enterpriseID;
		return (J) this;
	}
	
	public Systems getSystemID()
	{
		return this.systemID;
	}
	
	public J setSystemID(ISystems<?, ?> systemID)
	{
		this.systemID = (Systems) systemID;
		return (J) this;
	}
	
}
