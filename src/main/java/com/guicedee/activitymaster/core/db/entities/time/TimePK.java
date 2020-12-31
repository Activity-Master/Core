/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guicedee.activitymaster.core.db.entities.time;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author MMagon
 */
@Embeddable
@Getter
@Setter
@Accessors(chain = true)
public class TimePK
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@Column(nullable = false)
	private Integer hourID;
	@Basic(optional = false)
	@Column(nullable = false)
	private Integer minuteID;

	public TimePK()
	{
	}

	public TimePK(int hourID, int minuteID)
	{
		this.hourID = hourID;
		this.minuteID = minuteID;
	}

	@Override
	public int hashCode()
	{
		int hash = 0;
		hash += hourID;
		hash += minuteID;
		return hash;
	}

	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof TimePK))
		{
			return false;
		}
		TimePK other = (TimePK) object;
		if (hourID.equals(other.hourID))
		{
			return false;
		}
		if (minuteID.equals(other.minuteID))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "timelord.entities.TimePK[ hourID=" + hourID + ", minuteID=" + minuteID + " ]";
	}

}
