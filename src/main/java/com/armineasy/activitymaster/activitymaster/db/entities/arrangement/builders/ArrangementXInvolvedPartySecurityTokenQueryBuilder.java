package com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXInvolvedPartySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXInvolvedPartySecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ArrangementXInvolvedPartySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXInvolvedPartySecurityTokenQueryBuilder, ArrangementXInvolvedPartySecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXInvolvedPartySecurityToken_.base;
	}
}
