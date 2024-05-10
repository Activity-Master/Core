package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesXRulesTypeSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Rules",
       name = "RulesXRulesTypeSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class RulesXRulesTypeSecurityToken
		extends IWarehouseSecurityTable<RulesXRulesTypeSecurityToken, RulesXRulesTypeSecurityTokenQueryBuilder, String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesXRulesTypeSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "RulesXRulesTypeID",
	            referencedColumnName = "RulesXRulesTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private RulesXRulesType base;
	
	public RulesXRulesTypeSecurityToken()
	{
	
	}
	
	public RulesXRulesTypeSecurityToken(java.lang.String rulesXRulesTypeSecurityTokenID)
	{
		this.id = rulesXRulesTypeSecurityTokenID;
	}
	
	@Override
	public String toString()
	{
		return "RulesXRulesTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	@Override
	public java.lang.String getId()
	{
		return this.id;
	}
	
	@Override
	public RulesXRulesTypeSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public RulesXRulesType getBase()
	{
		return this.base;
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
