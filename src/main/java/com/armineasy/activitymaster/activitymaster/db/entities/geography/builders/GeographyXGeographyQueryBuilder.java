package com.armineasy.activitymaster.activitymaster.db.entities.geography.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.Geography;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXGeography;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXGeographySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXGeography_;

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
