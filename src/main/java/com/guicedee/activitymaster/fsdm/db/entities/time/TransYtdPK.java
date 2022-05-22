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
public class TransYtdPK
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@Column(name = "DayID",
	        nullable = false)
	private Integer dayID;
	@Basic(optional = false)
	@Column(name = "YtdDayID",
	        nullable = false)
	private Integer ytdDayID;
	
	public TransYtdPK()
	{
	}
	
	public Integer getDayID()
	{
		return dayID;
	}
	
	public TransYtdPK setDayID(Integer dayID)
	{
		this.dayID = dayID;
		return this;
	}
	
	public Integer getYtdDayID()
	{
		return ytdDayID;
	}
	
	public TransYtdPK setYtdDayID(Integer ytdDayID)
	{
		this.ytdDayID = ytdDayID;
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
		TransYtdPK that = (TransYtdPK) o;
		return getDayID().equals(that.getDayID()) &&
		       getYtdDayID().equals(that.getYtdDayID());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getDayID(), getYtdDayID());
	}
	
	@Override
	public String toString()
	{
		return "timelord.entities.TransYtdPK[ dayID=" + dayID + ", ytdDayID=" + ytdDayID + " ]";
	}
	
}
