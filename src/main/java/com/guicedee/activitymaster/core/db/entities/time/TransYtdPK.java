package com.guicedee.activitymaster.core.db.entities.time;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
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
public class TransYtdPK
		implements Serializable
{
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
