package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.TransQtdQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author MMagon
 * @since
 */

@Entity
@Table(name = "Trans_Qtd",
       schema = "Time")
@XmlRootElement
public class TransQtd
		extends BaseEntity<TransQtd, TransQtdQueryBuilder, TransQtdPK>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected TransQtdPK id;
	
	public TransQtd()
	{
	}
	
	@Override
	public TransQtdPK getId()
	{
		return id;
	}
	
	@Override
	public TransQtd setId(TransQtdPK id)
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
