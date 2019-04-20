package com.armineasy.activitymaster.activitymaster.db.entities.activeflag.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagXClassification_;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;

import javax.persistence.metamodel.Attribute;

public class ActiveFlagXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ActiveFlag, Classification, ActiveFlagXClassificationQueryBuilder,
				                                              ActiveFlagXClassification, Long, ActiveFlagXClassificationSecurityToken>
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
