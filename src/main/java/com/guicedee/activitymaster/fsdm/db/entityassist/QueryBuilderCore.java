package com.guicedee.activitymaster.fsdm.db.entityassist;

import com.entityassist.RootEntity;
import com.entityassist.enumerations.ActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderCore;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.metamodel.Attribute;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

import static com.entityassist.enumerations.Operand.InList;
import static java.time.ZoneOffset.UTC;

/**
 * @param <J> This Class
 * @param <E> Entity Class
 * @author GedMarc
 */
public abstract class QueryBuilderCore<J extends QueryBuilderCore<J, E, I>, E extends CoreEntity<E, J, I>, I extends UUID>
		extends QueryBuilderSCD<J, E, I>
		implements IQueryBuilderCore<J, E, I>
{
	/**
	 * The active flag column name
	 */
	@SuppressWarnings("WeakerAccess")
	public static final String ACTIVE_FLAG_DATE_COLUMN_NAME = "activeFlag";
	
	/**
	 * Filters from the Active Flag suite where it is in the active range
	 *
	 * @return This
	 */
	@Override
	@NotNull
	@SuppressWarnings("unchecked")
	public J inActiveRange()
	{
		where((Attribute<Object, Object>) getAttribute(ACTIVE_FLAG_DATE_COLUMN_NAME), InList, ActiveFlag.getActiveRangeAndUp());
		return (J) this;
	}
	
	/**
	 * Selects all records in the visible range
	 *
	 * @return This
	 */
	@Override
	@NotNull
	@SuppressWarnings("unchecked")
	public J inVisibleRange()
	{
		where((Attribute<Object, Object>) getAttribute(ACTIVE_FLAG_DATE_COLUMN_NAME), InList, ActiveFlag.getVisibleRangeAndUp());
		return (J) this;
	}
	
	/**
	 * Updates the current record with the given active flag type
	 * uses the merge
	 *
	 * @param newActiveFlagType The new flag type to apply
	 * @param entity            The entity to operate on
	 * @return The entity
	 */
	@Override
	public Uni<E> delete(ActiveFlag newActiveFlagType, E entity)
	{
		entity.setWarehouseLastUpdatedTimestamp(IQueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow()));
		entity.setEffectiveToDate(IQueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow()));
		entity.setActiveFlag(newActiveFlagType);
		return getEntityManager().merge(entity);
	}
	
	/**
	 * Updates the current record with the given active flag type
	 *
	 * @param entity The entity to delete
	 * @return the entity
	 */
	@Override
	public Uni<E> delete(E entity)
	{
		entity.setWarehouseLastUpdatedTimestamp(IQueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow()));
		entity.setEffectiveToDate(IQueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow()));
		entity.setActiveFlag(ActiveFlag.Deleted);
		return getEntityManager().merge(entity);
	}
	
	/**
	 * Marks the record as archived updating the warehouse and effective to date timestamps
	 *
	 * @param entity The entity
	 * @return The Entity
	 */
	@Override
	public E archive(E entity)
	{
		entity.setWarehouseLastUpdatedTimestamp(IQueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow()));
		entity.setEffectiveToDate(IQueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow()));
		entity.setActiveFlag(ActiveFlag.Archived);
		
		getEntityManager().merge(entity);
		return entity;
	}
	
	/**
	 * Marks the given entity as the given status, with the effective to date and warehouse last updated as now
	 * Merges the entity, then detaches,
	 * <p>
	 * Persists the new record down with the end of time used
	 *
	 * @param entity The entity
	 * @param status The new status
	 * @return The updated entity
	 */
	@Override
	@SuppressWarnings("unused")
	public E closeAndReturnNewlyUpdate(E entity, ActiveFlag status)
	{
		entity.setWarehouseLastUpdatedTimestamp(IQueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow()));
		entity.setEffectiveToDate(IQueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow()));
		entity.setActiveFlag(status);
		
		getEntityManager().merge(entity);
		getEntityManager().detach(entity);
		
		entity.setId(null);
		
		entity.setWarehouseCreatedTimestamp(IQueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow()));
		entity.setWarehouseLastUpdatedTimestamp(IQueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow()));
		entity.setEffectiveFromDate(IQueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow()));
		entity.setEffectiveToDate(SCDEntity.EndOfTime.atOffset(UTC));
		entity.setActiveFlag(ActiveFlag.Active);
		
		persist(entity);
		
		return entity;
	}
	
}
