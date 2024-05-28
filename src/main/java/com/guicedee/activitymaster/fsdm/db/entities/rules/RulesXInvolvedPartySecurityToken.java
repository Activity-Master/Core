package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesXInvolvedPartySecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Rules", name = "RulesXInvolvedPartySecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class RulesXInvolvedPartySecurityToken
		extends WarehouseSecurityTable<RulesXInvolvedPartySecurityToken, RulesXInvolvedPartySecurityTokenQueryBuilder, String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesXInvolvedPartySecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "RulesXInvolvedPartyID",
	            referencedColumnName = "RulesXInvolvedPartyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private RulesXInvolvedParty base;
	
	public RulesXInvolvedPartySecurityToken()
	{
	
	}
	
	public RulesXInvolvedPartySecurityToken(java.lang.String RulesXInvolvedPartySecurityTokenID)
	{
		this.id = RulesXInvolvedPartySecurityTokenID;
	}
	
	public String toString()
	{
		return "RulesXInvolvedPartySecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public RulesXInvolvedPartySecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public RulesXInvolvedParty getBase()
	{
		return this.base;
	}
	
	public RulesXInvolvedPartySecurityToken setBase(RulesXInvolvedParty base)
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
		if (!(o instanceof RulesXInvolvedPartySecurityToken))
		{
			return false;
		}
		final RulesXInvolvedPartySecurityToken other = (RulesXInvolvedPartySecurityToken) o;
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
		return other instanceof RulesXInvolvedPartySecurityToken;
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
