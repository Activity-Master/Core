package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXResourceItem;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXResourceItem_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class ArrangementXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, ResourceItem, ArrangementXResourceItemQueryBuilder,
						                                              ArrangementXResourceItem, java.util.UUID, ArrangementXResourceItemSecurityToken>
{
	@Override
	public SingularAttribute<ArrangementXResourceItem, Arrangement> getPrimaryAttribute()
	{
		return ArrangementXResourceItem_.arrangementID;
	}

	@Override
	public  SingularAttribute<ArrangementXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return ArrangementXResourceItem_.resourceItemID;
	}
}
