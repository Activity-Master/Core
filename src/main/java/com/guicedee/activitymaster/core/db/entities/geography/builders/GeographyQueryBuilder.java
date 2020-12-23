package com.guicedee.activitymaster.core.db.entities.geography.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsClassificationsQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.geography.*;
import com.guicedee.activitymaster.core.services.dto.IGeography;

import jakarta.persistence.criteria.JoinType;
import jakarta.validation.constraints.Null;

import static com.entityassist.enumerations.Operand.*;

public class GeographyQueryBuilder
		extends QueryBuilderSCDNameDescription<GeographyQueryBuilder, Geography, java.util.UUID, GeographySecurityToken>
		implements IContainsClassificationsQueryBuilder<GeographyQueryBuilder, Geography, java.util.UUID, GeographyXClassification>
		           //IContainsValueQueryBuilder<GeographyQueryBuilder, Geography, java.util.UUID>
{

	@jakarta.validation.constraints.NotNull
	public GeographyQueryBuilder withParent(IGeography<?> parent, @Null String value)
	{
		if(parent == null)
			return this;
		JoinExpression joinExpression = new JoinExpression();
		GeographyXGeographyQueryBuilder builder =
				new GeographyXGeography()
						.builder()
						.inActiveRange(((Geography) parent).getEnterpriseID())
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

	public GeographyQueryBuilder withGeoNameID(String id)
	{
		where(Geography_.originalSourceSystemUniqueID, Equals, id);
		return this;
	}
}
