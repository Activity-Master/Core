package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyNameTypeSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "InvolvedPartyNameTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyNameTypeSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyNameTypeSecurityToken, InvolvedPartyNameTypeSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyNameTypeSecurityTokenID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyNameTypeID",
			referencedColumnName = "InvolvedPartyNameTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private InvolvedPartyNameType base;

	public InvolvedPartyNameTypeSecurityToken()
	{

	}

	public InvolvedPartyNameTypeSecurityToken(Long involvedPartyNameTypeSecurityTokenID)
	{
		this.id = involvedPartyNameTypeSecurityTokenID;
	}

	public String toString()
	{
		return "InvolvedPartyNameTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedPartyNameType getBase()
	{
		return this.base;
	}

	public InvolvedPartyNameTypeSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyNameTypeSecurityToken setBase(InvolvedPartyNameType base)
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
		if (!(o instanceof InvolvedPartyNameTypeSecurityToken))
		{
			return false;
		}
		final InvolvedPartyNameTypeSecurityToken other = (InvolvedPartyNameTypeSecurityToken) o;
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
		return other instanceof InvolvedPartyNameTypeSecurityToken;
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
