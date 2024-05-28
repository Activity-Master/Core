package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyNonOrganicSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Party", name = "InvolvedPartyNonOrganicSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class InvolvedPartyNonOrganicSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyNonOrganicSecurityToken, InvolvedPartyNonOrganicSecurityTokenQueryBuilder, String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyNonOrganicSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "InvolvedPartyNonOrganicID",
	            referencedColumnName = "InvolvedPartyNonOrganicID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private InvolvedPartyNonOrganic base;
	
	public InvolvedPartyNonOrganicSecurityToken()
	{
	
	}
	
	public InvolvedPartyNonOrganicSecurityToken(java.lang.String involvedPartyNonOrganicSecurityTokenID)
	{
		this.id = involvedPartyNonOrganicSecurityTokenID;
	}
	
	public String toString()
	{
		return "InvolvedPartyNonOrganicSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public InvolvedPartyNonOrganicSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyNonOrganic getBase()
	{
		return this.base;
	}
	
	public InvolvedPartyNonOrganicSecurityToken setBase(InvolvedPartyNonOrganic base)
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
		if (!(o instanceof InvolvedPartyNonOrganicSecurityToken))
		{
			return false;
		}
		final InvolvedPartyNonOrganicSecurityToken other = (InvolvedPartyNonOrganicSecurityToken) o;
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
		return other instanceof InvolvedPartyNonOrganicSecurityToken;
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
