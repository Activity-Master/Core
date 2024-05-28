package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.capabilities.contains.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.systems.ActiveFlagSystem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.lang.reflect.ParameterizedType;

import static com.guicedee.client.IGuiceContext.*;

/**
 * @param <J>
 * @author Marc Magon
 * @version 1.0
 * @since 06 Dec 2016
 */
@MappedSuperclass

public abstract class WarehouseSCDTable<
		J extends WarehouseSCDTable<J, Q, I,S>,
		Q extends QueryBuilderSCD<Q, J, I,?>,
		I extends java.lang.String,
		S extends WarehouseSecurityTable<S,?,I>
		>
		extends WarehouseTable<J, Q, I,S>
		implements IWarehouseTable<J, Q, I,S>,
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
	
	@JoinColumn(name = "SystemID",
	            referencedColumnName = "SystemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Systems systemID;
	
	
	public WarehouseSCDTable()
	{
	}
	
	@NotNull
	public Class<S> findSecurityClass()
	{
		return (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
	}
	
	
	protected J configureDefaultsSystemValues(Systems requestingSystem)
	{
		setSystemID(requestingSystem);
		setActiveFlagID(com.guicedee.client.IGuiceContext.get(IActiveFlagService.class)
		                            .getActiveFlag(requestingSystem.getEnterpriseID()));
		setEnterpriseID(requestingSystem.getEnterpriseID());
		return (J) this;
	}
	
	
	@SuppressWarnings("unchecked")
	public J remove()
	{
		setActiveFlagID(com.guicedee.client.IGuiceContext.get(IActiveFlagService.class)
		                                                 .getDeletedFlag(getEnterpriseID(), get(ActiveFlagSystem.class).getSystemToken(getEnterpriseID())));
		setEffectiveToDate(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		update();
		return (J) this;
	}
	
	
	@SuppressWarnings("unchecked")
	public J archive()
	{
		setActiveFlagID(com.guicedee.client.IGuiceContext.get(IActiveFlagService.class)
		                                                 .getArchivedFlag(getEnterpriseID(), get(ActiveFlagSystem.class)
				                                                 .getSystemToken(getEnterpriseID())));
		update();
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
