package com.guicedee.activitymaster.core.db.entities.time;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.core.db.entities.time.builders.TransYtdQueryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author MMagon
 * @since
 */

@Entity
@Table(name = "Trans_Ytd",
		schema = "Time")
@XmlRootElement
@Getter
@Setter
@Accessors(chain = true)
public class TransYtd
		extends BaseEntity<TransYtd, TransYtdQueryBuilder, TransYtdPK>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected TransYtdPK id;

	public TransYtd()
	{
	}

	@Override
	public String toString()
	{
		return "" + id;
	}

}
