package com.guicedee.activitymaster.core.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.core.db.entities.time.builders.WeeksQueryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
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
@Getter
@Setter
@Accessors(chain = true)
public class Weeks
		extends BaseEntity<Weeks, WeeksQueryBuilder, Integer>
		implements Serializable
{

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
