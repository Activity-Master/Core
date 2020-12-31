package com.guicedee.activitymaster.core.db.entities.rules;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesXRulesTypeSecurityTokenQueryBuilder;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Rules",
       name = "RulesXRulesTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class RulesXRulesTypeSecurityToken
		extends WarehouseSecurityTable<RulesXRulesTypeSecurityToken, RulesXRulesTypeSecurityTokenQueryBuilder, java.util.UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesXRulesTypeSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
	@JoinColumn(name = "RulesXRulesTypeID",
	            referencedColumnName = "RulesXRulesTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private RulesXRulesType base;
	
	public RulesXRulesTypeSecurityToken()
	{
	
	}
	
	public RulesXRulesTypeSecurityToken(UUID rulesXRulesTypeSecurityTokenID)
	{
		this.id = rulesXRulesTypeSecurityTokenID;
	}
	
	@Override
	public String toString()
	{
		return "RulesXRulesTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	@Override
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public RulesXRulesType getBase()
	{
		return this.base;
	}
	
	@Override
	public RulesXRulesTypeSecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public RulesXRulesTypeSecurityToken setBase(RulesXRulesType base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof RulesXRulesTypeSecurityToken))
		{
			return false;
		}
		final RulesXRulesTypeSecurityToken other = (RulesXRulesTypeSecurityToken) o;
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
		return other instanceof RulesXRulesTypeSecurityToken;
	}
	
	@Override
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
