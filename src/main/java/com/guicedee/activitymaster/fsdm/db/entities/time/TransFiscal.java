package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.TransFiscalQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author MMagon
 * @since
 */

@Entity
@Table(name = "Trans_Fiscal",
       schema = "Time")
@XmlRootElement
public class TransFiscal
		extends BaseEntity<TransFiscal, TransFiscalQueryBuilder, Integer>
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
	public Integer getId()
	{
		return id;
	}
	
	@Override
	public TransFiscal setId(Integer id)
	{
		this.id = id;
		return this;
	}
	
	public int getFiscalDayID()
	{
		return fiscalDayID;
	}
	
	public TransFiscal setFiscalDayID(int fiscalDayID)
	{
		this.fiscalDayID = fiscalDayID;
		return this;
	}
	
	@Override
	public String toString()
	{
		return id + " - " + fiscalDayID;
	}
	
}
