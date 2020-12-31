package com.guicedee.activitymaster.core.db.entities.security.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXClassification;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXClassification_;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class SecurityTokenXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<SecurityToken, Classification, SecurityTokenXClassificationQueryBuilder,
				                                              SecurityTokenXClassification, java.util.UUID, SecurityTokenXClassificationSecurityToken>
{
	@Override
	public SingularAttribute<SecurityTokenXClassification, SecurityToken> getPrimaryAttribute()
	{
		return SecurityTokenXClassification_.securityTokenID;
	}

	@Override
	public  SingularAttribute<WarehouseClassificationRelationshipTable, Classification>  getSecondaryAttribute()
	{
		return SecurityTokenXClassification_.classificationID;
	}
}
