package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.capabilities.contains.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.systems.ActiveFlagSystem;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.guicedee.client.IGuiceContext.*;

/**
 * @param <J>
 * @author Marc Magon
 * @version 1.0
 * @since 06 Dec 2016
 */
@MappedSuperclass

public abstract class WarehouseSCDTable<
                                               J extends WarehouseSCDTable<J, Q, I, S>,
                                               Q extends QueryBuilderSCD<Q, J, I,?>,
                                               I extends java.util.UUID,
                                               S extends WarehouseSecurityTable<S, ?, I>
                                               >
        extends WarehouseTable<J, Q, I, S>
        implements IWarehouseTable<J, Q, I, S>,
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


    protected J configureDefaultsSystemValues(Systems requestingSystem)
    {
        setSystemID(requestingSystem);
        IActiveFlagService<?> service = IGuiceContext.get(IActiveFlagService.class);
        service.getActiveFlag(requestingSystem.getEnterpriseID())
                       .chain(flag->{
                           setActiveFlagID(flag);
                           return null;
                       })
                .await().atMost(Duration.of(50L, ChronoUnit.SECONDS));
        setEnterpriseID(requestingSystem.getEnterpriseID());
        return (J) this;
    }


    public Uni<J> remove()
    {
        IActiveFlagService<?> service = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
        return service.getDeletedFlag(getEnterpriseID(), get(ActiveFlagSystem.class).getSystemToken(getEnterpriseID()))
                .onItem()
                .invoke(flag -> {
                    setActiveFlagID(flag);
                    setEffectiveToDate(QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
                })
                .chain(this::update)
                .log("Removed record from database")
        ;
    }


    public Uni<J> archive()
    {
        IActiveFlagService<?> service = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
        return service.getArchivedFlag(getEnterpriseID(), get(ActiveFlagSystem.class).getSystemToken(getEnterpriseID()))
                .onItem()
                .invoke(flag -> {
                    setActiveFlagID(flag);
                    setEffectiveToDate(QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
                })
                .chain(this::update)
                .log("Archived record in database");
    }

    public ActiveFlag getActiveFlagID()
    {
        return this.activeFlagID;
    }

    public J setActiveFlagID(IActiveFlag<?, ?> activeFlagID)
    {
        this.activeFlagID = (ActiveFlag) activeFlagID;
        return (J) this;
    }


    public Systems getSystemID()
    {
        return this.systemID;
    }

    @SuppressWarnings("unchecked")
    public J setSystemID(ISystems<?, ?> systemID)
    {
        this.systemID = (Systems) systemID;
        return (J) this;
    }

}
