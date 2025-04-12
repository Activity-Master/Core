package com.guicedee.activitymaster.fsdm.db.entities.systems.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.systems.SystemsSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.systems.SystemsSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class SystemsSecurityTokenQueryBuilder
        extends QueryBuilderSecurities<SystemsSecurityTokenQueryBuilder, SystemsSecurityToken, UUID>
{
    @Override
    protected Attribute getMyAttribute()
    {
        return SystemsSecurityToken_.base;
    }
}
