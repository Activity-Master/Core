/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.HalfHourDayPartsQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author GedMarc
 */
@Entity
@Table(name = "HalfHourDayParts",
       schema = "Time",
       uniqueConstraints = {
		       @UniqueConstraint(columnNames = {"HourID", "MinuteID"})})
@XmlRootElement
public class HalfHourDayParts
		extends BaseEntity<HalfHourDayParts, HalfHourDayPartsQueryBuilder, Integer> implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(nullable = false,
	        name = "halfHourDayPartID")
	private Integer id;
	@JoinColumn(name = "DayPartID",
	            referencedColumnName = "DayPartID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private DayParts dayPartID;
	@Basic(optional = false)
	@Column(nullable = false)
	private Integer hourID;
	@Basic(optional = false)
	@Column(nullable = false)
	private Integer minuteID;
	
	@Override
	public Integer getId()
	{
		return id;
	}
	
	@Override
	public HalfHourDayParts setId(Integer id)
	{
		this.id = id;
		return this;
	}
	
	public DayParts getDayPartID()
	{
		return dayPartID;
	}
	
	public HalfHourDayParts setDayPartID(DayParts dayPartID)
	{
		this.dayPartID = dayPartID;
		return this;
	}
	
	public Integer getHourID()
	{
		return hourID;
	}
	
	public HalfHourDayParts setHourID(Integer hourID)
	{
		this.hourID = hourID;
		return this;
	}
	
	public Integer getMinuteID()
	{
		return minuteID;
	}
	
	public HalfHourDayParts setMinuteID(Integer minuteID)
	{
		this.minuteID = minuteID;
		return this;
	}
}
