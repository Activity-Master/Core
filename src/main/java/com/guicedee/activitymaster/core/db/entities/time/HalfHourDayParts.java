/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.core.db.entities.time.builders.HalfHourDayPartsQueryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author GedMarc
 */
@Entity
@Table(name = "HalfHourDayParts",
       schema = "Time",
       uniqueConstraints = {
		       @UniqueConstraint(columnNames = {"HourID", "MinuteID"})})
@XmlRootElement
@Getter
@Setter
@Accessors(chain = true)
public class HalfHourDayParts
		extends BaseEntity<HalfHourDayParts, HalfHourDayPartsQueryBuilder, Integer> implements Serializable
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(nullable = false,
	        name = "halfHourDayPartID")
	private Integer id;
	@JoinColumn(name = "DayPartID",
	            referencedColumnName = "DayPartID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private DayParts dayPartID;
	@Basic(optional = false)
	@Column(nullable = false)
	private Integer hourID;
	@Basic(optional = false)
	@Column(nullable = false)
	private Integer minuteID;
}
