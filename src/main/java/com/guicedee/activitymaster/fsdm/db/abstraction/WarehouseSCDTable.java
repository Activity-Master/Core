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
import lombok.extern.log4j.Log4j2;

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
@Log4j2
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

		// Set the system ID and enterprise ID (these don't require async operations)
		setSystemID(requestingSystem);
		setEnterpriseID(enterprise);

		// For backward compatibility, we need to block here to get the active flag
		// This is not ideal in a reactive context, but necessary to maintain compatibility
		IActiveFlagService<?> activeFlagService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
		try {
			IActiveFlag<?,?> activeFlag = activeFlagService.getActiveFlag(enterprise)
				.await().indefinitely();
			setActiveFlagID(activeFlag);
		} catch (Exception e) {
			// Log the error but continue
			log.error("Error getting active flag: {}", e.getMessage(), e);
		}

		return (J) this;
	}

	/**
	 * Configures default system values for this entity in a reactive way.
	 * This method is reactive as it needs to get the active flag asynchronously.
	 * 
	 * @param requestingSystem The system requesting the configuration
	 * @return A Uni that completes with this instance for method chaining
	 */
	@NotNull
	protected Uni<J> configureDefaultsSystemValuesReactive(Systems requestingSystem)
	{
		// Get the enterprise directly from the requesting system
		IEnterprise<?,?> enterprise = requestingSystem.getEnterpriseID();

		// Get the active flag service and active flag
		IActiveFlagService<?> activeFlagService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);

		// Set the system ID and enterprise ID (these don't require async operations)
		setSystemID(requestingSystem);
		setEnterpriseID(enterprise);

		// Get the active flag asynchronously and set it
		return activeFlagService.getActiveFlag(enterprise)
			.onSubscription().invoke(() -> log.debug("Getting active flag for enterprise: {}", enterprise.getName()))
			.onItem().invoke(activeFlag -> log.debug("Retrieved active flag: {}", activeFlag.getName()))
			.onFailure().invoke(error -> log.error("Error getting active flag: {}", error.getMessage(), error))
			.map(activeFlag -> {
				setActiveFlagID(activeFlag);
				log.debug("Set active flag ID for entity");
				return (J) this;
			});
	}


	/**
	 * Marks the entity as removed by setting its active flag to deleted.
	 * This method performs actions and returns a Uni that completes when the removal is done.
	 * It returns the result of calling update().
	 * 
	 * @return A Uni that completes when the removal is done
	 */
	public Uni<J> remove()
	{
		IEnterprise<?,?> enterprise = getEnterpriseID();
		ActiveFlagSystem activeSystem = get(ActiveFlagSystem.class);
		UUID systemToken = activeSystem.getSystemToken(enterprise);

		log.info("Removing entity with ID: {}", getId());

		IActiveFlagService<?> activeFlagService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
		return activeFlagService
			.getDeletedFlag(enterprise, systemToken)
			.onSubscription().invoke(() -> log.debug("Getting deleted flag for enterprise: {}", enterprise.getName()))
			.onItem().invoke(activeFlag -> log.debug("Retrieved deleted flag: {}", activeFlag.getName()))
			.onFailure().invoke(error -> log.error("Error getting deleted flag: {}", error.getMessage(), error))
			.chain(activeFlag -> {
				log.debug("Setting active flag to deleted for entity: {}", getId());
				setActiveFlagID(activeFlag);
				setEffectiveToDate(convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
				return update()
					.onItem().invoke(entity -> log.info("Successfully marked entity as removed: {}", entity.getId()))
					.onFailure().invoke(error -> log.error("Error updating entity as removed: {}", error.getMessage(), error));
			});
	}

	/**
	 * Archives the entity by setting its active flag to archived.
	 * This method performs actions and returns a Uni that completes when the archiving is done.
	 * It returns the result of calling update().
	 * 
	 * @return A Uni that completes when the archiving is done
	 */
	public Uni<J> archive()
	{
		IEnterprise<?,?> enterprise = getEnterpriseID();
		ActiveFlagSystem activeSystem = get(ActiveFlagSystem.class);
		UUID systemToken = activeSystem.getSystemToken(enterprise);

		log.info("Archiving entity with ID: {}", getId());

		IActiveFlagService<?> activeFlagService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
		return activeFlagService
			.getArchivedFlag(enterprise, systemToken)
			.onSubscription().invoke(() -> log.debug("Getting archived flag for enterprise: {}", enterprise.getName()))
			.onItem().invoke(activeFlag -> log.debug("Retrieved archived flag: {}", activeFlag.getName()))
			.onFailure().invoke(error -> log.error("Error getting archived flag: {}", error.getMessage(), error))
			.chain(activeFlag -> {
				log.debug("Setting active flag to archived for entity: {}", getId());
				setActiveFlagID(activeFlag);
				return update()
					.onItem().invoke(entity -> log.info("Successfully archived entity: {}", entity.getId()))
					.onFailure().invoke(error -> log.error("Error archiving entity: {}", error.getMessage(), error));
			});
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
