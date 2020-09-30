package com.guicedee.activitymaster.core.db.entities.time.builders;

import com.guicedee.activitymaster.core.db.entities.time.DayParts;
import com.guicedee.activitymaster.core.db.entities.time.DayParts_;

import static com.entityassist.enumerations.Operand.*;

public class DayPartsQueryBuilder
		extends DefaultTimeQueryBuilder<DayPartsQueryBuilder, DayParts, Integer>
{
	public DayPartsQueryBuilder findByName(String value)
	{
		where(DayParts_.dayPartName, Equals, value);
		return this;
	}
	
}
