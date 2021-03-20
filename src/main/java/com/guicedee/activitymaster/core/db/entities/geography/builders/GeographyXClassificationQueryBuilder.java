package com.guicedee.activitymaster.core.db.entities.geography.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.geography.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class GeographyXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Geography, Classification, GeographyXClassificationQueryBuilder,
				                                              GeographyXClassification, java.util.UUID>
{
	@Override
	public SingularAttribute<GeographyXClassification, Geography> getPrimaryAttribute()
	{
		return GeographyXClassification_.geographyID;
	}

	@Override
	public  SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return GeographyXClassification_.classificationID;
	}
}
