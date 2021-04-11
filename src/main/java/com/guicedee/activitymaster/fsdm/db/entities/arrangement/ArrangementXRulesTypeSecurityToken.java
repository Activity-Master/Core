package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXRulesTypeSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Arrangement",name = "ArrangementXRulesTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ArrangementXRulesTypeSecurityToken
		extends WarehouseSecurityTable<ArrangementXRulesTypeSecurityToken, ArrangementXRulesTypeSecurityTokenQueryBuilder, UUID>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "ArrangementXRulesTypeSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;

	@JoinColumn(name = "ArrangementXRulesTypeID",
			referencedColumnName = "ArrangementXRulesTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ArrangementXRulesType base;

	public ArrangementXRulesTypeSecurityToken()
	{

	}

	public ArrangementXRulesTypeSecurityToken(UUID arrangementXRulesSecurityTokenID)
	{
		this.id = arrangementXRulesSecurityTokenID;
	}

	public String toString()
	{
		return "ArrangementXRulesTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public UUID getId()
	{
		return this.id;
	}

	public ArrangementXRulesType getBase()
	{
		return this.base;
	}

	public ArrangementXRulesTypeSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}

	public ArrangementXRulesTypeSecurityToken setBase(ArrangementXRulesType base)
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
		if (!(o instanceof ArrangementXRulesTypeSecurityToken))
		{
			return false;
		}
		final ArrangementXRulesTypeSecurityToken other = (ArrangementXRulesTypeSecurityToken) o;
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
		return other instanceof ArrangementXRulesTypeSecurityToken;
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
