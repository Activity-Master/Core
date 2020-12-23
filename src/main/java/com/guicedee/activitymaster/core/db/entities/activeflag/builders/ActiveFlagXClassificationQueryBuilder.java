package com.guicedee.activitymaster.core.db.entities.activeflag.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlagXClassification;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlagXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlagXClassification_;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;

import jakarta.persistence.metamodel.Attribute;

public class ActiveFlagXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ActiveFlag, Classification, ActiveFlagXClassificationQueryBuilder,
						                                              ActiveFlagXClassification, java.util.UUID, ActiveFlagXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ActiveFlagXClassification_.activeFlagID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ActiveFlagXClassification_.classificationID;
	}
}
