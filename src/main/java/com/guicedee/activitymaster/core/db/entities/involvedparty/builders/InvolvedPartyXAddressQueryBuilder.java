package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class InvolvedPartyXAddressQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, Address, InvolvedPartyXAddressQueryBuilder,
						                                              InvolvedPartyXAddress, java.util.UUID>
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
