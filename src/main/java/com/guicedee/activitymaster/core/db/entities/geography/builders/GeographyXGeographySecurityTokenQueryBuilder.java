package com.guicedee.activitymaster.core.db.entities.geography.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXGeographySecurityToken;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXGeographySecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class GeographyXGeographySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<GeographyXGeographySecurityTokenQueryBuilder, GeographyXGeographySecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return GeographyXGeographySecurityToken_.base;
	}
}
