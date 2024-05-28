package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXArrangementTypeSecurityTokenQueryBuilder;
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
@Table(schema = "Arrangement", name = "ArrangementXArrangementTypeSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
public class ArrangementXArrangementTypeSecurityToken
		extends WarehouseSecurityTable<ArrangementXArrangementTypeSecurityToken, ArrangementXArrangementTypeSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXArrangementTypeSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	@JoinColumn(name = "ArrangementXArrangementTypeID",
	            referencedColumnName = "ArrangementXArrangementTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ArrangementXArrangementType base;
	
	public ArrangementXArrangementTypeSecurityToken()
	{
	
	}
	
	public ArrangementXArrangementTypeSecurityToken(java.lang.String arrangementXArrangementTypeSecurityTokenID)
	{
		this.id = arrangementXArrangementTypeSecurityTokenID;
	}
	
	public String toString()
	{
		return "ArrangementXArrangementTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public ArrangementXArrangementTypeSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementXArrangementTypeSecurityToken setBase(ArrangementXArrangementType base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public ArrangementXArrangementType getBase()
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
		ArrangementXArrangementTypeSecurityToken that = (ArrangementXArrangementTypeSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
