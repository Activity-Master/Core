package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.entityassist.enumerations.Operand;
import com.entityassist.querybuilder.QueryBuilderSCD;
import com.google.inject.ProvisionException;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderDefault;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;


public abstract class QueryBuilderDefault<J extends QueryBuilderDefault<J, E, I>,
        E extends WarehouseBaseTable<E, J, I>,
        I extends java.util.UUID>
        extends QueryBuilderSCD<J, E, I>
        implements IQueryBuilderDefault<J, E, I>
{
    public QueryBuilderDefault()
    {
        //setRunDetached(true);
        setReturnFirst(true);
        //setUseDirectConnection(true);
        //	setDetach(true);
    }

    @Override
    public boolean onCreate(E entity)
    {
        if (entity.getId() == null)
        {
            //noinspection unchecked
            entity.setId((I) UUID.randomUUID());
        }
        if (entity.getWarehouseFromDate() == null)
        {
            if (entity.getEffectiveFromDate() != null)
            {
                entity.setWarehouseFromDate(entity.getEffectiveFromDate().toLocalDate());
            }
            else
            {
                entity.setWarehouseFromDate(java.time.LocalDate.now());
            }
        }
        return super.onCreate(entity);
    }

    public J inPartition(LocalDate partition)
    {
        where(getAttribute("warehouseFromDate"), Operand.Equals, partition);
        return (J)this;
    }


    public J fromPartition(LocalDate partition)
    {
        where(getAttribute("warehouseFromDate"), Operand.GreaterThanEqualTo, partition);
        return (J)this;
    }

    public J tillPartition(LocalDate partition)
    {
        where(getAttribute("warehouseFromDate"), Operand.LessThanEqualTo, partition);
        return (J)this;
    }

    @Override
    public @NotNull J persist(E entity)
    {
        return super.persist(entity);
    }

    @Override
    public EntityManager getEntityManager()
    {
        try
        {
            return com.guicedee.client.IGuiceContext.get(EntityManager.class);
        }
        catch (ProvisionException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isIdGenerated()
    {
        return false;
    }
}
