/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.HoursQueryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mmagon
 */
@Entity
@Table(name = "Hours",
		schema = "Time")
@XmlRootElement
@Getter
@Setter
@Accessors(chain = true)
public class Hours
		extends BaseEntity<Hours, HoursQueryBuilder, Integer>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@OneToMany(mappedBy = "Hours")
	private List<Time> TimeList;

	@Id
	@Basic(optional = false)
	@Column(name = "HourID",
			nullable = false)
	private Integer id;
	@Basic(optional = false)
	@Column(name = "TwelveHourClockDesc",
			nullable = false,
			length = 10)
	private String twelveHour;
	@Basic(optional = false)
	@Column(name = "TwentyFourHourClockDesc",
			nullable = false,
			length = 10)
	private String twentyFourHour;
	@Basic(optional = false)
	@Column(name = "AmPmDesc",
			nullable = false,
			length = 5)
	private String amPmDesc;
	@Basic(optional = false)
	@Column(name = "PreviousHourID",
			nullable = false)
	private int previousHourID;
	@OneToMany(mappedBy = "Hours")
	private List<HalfHours> lUHalfHoursList;

	public Hours()
	{
	}

	public Hours(Integer id)
	{
		this.id = id;
	}

	public Hours(Integer id, String twelveHour, String twentyFourHour, String amPmDesc, int previousHourID)
	{
		this.id = id;
		this.twelveHour = twelveHour;
		this.twentyFourHour = twentyFourHour;
		this.amPmDesc = amPmDesc;
		this.previousHourID = previousHourID;
	}

	@XmlTransient
	public List<HalfHours> getLUHalfHoursList()
	{
		if (lUHalfHoursList == null)
		{
			lUHalfHoursList = new ArrayList<>();
		}
		return lUHalfHoursList;
	}

	@Override
	public String toString()
	{
		return twentyFourHour;
	}

	public List<Time> getTimeList()
	{
		if (TimeList == null)
		{
			TimeList = new ArrayList<>();
		}
		return TimeList;
	}

}
