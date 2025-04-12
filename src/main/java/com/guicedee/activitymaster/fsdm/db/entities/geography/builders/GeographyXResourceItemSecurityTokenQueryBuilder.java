package com.guicedee.activitymaster.fsdm.db.entities.geography.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.geography.GeographyXResourceItemSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.geography.GeographyXResourceItemSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class GeographyXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<GeographyXResourceItemSecurityTokenQueryBuilder, GeographyXResourceItemSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return GeographyXResourceItemSecurityToken_.base;
	}
}
