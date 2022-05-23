package com.guicedee.activitymaster.fsdm.db.entities.geography.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.geography.GeographyXGeographySecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.geography.GeographyXGeographySecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class GeographyXGeographySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<GeographyXGeographySecurityTokenQueryBuilder, GeographyXGeographySecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return GeographyXGeographySecurityToken_.base;
	}
}
