package com.guicedee.activitymaster.core.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.core.db.entities.time.builders.TransQtdQueryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
