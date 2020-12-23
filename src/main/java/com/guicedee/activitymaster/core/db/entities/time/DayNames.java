package com.guicedee.activitymaster.core.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.core.db.entities.time.builders.DayNamesQueryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.List;

/**
 * @author MMagon
 * @since
 */

@Entity
@Table(name = "DayNames",
		schema = "Time")
@XmlRootElement
@Getter
@Setter
@Accessors(chain = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DayNames
		extends BaseEntity<DayNames, DayNamesQueryBuilder, Integer>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	@Id
	
	@Basic(optional = false)
	@Column(name = "DayNameID",
			nullable = false)
	private Integer id;
	@Basic(optional = false)
	@Column(name = "DayName",
			nullable = false,
			length = 100)
	private String dayName;
	@Basic(optional = false)
	@Column(name = "DayShortName",
			nullable = false,
			length = 200)
	private String dayShortName;
	@Basic(optional = false)
	@Column(name = "DaySortOrder",
			nullable = false)
	private int daySortOrder;
	@Basic(optional = false)
	@Column(name = "DayIsBusinessDay",
			nullable = false)
	private short dayIsBusinessDay;
	@Basic(optional = false)
	@Column(name = "DayBusinessDayClassification",
			nullable = false,
			length = 50)
	private String dayBusinessDayClassification;
	@OneToMany(cascade =
			           {
					           CascadeType.MERGE, CascadeType.PERSIST
			           },
			mappedBy = "dayNameID")
	private List<Days> DaysList;

	@Basic(optional = false)
	@Column(name = "DayAbbreviation",
			nullable = false,
			length = 50)
	private String dayAbbreviation;

	@Basic(optional = false)
	@Column(name = "DayLongAbbreviation",
			nullable = false,
			length = 50)
	private String dayLongAbbreviation;

	public DayNames()
	{
	}

	public DayNames(Integer id)
	{
		this.id = id;
	}

	public DayNames(Integer id, String dayName, String dayShortName, int daySortOrder, short dayIsBusinessDay, String dayBusinessDayClassification)
	{
		this.id = id;
		this.dayName = dayName;
		this.dayShortName = dayShortName;
		this.daySortOrder = daySortOrder;
		this.dayIsBusinessDay = dayIsBusinessDay;
		this.dayBusinessDayClassification = dayBusinessDayClassification;
	}

	@Override
	public int hashCode()
	{
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
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
		DayNames dayNames = (DayNames) o;
		return getDayName().equals(dayNames.getDayName());
	}

	@Override
	public String toString()
	{
		return dayName;
	}

}
