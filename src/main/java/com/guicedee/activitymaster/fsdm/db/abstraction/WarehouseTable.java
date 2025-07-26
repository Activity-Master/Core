package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.UUID;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@MappedSuperclass
public abstract class WarehouseTable<
		J extends WarehouseTable<J, Q, I,S>,
		Q extends QueryBuilderTable<Q, J, I,?>,
		I extends java.util.UUID,
		S extends WarehouseSecurityTable<S,?,I>
		>
		extends WarehouseCoreTable<J, Q, I,S>
		implements IWarehouseTable<J, Q, I,S>
{
	@Serial
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Column(nullable = true,
	        name = "OriginalSourceSystemUniqueID")
	private UUID originalSourceSystemUniqueID = UUID.fromString("00000000-0000-0000-0000-000000000000");

	/*@JoinColumn(name = "OriginalSourceSystemID",
	            referencedColumnName = "SystemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)*/
	@Column(name = "OriginalSourceSystemID",nullable = false)
	private UUID originalSourceSystemID = UUID.fromString("00000000-0000-0000-0000-000000000000");

	@JoinColumn(name = "EnterpriseID",
	            referencedColumnName = "EnterpriseID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)

	private Enterprise enterpriseID;

	public WarehouseTable()
	{

	}

	@NotNull
	public Class<S> findPersistentSecurityClass()
	{
		return (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
	}

	@NotNull
	@SuppressWarnings("unchecked")
	protected J configureDefaultsSystemValues(Systems requestingSystem)
	{
		setOriginalSourceSystemID(requestingSystem);
		return (J) this;
	}

	public @NotNull UUID getOriginalSourceSystemUniqueID()
	{
		return this.originalSourceSystemUniqueID;
	}

	public J setOriginalSourceSystemUniqueID(@NotNull UUID originalSourceSystemUniqueID)
	{
		if(originalSourceSystemUniqueID == null)
		{
			this.originalSourceSystemUniqueID = UUID.fromString("00000000-0000-0000-0000-000000000000");
		}
		else
			this.originalSourceSystemUniqueID = originalSourceSystemUniqueID;
		return (J) this;
	}

	public UUID getOriginalSourceSystemID()
	{
		return this.originalSourceSystemID;
	}

	@SuppressWarnings("unchecked")
	public J setOriginalSourceSystemID(UUID originalSourceSystemID)
	{
		this.originalSourceSystemID = originalSourceSystemID;
		return (J)this;
	}

	public J setOriginalSourceSystemID(ISystems<?, ?> originalSourceSystemID)
	{
		if(originalSourceSystemID == null)
		{
			this.originalSourceSystemID = UUID.fromString("00000000-0000-0000-0000-000000000000");
		}else
			this.originalSourceSystemID = originalSourceSystemID.getId();
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

}
