package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXArrangement;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXArrangementSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXArrangement_;

import jakarta.persistence.metamodel.Attribute;

public class ArrangementXArrangementQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, Arrangement, ArrangementXArrangementQueryBuilder,
						                                              ArrangementXArrangement, java.util.UUID, ArrangementXArrangementSecurityToken>
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
