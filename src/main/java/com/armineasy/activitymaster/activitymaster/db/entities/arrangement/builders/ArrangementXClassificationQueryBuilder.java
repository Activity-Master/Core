package com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.Arrangement;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXClassification_;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;

import javax.persistence.metamodel.Attribute;

public class ArrangementXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, Classification, ArrangementXClassificationQueryBuilder,
				                                              ArrangementXClassification, Long, ArrangementXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ArrangementXClassification_.arrangementID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ArrangementXClassification_.classificationID;
	}
}
