/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guicedee.activitymaster.fsdm.db.entities.time;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author mmagon
 */
@Embeddable
public class TransQtmPK
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@Column(name = "MonthID",
	        nullable = false)
	private Integer monthID;
	@Basic(optional = false)
	@Column(name = "QTM_MonthID",
	        nullable = false)
	private Integer qTMMonthID;
	
	public TransQtmPK()
	{
	}
	
	public Integer getMonthID()
	{
		return monthID;
	}
	
	public TransQtmPK setMonthID(Integer monthID)
	{
		this.monthID = monthID;
		return this;
	}
	
	public Integer getqTMMonthID()
	{
		return qTMMonthID;
	}
	
	public TransQtmPK setqTMMonthID(Integer qTMMonthID)
	{
		this.qTMMonthID = qTMMonthID;
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
		TransQtmPK that = (TransQtmPK) o;
		return getMonthID().equals(that.getMonthID()) &&
		       qTMMonthID.equals(that.qTMMonthID);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getMonthID(), qTMMonthID);
	}
	
	@Override
	public String toString()
	{
		return "timelord.entities.TransQtmPK[ monthID=" + monthID + ", qTMMonthID=" + qTMMonthID + " ]";
	}
	
}
