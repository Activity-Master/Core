package com.guicedee.activitymaster.core.db.entities.geography.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXClassification;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXClassification_;

import jakarta.persistence.metamodel.Attribute;

public class GeographyXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Geography, Classification, GeographyXClassificationQueryBuilder,
				                                              GeographyXClassification, java.util.UUID, GeographyXClassificationSecurityToken>
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
