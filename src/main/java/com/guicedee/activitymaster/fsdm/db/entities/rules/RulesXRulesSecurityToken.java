package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesXRulesSecurityTokenQueryBuilder;
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
@Table(schema = "Rules", name = "RulesXRulesSecurityToken")
@XmlRootElement

@Access(FIELD)
public class RulesXRulesSecurityToken
		extends WarehouseSecurityTable<RulesXRulesSecurityToken, RulesXRulesSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesXRulesSecurityTokenID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@JoinColumn(name = "RulesXRulesID",
	            referencedColumnName = "RulesXRulesID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private RulesXRules base;
	
	public RulesXRulesSecurityToken()
	{
	
	}
	
	public RulesXRulesSecurityToken(UUID rulesXRulesSecurityTokenID)
	{
		this.id = rulesXRulesSecurityTokenID;
	}
	
	public String toString()
	{
		return "RulesXRulesSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public RulesXRulesSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public RulesXRules getBase()
	{
		return this.base;
	}
	
	public RulesXRulesSecurityToken setBase(RulesXRules base)
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
		if (!(o instanceof RulesXRulesSecurityToken))
		{
			return false;
		}
		final RulesXRulesSecurityToken other = (RulesXRulesSecurityToken) o;
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
		return other instanceof RulesXRulesSecurityToken;
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
