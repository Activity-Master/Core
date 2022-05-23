package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Party", name = "InvolvedPartyIdentificationTypeSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class InvolvedPartyIdentificationTypeSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyIdentificationTypeSecurityToken, InvolvedPartyIdentificationTypeSecurityTokenQueryBuilder, java.lang.String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyIdentificationTypeSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "InvolvedPartyIdentificationTypeID",
	            referencedColumnName = "InvolvedPartyIdentificationTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private InvolvedPartyIdentificationType base;
	
	public InvolvedPartyIdentificationTypeSecurityToken()
	{
	
	}
	
	public InvolvedPartyIdentificationTypeSecurityToken(java.lang.String involvedPartyIdentificationTypeSecurityTokenID)
	{
		this.id = involvedPartyIdentificationTypeSecurityTokenID;
	}
	
	public String toString()
	{
		return "InvolvedPartyIdentificationTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public InvolvedPartyIdentificationTypeSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyIdentificationType getBase()
	{
		return this.base;
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
