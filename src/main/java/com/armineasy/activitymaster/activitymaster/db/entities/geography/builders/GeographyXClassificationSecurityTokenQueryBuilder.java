package com.armineasy.activitymaster.activitymaster.db.entities.geography.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class GeographyXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<GeographyXClassificationSecurityTokenQueryBuilder, GeographyXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return GeographyXClassificationSecurityToken_.base;
	}
}
