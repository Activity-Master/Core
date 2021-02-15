package com.guicedee.activitymaster.core.db.abstraction;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.IActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @param <S>
 * @param <J>
 * @param <S>
 *
 * @author Marc Magon
 * @version 1.0
 * @since 06 Dec 2016
 */
@MappedSuperclass

public abstract class WarehouseSCDTable<J extends WarehouseSCDTable<J, Q, I, S>,
		Q extends QueryBuilderSCD<Q, J, I, S>,
		I extends Serializable, S extends WarehouseSecurityTable>
		extends WarehouseCoreTable<J, Q, I, S>
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
	
	
	public WarehouseSCDTable(IEnterprise<?> enterprise)
	{
		setEnterpriseID((Enterprise) enterprise);
	}
	
	public WarehouseSCDTable()
	{
	}
	
	protected J configureDefaultsSystemValues(Systems requestingSystem)
	{
		setSystemID(requestingSystem);
		setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                         .getActiveFlag(requestingSystem.getEnterpriseID()));
		setEnterpriseID(requestingSystem.getEnterpriseID());
		return (J) this;
	}
	
	public ActiveFlag getActiveFlagID()
	{
		return this.activeFlagID;
	}
	
	public Enterprise getEnterpriseID()
	{
		return this.enterpriseID;
	}
	
	public Systems getSystemID()
	{
		return this.systemID;
	}
	
	public J setActiveFlagID(IActiveFlag<?> activeFlagID)
	{
		this.activeFlagID = (ActiveFlag) activeFlagID;
		return (J) this;
	}
	
	public J setActiveFlagID(ActiveFlag activeFlagID)
	{
		this.activeFlagID = activeFlagID;
		return (J) this;
	}
	
	public J setEnterpriseID(Enterprise enterpriseID)
	{
		this.enterpriseID = enterpriseID;
		return (J) this;
	}
	
	public J setEnterpriseID(IEnterprise<?> enterpriseID)
	{
		this.enterpriseID = (Enterprise) enterpriseID;
		return (J) this;
	}
	
	public J setSystemID(Systems systemID)
	{
		this.systemID = systemID;
		return (J) this;
	}
}
