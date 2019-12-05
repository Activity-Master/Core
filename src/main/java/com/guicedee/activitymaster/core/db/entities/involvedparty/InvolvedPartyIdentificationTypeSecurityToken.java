package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "InvolvedPartyIdentificationTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyIdentificationTypeSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyIdentificationTypeSecurityToken, InvolvedPartyIdentificationTypeSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyIdentificationTypeSecurityTokenID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyIdentificationTypeID",
			referencedColumnName = "InvolvedPartyIdentificationTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedPartyIdentificationType base;

	public InvolvedPartyIdentificationTypeSecurityToken()
	{

	}

	public InvolvedPartyIdentificationTypeSecurityToken(Long involvedPartyIdentificationTypeSecurityTokenID)
	{
		this.id = involvedPartyIdentificationTypeSecurityTokenID;
	}

	public String toString()
	{
		return "InvolvedPartyIdentificationTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedPartyIdentificationType getBase()
	{
		return this.base;
	}

	public InvolvedPartyIdentificationTypeSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyIdentificationTypeSecurityToken setBase(InvolvedPartyIdentificationType base)
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
		if (!(o instanceof InvolvedPartyIdentificationTypeSecurityToken))
		{
			return false;
		}
		final InvolvedPartyIdentificationTypeSecurityToken other = (InvolvedPartyIdentificationTypeSecurityToken) o;
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
		return other instanceof InvolvedPartyIdentificationTypeSecurityToken;
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
