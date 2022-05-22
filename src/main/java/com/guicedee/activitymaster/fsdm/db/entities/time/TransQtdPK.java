package com.guicedee.activitymaster.fsdm.db.entities.time;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author MMagon
 * @since
 */

@Embeddable
public class TransQtdPK
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@Column(name = "DayID",
	        nullable = false)
	private Integer dayID;
	@Basic(optional = false)
	@Column(name = "QtdDayID",
	        nullable = false)
	private Integer qtdDayID;
	
	public TransQtdPK()
	{
	}
	
	public Integer getDayID()
	{
		return dayID;
	}
	
	public TransQtdPK setDayID(Integer dayID)
	{
		this.dayID = dayID;
		return this;
	}
	
	public Integer getQtdDayID()
	{
		return qtdDayID;
	}
	
	public TransQtdPK setQtdDayID(Integer qtdDayID)
	{
		this.qtdDayID = qtdDayID;
		return this;
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
		TransQtdPK that = (TransQtdPK) o;
		return getDayID().equals(that.getDayID()) &&
		       getQtdDayID().equals(that.getQtdDayID());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getDayID(), getQtdDayID());
	}
	
	@Override
	public String toString()
	{
		return "timelord.entities.TransQtdPK[ dayID=" + dayID + ", qtdDayID=" + qtdDayID + " ]";
	}
	
}
