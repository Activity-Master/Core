package com.guicedee.activitymaster.core.db.entities.rules;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesXInvolvedPartySecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.UUID;

import static jakarta.persistence.AccessType.FIELD;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Rules",name = "RulesXInvolvedPartySecurityToken")
@XmlRootElement

@Access(FIELD)
public class RulesXInvolvedPartySecurityToken
		extends WarehouseSecurityTable<RulesXInvolvedPartySecurityToken, RulesXInvolvedPartySecurityTokenQueryBuilder, java.util.UUID>
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "RulesXInvolvedPartySecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "RulesXInvolvedPartyID",
			referencedColumnName = "RulesXInvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private RulesXInvolvedParty base;

	public RulesXInvolvedPartySecurityToken()
	{

	}

	public RulesXInvolvedPartySecurityToken(UUID RulesXInvolvedPartySecurityTokenID)
	{
		this.id = RulesXInvolvedPartySecurityTokenID;
	}

	public String toString()
	{
		return "RulesXInvolvedPartySecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public RulesXInvolvedParty getBase()
	{
		return this.base;
	}

	public RulesXInvolvedPartySecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
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
