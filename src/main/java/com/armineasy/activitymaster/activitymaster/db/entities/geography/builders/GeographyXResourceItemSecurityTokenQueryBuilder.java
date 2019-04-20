package com.armineasy.activitymaster.activitymaster.db.entities.geography.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXResourceItemSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class GeographyXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<GeographyXResourceItemSecurityTokenQueryBuilder, GeographyXResourceItemSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return GeographyXResourceItemSecurityToken_.base;
	}
}
