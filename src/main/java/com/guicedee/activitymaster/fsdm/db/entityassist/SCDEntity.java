package com.guicedee.activitymaster.fsdm.db.entityassist;

import com.entityassist.BaseEntity;
import com.entityassist.RootEntity;
import com.guicedee.activitymaster.fsdm.client.services.builders.ISCDEntity;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@MappedSuperclass()
@JsonAutoDetect(fieldVisibility = ANY,
		getterVisibility = NONE,
		setterVisibility = NONE)
@JsonInclude(NON_NULL)
public abstract class SCDEntity<J extends SCDEntity<J, Q, I>, Q extends QueryBuilderSCD<Q, J, I>, I extends Serializable>
		extends BaseEntity<J, Q, I>
	implements ISCDEntity<J,Q,I>
{
	/**
	 * A timestamp designating the end of time or not applied
	 */
	public static final LocalDateTime EndOfTime = LocalDateTime.of(2999, 12, 31, 23, 59, 59, 999);

	/**
	 * A date to designate when this record is effective from
	 */
	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@Column(nullable = false,
			name = "EffectiveFromDate")
	private OffsetDateTime effectiveFromDate;
	/**
	 * A date to designate when this record is effective to
	 */
	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@Column(nullable = false,
			name = "EffectiveToDate")
	private OffsetDateTime effectiveToDate;
	/**
	 * A date to mark when a warehouse can fetch the given record
	 */
	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@Column(nullable = false,
			name = "WarehouseCreatedTimestamp")
	private OffsetDateTime warehouseCreatedTimestamp;
	/**
	 * A date to mark when a warehouse can fetch the given record
	 */
	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@Column(nullable = false,
			name = "WarehouseCreatedDate")
	@Access(AccessType.FIELD)

	private LocalDate warehouseCreatedDate;
	/**
	 * A marker for the warehouse to identify when last this field was updated
	 */
	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@Column(nullable = false,
			name = "WarehouseLastUpdatedTimestamp")
	private OffsetDateTime warehouseLastUpdatedTimestamp;

	public SCDEntity()
	{
		effectiveToDate = EndOfTime.atOffset(ZoneOffset.UTC);
		effectiveFromDate = IQueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow());
		warehouseCreatedTimestamp = IQueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow());
		warehouseCreatedDate = com.entityassist.RootEntity.getNow().toLocalDate();
		warehouseLastUpdatedTimestamp = IQueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow());
	}

	public LocalDate getWarehouseCreatedDate()
	{
		if (warehouseCreatedDate == null)
		{
			warehouseCreatedDate = warehouseCreatedTimestamp.toLocalDate();
		}
		return warehouseCreatedDate;
	}

	/**
	 * Returns the effective from date for the given setting
	 *
	 * @return
	 */
	@SuppressWarnings("all")
	public OffsetDateTime getEffectiveFromDate()
	{
		return effectiveFromDate;
	}

	/**
	 * Sets the effective from date value for default value
	 *
	 * @param effectiveFromDate
	 *
	 * @return
	 */
	@NotNull
	@SuppressWarnings("all")
	public J setEffectiveFromDate(@NotNull OffsetDateTime effectiveFromDate)
	{
		this.effectiveFromDate = effectiveFromDate;
		return (J) this;
	}

	/**
	 * Returns the effice to date setting for active flag calculation
	 *
	 * @return
	 */
	@SuppressWarnings("all")
	public OffsetDateTime getEffectiveToDate()
	{
		return effectiveToDate;
	}

	/**
	 * Sets the effective to date column value for active flag determination
	 *
	 * @param effectiveToDate
	 *
	 * @return This
	 */
	@NotNull
	@SuppressWarnings("all")
	public J setEffectiveToDate(@NotNull OffsetDateTime effectiveToDate)
	{
		this.effectiveToDate = effectiveToDate;
		return (J) this;
	}

	/**
	 * Returns the warehouse created timestamp column value
	 *
	 * @return The current time
	 */
	public OffsetDateTime getWarehouseCreatedTimestamp()
	{
		return warehouseCreatedTimestamp;
	}

	/**
	 * Sets the warehouse created timestamp
	 *
	 * @param warehouseCreatedTimestamp
	 * 		The time to apply
	 *
	 * @return This
	 */
	@NotNull
	@SuppressWarnings("all")
	public J setWarehouseCreatedTimestamp(@NotNull OffsetDateTime warehouseCreatedTimestamp)
	{
		this.warehouseCreatedTimestamp = warehouseCreatedTimestamp;
		return (J) this;
	}

	/**
	 * Returns the last time the warehouse timestamp column was updated
	 *
	 * @return The time
	 */
	public OffsetDateTime getWarehouseLastUpdatedTimestamp()
	{
		return warehouseLastUpdatedTimestamp;
	}

	/**
	 * Sets the last time the warehouse timestamp column was updated
	 *
	 * @param warehouseLastUpdatedTimestamp
	 *
	 * @return This
	 */
	@NotNull
	@SuppressWarnings("all")
	public J setWarehouseLastUpdatedTimestamp(@NotNull OffsetDateTime warehouseLastUpdatedTimestamp)
	{
		this.warehouseLastUpdatedTimestamp = warehouseLastUpdatedTimestamp;
		return (J) this;
	}
}
