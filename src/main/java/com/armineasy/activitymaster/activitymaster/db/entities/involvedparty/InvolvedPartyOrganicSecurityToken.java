package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyOrganicSecurityTokenQueryBuilder;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "InvolvedPartyOrganicSecurityToken")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyOrganicSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyOrganicSecurityToken, InvolvedPartyOrganicSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyOrganicSecurityTokenID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyOrganicID",
			referencedColumnName = "InvolvedPartyOrganicID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedPartyOrganic base;

	public InvolvedPartyOrganicSecurityToken()
	{

	}

	public InvolvedPartyOrganicSecurityToken(Long involvedPartyOrganicSecurityTokenID)
	{
		this.id = involvedPartyOrganicSecurityTokenID;
	}

	public String toString()
	{
		return "InvolvedPartyOrganicSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedPartyOrganic getBase()
	{
		return this.base;
	}

	public InvolvedPartyOrganicSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyOrganicSecurityToken setBase(InvolvedPartyOrganic base)
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
		if (!(o instanceof InvolvedPartyOrganicSecurityToken))
		{
			return false;
		}
		final InvolvedPartyOrganicSecurityToken other = (InvolvedPartyOrganicSecurityToken) o;
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
		return other instanceof InvolvedPartyOrganicSecurityToken;
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
