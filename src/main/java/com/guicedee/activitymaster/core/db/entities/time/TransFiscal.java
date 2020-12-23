package com.guicedee.activitymaster.core.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.core.db.entities.time.builders.TransFiscalQueryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author MMagon
 * @since
 */

@Entity
@Table(name = "Trans_Fiscal",
		schema = "Time")
@XmlRootElement
@Getter
@Setter
@Accessors(chain = true)
public class TransFiscal
		extends BaseEntity<TransFiscal, TransFiscalQueryBuilder, Integer>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "DayID",
			nullable = false)
	private Integer id;
	@Basic(optional = false)
	@Column(name = "FiscalDayID",
			nullable = false)
	private int fiscalDayID;

	public TransFiscal()
	{
	}

	public TransFiscal(Integer id, int fiscalDayID)
	{
		this.id = id;
		this.fiscalDayID = fiscalDayID;
	}

	@Override
	public String toString()
	{
		return id + " - " + fiscalDayID;
	}

}
