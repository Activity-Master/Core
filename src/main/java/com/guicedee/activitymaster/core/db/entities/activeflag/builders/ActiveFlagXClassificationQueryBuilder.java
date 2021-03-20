package com.guicedee.activitymaster.core.db.entities.activeflag.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.activeflag.*;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import jakarta.persistence.metamodel.SingularAttribute;

public class ActiveFlagXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ActiveFlag, Classification, ActiveFlagXClassificationQueryBuilder,
						                                              ActiveFlagXClassification, java.util.UUID>
{
	@Override
	public SingularAttribute<WarehouseSCDTable, ActiveFlag> getPrimaryAttribute()
	{
		return ActiveFlagXClassification_.activeFlagID;
	}

	@Override
	public  SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return ActiveFlagXClassification_.classificationID;
	}
}
