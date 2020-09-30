package com.guicedee.activitymaster.core.db.entities.rules;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesXRulesSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Rules",name = "RulesXRulesSecurityToken")
@XmlRootElement

@Access(FIELD)
public class RulesXRulesSecurityToken
		extends WarehouseSecurityTable<RulesXRulesSecurityToken, RulesXRulesSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "RulesXRulesSecurityTokenID")
	private Long id;

	@JoinColumn(name = "RulesXRulesID",
			referencedColumnName = "RulesXRulesID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private RulesXRules base;

	public RulesXRulesSecurityToken()
	{

	}

	public RulesXRulesSecurityToken(Long rulesXRulesSecurityTokenID)
	{
		this.id = rulesXRulesSecurityTokenID;
	}

	public String toString()
	{
		return "RulesXRulesSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public RulesXRules getBase()
	{
		return this.base;
	}

	public RulesXRulesSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
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
