/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.TimeQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author MMagon
 */
@Entity
@Table(name = "Time",
       schema = "Time")
@XmlRootElement
public class Time
		extends BaseEntity<Time, TimeQueryBuilder, TimePK>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected TimePK id;
	@Basic(optional = false)
	@Column(name = "TwelveHourClockDesc",
	        nullable = false,
	        length = 10)
	private String twelveHoursDesc;
	@Basic(optional = false)
	@Column(name = "TwentyFourHourClockDesc",
	        nullable = false,
	        length = 10)
	private String twentyFourHoursDesc;
	@Basic(optional = false)
	@Column(name = "AmPmDesc",
	        nullable = false,
	        length = 5)
	private String amPmDesc;
	@Basic(optional = false)
	@Column(name = "PreviousHourID",
	        nullable = false)
	private int previousHourID;
	@Basic(optional = false)
	@Column(name = "PreviousMinuteID",
	        nullable = false)
	private int previousMinuteID;
	@JoinColumn(name = "HourID",
	            referencedColumnName = "HourID",
	            nullable = false,
	            insertable = false,
	            updatable = false)
	@ManyToOne(optional = false)
	private Hours Hours;
	
	public Time()
	{
	}
	
	@Override
	public TimePK getId()
	{
		return id;
	}
	
	@Override
	public Time setId(TimePK id)
	{
		this.id = id;
		return this;
	}
	
	public String getTwelveHoursDesc()
	{
		return twelveHoursDesc;
	}
	
	public Time setTwelveHoursDesc(String twelveHoursDesc)
	{
		this.twelveHoursDesc = twelveHoursDesc;
		return this;
	}
	
	public String getTwentyFourHoursDesc()
	{
		return twentyFourHoursDesc;
	}
	
	public Time setTwentyFourHoursDesc(String twentyFourHoursDesc)
	{
		this.twentyFourHoursDesc = twentyFourHoursDesc;
		return this;
	}
	
	public String getAmPmDesc()
	{
		return amPmDesc;
	}
	
	public Time setAmPmDesc(String amPmDesc)
	{
		this.amPmDesc = amPmDesc;
		return this;
	}
	
	public int getPreviousHourID()
	{
		return previousHourID;
	}
	
	public Time setPreviousHourID(int previousHourID)
	{
		this.previousHourID = previousHourID;
		return this;
	}
	
	public int getPreviousMinuteID()
	{
		return previousMinuteID;
	}
	
	public Time setPreviousMinuteID(int previousMinuteID)
	{
		this.previousMinuteID = previousMinuteID;
		return this;
	}
	
	public com.guicedee.activitymaster.fsdm.db.entities.time.Hours getHours()
	{
		return Hours;
	}
	
	public Time setHours(com.guicedee.activitymaster.fsdm.db.entities.time.Hours hours)
	{
		Hours = hours;
		return this;
	}
	
	public Time(TimePK id)
	{
		this.id = id;
	}
	
	@Override
	public String toString()
	{
		return id + "";
	}
}
