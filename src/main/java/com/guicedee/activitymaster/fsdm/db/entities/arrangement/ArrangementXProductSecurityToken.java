package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXProductSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Arrangement", name = "ArrangementXProductSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
public class ArrangementXProductSecurityToken
		extends WarehouseSecurityTable<ArrangementXProductSecurityToken, ArrangementXProductSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXProductSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ArrangementXProductID",
	            referencedColumnName = "ArrangementXProductID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ArrangementXProduct base;
	
	public ArrangementXProductSecurityToken()
	{
	
	}
	
	public ArrangementXProductSecurityToken(java.lang.String arrangementXProductSecurityTokenID)
	{
		this.id = arrangementXProductSecurityTokenID;
	}
	
	public String toString()
	{
		return "ArrangementXProductSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public ArrangementXProductSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementXProductSecurityToken setBase(ArrangementXProduct base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public ArrangementXProduct getBase()
	{
		return base;
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
		ArrangementXProductSecurityToken that = (ArrangementXProductSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
