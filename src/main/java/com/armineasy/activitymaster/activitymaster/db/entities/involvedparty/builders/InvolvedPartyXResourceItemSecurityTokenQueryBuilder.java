package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXResourceItemSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXResourceItemSecurityTokenQueryBuilder, InvolvedPartyXResourceItemSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXResourceItemSecurityToken_.base;
	}
}
