/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.DayPartsQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @author GedMarc
 */
@Entity
@Table(name = "DayParts",
       schema = "Time")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DayParts extends BaseEntity<DayParts, DayPartsQueryBuilder, Integer> implements Serializable

{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Basic(optional = false)
	@Column(nullable = false,
	        name = "dayPartID")
	private Integer id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 100)
	@Column(nullable = false,
	        length = 100)
	private String dayPartName;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 100)
	@Column(nullable = false,
	        length = 100)
	private String dayPartDescription;
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false)
	private int dayPartSortOrder;
	@OneToMany(cascade = CascadeType.ALL,
	           mappedBy = "dayPartID",
	           fetch = FetchType.LAZY)
	private Set<HalfHourDayParts> halfHourDayPartsSet;
	
	public DayParts()
	{
	}
	
	@Override
	public Integer getId()
	{
		return id;
	}
	
	@Override
	public DayParts setId(Integer id)
	{
		this.id = id;
		return this;
	}
	
	public String getDayPartName()
	{
		return dayPartName;
	}
	
	public DayParts setDayPartName(String dayPartName)
	{
		this.dayPartName = dayPartName;
		return this;
	}
	
	public String getDayPartDescription()
	{
		return dayPartDescription;
	}
	
	public DayParts setDayPartDescription(String dayPartDescription)
	{
		this.dayPartDescription = dayPartDescription;
		return this;
	}
	
	public int getDayPartSortOrder()
	{
		return dayPartSortOrder;
	}
	
	public DayParts setDayPartSortOrder(int dayPartSortOrder)
	{
		this.dayPartSortOrder = dayPartSortOrder;
		return this;
	}
	
	public Set<HalfHourDayParts> getHalfHourDayPartsSet()
	{
		return halfHourDayPartsSet;
	}
	
	public DayParts setHalfHourDayPartsSet(Set<HalfHourDayParts> halfHourDayPartsSet)
	{
		this.halfHourDayPartsSet = halfHourDayPartsSet;
		return this;
	}
}
