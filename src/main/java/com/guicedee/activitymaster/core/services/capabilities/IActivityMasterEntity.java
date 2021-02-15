package com.guicedee.activitymaster.core.services.capabilities;

import com.guicedee.activitymaster.core.services.dto.ISystems;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IActivityMasterEntity<J extends IActivityMasterEntity<J>>
{
	UUID getId();

	J setId(UUID id);

	void createDefaultSecurity(ISystems<?> system, UUID... identity);

	J archive();

	J remove();
	
	default @NotNull boolean isFake()
	{
		return getId() == null;
	}
	
	/**
	 * Returns the effective from date for the given setting
	 *
	 * @return
	 */
	
	LocalDateTime getEffectiveFromDate();

	/**
	 * Sets the effective from date value for default value
	 *
	 * @param effectiveFromDate
	 *
	 * @return
	 */
	@NotNull
	
	J setEffectiveFromDate(@NotNull LocalDateTime effectiveFromDate);

	/**
	 * Returns the effice to date setting for active flag calculation
	 *
	 * @return
	 */
	
	LocalDateTime getEffectiveToDate();

	/**
	 * Sets the effective to date column value for active flag determination
	 *
	 * @param effectiveToDate
	 *
	 * @return This
	 */
	@NotNull
	
	J setEffectiveToDate(@NotNull LocalDateTime effectiveToDate);

	/**
	 * Returns the warehouse created timestamp column value
	 *
	 * @return The current time
	 */
	LocalDateTime getWarehouseCreatedTimestamp();

	/**
	 * Sets the warehouse created timestamp
	 *
	 * @param warehouseCreatedTimestamp
	 * 		The time to apply
	 *
	 * @return This
	 */
	@NotNull
	
	J setWarehouseCreatedTimestamp(@NotNull LocalDateTime warehouseCreatedTimestamp);

	/**
	 * Returns the last time the warehouse timestamp column was updated
	 *
	 * @return The time
	 */
	LocalDateTime getWarehouseLastUpdatedTimestamp();

	/**
	 * Sets the last time the warehouse timestamp column was updated
	 *
	 * @param warehouseLastUpdatedTimestamp
	 *
	 * @return This
	 */
	@NotNull
	
	J setWarehouseLastUpdatedTimestamp(@NotNull LocalDateTime warehouseLastUpdatedTimestamp);
}
