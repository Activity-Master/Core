package com.guicedee.activitymaster.core.db.entities.systems.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.systems.SystemXClassification;
import com.guicedee.activitymaster.core.db.entities.systems.SystemXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.systems.SystemXClassification_;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;

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
