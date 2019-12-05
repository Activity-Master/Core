package com.guicedee.activitymaster.core.db.entities.geography.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXClassificationSecurityToken_;

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
