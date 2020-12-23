package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXResourceItem_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;

import jakarta.persistence.metamodel.Attribute;

public class RulesXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, ResourceItem, RulesXResourceItemQueryBuilder,
				                                              RulesXResourceItem, java.util.UUID, RulesXResourceItemSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return RulesXResourceItem_.rulesID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return RulesXResourceItem_.resourceItemID;
	}
}
