package com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.Arrangement;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXResourceItem_;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;

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
