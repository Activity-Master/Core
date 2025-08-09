package com.guicedee.activitymaster.fsdm;

//import com.google.inject.persist.Transactional;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.ITimeService;
import com.guicedee.activitymaster.fsdm.db.entities.time.DayParts;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.DateTimeFormats.*;

@Singleton
public class TimeService<J extends TimeService<J>>
		implements ITimeService
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
	//@Transactional()
	//@CacheResult(cacheName = "TimeDayParts")
	public Uni<DayParts> getDayPart(Mutiny.StatelessSession session, int hour, int minute)
	{
		if (hour <= 3)
		{
			if (hour == 3 && minute <= 30)
			{
				return new DayParts().builder(session)
				                     .findByName("Midnight Morning")
				                     .get();
			}
			else if (hour < 3)
			{
				return new DayParts().builder(session)
				                     .findByName("Midnight Morning")
				                     .get();
			}
			else
			{
				return new DayParts().builder(session)
				                     .findByName("Early Morning")
				                     .get();
			}
		}
		else if (hour <= 6)
		{
			if (hour == 6 && minute <= 30)
			{
				return new DayParts().builder(session)
				                     .findByName("Early Morning")
				                     .get();
			}
			else if (hour < 6)
			{
				return new DayParts().builder(session)
				                     .findByName("Early Morning")
				                     .get();
			}
			else
			{
				return new DayParts().builder(session)
				                     .findByName("Morning")
				                     .get();
			}
		}
		else if (hour <= 9)
		{
			return new DayParts().builder(session)
			                     .findByName("Morning")
			                     .get();
		}
		else if (hour <= 10)
		{
			if (hour == 10 && minute <= 30)
			{
				return new DayParts().builder(session)
				                     .findByName("Late Morning")
				                     .get();
			}
			else if (hour < 10)
			{
				return new DayParts().builder(session)
				                     .findByName("Late Morning")
				                     .get();
			}
			else
			{
				return new DayParts().builder(session)
				                     .findByName("Early Afternoon")
				                     .get();
				
			}
		}
		else if (hour < 12)
		{
			return new DayParts().builder(session)
			                     .findByName("Early Afternoon")
			                     .get();
		}
		else if (hour <= 14)
		{
			return new DayParts().builder(session)
			                     .findByName("Afternoon")
			                     .get();
		}
		else if (hour <= 15)
		{
			if (hour == 15 && minute <= 30)
			{
				return new DayParts().builder(session)
				                     .findByName("Late Afternoon")
				                     .get();
			}
			else if (hour < 10)
			{
				return new DayParts().builder(session)
				                     .findByName("Late Afternoon")
				                     .get();
			}
			else
			{
				return new DayParts().builder(session)
				                     .findByName("Early Evening")
				                     .get();
			}
		}
		else if (hour <= 16)
		{
			if (hour == 16 && minute <= 30)
			{
				return new DayParts().builder(session)
				                     .findByName("Early Evening")
				                     .get();
			}
			else if (hour < 16)
			{
				return new DayParts().builder(session)
				                     .findByName("Early Evening")
				                     .get();
			}
			else
			{
				return new DayParts().builder(session)
				                     .findByName("Evening")
				                     .get();
			}
		}
		else if (hour <= 19)
		{
			return new DayParts().builder(session)
			                     .findByName("Evening")
			                     .get();
		}
		else if (hour <= 21)
		{
			if (hour == 21 && minute <= 30)
			{
				return new DayParts().builder(session)
				                     .findByName("Late Evening")
				                     .get();
			}
			else if (hour < 21)
			{
				return new DayParts().builder(session)
				                     .findByName("Late Evening")
				                     .get();
			}
			else
			{
				return new DayParts().builder(session)
				                     .findByName("Midnight Evening")
				                     .get();
			}
		}
		else if (hour <= 24)
		{
			return new DayParts().builder(session)
			                     .findByName("Midnight Evening")
			                     .get();
		}
		
		else
		{
			return new DayParts().builder(session)
			                     .findByName("Midnight Morning")
			                     .get();
		}
	}
	
}
