package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.capabilities.contains.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.systems.ActiveFlagSystem;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD.convertToUTCDateTime;
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
		I extends java.util.UUID,
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
	public Class<S> findPersistentSecurityClass()
	{
		return (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
	}


	/**
	 * Configures default system values for this entity.
	 * This method is non-reactive as it simply sets property values.
	 * 
	 * @param requestingSystem The system requesting the configuration
	 * @return This instance for method chaining
	 */
	@NotNull
	@SuppressWarnings("unchecked")
	protected J configureDefaultsSystemValues(Systems requestingSystem)
	{
		// Get the enterprise directly from the requesting system
		IEnterprise<?,?> enterprise = requestingSystem.getEnterpriseID();

		// Get the active flag service and active flag
		IActiveFlagService<?> activeFlagService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
		IActiveFlag<?,?> activeFlag = activeFlagService.getActiveFlag(enterprise);

		// Set the system ID, active flag ID, and enterprise ID
		setSystemID(requestingSystem);
		setActiveFlagID(activeFlag);
		setEnterpriseID(enterprise);

		return (J) this;
	}


	/**
	 * Marks the entity as removed by setting its active flag to deleted.
	 * This method performs actions and returns a Uni that completes when the removal is done.
	 * It returns the result of calling update().
	 * 
	 * @return A Uni that completes when the removal is done
	 */
	@SuppressWarnings("unchecked")
	public Uni<J> remove()
	{
		IEnterprise<?,?> enterprise = getEnterpriseID();
		ActiveFlagSystem activeSystem = get(ActiveFlagSystem.class);
		UUID systemToken = activeSystem.getSystemToken(enterprise);

		setActiveFlagID(com.guicedee.client.IGuiceContext.get(IActiveFlagService.class)
			.getDeletedFlag(enterprise, systemToken));
		setEffectiveToDate(convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		return update();
	}

	/**
	 * Archives the entity by setting its active flag to archived.
	 * This method is non-reactive as it simply sets property values and calls update().
	 * 
	 * @return This instance for method chaining
	 */
	@SuppressWarnings("unchecked")
	public J archive()
	{
		IEnterprise<?,?> enterprise = getEnterpriseID();
		ActiveFlagSystem activeSystem = get(ActiveFlagSystem.class);
		UUID systemToken = activeSystem.getSystemToken(enterprise);

		setActiveFlagID(com.guicedee.client.IGuiceContext.get(IActiveFlagService.class)
			.getArchivedFlag(enterprise, systemToken));
		update();

		return (J) this;
	}

	/**
	 * Gets the active flag associated with this entity.
	 * This method is non-reactive as it simply returns a property value.
	 * 
	 * @return The active flag
	 */
	public IActiveFlag<?,?> getActiveFlagID()
	{
		return this.activeFlagID;
	}

	/**
	 * Sets the active flag for this entity.
	 * This method is non-reactive as it simply sets a property value.
	 * 
	 * @param activeFlagID The active flag to set
	 * @return This instance for method chaining
	 */
	@SuppressWarnings("unchecked")
	public J setActiveFlagID(IActiveFlag<?, ?> activeFlagID)
	{
		this.activeFlagID = (ActiveFlag) activeFlagID;
		return (J) this;
	}

	/**
	 * Gets the system ID associated with this entity.
	 * This method is non-reactive as it simply returns a property value.
	 * 
	 * @return The system ID
	 */
	public ISystems<?,?> getSystemID()
	{
		return this.systemID;
	}

	/**
	 * Sets the system ID for this entity.
	 * This method is non-reactive as it simply sets a property value.
	 * 
	 * @param systemID The system ID to set
	 * @return This instance for method chaining
	 */
	@SuppressWarnings("unchecked")
    public J setSystemID(ISystems<?, ?> systemID)
	{
		this.systemID = (Systems) systemID;
		return (J) this;
	}

}
