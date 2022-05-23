package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXArrangementTypeSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;

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
		extends WarehouseSecurityTable<ArrangementXArrangementTypeSecurityToken, ArrangementXArrangementTypeSecurityTokenQueryBuilder, java.lang.String>
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
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public ArrangementXArrangementTypeSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementXArrangementType getBase()
	{
		return this.base;
	}
	
	public ArrangementXArrangementTypeSecurityToken setBase(ArrangementXArrangementType base)
	{
		this.base = base;
		return this;
	}
	
	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ArrangementXArrangementTypeSecurityToken))
		{
			return false;
		}
		final ArrangementXArrangementTypeSecurityToken other = (ArrangementXArrangementTypeSecurityToken) o;
		if (!other.canEqual(this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		return this$id == null ? other$id == null : this$id.equals(other$id);
	}
	
	protected boolean canEqual(final Object other)
	{
		return other instanceof ArrangementXArrangementTypeSecurityToken;
	}
	
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
