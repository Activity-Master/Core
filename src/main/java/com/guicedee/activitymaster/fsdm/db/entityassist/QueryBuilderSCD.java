package com.guicedee.activitymaster.fsdm.db.entityassist;

import com.entityassist.enumerations.Operand;
import com.entityassist.enumerations.OrderByType;
import com.entityassist.querybuilder.QueryBuilder;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.entityassist.RootEntity.getNow;
import static java.time.ZoneOffset.UTC;

@SuppressWarnings("unused")
public abstract class QueryBuilderSCD<J extends QueryBuilderSCD<J, E, I>,
                                             E extends SCDEntity<E,J,I>,
                                             I extends UUID>
        extends QueryBuilder<J, E, I>
        implements IQueryBuilderSCD<J, E, I>
{
    /**
     * The effective to date column name
     */
    @SuppressWarnings("WeakerAccess")
    public static final String EFFECTIVE_TO_DATE_COLUMN_NAME = "effectiveToDate" ;
    /**
     * The effective from date column name
     */
    @SuppressWarnings("WeakerAccess")
    public static final String EFFECTIVE_FROM_DATE_COLUMN_NAME = "effectiveFromDate" ;

    /**
     * The effective from date column name
     */
    @SuppressWarnings("WeakerAccess")
    public static final String WAREHOUSE_CREATED_DATE_COLUMN_NAME = "warehouseCreatedTimestamp" ;
    /**
     * The effective from date column name
     */
    @SuppressWarnings("WeakerAccess")
    public static final String WAREHOUSE_UPDATED_DATE_COLUMN_NAME = "warehouseLastUpdatedTimestamp" ;

    /**
     * Where effective from date is greater than today
     *
     * @return This
     */
    @Override
    @NotNull
    public J inDateRange()
    {
        return inDateRange(getNow());
    }


    /**
     * Returns the effective from and to date to be applied
     * <p>
     * Usually getDate()
     *
     * @param betweenThisDate The date
     * @return This
     */
    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public J inDateRange(LocalDateTime betweenThisDate)
    {
        where(getAttribute(EFFECTIVE_FROM_DATE_COLUMN_NAME), Operand.LessThanEqualTo, IQueryBuilderSCD.convertToUTCDateTime(betweenThisDate));
        where(getAttribute(EFFECTIVE_TO_DATE_COLUMN_NAME), Operand.GreaterThanEqualTo, IQueryBuilderSCD.convertToUTCDateTime(betweenThisDate));
        return (J) this;
    }

    /**
     * Returns the effective from and to date to be applied when only the effective date is taken into consideration
     *
     * @param effectiveToDate The date
     * @return This
     */
    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public J inDateRange(LocalDateTime effectiveToDate, boolean toDate)
    {
        where(getAttribute(EFFECTIVE_TO_DATE_COLUMN_NAME), Operand.LessThanEqualTo, IQueryBuilderSCD.convertToUTCDateTime(effectiveToDate));
        return (J) this;
    }


    /**
     * In date range from till now
     *
     * @param fromDate The date for from
     * @return This
     */
    @Override
    @NotNull
    public J inDateRangeSpecified(LocalDateTime fromDate)
    {
        return inDateRange(fromDate, getNow());
    }

    /**
     * Specifies where effective from date greater and effective to date less than
     *
     * @param fromDate The from date
     * @param toDate   The to date
     * @return This
     */
    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public J inDateRange(LocalDateTime fromDate, LocalDateTime toDate)
    {
        if (fromDate != null)
        {
            where(getAttribute(EFFECTIVE_FROM_DATE_COLUMN_NAME), Operand.GreaterThanEqualTo, IQueryBuilderSCD.convertToUTCDateTime(fromDate));
        }
        //noinspection ReplaceNullCheck
        if (toDate != null)
        {
            where(getAttribute(EFFECTIVE_TO_DATE_COLUMN_NAME), Operand.LessThanEqualTo, IQueryBuilderSCD.convertToUTCDateTime(toDate));
        }
        else
        {
            where(getAttribute(EFFECTIVE_TO_DATE_COLUMN_NAME), Operand.LessThanEqualTo, EndOfTime.atOffset(UTC));
        }

        return (J) this;
    }

    public J withWarehouseCreated(LocalDateTime time)
    {
        if (time != null)
        {
            where(getAttribute(WAREHOUSE_CREATED_DATE_COLUMN_NAME), Operand.Equals, IQueryBuilderSCD.convertToUTCDateTime(time));
        }
        //noinspection unchecked
        return (J) this;
    }

    public J withWarehouseUpdated(LocalDateTime time)
    {
        if (time != null)
        {
            where(getAttribute(WAREHOUSE_UPDATED_DATE_COLUMN_NAME), Operand.Equals, IQueryBuilderSCD.convertToUTCDateTime(time));
        }
        //noinspection unchecked
        return (J) this;
    }

    @Override
    public @NotNull Uni<E> update(E entity)
    {
        entity.setWarehouseLastUpdatedTimestamp(IQueryBuilderSCD.convertToUTCDateTime(getNow()));
        return super.update(entity);
    }

    public @NotNull Uni<E> update(E entity, java.time.Duration expiresIn)
    {
        entity.setEffectiveToDate(IQueryBuilderSCD.convertToUTCDateTime(getNow())
                .plus(expiresIn));
        entity.setWarehouseLastUpdatedTimestamp(IQueryBuilderSCD.convertToUTCDateTime(getNow()));
        return super.update(entity);
    }

    /**
     * Performs any required logic between the original and new entities during an update operation
     * which is a delete and marking of the record as historical, and the insert of a new record which is updated
     * <p>
     * The old and new entities may have the same id, the new entity id is emptied after this call for persistence.
     *
     * @param originalEntity The entity that is going to be deleted
     * @param newEntity      The entity that is going to be created
     * @return currently always true @TODO
     */
    @Override
    public boolean onDeleteUpdate(E originalEntity, E newEntity)
    {
        return true;
    }

    /**
     * Sets the SCD values to new ones if not present
     *
     * @param entity The entity
     * @return true if must create
     */
    @Override
    public boolean onCreate(E entity)
    {
        if (entity.getWarehouseCreatedTimestamp() == null)
        {
            entity.setWarehouseCreatedTimestamp(IQueryBuilderSCD.convertToUTCDateTime(getNow()));
        }
        if (entity.getWarehouseLastUpdatedTimestamp() == null)
        {
            entity.setWarehouseLastUpdatedTimestamp(IQueryBuilderSCD.convertToUTCDateTime(getNow()));
        }
        if (entity.getEffectiveFromDate() == null)
        {
            entity.setEffectiveFromDate(IQueryBuilderSCD.convertToUTCDateTime(getNow()));
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
     * @return boolean
     */
    @Override
    public boolean onUpdate(E entity)
    {
        entity.setWarehouseLastUpdatedTimestamp(IQueryBuilderSCD.convertToUTCDateTime(getNow()));
        return true;
    }

    public J latestFirst()
    {
        orderBy(getAttribute(WAREHOUSE_UPDATED_DATE_COLUMN_NAME), OrderByType.DESC);
        return (J) this;
    }
}
