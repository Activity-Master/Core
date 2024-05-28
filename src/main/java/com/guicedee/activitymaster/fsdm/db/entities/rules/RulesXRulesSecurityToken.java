package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesXRulesSecurityTokenQueryBuilder;
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
@Table(schema = "Rules", name = "RulesXRulesSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class RulesXRulesSecurityToken
		extends WarehouseSecurityTable<RulesXRulesSecurityToken, RulesXRulesSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesXRulesSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "RulesXRulesID",
	            referencedColumnName = "RulesXRulesID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private RulesXRules base;
	
	public RulesXRulesSecurityToken()
	{
	
	}
	
	public RulesXRulesSecurityToken(java.lang.String rulesXRulesSecurityTokenID)
	{
		this.id = rulesXRulesSecurityTokenID;
	}
	
	public String toString()
	{
		return "RulesXRulesSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public RulesXRulesSecurityToken setId(java.lang.String id)
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
