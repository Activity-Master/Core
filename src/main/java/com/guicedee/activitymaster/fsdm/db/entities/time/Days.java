package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.DaysQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @since @version @author MMagon
 */
@Entity
@Table(name = "Days",
       schema = "Time")
@XmlRootElement
public class Days
		extends BaseEntity<Days, DaysQueryBuilder, Integer>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "DayID",
	        nullable = false)
	private Integer id;
	
	@Basic(optional = false)
	@Column(name = "DayDate",
	        nullable = false,
	        length = 10)
	private LocalDate dayDate;
	@Basic(optional = false)
	@Column(name = "DayDateTime",
	        nullable = false)
	private LocalDateTime dayDateTime;
	@Basic(optional = false)
	@Column(name = "DayInMonth",
	        nullable = false)
	private int dayInMonth;
	@Basic(optional = false)
	@Column(name = "DayInYear",
	        nullable = false)
	private int dayInYear;
	@Basic(optional = false)
	@Column(name = "QuarterID",
	        nullable = false)
	private int quarterID;
	@Basic(optional = false)
	@Column(name = "YearID",
	        nullable = false)
	private int yearID;
	@Basic(optional = false)
	@Column(name = "LastDayID",
	        nullable = false)
	private int lastDayID;
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
	@Column(name = "DayMonthDescription",
	        nullable = false,
	        length = 50)
	private String dayMonthDescription;
	@Basic(optional = false)
	@Column(name = "DayMMQQDescription",
	        nullable = false,
	        length = 50)
	private String dayMMQQDescription;
	@Basic(optional = false)
	@Column(name = "DayYYYYMMDescription",
	        nullable = false,
	        length = 50)
	private String dayYYYYMMDescription;
	@Basic(optional = false)
	@Column(name = "DayDDMMYYYYDescription",
	        nullable = false,
	        length = 50)
	private String dayDDMMYYYYDescription;
	@Basic(optional = false)
	@Column(name = "DayDDMMYYYYSlashDescription",
	        nullable = false,
	        length = 50)
	private String dayDDMMYYYYSlashDescription;
	@Basic(optional = false)
	@Column(name = "DayDDMMYYYYHyphenDescription",
	        nullable = false,
	        length = 50)
	private String dayDDMMYYYYHyphenDescription;
	@Basic(optional = false)
	@Column(name = "DayLongDescription",
	        nullable = false,
	        length = 50)
	private String dayLongDescription;
	@Basic(optional = false)
	@Column(name = "DayFullDescription",
	        nullable = false,
	        length = 50)
	private String dayFullDescription;
	@Basic(optional = false)
	@Column(name = "DayIsPublicHoliday",
	        nullable = false)
	private short dayIsPublicHoliday;
	@JoinColumn(name = "WeekID",
	            referencedColumnName = "WeekID",
	            nullable = false)
	@ManyToOne(optional = false)
	private Weeks weekID;
	@JoinColumn(name = "MonthID",
	            referencedColumnName = "MonthID",
	            nullable = false)
	@ManyToOne(optional = false)
	private Months monthID;
	@JoinColumn(name = "DayNameID",
	            referencedColumnName = "DayNameID",
	            nullable = false)
	@ManyToOne(optional = false)
	private DayNames dayNameID;
	
	public Days()
	{
		//Nothing needed
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
		Days days = (Days) o;
		return getId().equals(days.getId());
	}
	
	@Override
	public Integer getId()
	{
		return id;
	}
	
	@Override
	public Days setId(Integer id)
	{
		this.id = id;
		return this;
	}
	
	public LocalDate getDayDate()
	{
		return dayDate;
	}
	
	public Days setDayDate(LocalDate dayDate)
	{
		this.dayDate = dayDate;
		return this;
	}
	
	public LocalDateTime getDayDateTime()
	{
		return dayDateTime;
	}
	
	public Days setDayDateTime(LocalDateTime dayDateTime)
	{
		this.dayDateTime = dayDateTime;
		return this;
	}
	
	public int getDayInMonth()
	{
		return dayInMonth;
	}
	
	public Days setDayInMonth(int dayInMonth)
	{
		this.dayInMonth = dayInMonth;
		return this;
	}
	
	public int getDayInYear()
	{
		return dayInYear;
	}
	
	public Days setDayInYear(int dayInYear)
	{
		this.dayInYear = dayInYear;
		return this;
	}
	
	public int getQuarterID()
	{
		return quarterID;
	}
	
	public Days setQuarterID(int quarterID)
	{
		this.quarterID = quarterID;
		return this;
	}
	
	public int getYearID()
	{
		return yearID;
	}
	
	public Days setYearID(int yearID)
	{
		this.yearID = yearID;
		return this;
	}
	
	public int getLastDayID()
	{
		return lastDayID;
	}
	
	public Days setLastDayID(int lastDayID)
	{
		this.lastDayID = lastDayID;
		return this;
	}
	
	public int getLastMonthID()
	{
		return lastMonthID;
	}
	
	public Days setLastMonthID(int lastMonthID)
	{
		this.lastMonthID = lastMonthID;
		return this;
	}
	
	public int getLastQuarterID()
	{
		return lastQuarterID;
	}
	
	public Days setLastQuarterID(int lastQuarterID)
	{
		this.lastQuarterID = lastQuarterID;
		return this;
	}
	
	public int getLastYearID()
	{
		return lastYearID;
	}
	
	public Days setLastYearID(int lastYearID)
	{
		this.lastYearID = lastYearID;
		return this;
	}
	
	public String getDayMonthDescription()
	{
		return dayMonthDescription;
	}
	
	public Days setDayMonthDescription(String dayMonthDescription)
	{
		this.dayMonthDescription = dayMonthDescription;
		return this;
	}
	
	public String getDayMMQQDescription()
	{
		return dayMMQQDescription;
	}
	
	public Days setDayMMQQDescription(String dayMMQQDescription)
	{
		this.dayMMQQDescription = dayMMQQDescription;
		return this;
	}
	
	public String getDayYYYYMMDescription()
	{
		return dayYYYYMMDescription;
	}
	
	public Days setDayYYYYMMDescription(String dayYYYYMMDescription)
	{
		this.dayYYYYMMDescription = dayYYYYMMDescription;
		return this;
	}
	
	public String getDayDDMMYYYYDescription()
	{
		return dayDDMMYYYYDescription;
	}
	
	public Days setDayDDMMYYYYDescription(String dayDDMMYYYYDescription)
	{
		this.dayDDMMYYYYDescription = dayDDMMYYYYDescription;
		return this;
	}
	
	public String getDayDDMMYYYYSlashDescription()
	{
		return dayDDMMYYYYSlashDescription;
	}
	
	public Days setDayDDMMYYYYSlashDescription(String dayDDMMYYYYSlashDescription)
	{
		this.dayDDMMYYYYSlashDescription = dayDDMMYYYYSlashDescription;
		return this;
	}
	
	public String getDayDDMMYYYYHyphenDescription()
	{
		return dayDDMMYYYYHyphenDescription;
	}
	
	public Days setDayDDMMYYYYHyphenDescription(String dayDDMMYYYYHyphenDescription)
	{
		this.dayDDMMYYYYHyphenDescription = dayDDMMYYYYHyphenDescription;
		return this;
	}
	
	public String getDayLongDescription()
	{
		return dayLongDescription;
	}
	
	public Days setDayLongDescription(String dayLongDescription)
	{
		this.dayLongDescription = dayLongDescription;
		return this;
	}
	
	public String getDayFullDescription()
	{
		return dayFullDescription;
	}
	
	public Days setDayFullDescription(String dayFullDescription)
	{
		this.dayFullDescription = dayFullDescription;
		return this;
	}
	
	public short getDayIsPublicHoliday()
	{
		return dayIsPublicHoliday;
	}
	
	public Days setDayIsPublicHoliday(short dayIsPublicHoliday)
	{
		this.dayIsPublicHoliday = dayIsPublicHoliday;
		return this;
	}
	
	public Weeks getWeekID()
	{
		return weekID;
	}
	
	public Days setWeekID(Weeks weekID)
	{
		this.weekID = weekID;
		return this;
	}
	
	public Months getMonthID()
	{
		return monthID;
	}
	
	public Days setMonthID(Months monthID)
	{
		this.monthID = monthID;
		return this;
	}
	
	public DayNames getDayNameID()
	{
		return dayNameID;
	}
	
	public Days setDayNameID(DayNames dayNameID)
	{
		this.dayNameID = dayNameID;
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
		return dayFullDescription;
	}
}
