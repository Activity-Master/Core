package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXClassification_;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;

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
