package com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class EnterpriseXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Enterprise, Classification, EnterpriseXClassificationQueryBuilder,
		EnterpriseXClassification, UUID,EnterpriseXClassificationSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<WarehouseTable, Enterprise> getPrimaryAttribute()
	{
		return EnterpriseXClassification_.enterpriseID;
	}
	
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return EnterpriseXClassification_.classificationID;
	}
}
