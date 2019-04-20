package com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.Arrangement;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXArrangement;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXArrangementSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXArrangement_;

import javax.persistence.metamodel.Attribute;

public class ArrangementXArrangementQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, Arrangement, ArrangementXArrangementQueryBuilder,
				                                              ArrangementXArrangement, Long, ArrangementXArrangementSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ArrangementXArrangement_.parentArrangementID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ArrangementXArrangement_.childArrangementID;
	}
}
