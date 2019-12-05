package com.guicedee.activitymaster.core.db.entities.geography.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXResourceItemSecurityToken_;

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
