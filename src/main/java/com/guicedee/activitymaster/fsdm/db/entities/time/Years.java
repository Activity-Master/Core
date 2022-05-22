package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.YearsQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author MMagon
 * @since
 */
@Entity
@Table(name = "Years",
       schema = "Time")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Years
		extends BaseEntity<Years, YearsQueryBuilder, Short>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "YearID",
	        nullable = false)
	private Short id;
	@Basic(optional = false)
	@Column(name = "YYName",
	        nullable = false,
	        length = 2)
	private String yYName;
	@Basic(optional = false)
	@Column(name = "YYYName",
	        nullable = false,
	        length = 3)
	private String yYYName;
	@Basic(optional = false)
	@Column(name = "YearFullName",
	        nullable = false,
	        length = 250)
	private String yearFullName;
	@Basic(optional = false)
	@Column(name = "YearName",
	        nullable = false,
	        length = 10)
	private String yearName;
	@Basic(optional = false)
	@Column(name = "LeapYearFlag",
	        nullable = false)
	private short leapYearFlag;
	@Basic(optional = false)
	@Column(name = "LastYearID",
	        nullable = false)
	private short lastYearID;
	@OneToMany(mappedBy = "yearID",
	           fetch = FetchType.LAZY)
	private List<Quarters> QuartersList;
	@Basic(optional = false)
	@Column(name = "Century",
	        nullable = false)
	private Short century;
	
	public Years()
	{
	}
	
	@Override
	public Short getId()
	{
		return id;
	}
	
	@Override
	public Years setId(Short id)
	{
		this.id = id;
		return this;
	}
	
	public String getyYName()
	{
		return yYName;
	}
	
	public Years setyYName(String yYName)
	{
		this.yYName = yYName;
		return this;
	}
	
	public String getyYYName()
	{
		return yYYName;
	}
	
	public Years setyYYName(String yYYName)
	{
		this.yYYName = yYYName;
		return this;
	}
	
	public String getYearFullName()
	{
		return yearFullName;
	}
	
	public Years setYearFullName(String yearFullName)
	{
		this.yearFullName = yearFullName;
		return this;
	}
	
	public String getYearName()
	{
		return yearName;
	}
	
	public Years setYearName(String yearName)
	{
		this.yearName = yearName;
		return this;
	}
	
	public short getLeapYearFlag()
	{
		return leapYearFlag;
	}
	
	public Years setLeapYearFlag(short leapYearFlag)
	{
		this.leapYearFlag = leapYearFlag;
		return this;
	}
	
	public short getLastYearID()
	{
		return lastYearID;
	}
	
	public Years setLastYearID(short lastYearID)
	{
		this.lastYearID = lastYearID;
		return this;
	}
	
	public List<Quarters> getQuartersList()
	{
		return QuartersList;
	}
	
	public Years setQuartersList(List<Quarters> quartersList)
	{
		QuartersList = quartersList;
		return this;
	}
	
	public Short getCentury()
	{
		return century;
	}
	
	public Years setCentury(Short century)
	{
		this.century = century;
		return this;
	}
	
	@Override
	public String toString()
	{
		return "" + id;
	}
}
