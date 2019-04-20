package com.armineasy.activitymaster.activitymaster.db.entities.geography.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographySecurityToken_;

import javax.persistence.metamodel.Attribute;

public class GeographySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<GeographySecurityTokenQueryBuilder, GeographySecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return GeographySecurityToken_.base;
	}
}
