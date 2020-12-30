package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.rules.RulesType;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXClassification;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXClassification_;
import jakarta.persistence.metamodel.Attribute;

public class RulesTypeXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<RulesType, Classification, RulesTypeXClassificationQueryBuilder,
				                                              RulesTypeXClassification, java.util.UUID, RulesTypeXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return RulesTypeXClassification_.rulesTypeID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return RulesTypeXClassification_.classificationID;
	}
}
