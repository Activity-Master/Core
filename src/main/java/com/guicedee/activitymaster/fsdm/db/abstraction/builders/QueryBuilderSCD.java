package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.entityassist.enumerations.OrderByType;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderFlags;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;

import java.time.LocalDateTime;
import java.util.List;
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
		//must delegate up the chain - the identifier, source system and partition defaults
		//are applied by QueryBuilderTable / QueryBuilderDefault
		return super.onCreate(entity);
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

	/**
	 * Dynamically applies a transport-neutral {@link WarehouseQuerySpec} to this builder.
	 *
	 * <p>This is the single reusable entry point used by the GraphQL (and any other) layer to
	 * build an EntityAssist query from external input. It applies, in order:</p>
	 * <ol>
	 *     <li>enterprise scoping via {@link IQueryBuilderEnterprise#withEnterprise}</li>
	 *     <li>active-flag range via {@link IQueryBuilderFlags#inActiveRange()} (when requested)</li>
	 *     <li>effective date range via {@code inDateRange()} (when requested)</li>
	 *     <li>each dynamic {@link WarehouseQueryFilter} via dot-notation {@code where(path, operand, value)}</li>
	 *     <li>an optional order-by attribute</li>
	 *     <li>pagination via {@code setFirstResults} / {@code setMaxResults}</li>
	 * </ol>
	 *
	 * <p>The final built query is then executed by the caller (e.g. {@code getAll()} / {@code getCount()}).</p>
	 *
	 * @param spec the query specification, may be {@code null}
	 * @return this builder for chaining
	 */
	@SuppressWarnings("unchecked")
	public J applyQuerySpec(WarehouseQuerySpec spec)
	{
		if (spec == null)
		{
			return (J) this;
		}
		if (spec.getEnterprise() != null)
		{
			withEnterprise(spec.getEnterprise());
		}
		if (spec.isActiveOnly())
		{
			inActiveRange();
		}
		if (spec.isInDateRange())
		{
			inDateRange();
		}
		List<WarehouseQueryFilter> filters = spec.getFilters();
		if (filters != null)
		{
			for (WarehouseQueryFilter filter : filters)
			{
				if (filter == null || filter.getPath() == null)
				{
					continue;
				}
				if (filter.getValues() != null && !filter.getValues().isEmpty())
				{
					where(filter.getPath(), filter.getOperand(), filter.getValues());
				}
				else
				{
					where(filter.getPath(), filter.getOperand(), filter.getValue());
				}
			}
		}
		if (spec.getOrderBy() != null && !spec.getOrderBy().isBlank())
		{
			orderBy(getAttribute(spec.getOrderBy()), spec.isDescending() ? OrderByType.DESC : OrderByType.ASC);
		}
		if (spec.getFirst() != null)
		{
			setFirstResults(spec.getFirst());
		}
		if (spec.getMax() != null)
		{
			setMaxResults(spec.getMax());
		}
		return (J) this;
	}
}