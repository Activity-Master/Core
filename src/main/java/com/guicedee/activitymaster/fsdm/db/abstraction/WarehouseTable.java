package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.capabilities.contains.IContainsActiveFlags;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.systems.ActiveFlagSystem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;

import static com.guicedee.client.IGuiceContext.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@MappedSuperclass
public abstract class WarehouseTable<J extends WarehouseTable<J, Q, I>,
		Q extends QueryBuilderTable<Q, J, I>,
		I extends java.lang.String>
		extends WarehouseSCDTable<J, Q, I>
		implements IContainsActiveFlags<J>,
		           IWarehouseTable<J, Q, I>
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
	        name = "OriginalSourceSystemUniqueID")
	private String originalSourceSystemUniqueID = "";
	
	@JoinColumn(name = "OriginalSourceSystemID",
	            referencedColumnName = "SystemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Systems originalSourceSystemID;
	
	public WarehouseTable()
	{
	
	}
	
	@Override
	@NotNull
	@SuppressWarnings("unchecked")
	protected J configureDefaultsSystemValues(Systems requestingSystem)
	{
		super.configureDefaultsSystemValues(requestingSystem);
		setOriginalSourceSystemID(requestingSystem);
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
	
	public @NotNull String getOriginalSourceSystemUniqueID()
	{
		return this.originalSourceSystemUniqueID;
	}
	
	public J setOriginalSourceSystemUniqueID(@NotNull String originalSourceSystemUniqueID)
	{
		this.originalSourceSystemUniqueID = originalSourceSystemUniqueID;
		return (J) this;
	}
	
	public ISystems<?, ?> getOriginalSourceSystemID()
	{
		return this.originalSourceSystemID;
	}
	
	@SuppressWarnings("unchecked")
	public J setOriginalSourceSystemID(ISystems<?, ?> originalSourceSystemID)
	{
		this.originalSourceSystemID = (Systems) originalSourceSystemID;
		return (J) this;
	}
}
