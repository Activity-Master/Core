package com.guicedee.activitymaster.core.implementations;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.db.entities.time.Hours;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.guicedee.activitymaster.core.services.types.DateTimeFormats.*;

@Singleton
public class TimeService<J extends TimeService<J>>
		implements com.guicedee.activitymaster.core.services.system.ITimeService
{
	@Override
	public int getDayID(LocalDateTime dateTime)
	{
		return Integer.parseInt(DayIDFormat.getDateFormatter()
		                                   .format(dateTime));
	}

	@Override
	public int getDayID(LocalDate dateTime)
	{
		return Integer.parseInt(DayIDFormat.getDateFormatter()
		                                   .format(dateTime));
	}

	@Override
	public int getDayID(Date dateTime)
	{
		return Integer.parseInt(DayIDFormat.getSimpleDateFormat()
		                                   .format(dateTime));
	}

	@Override
	public int getHourID(LocalDateTime dateTime)
	{
		return dateTime.getHour();
	}

	@Override
	public int getHourID(LocalDate dateTime)
	{
		return 0;
	}

	@Override
	public int getHourID(Date dateTime)
	{
		return dateTime.toInstant()
		               .get(ChronoField.MINUTE_OF_HOUR);
	}

	@Override
	public int getMinuteID(LocalDateTime dateTime)
	{
		return dateTime.getMinute();
	}

	@Override
	public int getMinuteID(LocalDate dateTime)
	{
		return 0;
	}

	@Override
	public int getMinuteID(Date dateTime)
	{
		return dateTime.toInstant()
		               .get(ChronoField.MINUTE_OF_HOUR);
	}
}
