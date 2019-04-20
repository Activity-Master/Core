package com.armineasy.activitymaster.activitymaster.db.entities.geography.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.Geography;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXClassification_;

import javax.persistence.metamodel.Attribute;

public class GeographyXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Geography, Classification, GeographyXClassificationQueryBuilder,
				                                              GeographyXClassification, Long, GeographyXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return GeographyXClassification_.geographyID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return GeographyXClassification_.classificationID;
	}
}
