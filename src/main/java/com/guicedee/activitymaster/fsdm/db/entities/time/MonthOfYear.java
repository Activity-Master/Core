package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.MonthOfYearQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author MMagon
 * @since
 */

@Entity
@Table(name = "MonthOfYear",
       schema = "Time")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MonthOfYear
		extends BaseEntity<MonthOfYear, MonthOfYearQueryBuilder, Integer>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Basic(optional = false)
	@Column(name = "MonthOfYearID",
	        nullable = false)
	private Integer id;
	@Basic(optional = false)
	@Column(name = "MonthInYearNumber",
	        nullable = false)
	private int monthInYearNumber;
	@Basic(optional = false)
	@Column(name = "MonthOfYearName",
	        nullable = false,
	        length = 50)
	private String MonthOfYearName;
	
	@Basic(optional = false)
	@Column(name = "MonthOfYearShortName",
	        nullable = false,
	        length = 50)
	private String MonthOfYearShortName;
	
	@Basic(optional = false)
	@Column(name = "MonthOfYearAbbreviation",
	        nullable = false,
	        length = 50)
	private String MonthOfYearAbbreviation;
	@OneToMany(mappedBy = "MonthOfYearID")
	private List<Months> lUMonthsList;
	
	public MonthOfYear()
	{
	}
	
	public MonthOfYear(Integer id)
	{
		this.id = id;
	}
	
	public MonthOfYear(Integer id, int monthInYearNumber, String MonthOfYearName)
	{
		this.id = id;
		this.monthInYearNumber = monthInYearNumber;
		this.MonthOfYearName = MonthOfYearName;
	}
	
	@Override
	public Integer getId()
	{
		return id;
	}
	
	@Override
	public MonthOfYear setId(Integer id)
	{
		this.id = id;
		return this;
	}
	
	public int getMonthInYearNumber()
	{
		return monthInYearNumber;
	}
	
	public MonthOfYear setMonthInYearNumber(int monthInYearNumber)
	{
		this.monthInYearNumber = monthInYearNumber;
		return this;
	}
	
	public String getMonthOfYearName()
	{
		return MonthOfYearName;
	}
	
	public MonthOfYear setMonthOfYearName(String monthOfYearName)
	{
		MonthOfYearName = monthOfYearName;
		return this;
	}
	
	public String getMonthOfYearShortName()
	{
		return MonthOfYearShortName;
	}
	
	public MonthOfYear setMonthOfYearShortName(String monthOfYearShortName)
	{
		MonthOfYearShortName = monthOfYearShortName;
		return this;
	}
	
	public String getMonthOfYearAbbreviation()
	{
		return MonthOfYearAbbreviation;
	}
	
	public MonthOfYear setMonthOfYearAbbreviation(String monthOfYearAbbreviation)
	{
		MonthOfYearAbbreviation = monthOfYearAbbreviation;
		return this;
	}
	
	public List<Months> getlUMonthsList()
	{
		return lUMonthsList;
	}
	
	public MonthOfYear setlUMonthsList(List<Months> lUMonthsList)
	{
		this.lUMonthsList = lUMonthsList;
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
		MonthOfYear that = (MonthOfYear) o;
		return getMonthInYearNumber() == that.getMonthInYearNumber();
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getMonthInYearNumber());
	}
	
	@Override
	public String toString()
	{
		return getMonthOfYearName();
	}
	
}
