/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.core.db.entities.time.builders.DayPartsQueryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Set;

/**
 * @author GedMarc
 */
@Entity
@Table(name = "DayParts",
       schema = "Time")
@XmlRootElement
@Getter
@Setter
@Accessors(chain = true)
public class DayParts extends BaseEntity<DayParts, DayPartsQueryBuilder, Integer> implements Serializable

{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(nullable = false,
	        name = "dayPartID")
	private Integer id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 100)
	@Column(nullable = false,
	        length = 100)
	private String dayPartName;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 100)
	@Column(nullable = false,
	        length = 100)
	private String dayPartDescription;
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false)
	@OrderBy
	private int dayPartSortOrder;
	@OneToMany(cascade = CascadeType.ALL,
	           mappedBy = "dayPartID",
	           fetch = FetchType.LAZY)
	private Set<HalfHourDayParts> halfHourDayPartsSet;
	
	public DayParts()
	{
	}
}
