package com.guicedee.activitymaster.core.db.entities.arrangement;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementXInvolvedPartySecurityTokenQueryBuilder;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Arrangement",name = "ArrangementXInvolvedPartySecurityToken")
@XmlRootElement

@Access(FIELD)
public class ArrangementXInvolvedPartySecurityToken
		extends WarehouseSecurityTable<ArrangementXInvolvedPartySecurityToken, ArrangementXInvolvedPartySecurityTokenQueryBuilder, java.util.UUID>
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "ArrangementXInvolvedPartySecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "ArrangementXInvolvedPartyID",
			referencedColumnName = "ArrangementXInvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ArrangementXInvolvedParty base;

	public ArrangementXInvolvedPartySecurityToken()
	{

	}

	public ArrangementXInvolvedPartySecurityToken(UUID arrangementXInvolvedPartySecurityTokenID)
	{
		this.id = arrangementXInvolvedPartySecurityTokenID;
	}

	public String toString()
	{
		return "ArrangementXInvolvedPartySecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public ArrangementXInvolvedParty getBase()
	{
		return this.base;
	}

	public ArrangementXInvolvedPartySecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public ArrangementXInvolvedPartySecurityToken setBase(ArrangementXInvolvedParty base)
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
		if (!(o instanceof ArrangementXInvolvedPartySecurityToken))
		{
			return false;
		}
		final ArrangementXInvolvedPartySecurityToken other = (ArrangementXInvolvedPartySecurityToken) o;
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
		return other instanceof ArrangementXInvolvedPartySecurityToken;
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
