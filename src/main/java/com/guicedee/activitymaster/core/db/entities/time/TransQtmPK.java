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
import java.io.Serializable;
import java.util.Objects;

/**
 * @author mmagon
 */
@Embeddable
@Getter
@Setter
@Accessors(chain = true)
public class TransQtmPK
		implements Serializable
{
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
