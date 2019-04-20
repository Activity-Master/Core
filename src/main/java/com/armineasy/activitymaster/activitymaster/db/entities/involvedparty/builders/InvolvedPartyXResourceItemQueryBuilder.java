package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXResourceItem_;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;

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
