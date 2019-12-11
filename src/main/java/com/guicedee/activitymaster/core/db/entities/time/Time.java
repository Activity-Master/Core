/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guicedee.activitymaster.core.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.core.db.entities.time.builders.TimeQueryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author MMagon
 */
@Entity
@Table(name = "Time",
		schema = "Time")
@XmlRootElement
@Getter
@Setter
@Accessors(chain = true)
public class Time
		extends BaseEntity<Time, TimeQueryBuilder, TimePK>
		implements Serializable
{
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

	private static final long serialVersionUID = 1L;

	public Time()
	{
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
