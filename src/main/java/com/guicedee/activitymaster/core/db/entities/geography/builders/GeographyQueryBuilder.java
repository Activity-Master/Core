package com.guicedee.activitymaster.core.db.entities.geography.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.geography.*;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IGeography;

import javax.persistence.criteria.JoinType;
import javax.validation.constraints.Null;

import static com.entityassist.enumerations.Operand.*;

public class GeographyQueryBuilder
		extends QueryBuilderSCDNameDescription<GeographyQueryBuilder, Geography, Long, GeographySecurityToken>
{

	@javax.validation.constraints.NotNull
	public GeographyQueryBuilder withClassification(IClassification<?> classification)
	{
		where(Geography_.classificationID, Equals, (Classification) classification);
		return this;
	}

	@javax.validation.constraints.NotNull
	public GeographyQueryBuilder withClassification(IClassification<?> classification, String value)
	{
		JoinExpression joinExpression = new JoinExpression();
		GeographyXClassificationQueryBuilder builder =
				new GeographyXClassification()
						.builder()
						.inActiveRange(classification.getEnterpriseID())
						.inDateRange()
						.where(GeographyXClassification_.classificationID, Equals, (Classification) classification);
		if (!Strings.isNullOrEmpty(value))
		{
			builder.where(GeographyXClassification_.value, Equals, value);
		}

		join(Geography_.classifications,
		     builder,
		     JoinType.INNER, joinExpression);
		return this;
	}

	@javax.validation.constraints.NotNull
	public GeographyQueryBuilder withParent(IGeography<?> parent, @Null String value)
	{
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
}
