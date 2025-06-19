package com.guicedee.activitymaster.fsdm.db.entities.geography.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.geography.IGeography;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.geography.IGeographyQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.geography.*;
import jakarta.persistence.criteria.JoinType;
import jakarta.validation.constraints.Null;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class GeographyQueryBuilder
		extends QueryBuilderSCD<GeographyQueryBuilder, Geography, UUID,GeographySecurityTokenQueryBuilder>
		implements IGeographyQueryBuilder<GeographyQueryBuilder, Geography>,
		           IQueryBuilderNamesAndDescriptions<GeographyQueryBuilder,Geography,java.util.UUID>
{
	
	@jakarta.validation.constraints.NotNull
	public GeographyQueryBuilder withParent(IGeography<?, ?> parent, @Null String value)
	{
		if (parent == null)
		{
			return this;
		}
		JoinExpression joinExpression = new JoinExpression();
		GeographyXGeographyQueryBuilder builder =
				new GeographyXGeography()
						.builder()
						.inActiveRange()
						.inDateRange()
						.where(GeographyXGeography_.parentGeographyID, Equals, ((Geography) parent));
		if (!Strings.isNullOrEmpty(value))
		{
			builder.where(GeographyXClassification_.value, Equals, value);
		}
		
		join(Geography_.geographyXGeographyList,
				builder,
				JoinType.INNER, joinExpression);
		return this;
	}
	
	public GeographyQueryBuilder withGeoNameID(UUID id)
	{
		where(Geography_.originalSourceSystemUniqueID, Equals, id);
		return this;
	}
}
