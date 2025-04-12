package com.guicedee.activitymaster.fsdm.db.entities.geography.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.geography.GeographyXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.geography.GeographyXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class GeographyXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<GeographyXClassificationSecurityTokenQueryBuilder, GeographyXClassificationSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return GeographyXClassificationSecurityToken_.base;
	}
}
