package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXRulesSecurityTokenQueryBuilder;
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
@Table(schema = "Arrangement", name = "ArrangementXRulesSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class ArrangementXRulesSecurityToken
		extends WarehouseSecurityTable<ArrangementXRulesSecurityToken, ArrangementXRulesSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXRulesSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
	@JoinColumn(name = "ArrangementXRulesID",
	            referencedColumnName = "ArrangementXRulesID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ArrangementXRules base;
	
	public ArrangementXRulesSecurityToken()
	{
	
	}
	
	public ArrangementXRulesSecurityToken(UUID arrangementXRulesSecurityTokenID)
	{
		this.id = arrangementXRulesSecurityTokenID;
	}
	
	public String toString()
	{
		return "ArrangementXRulesSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public ArrangementXRulesSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementXRules getBase()
	{
		return this.base;
	}
	
	public ArrangementXRulesSecurityToken setBase(ArrangementXRules base)
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
		if (!(o instanceof ArrangementXRulesSecurityToken))
		{
			return false;
		}
		final ArrangementXRulesSecurityToken other = (ArrangementXRulesSecurityToken) o;
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
		return other instanceof ArrangementXRulesSecurityToken;
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
