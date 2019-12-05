package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXResourceItem;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXResourceItem_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, ResourceItem, InvolvedPartyXResourceItemQueryBuilder,
						                                              InvolvedPartyXResourceItem, Long, InvolvedPartyXResourceItemSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return InvolvedPartyXResourceItem_.involvedPartyID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return InvolvedPartyXResourceItem_.resourceItemID;
	}
}
