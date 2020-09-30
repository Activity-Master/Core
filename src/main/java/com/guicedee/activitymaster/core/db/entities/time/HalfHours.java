/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guicedee.activitymaster.core.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.core.db.entities.time.builders.HalfHoursQueryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author mmagon
 */
@Entity
@Table(name = "HalfHours",
       schema = "Time")
@XmlRootElement
@Getter
@Setter
@Accessors(chain = true)
public class HalfHours
		extends BaseEntity<HalfHours, HalfHoursQueryBuilder, TimePK>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private TimePK id;
	
	@Basic(optional = false)
	@Column(name = "TwelveHourClockDesc",
	        nullable = false,
	        length = 10)
	private String twelveHourClockDesc;
	@Basic(optional = false)
	@Column(name = "TwentyFourHourClockDesc",
	        nullable = false,
	        length = 10)
	private String twentyFourHourClockDesc;
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
	@Column(name = "PreviousHalfHourMinuteID",
	        nullable = false)
	private int previousHalfHourMinuteID;
	@JoinColumn(name = "HourID",
	            referencedColumnName = "HourID",
	            nullable = false,
	            insertable = false,
	            updatable = false)
	@ManyToOne(optional = false)
	private Hours Hours;
	
	public HalfHours()
	{
	}
	
	public HalfHours(TimePK id)
	{
		this.id = id;
	}
	
	public HalfHours(TimePK id, String hourClockDesc, String twentyFourHourClockDesc, String amPmDesc, int previousHourID, int previousHalfHourMinuteID)
	{
		this.id = id;
		twelveHourClockDesc = hourClockDesc;
		this.twentyFourHourClockDesc = twentyFourHourClockDesc;
		this.amPmDesc = amPmDesc;
		this.previousHourID = previousHourID;
		this.previousHalfHourMinuteID = previousHalfHourMinuteID;
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
		HalfHours halfHours = (HalfHours) o;
		return id.equals(halfHours.id);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(id);
	}
	
	@Override
	public String toString()
	{
		return "timelord.entities.HalfHours[ lUHalfHoursPK=" + id + " ]";
	}
	
}
