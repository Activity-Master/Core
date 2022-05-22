package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.TransYtdQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author MMagon
 * @since
 */

@Entity
@Table(name = "Trans_Ytd",
       schema = "Time")
@XmlRootElement
public class TransYtd
		extends BaseEntity<TransYtd, TransYtdQueryBuilder, TransYtdPK>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected TransYtdPK id;
	
	public TransYtd()
	{
	}
	
	@Override
	public TransYtdPK getId()
	{
		return id;
	}
	
	@Override
	public TransYtd setId(TransYtdPK id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public String toString()
	{
		return "" + id;
	}
	
}
