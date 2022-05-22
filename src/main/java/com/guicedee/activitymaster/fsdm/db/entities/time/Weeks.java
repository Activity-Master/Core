package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.WeeksQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @since @version @author MMagon
 */
@Entity
@Table(name = "Weeks",
       schema = "Time")
@XmlRootElement
public class Weeks
		extends BaseEntity<Weeks, WeeksQueryBuilder, Integer>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "WeekID",
	        nullable = false)
	private Integer id;
	@Basic(optional = false)
	@Column(name = "WeekOfMonth",
	        nullable = false)
	private int weekOfMonth;
	@Basic(optional = false)
	@Column(name = "WeekOfYear",
	        nullable = false)
	private int weekOfYear;
	@Basic(optional = false)
	@Column(name = "WeekShortDescription",
	        nullable = false,
	        length = 50)
	private String weekShortDescription;
	@Basic(optional = false)
	@Column(name = "WeekDescription",
	        nullable = false,
	        length = 50)
	private String weekDescription;
	@Basic(optional = false)
	@Column(name = "MonthID",
	        nullable = false)
	private int monthID;
	@Basic(optional = false)
	@Column(name = "QuarterID",
	        nullable = false)
	private int quarterID;
	@Basic(optional = false)
	@Column(name = "YearID",
	        nullable = false)
	private int yearID;
	@OneToMany(mappedBy = "weekID",
	           fetch = FetchType.LAZY)
	private List<Days> DaysList;
	
	public Weeks()
	{
	}
	
	@Override
	public Integer getId()
	{
		return id;
	}
	
	@Override
	public Weeks setId(Integer id)
	{
		this.id = id;
		return this;
	}
	
	public int getWeekOfMonth()
	{
		return weekOfMonth;
	}
	
	public Weeks setWeekOfMonth(int weekOfMonth)
	{
		this.weekOfMonth = weekOfMonth;
		return this;
	}
	
	public int getWeekOfYear()
	{
		return weekOfYear;
	}
	
	public Weeks setWeekOfYear(int weekOfYear)
	{
		this.weekOfYear = weekOfYear;
		return this;
	}
	
	public String getWeekShortDescription()
	{
		return weekShortDescription;
	}
	
	public Weeks setWeekShortDescription(String weekShortDescription)
	{
		this.weekShortDescription = weekShortDescription;
		return this;
	}
	
	public String getWeekDescription()
	{
		return weekDescription;
	}
	
	public Weeks setWeekDescription(String weekDescription)
	{
		this.weekDescription = weekDescription;
		return this;
	}
	
	public int getMonthID()
	{
		return monthID;
	}
	
	public Weeks setMonthID(int monthID)
	{
		this.monthID = monthID;
		return this;
	}
	
	public int getQuarterID()
	{
		return quarterID;
	}
	
	public Weeks setQuarterID(int quarterID)
	{
		this.quarterID = quarterID;
		return this;
	}
	
	public int getYearID()
	{
		return yearID;
	}
	
	public Weeks setYearID(int yearID)
	{
		this.yearID = yearID;
		return this;
	}
	
	public List<Days> getDaysList()
	{
		return DaysList;
	}
	
	public Weeks setDaysList(List<Days> daysList)
	{
		DaysList = daysList;
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
		Weeks weeks = (Weeks) o;
		return getId().equals(weeks.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public String toString()
	{
		return id.toString();
	}
	
}
