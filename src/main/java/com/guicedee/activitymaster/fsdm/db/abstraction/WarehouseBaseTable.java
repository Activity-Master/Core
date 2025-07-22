package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseBaseTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderDefault;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.Duration;
import java.time.LocalDate;

@MappedSuperclass()
public abstract class WarehouseBaseTable<J extends WarehouseBaseTable<J, Q, I>,
        Q extends QueryBuilderDefault<Q, J, I>, I extends java.util.UUID>
        extends SCDEntity<J, Q, I>
        implements IWarehouseBaseTable<J, Q, I>
{
    @Getter
    @Setter
    @Transient
    private int yearRange = 10;

    @Column
    @Getter
    @Setter
    private LocalDate warehouseFromDate;

    @Serial
    private static final long serialVersionUID = 1L;

    public J expireIn(Duration duration)
    {
        setEffectiveToDate(QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow())
                .plus(duration));
        update();
        return (J) this;
    }

    @Override
    public @NotNull Q builder()
    {
        Q builder = super.builder();
        return builder;
    }

    @Override
    public @NotNull boolean isFake()
    {
        return getId() == null;
    }

    @Override
    public @NotNull Uni<J> persist()
    {
        return super.persist();
    }
}
