/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.TransQtmQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author mmagon
 */
@Entity
@Table(name = "Trans_Qtm",
       schema = "Time")
@XmlRootElement
public class TransQtm
		extends BaseEntity<TransQtm, TransQtmQueryBuilder, TransQtmPK>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected TransQtmPK id;
	
	public TransQtm()
	{
	}
	
	@Override
	public TransQtmPK getId()
	{
		return id;
	}
	
	@Override
	public TransQtm setId(TransQtmPK id)
	{
		this.id = id;
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
		TransQtm transQtm = (TransQtm) o;
		return getId().equals(transQtm.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public String toString()
	{
		return "" + id;
	}
	
}
