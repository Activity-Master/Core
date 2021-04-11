package com.guicedee.activitymaster.fsdm.db.entities.systems;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.systems.builders.SystemXClassificationSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "SystemXClassificationSecurityToken")
@XmlRootElement

@Access(FIELD)
public class SystemsXClassificationSecurityToken
		extends WarehouseSecurityTable<SystemsXClassificationSecurityToken, SystemXClassificationSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "SystemXClassificationSecurityTokenID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@JoinColumn(name = "SystemXClassificationID",
	            referencedColumnName = "SystemXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private SystemsXClassification base;
	
	public SystemsXClassificationSecurityToken()
	{
	
	}
	
	public SystemsXClassificationSecurityToken(UUID systemXClassificationSecurityTokenID)
	{
		this.id = systemXClassificationSecurityTokenID;
	}
	
	public String toString()
	{
		return "SystemXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public SystemsXClassification getBase()
	{
		return this.base;
	}
	
	public SystemsXClassificationSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public SystemsXClassificationSecurityToken setBase(SystemsXClassification base)
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
		if (!(o instanceof SystemsXClassificationSecurityToken))
		{
			return false;
		}
		final SystemsXClassificationSecurityToken other = (SystemsXClassificationSecurityToken) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}
	
	protected boolean canEqual(final Object other)
	{
		return other instanceof SystemsXClassificationSecurityToken;
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
