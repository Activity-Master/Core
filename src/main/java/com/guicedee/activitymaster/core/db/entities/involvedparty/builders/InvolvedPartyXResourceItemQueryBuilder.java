package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXResourceItem;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXResourceItem_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class InvolvedPartyXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, ResourceItem, InvolvedPartyXResourceItemQueryBuilder,
						                                              InvolvedPartyXResourceItem, java.util.UUID, InvolvedPartyXResourceItemSecurityToken>
{
	@Override
	public SingularAttribute<InvolvedPartyXResourceItem, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXResourceItem_.involvedPartyID;
	}

	@Override
	public SingularAttribute<InvolvedPartyXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return InvolvedPartyXResourceItem_.resourceItemID;
	}
}
