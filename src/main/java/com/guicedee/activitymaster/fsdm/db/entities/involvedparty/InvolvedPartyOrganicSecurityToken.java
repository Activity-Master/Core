package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyOrganicSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.UUID;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Party", name = "InvolvedPartyOrganicSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class InvolvedPartyOrganicSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyOrganicSecurityToken, InvolvedPartyOrganicSecurityTokenQueryBuilder, UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyOrganicSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
	@JoinColumn(name = "InvolvedPartyOrganicID",
	            referencedColumnName = "InvolvedPartyOrganicID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private InvolvedPartyOrganic base;
	
	public InvolvedPartyOrganicSecurityToken()
	{
	
	}
	
	public InvolvedPartyOrganicSecurityToken(UUID involvedPartyOrganicSecurityTokenID)
	{
		this.id = involvedPartyOrganicSecurityTokenID;
	}
	
	public String toString()
	{
		return "InvolvedPartyOrganicSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public InvolvedPartyOrganicSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyOrganic getBase()
	{
		return this.base;
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
