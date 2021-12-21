package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesTypeXResourceItemSecurityTokenQueryBuilder;
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
@Table(schema = "Rules", name = "RulesTypeXResourceItemSecurityToken")
@XmlRootElement

@Access(FIELD)
public class RulesTypeXResourceItemSecurityToken
		extends WarehouseSecurityTable<RulesTypeXResourceItemSecurityToken, RulesTypeXResourceItemSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesTypeXResourceItemSecurityTokenID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@JoinColumn(name = "RulesTypeXResourceItemID",
	            referencedColumnName = "RulesTypeXResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private RulesTypeXResourceItem base;
	
	public RulesTypeXResourceItemSecurityToken()
	{
	
	}
	
	public RulesTypeXResourceItemSecurityToken(UUID rulesXResourceItemSecurityTokenID)
	{
		this.id = rulesXResourceItemSecurityTokenID;
	}
	
	public String toString()
	{
		return "RulesTypeXResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public RulesTypeXResourceItemSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public RulesTypeXResourceItem getBase()
	{
		return this.base;
	}
	
	public RulesTypeXResourceItemSecurityToken setBase(RulesTypeXResourceItem base)
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
		if (!(o instanceof RulesTypeXResourceItemSecurityToken))
		{
			return false;
		}
		final RulesTypeXResourceItemSecurityToken other = (RulesTypeXResourceItemSecurityToken) o;
		if (!other.canEqual(this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		return this$id == null ? other$id == null : this$id.equals(other$id);
	}
	
	protected boolean canEqual(final Object other)
	{
		return other instanceof RulesTypeXResourceItemSecurityToken;
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
