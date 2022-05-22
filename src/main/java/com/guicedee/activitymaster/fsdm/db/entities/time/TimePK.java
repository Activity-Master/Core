/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guicedee.activitymaster.fsdm.db.entities.time;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author MMagon
 */
@Embeddable
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
	
	public Integer getHourID()
	{
		return hourID;
	}
	
	public TimePK setHourID(Integer hourID)
	{
		this.hourID = hourID;
		return this;
	}
	
	public Integer getMinuteID()
	{
		return minuteID;
	}
	
	public TimePK setMinuteID(Integer minuteID)
	{
		this.minuteID = minuteID;
		return this;
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
		return !minuteID.equals(other.minuteID);
	}
	
	@Override
	public String toString()
	{
		return "timelord.entities.TimePK[ hourID=" + hourID + ", minuteID=" + minuteID + " ]";
	}
	
}
