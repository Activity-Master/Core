package com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationship;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.*;

import javax.persistence.metamodel.Attribute;

public class ArrangementXArrangementTypeQueryBuilder
		extends QueryBuilderRelationship<Arrangement, ArrangementType, ArrangementXArrangementTypeQueryBuilder,
				                                ArrangementXArrangementType, Long, ArrangementXArrangementTypeSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ArrangementXArrangementType_.arrangement;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ArrangementXArrangementType_.type;
	}
}
