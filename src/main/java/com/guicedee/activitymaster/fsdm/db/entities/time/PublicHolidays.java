package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.PublicHolidaysQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author GedMarc
 * @since 25 Nov 2016
 */
@Entity
@Table(schema = "Time",
       name = "PublicHolidays")
@XmlRootElement
public class PublicHolidays
		extends BaseEntity<PublicHolidays, PublicHolidaysQueryBuilder, Integer>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(nullable = false,
	        name = "PublicHolidayID")
	
	private Integer id;
	@Basic(optional = false)
	@Column(nullable = false,
	        name = "DayID")
	private int dayID;
	@Basic(optional = false)
	@Column(nullable = false,
	        length = 250,
	        name = "PublicHolidayName")
	private String publicHolidayName;
	@Basic(optional = false)
	@Column(nullable = false,
	        length = 250,
	        name = "PublicHolidayType")
	private String publicHolidayType;
	
	public PublicHolidays()
	{
	
	}
	
	public PublicHolidays(Integer id)
	{
		this.id = id;
	}
	
	public PublicHolidays(Integer id, int dayID, String publicHolidayName, String publicHolidayType)
	{
		this.id = id;
		this.dayID = dayID;
		this.publicHolidayName = publicHolidayName;
		this.publicHolidayType = publicHolidayType;
	}
	
	@Override
	public Integer getId()
	{
		return id;
	}
	
	@Override
	public PublicHolidays setId(Integer id)
	{
		this.id = id;
		return this;
	}
	
	public int getDayID()
	{
		return dayID;
	}
	
	public PublicHolidays setDayID(int dayID)
	{
		this.dayID = dayID;
		return this;
	}
	
	public String getPublicHolidayName()
	{
		return publicHolidayName;
	}
	
	public PublicHolidays setPublicHolidayName(String publicHolidayName)
	{
		this.publicHolidayName = publicHolidayName;
		return this;
	}
	
	public String getPublicHolidayType()
	{
		return publicHolidayType;
	}
	
	public PublicHolidays setPublicHolidayType(String publicHolidayType)
	{
		this.publicHolidayType = publicHolidayType;
		return this;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		PublicHolidays that = (PublicHolidays) o;
		return getDayID() == that.getDayID();
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getDayID());
	}
	
	@Override
	public String toString()
	{
		return dayID + " - " + publicHolidayName + " - " + publicHolidayType;
	}
}
