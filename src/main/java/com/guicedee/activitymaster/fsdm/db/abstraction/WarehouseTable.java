package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
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
	private UUID originalSourceSystemUniqueID;

	/*@JoinColumn(name = "OriginalSourceSystemID",
	            referencedColumnName = "SystemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)*/
	@Column(name = "OriginalSourceSystemID",nullable = false)
	private UUID originalSourceSystemID;

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
		return setOriginalSourceSystemID(requestingSystem);
	}

	/**
	 * Gets the original source system unique ID.
	 * This method is non-reactive as it simply returns a property value.
	 * 
	 * @return The original source system unique ID
	 */
	public @NotNull UUID getOriginalSourceSystemUniqueID()
	{
		return this.originalSourceSystemUniqueID;
	}

	/**
	 * Sets the original source system unique ID.
	 * This method is non-reactive as it simply sets a property value.
	 * 
	 * @param originalSourceSystemUniqueID The original source system unique ID to set
	 * @return This instance for method chaining
	 */
	@SuppressWarnings("unchecked")
	public J setOriginalSourceSystemUniqueID(@NotNull UUID originalSourceSystemUniqueID)
	{
		this.originalSourceSystemUniqueID = originalSourceSystemUniqueID;
		return (J) this;
	}

	/**
	 * Gets the original source system ID.
	 * This method is non-reactive as it simply returns a property value.
	 * 
	 * @return The original source system ID
	 */
	public UUID getOriginalSourceSystemID()
	{
		return this.originalSourceSystemID;
	}

	/**
	 * Sets the original source system ID.
	 * This method is non-reactive as it simply sets a property value.
	 * 
	 * @param originalSourceSystemID The original source system ID to set
	 * @return This instance for method chaining
	 */
	@SuppressWarnings("unchecked")
	public J setOriginalSourceSystemID(UUID originalSourceSystemID)
	{
		this.originalSourceSystemID = originalSourceSystemID;
		return (J)this;
	}

	/**
	 * Sets the original source system ID using an ISystems object.
	 * This method is non-reactive as it simply sets a property value.
	 * 
	 * @param originalSourceSystemID The original source system to set
	 * @return This instance for method chaining
	 */
	@SuppressWarnings("unchecked")
	public J setOriginalSourceSystemID(ISystems<?, ?> originalSourceSystemID)
	{
		if(originalSourceSystemID == null)
		{
			this.originalSourceSystemUniqueID = UUID.fromString("00000000-0000-0000-0000-000000000000");
		}else
			this.originalSourceSystemID = originalSourceSystemID.getId();
		return (J) this;
	}

	/**
	 * Gets the enterprise ID associated with this entity.
	 * This method is non-reactive as it simply returns a property value.
	 * 
	 * @return The enterprise ID
	 */
	public IEnterprise<?, ?> getEnterpriseID()
	{
		return this.enterpriseID;
	}

	/**
	 * Sets the enterprise ID for this entity.
	 * This method is non-reactive as it simply sets a property value.
	 * 
	 * @param enterpriseID The enterprise ID to set
	 * @return This instance for method chaining
	 */
	@SuppressWarnings("unchecked")
	public J setEnterpriseID(IEnterprise<?, ?> enterpriseID)
	{
		this.enterpriseID = (Enterprise) enterpriseID;
		return (J) this;
	}

}
