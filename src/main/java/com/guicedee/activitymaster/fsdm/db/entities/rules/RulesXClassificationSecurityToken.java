package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesXClassificationSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Rules", name = "RulesXClassificationSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class RulesXClassificationSecurityToken
		extends WarehouseSecurityTable<RulesXClassificationSecurityToken, RulesXClassificationSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesXClassificationSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
	@JoinColumn(name = "RulesXClassificationID",
	            referencedColumnName = "RulesXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private RulesXClassification base;
	
	public RulesXClassificationSecurityToken()
	{
	
	}
	
	public RulesXClassificationSecurityToken(UUID rulesXClassificationSecurityTokenID)
	{
		this.id = rulesXClassificationSecurityTokenID;
	}
	
	public String toString()
	{
		return "RulesXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public RulesXClassificationSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public RulesXClassification getBase()
	{
		return this.base;
	}
	
	public RulesXClassificationSecurityToken setBase(RulesXClassification base)
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
		if (!(o instanceof RulesXClassificationSecurityToken))
		{
			return false;
		}
		final RulesXClassificationSecurityToken other = (RulesXClassificationSecurityToken) o;
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
		return other instanceof RulesXClassificationSecurityToken;
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
