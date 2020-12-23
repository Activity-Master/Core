package com.guicedee.activitymaster.core.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.core.db.entities.time.builders.YearsQueryBuilder;
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
@Table(name = "Years",
		schema = "Time")
@XmlRootElement
@Getter
@Setter
@Accessors(chain = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Years
		extends BaseEntity<Years, YearsQueryBuilder, Short>
		implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "YearID",
			nullable = false)
	private Short id;
	@Basic(optional = false)
	@Column(name = "YYName",
			nullable = false,
			length = 2)
	private String yYName;
	@Basic(optional = false)
	@Column(name = "YYYName",
			nullable = false,
			length = 3)
	private String yYYName;
	@Basic(optional = false)
	@Column(name = "YearFullName",
			nullable = false,
			length = 250)
	private String yearFullName;
	@Basic(optional = false)
	@Column(name = "YearName",
			nullable = false,
			length = 10)
	private String yearName;
	@Basic(optional = false)
	@Column(name = "LeapYearFlag",
			nullable = false)
	private short leapYearFlag;
	@Basic(optional = false)
	@Column(name = "LastYearID",
			nullable = false)
	private short lastYearID;
	@OneToMany(mappedBy = "yearID",
			fetch = FetchType.LAZY)
	private List<Quarters> QuartersList;
	@Basic(optional = false)
	@Column(name = "Century",
			nullable = false)
	private Short century;

	public Years()
	{
	}

	@Override
	public String toString()
	{
		return "" + id;
	}
}
