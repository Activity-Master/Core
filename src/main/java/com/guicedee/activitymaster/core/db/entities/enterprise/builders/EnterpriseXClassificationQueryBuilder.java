package com.guicedee.activitymaster.core.db.entities.enterprise.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseXClassification;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseXClassification_;

import jakarta.persistence.metamodel.Attribute;

public class EnterpriseXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Enterprise, Classification, EnterpriseXClassificationQueryBuilder,
						                                              EnterpriseXClassification, java.util.UUID, EnterpriseXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return EnterpriseXClassification_.enterpriseID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return EnterpriseXClassification_.classificationID;
	}
}
