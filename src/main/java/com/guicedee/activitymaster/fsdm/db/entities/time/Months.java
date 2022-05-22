package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.MonthsQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author MMagon
 * @since
 */

@Entity
@Table(name = "Months",
       schema = "Time")
@XmlRootElement
public class Months
		extends BaseEntity<Months, MonthsQueryBuilder, Integer>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "MonthID",
	        nullable = false)
	private Integer id;
	@Basic(optional = false)
	@Column(name = "MonthDescription",
	        nullable = false,
	        length = 50)
	private String monthDescription;
	@Basic(optional = false)
	@Column(name = "YearID",
	        nullable = false)
	private int yearID;
	@Basic(optional = false)
	@Column(name = "MonthDayDuration",
	        nullable = false)
	private short monthDayDuration;
	@Basic(optional = false)
	@Column(name = "LastMonthID",
	        nullable = false)
	private int lastMonthID;
	@Basic(optional = false)
	@Column(name = "LastQuarterID",
	        nullable = false)
	private int lastQuarterID;
	@Basic(optional = false)
	@Column(name = "LastYearID",
	        nullable = false)
	private int lastYearID;
	@Basic(optional = false)
	@Column(name = "MonthShortDescription",
	        nullable = false,
	        length = 50)
	private String monthShortDescription;
	@Basic(optional = false)
	@Column(name = "MonthYYDescription",
	        nullable = false,
	        length = 50)
	private String monthYYDescription;
	@Basic(optional = false)
	@Column(name = "MonthMMMYYDescription",
	        nullable = false,
	        length = 50)
	private String monthMMMYYDescription;
	@Basic(optional = false)
	@Column(name = "MonthMMYYYYDescription",
	        nullable = false,
	        length = 50)
	private String monthMMYYYYDescription;
	@Basic(optional = false)
	@Column(name = "MonthNameYYYYDescription",
	        nullable = false,
	        length = 50)
	private String monthNameYYYYDescription;
	@OneToMany(cascade =
			           {
					           CascadeType.MERGE, CascadeType.PERSIST
			           },
	           mappedBy = "monthID",
	           fetch = FetchType.LAZY)
	private List<Days> DaysList;
	@JoinColumn(name = "QuarterID",
	            referencedColumnName = "QuarterID",
	            nullable = false)
	@ManyToOne(optional = false)
	private Quarters quarterID;
	@JoinColumn(name = "MonthOfYearID",
	            referencedColumnName = "MonthOfYearID",
	            nullable = false)
	@ManyToOne(optional = false)
	private MonthOfYear MonthOfYearID;
	
	public Months()
	{
	}
	
	public Months(Integer id)
	{
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (getClass() != o.getClass())
		{
			return false;
		}
		Months months = (Months) o;
		return Objects.equals(getId(), months.getId());
	}
	
	@Override
	public Integer getId()
	{
		return id;
	}
	
	@Override
	public Months setId(Integer id)
	{
		this.id = id;
		return this;
	}
	
	public String getMonthDescription()
	{
		return monthDescription;
	}
	
	public Months setMonthDescription(String monthDescription)
	{
		this.monthDescription = monthDescription;
		return this;
	}
	
	public int getYearID()
	{
		return yearID;
	}
	
	public Months setYearID(int yearID)
	{
		this.yearID = yearID;
		return this;
	}
	
	public short getMonthDayDuration()
	{
		return monthDayDuration;
	}
	
	public Months setMonthDayDuration(short monthDayDuration)
	{
		this.monthDayDuration = monthDayDuration;
		return this;
	}
	
	public int getLastMonthID()
	{
		return lastMonthID;
	}
	
	public Months setLastMonthID(int lastMonthID)
	{
		this.lastMonthID = lastMonthID;
		return this;
	}
	
	public int getLastQuarterID()
	{
		return lastQuarterID;
	}
	
	public Months setLastQuarterID(int lastQuarterID)
	{
		this.lastQuarterID = lastQuarterID;
		return this;
	}
	
	public int getLastYearID()
	{
		return lastYearID;
	}
	
	public Months setLastYearID(int lastYearID)
	{
		this.lastYearID = lastYearID;
		return this;
	}
	
	public String getMonthShortDescription()
	{
		return monthShortDescription;
	}
	
	public Months setMonthShortDescription(String monthShortDescription)
	{
		this.monthShortDescription = monthShortDescription;
		return this;
	}
	
	public String getMonthYYDescription()
	{
		return monthYYDescription;
	}
	
	public Months setMonthYYDescription(String monthYYDescription)
	{
		this.monthYYDescription = monthYYDescription;
		return this;
	}
	
	public String getMonthMMMYYDescription()
	{
		return monthMMMYYDescription;
	}
	
	public Months setMonthMMMYYDescription(String monthMMMYYDescription)
	{
		this.monthMMMYYDescription = monthMMMYYDescription;
		return this;
	}
	
	public String getMonthMMYYYYDescription()
	{
		return monthMMYYYYDescription;
	}
	
	public Months setMonthMMYYYYDescription(String monthMMYYYYDescription)
	{
		this.monthMMYYYYDescription = monthMMYYYYDescription;
		return this;
	}
	
	public String getMonthNameYYYYDescription()
	{
		return monthNameYYYYDescription;
	}
	
	public Months setMonthNameYYYYDescription(String monthNameYYYYDescription)
	{
		this.monthNameYYYYDescription = monthNameYYYYDescription;
		return this;
	}
	
	public List<Days> getDaysList()
	{
		return DaysList;
	}
	
	public Months setDaysList(List<Days> daysList)
	{
		DaysList = daysList;
		return this;
	}
	
	public Quarters getQuarterID()
	{
		return quarterID;
	}
	
	public Months setQuarterID(Quarters quarterID)
	{
		this.quarterID = quarterID;
		return this;
	}
	
	public MonthOfYear getMonthOfYearID()
	{
		return MonthOfYearID;
	}
	
	public Months setMonthOfYearID(MonthOfYear monthOfYearID)
	{
		MonthOfYearID = monthOfYearID;
		return this;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public String toString()
	{
		return "timelord.entities.Months[ monthID=" + id + " ]";
	}
	
}
