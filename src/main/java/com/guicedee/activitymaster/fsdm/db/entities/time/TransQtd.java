package com.guicedee.activitymaster.fsdm.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.entities.time.builders.TransQtdQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@Getter
@Setter
@Accessors(chain = true)
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
	public String toString()
	{
		return "" + id;
	}
	
}
