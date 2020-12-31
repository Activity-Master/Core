package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.rules.RulesType;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXClassification;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXClassification_;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class RulesTypeXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<RulesType, Classification, RulesTypeXClassificationQueryBuilder,
				                                              RulesTypeXClassification, java.util.UUID, RulesTypeXClassificationSecurityToken>
{
	@Override
	public SingularAttribute<RulesTypeXClassification, RulesType> getPrimaryAttribute()
	{
		return RulesTypeXClassification_.rulesTypeID;
	}

	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return RulesTypeXClassification_.classificationID;
	}
}
