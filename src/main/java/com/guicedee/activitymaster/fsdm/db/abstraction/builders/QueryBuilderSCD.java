package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.entityassist.enumerations.OrderByType;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderFlags;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.entityassist.RootEntity.getNow;
import static java.time.ZoneOffset.UTC;

@SuppressWarnings("unused")
public abstract class QueryBuilderSCD<
		J extends QueryBuilderSCD<J, E, I,QS>,
		E extends WarehouseSCDTable<E, J, I,?>,
		I extends java.util.UUID,
		QS extends QueryBuilderSecurities<QS,?,I>>
		extends QueryBuilderTable<J, E, I,QS>
		implements IQueryBuilderEnterprise<J, E, I>,
		           IQueryBuilderFlags<J, E, I>
{

	/**
	 * Sets the SCD values to new ones if not present
	 *
	 * @param entity The entity
	 *
	 * @return true if must create
	 */
	@Override
	public boolean onCreate(E entity)
	{
		if (entity.getWarehouseCreatedTimestamp() == null)
		{
			entity.setWarehouseCreatedTimestamp(convertToUTCDateTime(getNow()));
		}
		if (entity.getWarehouseLastUpdatedTimestamp() == null)
		{
			entity.setWarehouseLastUpdatedTimestamp(convertToUTCDateTime(getNow()));
		}
		if (entity.getEffectiveFromDate() == null)
		{
			entity.setEffectiveFromDate(convertToUTCDateTime(getNow()));
		}
		if (entity.getEffectiveToDate() == null)
		{
			entity.setEffectiveToDate(EndOfTime.atOffset(UTC));
		}
		return true;
	}

	/**
	 * Updates the on update to specify the new warehouse last updated
	 *
	 * @param entity The entity
	 *
	 * @return boolean
	 */
	@Override
	public boolean onUpdate(E entity)
	{
		entity.setWarehouseLastUpdatedTimestamp(convertToUTCDateTime(getNow()));
		return true;
	}

	public static OffsetDateTime convertToUTCDateTime(LocalDateTime ldt) {
		if (ldt == null)
		{
			return null;
		}
		ZonedDateTime zonedDateTime = ldt.atZone(ZoneId.systemDefault());
		ZonedDateTime utcZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
		OffsetDateTime offsetDateTime = utcZonedDateTime.toOffsetDateTime();
		return offsetDateTime;
	}

	public static LocalDateTime convertToLocalDateTime(OffsetDateTime ldt) {
		if (ldt == null)
		{
			return null;
		}
		ZonedDateTime zonedDateTime = ldt.atZoneSameInstant(ZoneId.systemDefault());
		return zonedDateTime.toLocalDateTime();
	}

	public static LocalDateTime convertToLocalDateTime(OffsetDateTime ldt, ZoneId zone) {
		if (ldt == null)
		{
			return null;
		}
		ZonedDateTime zonedDateTime = ldt.atZoneSameInstant(zone);
		return zonedDateTime.toLocalDateTime();
	}

	public static LocalDateTime convertToLocalDateTime(OffsetDateTime ldt, String timezone) {
		if (ldt == null)
		{
			return null;
		}
		ZonedDateTime zonedDateTime = ldt.atZoneSameInstant(ZoneId.of(timezone));
		return zonedDateTime.toLocalDateTime();
	}

	public J latestFirst()
	{
		orderBy(getAttribute(WAREHOUSE_UPDATED_DATE_COLUMN_NAME), OrderByType.DESC);
		return (J)this;
	}
}