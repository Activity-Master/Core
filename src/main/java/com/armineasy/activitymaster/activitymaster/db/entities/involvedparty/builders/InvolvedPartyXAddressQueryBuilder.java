package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXAddress;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXAddressSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXAddress_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXAddressQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, Address, InvolvedPartyXAddressQueryBuilder,
				                                              InvolvedPartyXAddress, Long, InvolvedPartyXAddressSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return InvolvedPartyXAddress_.involvedPartyID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return InvolvedPartyXAddress_.addressID;
	}
}
