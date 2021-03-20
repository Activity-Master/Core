package com.guicedee.activitymaster.core;

import com.guicedee.activitymaster.core.db.entities.time.DayParts;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Date;

import static com.guicedee.activitymaster.client.services.classifications.types.DateTimeFormats.*;


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
	
	@CacheResult(cacheName = "TimeDayParts")
	public DayParts getDayPart(@CacheKey int hour, @CacheKey int minute)
	{
		if (hour <= 3)
		{
			if (hour == 3 && minute <= 30)
			{
				return new DayParts().builder()
				                     .findByName("Midnight Morning")
				                     .get()
				                     .orElseThrow();
			}
			else if (hour < 3)
			{
				return new DayParts().builder()
				                     .findByName("Midnight Morning")
				                     .get()
				                     .orElseThrow();
			}
			else
			{
				return new DayParts().builder()
				                     .findByName("Early Morning")
				                     .get()
				                     .orElseThrow();
			}
		}
		else if (hour <= 6)
		{
			if (hour == 6 && minute <= 30)
			{
				return new DayParts().builder()
				                     .findByName("Early Morning")
				                     .get()
				                     .orElseThrow();
			}
			else if (hour < 6)
			{
				return new DayParts().builder()
				                     .findByName("Early Morning")
				                     .get()
				                     .orElseThrow();
			}
			else
			{
				return new DayParts().builder()
				                     .findByName("Morning")
				                     .get()
				                     .orElseThrow();
			}
		}
		else if (hour <= 9)
		{
			return new DayParts().builder()
			                     .findByName("Morning")
			                     .get()
			                     .orElseThrow();
		}
		else if (hour <= 10)
		{
			if (hour == 10 && minute <= 30)
			{
				return new DayParts().builder()
				                     .findByName("Late Morning")
				                     .get()
				                     .orElseThrow();
			}
			else if (hour < 10)
			{
				return new DayParts().builder()
				                     .findByName("Late Morning")
				                     .get()
				                     .orElseThrow();
			}
			else
			{
				return new DayParts().builder()
				                     .findByName("Early Afternoon")
				                     .get()
				                     .orElseThrow();
				
			}
		}
		else if (hour < 12)
		{
			return new DayParts().builder()
			                     .findByName("Early Afternoon")
			                     .get()
			                     .orElseThrow();
		}
		else if (hour <= 14)
		{
			return new DayParts().builder()
			                     .findByName("Afternoon")
			                     .get()
			                     .orElseThrow();
		}
		else if (hour <= 15)
		{
			if (hour == 15 && minute <= 30)
			{
				return new DayParts().builder()
				                     .findByName("Late Afternoon")
				                     .get()
				                     .orElseThrow();
			}
			else if (hour < 10)
			{
				return new DayParts().builder()
				                     .findByName("Late Afternoon")
				                     .get()
				                     .orElseThrow();
			}
			else
			{
				return new DayParts().builder()
				                     .findByName("Early Evening")
				                     .get()
				                     .orElseThrow();
			}
		}
		else if (hour <= 16)
		{
			if (hour == 16 && minute <= 30)
			{
				return new DayParts().builder()
				                     .findByName("Early Evening")
				                     .get()
				                     .orElseThrow();
			}
			else if (hour < 16)
			{
				return new DayParts().builder()
				                     .findByName("Early Evening")
				                     .get()
				                     .orElseThrow();
			}
			else
			{
				return new DayParts().builder()
				                     .findByName("Evening")
				                     .get()
				                     .orElseThrow();
			}
		}
		else if (hour <= 19)
		{
			return new DayParts().builder()
			                     .findByName("Evening")
			                     .get()
			                     .orElseThrow();
		}
		else if (hour <= 21)
		{
			if (hour == 21 && minute <= 30)
			{
				return new DayParts().builder()
				                     .findByName("Late Evening")
				                     .get()
				                     .orElseThrow();
			}
			else if (hour < 21)
			{
				return new DayParts().builder()
				                     .findByName("Late Evening")
				                     .get()
				                     .orElseThrow();
			}
			else
			{
				return new DayParts().builder()
				                     .findByName("Midnight Evening")
				                     .get()
				                     .orElseThrow();
			}
		}
		else if (hour <= 24)
		{
			return new DayParts().builder()
			                     .findByName("Midnight Evening")
			                     .get()
			                     .orElseThrow();
		}
		
		else
		{
			return new DayParts().builder()
			                     .findByName("Midnight Morning")
			                     .get()
			                     .orElseThrow();
		}
	}
	
}
