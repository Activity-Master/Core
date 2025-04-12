package com.guicedee.activitymaster.fsdm.db.entities.geography.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.geography.GeographySecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.geography.GeographySecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class GeographySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<GeographySecurityTokenQueryBuilder, GeographySecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return GeographySecurityToken_.base;
	}
}
