package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXClassificationSecurityTokenQueryBuilder, InvolvedPartyXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXClassificationSecurityToken_.base;
	}
}
