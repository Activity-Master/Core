package com.armineasy.activitymaster.activitymaster.db.entities.yesno.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNo;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNoXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNoXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNoXClassification_;

import javax.persistence.metamodel.Attribute;

public class YesNoXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<YesNo, Classification, YesNoXClassificationQueryBuilder,
				                                              YesNoXClassification, Long, YesNoXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return YesNoXClassification_.yesNoID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return YesNoXClassification_.classificationID;
	}
}
