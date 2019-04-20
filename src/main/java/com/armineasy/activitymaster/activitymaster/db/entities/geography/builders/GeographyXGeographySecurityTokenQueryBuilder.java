package com.armineasy.activitymaster.activitymaster.db.entities.geography.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXGeographySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXGeographySecurityToken_;

import javax.persistence.metamodel.Attribute;

public class GeographyXGeographySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<GeographyXGeographySecurityTokenQueryBuilder, GeographyXGeographySecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return GeographyXGeographySecurityToken_.base;
	}
}
