package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXResourceItem;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXResourceItem_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;

import javax.persistence.metamodel.Attribute;

public class ArrangementXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, ResourceItem, ArrangementXResourceItemQueryBuilder,
						                                              ArrangementXResourceItem, Long, ArrangementXResourceItemSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ArrangementXResourceItem_.arrangementID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ArrangementXResourceItem_.resourceItemID;
	}
}
