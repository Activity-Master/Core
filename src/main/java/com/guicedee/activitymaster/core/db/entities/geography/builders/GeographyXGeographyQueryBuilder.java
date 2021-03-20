package com.guicedee.activitymaster.core.db.entities.geography.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.geography.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class GeographyXGeographyQueryBuilder
		extends QueryBuilderRelationshipClassification<Geography, Geography, GeographyXGeographyQueryBuilder,
				                                              GeographyXGeography, java.util.UUID>
{
	@Override
	public SingularAttribute<GeographyXGeography, Geography> getPrimaryAttribute()
	{
		return GeographyXGeography_.parentGeographyID;
	}

	@Override
	public SingularAttribute<GeographyXGeography, Geography> getSecondaryAttribute()
	{
		return GeographyXGeography_.childGeographyID;
	}
}
