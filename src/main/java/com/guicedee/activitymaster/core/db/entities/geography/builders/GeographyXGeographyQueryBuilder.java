package com.guicedee.activitymaster.core.db.entities.geography.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXGeography;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXGeographySecurityToken;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXGeography_;

import javax.persistence.metamodel.Attribute;

public class GeographyXGeographyQueryBuilder
		extends QueryBuilderRelationshipClassification<Geography, Geography, GeographyXGeographyQueryBuilder,
				                                              GeographyXGeography, Long, GeographyXGeographySecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return GeographyXGeography_.parentGeographyID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return GeographyXGeography_.childGeographyID;
	}
}
