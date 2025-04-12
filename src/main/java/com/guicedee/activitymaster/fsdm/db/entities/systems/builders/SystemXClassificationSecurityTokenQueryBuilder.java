package com.guicedee.activitymaster.fsdm.db.entities.systems.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.systems.SystemsXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.systems.SystemsXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class SystemXClassificationSecurityTokenQueryBuilder
        extends QueryBuilderSecurities<SystemXClassificationSecurityTokenQueryBuilder, SystemsXClassificationSecurityToken, UUID>
{
    @Override
    protected Attribute getMyAttribute()
    {
        return SystemsXClassificationSecurityToken_.base;
    }
}
