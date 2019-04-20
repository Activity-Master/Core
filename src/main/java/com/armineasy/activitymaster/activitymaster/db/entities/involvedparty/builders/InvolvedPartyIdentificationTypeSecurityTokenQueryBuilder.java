package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyIdentificationTypeSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyIdentificationTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyIdentificationTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyIdentificationTypeSecurityTokenQueryBuilder, InvolvedPartyIdentificationTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyIdentificationTypeSecurityToken_.base;
	}
}
