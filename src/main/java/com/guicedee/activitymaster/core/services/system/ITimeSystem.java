package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.entities.time.*;

import java.util.Date;

public interface ITimeSystem
{
	void loadTimeRange(int startYear, int endYear);

	Years getYear(Date date);

	Quarters getQuarter(Date date);

	Months getMonth(Date date);

	Weeks getWeek(Date date);

	Days getDay(Date date);

	void createTime();
}
