package com.armineasy.activitymaster.activitymaster.db.entities.enterprise.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.EnterpriseXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.EnterpriseXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.EnterpriseXClassification_;

import javax.persistence.metamodel.Attribute;

public class EnterpriseXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Enterprise, Classification, EnterpriseXClassificationQueryBuilder,
				                                              EnterpriseXClassification, Long, EnterpriseXClassificationSecurityToken>
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
