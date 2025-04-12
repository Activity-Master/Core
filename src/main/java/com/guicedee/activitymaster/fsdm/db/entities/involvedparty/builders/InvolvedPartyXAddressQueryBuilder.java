package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class InvolvedPartyXAddressQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, Address, InvolvedPartyXAddressQueryBuilder,
		InvolvedPartyXAddress, UUID,InvolvedPartyXAddressSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<InvolvedPartyXAddress, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXAddress_.involvedPartyID;
	}
	
	@Override
	public SingularAttribute<InvolvedPartyXAddress, Address> getSecondaryAttribute()
	{
		return InvolvedPartyXAddress_.addressID;
	}
}
