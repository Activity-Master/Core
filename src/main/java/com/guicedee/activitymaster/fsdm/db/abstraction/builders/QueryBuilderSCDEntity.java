package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.entityassist.enumerations.ActiveFlag;
import com.entityassist.enumerations.Operand;
import com.entityassist.enumerations.OrderByType;
import com.entityassist.querybuilder.QueryBuilder;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.abstraction.SCDEntity;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.criteria.Order;
import jakarta.validation.constraints.NotNull;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.entityassist.RootEntity.getNow;
import static java.time.ZoneOffset.UTC;

public abstract class QueryBuilderSCDEntity<J extends QueryBuilderSCDEntity<J, E, I>,
                                               E extends SCDEntity<E, J, I>,
                                               I extends java.util.UUID>
    extends QueryBuilder<J, E, I>
    implements IQueryBuilderSCD<J, E, I>
{
  @Override
  public boolean isIdGenerated()
  {
    return false;
  }

  @Override
  public Uni<E> get()
  {
    orderBy(getAttribute(EFFECTIVE_FROM_DATE_COLUMN_NAME), OrderByType.DESC);
    return super.get();
  }

  /**
   * The effective to date column name
   */
  @SuppressWarnings("WeakerAccess")
  public static final String EFFECTIVE_TO_DATE_COLUMN_NAME = "effectiveToDate";
  /**
   * The effective from date column name
   */
  @SuppressWarnings("WeakerAccess")
  public static final String EFFECTIVE_FROM_DATE_COLUMN_NAME = "effectiveFromDate";

  /**
   * The effective from date column name
   */
  @SuppressWarnings("WeakerAccess")
  public static final String WAREHOUSE_CREATED_DATE_COLUMN_NAME = "warehouseCreatedTimestamp";
  /**
   * The effective from date column name
   */
  @SuppressWarnings("WeakerAccess")
  public static final String WAREHOUSE_UPDATED_DATE_COLUMN_NAME = "warehouseLastUpdatedTimestamp";

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
  public @NotNull Uni<E> update()
  {
    getEntity().setWarehouseLastUpdatedTimestamp(IQueryBuilderSCD.convertToUTCDateTime(getNow()));
    try
    {
      return super.update();
    }
    catch (Throwable e)
    {
      Logger.getLogger(getClass().getName())
          .log(Level.WARNING, "Unable to update id : " + e, e);
      return Uni.createFrom()
                 .failure(e);
    }
  }

  public @NotNull Uni<E> update(E entity, java.time.Duration expiresIn)
  {
    entity.setEffectiveToDate(IQueryBuilderSCD.convertToUTCDateTime(getNow())
                                  .plus(expiresIn));
    entity.setWarehouseLastUpdatedTimestamp(IQueryBuilderSCD.convertToUTCDateTime(getNow()));
    try
    {
      return super.update();
    }
    catch (Throwable e)
    {
      Logger.getLogger(getClass().getName())
          .log(Level.WARNING, "Unable to update id : " + e, e);
      return Uni.createFrom()
                 .failure(e);
    }
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

  @Override
  public Uni<E> delete(ActiveFlag newActiveFlagType, E entity)
  {
    return IGuiceContext.get(Mutiny.SessionFactory.class)
               .openSession()
               .chain(session -> session.withTransaction(transaction -> {
                   //todo complete the deletion by setting the active flag to the new ActiveFlagType
                   //entity.setActiveFlagID((IActiveFlag<?, ?>) newActiveFlagType);
                   return session.merge(entity);
                 })
                 .eventually(session::close));
  }

  @Override
  public Uni<E> archive(E entity)
  {
    return delete(ActiveFlag.Archived, entity);
  }

  @Override
  public Uni<E> closeAndReturnNewlyUpdate(E entity, ActiveFlag status)
  {
    return delete(status, entity);
  }


}
