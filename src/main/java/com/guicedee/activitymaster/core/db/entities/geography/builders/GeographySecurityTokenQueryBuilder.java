package com.guicedee.activitymaster.core.db.entities.geography.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.geography.GeographySecurityToken;
import com.guicedee.activitymaster.core.db.entities.geography.GeographySecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class GeographySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<GeographySecurityTokenQueryBuilder, GeographySecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return GeographySecurityToken_.base;
	}
}
