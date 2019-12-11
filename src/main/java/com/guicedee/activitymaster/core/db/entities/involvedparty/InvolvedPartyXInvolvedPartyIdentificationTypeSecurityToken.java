package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenQueryBuilder;

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
@Table(schema="Party",name = "InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken,
				                              InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyXInvolvedPartyIdentificationTypeID",
			referencedColumnName = "InvolvedPartyXInvolvedPartyIdentificationTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private InvolvedPartyXInvolvedPartyIdentificationType base;

	public InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken()
	{

	}

	public InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken(Long involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenID)
	{
		this.id = involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenID;
	}

	public String toString()
	{
		return "InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedPartyXInvolvedPartyIdentificationType getBase()
	{
		return this.base;
	}

	public InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken setBase(InvolvedPartyXInvolvedPartyIdentificationType base)
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
		if (!(o instanceof InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken))
		{
			return false;
		}
		final InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken other = (InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken) o;
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
		return other instanceof InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken;
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
