package com.guicedee.activitymaster.fsdm.db.entities.time;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author MMagon
 * @since
 */

@Embeddable
@Getter
@Setter
@Accessors(chain = true)
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
