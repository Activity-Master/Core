package com.guicedee.activitymaster.core.db.entities.time.builders;

import com.guicedee.activitymaster.core.db.entities.time.PublicHolidays;

public class PublicHolidaysQueryBuilder
		extends DefaultTimeQueryBuilder<PublicHolidaysQueryBuilder, PublicHolidays, Integer>
{
	@Override
	protected boolean isIdGenerated()
	{
		return true;
	}
}
