package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.QuartersQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author MMagon
 * @since
 */

@Entity
@Table(name = "Quarters",
       schema = "Time")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Quarters
		extends BaseEntity<Quarters, QuartersQueryBuilder, Integer>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "QuarterID",
	        nullable = false)
	private Integer id;
	@Basic(optional = false)
	@Column(name = "QuarterDescription",
	        nullable = false,
	        length = 50)
	private String quarterDescription;
	@Basic(optional = false)
	@Column(name = "QuarterInYear",
	        nullable = false)
	private int quarterInYear;
	@Basic(optional = false)
	@Column(name = "LastQuarterID",
	        nullable = false)
	private short lastQuarterID;
	@Basic(optional = false)
	@Column(name = "LastYearID",
	        nullable = false)
	private short lastYearID;
	@Basic(optional = false)
	@Column(name = "QuarterGraphDescription",
	        nullable = false,
	        length = 50)
	private String quarterGraphDescription;
	@Basic(optional = false)
	@Column(name = "QuarterGridDescription",
	        nullable = false,
	        length = 50)
	private String quarterGridDescription;
	@Basic(optional = false)
	@Column(name = "QuarterSmallDescription",
	        nullable = false,
	        length = 50)
	private String quarterSmallDescription;
	@Basic(optional = false)
	@Column(name = "QuarterYearDescription",
	        nullable = false,
	        length = 50)
	private String quarterYearDescription;
	@Basic(optional = false)
	@Column(name = "QuarterYYMMDescription",
	        nullable = false,
	        length = 50)
	private String quarterYYMMDescription;
	@Basic(optional = false)
	@Column(name = "QuarterQQMMDescription",
	        nullable = false,
	        length = 50)
	private String quarterQQMMDescription;
	@OneToMany(fetch = FetchType.LAZY)
	private List<Months> lUMonthsList = new ArrayList<>();
	@JoinColumn(name = "YearID",
	            referencedColumnName = "YearID",
	            nullable = false)
	@ManyToOne(optional = false)
	private Years yearID;
	
	public Quarters()
	{
	}
	
	public Quarters(Integer id)
	{
		this.id = id;
	}
	
	@Override
	public Integer getId()
	{
		return id;
	}
	
	@Override
	public Quarters setId(Integer id)
	{
		this.id = id;
		return this;
	}
	
	public String getQuarterDescription()
	{
		return quarterDescription;
	}
	
	public Quarters setQuarterDescription(String quarterDescription)
	{
		this.quarterDescription = quarterDescription;
		return this;
	}
	
	public int getQuarterInYear()
	{
		return quarterInYear;
	}
	
	public Quarters setQuarterInYear(int quarterInYear)
	{
		this.quarterInYear = quarterInYear;
		return this;
	}
	
	public short getLastQuarterID()
	{
		return lastQuarterID;
	}
	
	public Quarters setLastQuarterID(short lastQuarterID)
	{
		this.lastQuarterID = lastQuarterID;
		return this;
	}
	
	public short getLastYearID()
	{
		return lastYearID;
	}
	
	public Quarters setLastYearID(short lastYearID)
	{
		this.lastYearID = lastYearID;
		return this;
	}
	
	public String getQuarterGraphDescription()
	{
		return quarterGraphDescription;
	}
	
	public Quarters setQuarterGraphDescription(String quarterGraphDescription)
	{
		this.quarterGraphDescription = quarterGraphDescription;
		return this;
	}
	
	public String getQuarterGridDescription()
	{
		return quarterGridDescription;
	}
	
	public Quarters setQuarterGridDescription(String quarterGridDescription)
	{
		this.quarterGridDescription = quarterGridDescription;
		return this;
	}
	
	public String getQuarterSmallDescription()
	{
		return quarterSmallDescription;
	}
	
	public Quarters setQuarterSmallDescription(String quarterSmallDescription)
	{
		this.quarterSmallDescription = quarterSmallDescription;
		return this;
	}
	
	public String getQuarterYearDescription()
	{
		return quarterYearDescription;
	}
	
	public Quarters setQuarterYearDescription(String quarterYearDescription)
	{
		this.quarterYearDescription = quarterYearDescription;
		return this;
	}
	
	public String getQuarterYYMMDescription()
	{
		return quarterYYMMDescription;
	}
	
	public Quarters setQuarterYYMMDescription(String quarterYYMMDescription)
	{
		this.quarterYYMMDescription = quarterYYMMDescription;
		return this;
	}
	
	public String getQuarterQQMMDescription()
	{
		return quarterQQMMDescription;
	}
	
	public Quarters setQuarterQQMMDescription(String quarterQQMMDescription)
	{
		this.quarterQQMMDescription = quarterQQMMDescription;
		return this;
	}
	
	public List<Months> getlUMonthsList()
	{
		return lUMonthsList;
	}
	
	public Quarters setlUMonthsList(List<Months> lUMonthsList)
	{
		this.lUMonthsList = lUMonthsList;
		return this;
	}
	
	public Years getYearID()
	{
		return yearID;
	}
	
	public Quarters setYearID(Years yearID)
	{
		this.yearID = yearID;
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
		Quarters quarters = (Quarters) o;
		return getId().equals(quarters.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public String toString()
	{
		return "" + id;
	}
	
}
