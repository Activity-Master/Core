package com.guicedee.activitymaster.core.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.core.db.entities.time.builders.MonthOfYearQueryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
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
@Getter
@Setter
@Accessors(chain = true)
public class MonthOfYear
		extends BaseEntity<MonthOfYear, MonthOfYearQueryBuilder, Integer>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
