package com.guicedee.activitymaster.fsdm.db.entities.security.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.security.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class SecurityTokenXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<SecurityToken, Classification, SecurityTokenXClassificationQueryBuilder,
		SecurityTokenXClassification, UUID,SecurityTokenXClassificationSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<SecurityTokenXClassification, SecurityToken> getPrimaryAttribute()
	{
		return SecurityTokenXClassification_.securityTokenID;
	}
	
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return SecurityTokenXClassification_.classificationID;
	}
}
