package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.DaysQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@Getter
@Setter
@Accessors(chain = true)
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
