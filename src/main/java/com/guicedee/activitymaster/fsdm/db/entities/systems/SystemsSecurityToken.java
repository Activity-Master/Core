package com.guicedee.activitymaster.fsdm.db.entities.systems;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.systems.builders.SystemsSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "SystemsSecurityToken",
       schema = "dbo")
@XmlRootElement

@Access(AccessType.FIELD)
public class SystemsSecurityToken
		extends IWarehouseSecurityTable<SystemsSecurityToken, SystemsSecurityTokenQueryBuilder, String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "SystemsSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "SystemID",
	            referencedColumnName = "SystemID",
	            nullable = false,
	            updatable = false,
	            insertable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Systems base;
	
	public SystemsSecurityToken()
	{
	
	}
	
	public SystemsSecurityToken(java.lang.String systemsSecurityTokenID)
	{
		this.id = systemsSecurityTokenID;
	}
	
	public String toString()
	{
		return "SystemsSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public SystemsSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public Systems getBase()
	{
		return this.base;
	}
	
	public SystemsSecurityToken setBase(Systems base)
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
		if (!(o instanceof SystemsSecurityToken))
		{
			return false;
		}
		final SystemsSecurityToken other = (SystemsSecurityToken) o;
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
		return other instanceof SystemsSecurityToken;
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
