package com.guicedee.activitymaster.core.services.system;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public interface ITimeService
{
	String TimeSystemName = "";
	int getDayID(LocalDateTime dateTime);

	int getDayID(LocalDate dateTime);

	int getDayID(Date dateTime);

	int getHourID(LocalDateTime dateTime);

	int getHourID(LocalDate dateTime);

	int getHourID(Date dateTime);

	int getMinuteID(LocalDateTime dateTime);

	int getMinuteID(LocalDate dateTime);

	int getMinuteID(Date dateTime);
}
