package com.guicedee.activitymaster.core.db.entities.arrangement;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementXResourceItemSecurityTokenQueryBuilder;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Arrangement",name = "ArrangementXResourceItemSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ArrangementXResourceItemSecurityToken
		extends WarehouseSecurityTable<ArrangementXResourceItemSecurityToken, ArrangementXResourceItemSecurityTokenQueryBuilder, java.util.UUID>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "ArrangementXResourceItemSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "ArrangementXResourceItemID",
			referencedColumnName = "ArrangementXResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ArrangementXResourceItem base;

	public ArrangementXResourceItemSecurityToken()
	{

	}

	public ArrangementXResourceItemSecurityToken(UUID arrangementXResourceItemSecurityTokenID)
	{
		this.id = arrangementXResourceItemSecurityTokenID;
	}

	public String toString()
	{
		return "ArrangementXResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public ArrangementXResourceItem getBase()
	{
		return this.base;
	}

	public ArrangementXResourceItemSecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public ArrangementXResourceItemSecurityToken setBase(ArrangementXResourceItem base)
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
		if (!(o instanceof ArrangementXResourceItemSecurityToken))
		{
			return false;
		}
		final ArrangementXResourceItemSecurityToken other = (ArrangementXResourceItemSecurityToken) o;
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
		return other instanceof ArrangementXResourceItemSecurityToken;
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
