package com.armineasy.activitymaster.activitymaster.db.entities.security.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXClassification_;

import javax.persistence.metamodel.Attribute;

public class SecurityTokenXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<SecurityToken, Classification, SecurityTokenXClassificationQueryBuilder,
				                                              SecurityTokenXClassification, Long, SecurityTokenXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return SecurityTokenXClassification_.securityTokenID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return SecurityTokenXClassification_.classificationID;
	}
}
