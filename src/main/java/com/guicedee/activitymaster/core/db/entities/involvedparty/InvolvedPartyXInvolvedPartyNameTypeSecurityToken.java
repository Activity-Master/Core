package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyNameTypeSecurityTokenQueryBuilder;

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
@Table(schema="Party",name = "InvolvedPartyXInvolvedPartyNameTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyXInvolvedPartyNameTypeSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyXInvolvedPartyNameTypeSecurityToken, InvolvedPartyXInvolvedPartyNameTypeSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXInvolvedPartyNameTypeSecurityTokenID")
	private Long id;
	@JoinColumn(name = "InvolvedPartyXInvolvedPartyNameTypeID",
			referencedColumnName = "InvolvedPartyXInvolvedPartyNameTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private InvolvedPartyXInvolvedPartyNameType base;

	public InvolvedPartyXInvolvedPartyNameTypeSecurityToken()
	{

	}

	public InvolvedPartyXInvolvedPartyNameTypeSecurityToken(Long involvedPartyXInvolvedPartyNameTypeSecurityTokenID)
	{
		this.id = involvedPartyXInvolvedPartyNameTypeSecurityTokenID;
	}

	public String toString()
	{
		return "InvolvedPartyXInvolvedPartyNameTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedPartyXInvolvedPartyNameType getBase()
	{
		return this.base;
	}

	public InvolvedPartyXInvolvedPartyNameTypeSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyXInvolvedPartyNameTypeSecurityToken setBase(InvolvedPartyXInvolvedPartyNameType base)
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
		if (!(o instanceof InvolvedPartyXInvolvedPartyNameTypeSecurityToken))
		{
			return false;
		}
		final InvolvedPartyXInvolvedPartyNameTypeSecurityToken other = (InvolvedPartyXInvolvedPartyNameTypeSecurityToken) o;
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
		return other instanceof InvolvedPartyXInvolvedPartyNameTypeSecurityToken;
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
