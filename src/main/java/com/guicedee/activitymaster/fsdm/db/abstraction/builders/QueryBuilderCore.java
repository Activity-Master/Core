package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.entityassist.RootEntity;
import com.entityassist.enumerations.ActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.capabilities.contains.IContainsActiveFlags;
import com.guicedee.activitymaster.fsdm.client.services.capabilities.contains.IContainsEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.capabilities.contains.IContainsSystem;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseCoreTable;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static com.entityassist.enumerations.Operand.InList;
import static com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD.convertToUTCDateTime;
import static com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD.EndOfTime;
import static java.time.ZoneOffset.UTC;

public abstract class QueryBuilderCore<J extends QueryBuilderCore<J, E, I>,
		E extends WarehouseCoreTable<E, J, I,?>,
		I extends java.util.UUID>
		extends QueryBuilderDefault<J, E, I>
{
    /**
     * Filters from the Active Flag suite where it is in the active range
     *
     * @return This
     */
    public J inActiveRange() {
        IEnterprise<?,?> enterprise = IGuiceContext.get(IEnterprise.class);
        try {
            if (enterprise.isFake()) {
                return (J) this;
            }
        } catch (IllegalStateException e) {
            // Not yet loaded
            return (J) this;
        }

        IActiveFlagService<?> flagService = IGuiceContext.get(IActiveFlagService.class);
        Collection<IActiveFlag<?,?>> flags = flagService.findActiveRange(enterprise);
        Collection<IActiveFlag<?,?>> flagss = new ArrayList<>();
        for (IActiveFlag<?,?> flag : flags) {
            flagss.add(flag);
        }
        where(this.<E, IActiveFlag<?,?>>getAttribute("activeFlagID"), InList, flagss);
        return (J) this;
    }

    /**
     * Selects all records in the visible range
     *
     * @return This
     */
    public J inVisibleRange() {
        IEnterprise<?,?> enterprise = IGuiceContext.get(IEnterprise.class);
        try {
            if (enterprise.isFake()) {
                return (J) this;
            }
        } catch (IllegalStateException e) {
            // Not yet loaded
            return (J) this;
        }

        IActiveFlagService<?> flagService = IGuiceContext.get(IActiveFlagService.class);
        Collection<IActiveFlag<?,?>> flags = flagService.getVisibleRange(enterprise);
        Collection<IActiveFlag<?,?>> flagss = new ArrayList<>();
        for (IActiveFlag<?,?> flag : flags) {
            flagss.add(flag);
        }
        where(this.<E, IActiveFlag<?,?>>getAttribute("activeFlagID"), InList, flagss);
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
    public E delete(ActiveFlag newActiveFlagType, E entity) {
        // Set the effective to date and warehouse last updated timestamp
        entity.setEffectiveToDate(convertToUTCDateTime(RootEntity.getNow()));
        entity.setWarehouseLastUpdatedTimestamp(convertToUTCDateTime(RootEntity.getNow()));

        // Get the appropriate flag based on the type
        IActiveFlagService<?> flagService = IGuiceContext.get(IActiveFlagService.class);
        IActiveFlag<?,?> flag;

        // Get the enterprise from the entity
        IEnterprise<?,?> enterprise = null;
        if (entity instanceof IContainsEnterprise) {
            enterprise = ((IContainsEnterprise<?>) entity).getEnterpriseID();
        }

        if (enterprise == null) {
            throw new IllegalStateException("Entity does not contain enterprise information");
        }

        if (newActiveFlagType == ActiveFlag.Deleted) {
            flag = flagService.getDeletedFlag(enterprise, UUID.randomUUID());
        } else if (newActiveFlagType == ActiveFlag.Archived) {
            flag = flagService.getArchivedFlag(enterprise, UUID.randomUUID());
        } else {
            flag = flagService.getActiveFlag(enterprise, UUID.randomUUID());
        }

        // Update the entity with the new flag
        if (entity instanceof IContainsActiveFlags) {
            ((IContainsActiveFlags<?>) entity).setActiveFlagID(flag);
            entity.update();
            return entity;
        } else {
            throw new IllegalStateException("Entity does not support active flags");
        }
    }

    /**
     * Marks the record as archived updating the warehouse and effective to date timestamps
     *
     * @param entity The entity
     * @return The Entity
     */
    public E archive(E entity) {
        return delete(ActiveFlag.Archived, entity);
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
    public E closeAndReturnNewlyUpdate(E entity, ActiveFlag status) {
        // First update the current entity to close it
        E updatedEntity = delete(status, entity);

        try {
            // Create a new entity with the same data but new ID
            E newEntity = (E) entity.getClass().getDeclaredConstructor().newInstance();

            // Set effective dates
            newEntity.setEffectiveFromDate(convertToUTCDateTime(RootEntity.getNow()));
            newEntity.setEffectiveToDate(EndOfTime.atOffset(UTC));
            newEntity.setWarehouseCreatedTimestamp(convertToUTCDateTime(RootEntity.getNow()));
            newEntity.setWarehouseLastUpdatedTimestamp(convertToUTCDateTime(RootEntity.getNow()));

            // Get the enterprise from the entity
            IEnterprise<?,?> enterprise = null;
            if (entity instanceof IContainsEnterprise) {
                enterprise = ((IContainsEnterprise<?>) entity).getEnterpriseID();
            }

            if (enterprise == null) {
                throw new IllegalStateException("Entity does not contain enterprise information");
            }

            // Get the active flag
            IActiveFlagService<?> flagService = IGuiceContext.get(IActiveFlagService.class);
            IActiveFlag<?,?> activeFlag = flagService.getActiveFlag(enterprise, UUID.randomUUID());

            // Set the enterprise ID if the new entity supports it
            if (newEntity instanceof IContainsEnterprise) {
                ((IContainsEnterprise<?>) newEntity).setEnterpriseID(enterprise);
            }

            // Set the active flag if the new entity supports it
            if (newEntity instanceof IContainsActiveFlags) {
                ((IContainsActiveFlags<?>) newEntity).setActiveFlagID(activeFlag);
                newEntity.persist();
                return newEntity;
            } else {
                throw new IllegalStateException("Entity does not support active flags");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new entity", e);
        }
    }
}
