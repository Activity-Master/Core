package com.armineasy.activitymaster.activitymaster.db.entities.systems.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemXClassification_;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;

import javax.persistence.metamodel.Attribute;

public class SystemsXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Systems, Classification, SystemsXClassificationQueryBuilder,
				                                              SystemXClassification, Long, SystemXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return SystemXClassification_.systemID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return SystemXClassification_.classificationID;
	}
}
