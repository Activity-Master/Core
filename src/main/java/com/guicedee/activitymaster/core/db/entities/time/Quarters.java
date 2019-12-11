package com.guicedee.activitymaster.core.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.core.db.entities.time.builders.QuartersQueryBuilder;
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
@Table(name = "Quarters",
		schema = "Time")
@XmlRootElement
@Getter
@Setter
@Accessors(chain = true)
public class Quarters
		extends BaseEntity<Quarters, QuartersQueryBuilder, Integer>
		implements Serializable
{
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
	private List<Months> lUMonthsList;
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
