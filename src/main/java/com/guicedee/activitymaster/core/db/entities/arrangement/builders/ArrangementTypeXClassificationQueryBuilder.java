package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementType;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementTypeXClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementTypeXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementTypeXClassification_;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;

import javax.persistence.metamodel.Attribute;

public class ArrangementTypeXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ArrangementType, Classification, ArrangementTypeXClassificationQueryBuilder,
		ArrangementTypeXClassification, Long, ArrangementTypeXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ArrangementTypeXClassification_.arrangementID;
	}
	
	@Override
	public Attribute getSecondaryAttribute()
	{
		return ArrangementTypeXClassification_.classificationID;
	}
}
