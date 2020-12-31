package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXClassification;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXClassification_;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class RulesXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, Classification, RulesXClassificationQueryBuilder,
				                                              RulesXClassification, java.util.UUID, RulesXClassificationSecurityToken>
{
	@Override
	public SingularAttribute<RulesXClassification, Rules> getPrimaryAttribute()
	{
		return RulesXClassification_.rulesID;
	}

	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return RulesXClassification_.classificationID;
	}
}
