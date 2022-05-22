package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.TransMtdQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author MMagon
 * @since
 */

@Entity
@Table(name = "Trans_Mtd",
       schema = "Time")
@XmlRootElement
public class TransMtd
		extends BaseEntity<TransMtd, TransMtdQueryBuilder, TransMtdPK>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected TransMtdPK id;
	
	public TransMtd()
	{
	}
	
	@Override
	public TransMtdPK getId()
	{
		return id;
	}
	
	@Override
	public TransMtd setId(TransMtdPK id)
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
		TransMtd transMtd = (TransMtd) o;
		return getId().equals(transMtd.getId());
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
