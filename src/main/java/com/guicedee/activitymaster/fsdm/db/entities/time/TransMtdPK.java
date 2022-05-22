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
public class TransMtdPK
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@Column(name = "DayID",
	        nullable = false)
	private Integer dayID;
	@Basic(optional = false)
	@Column(name = "MtdDayID",
	        nullable = false)
	private Integer mtdDayID;
	
	public TransMtdPK()
	{
	}
	
	public Integer getDayID()
	{
		return dayID;
	}
	
	public TransMtdPK setDayID(Integer dayID)
	{
		this.dayID = dayID;
		return this;
	}
	
	public Integer getMtdDayID()
	{
		return mtdDayID;
	}
	
	public TransMtdPK setMtdDayID(Integer mtdDayID)
	{
		this.mtdDayID = mtdDayID;
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
		TransMtdPK that = (TransMtdPK) o;
		return getDayID().equals(that.getDayID()) &&
		       getMtdDayID().equals(that.getMtdDayID());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getDayID(), getMtdDayID());
	}
	
	@Override
	public String toString()
	{
		return "timelord.entities.TransMtdPK[ dayID=" + dayID + ", mtdDayID=" + mtdDayID + " ]";
	}
	
}
