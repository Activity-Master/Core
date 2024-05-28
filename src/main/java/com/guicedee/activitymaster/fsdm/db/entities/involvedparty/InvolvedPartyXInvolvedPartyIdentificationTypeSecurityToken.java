package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenQueryBuilder;
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
@Table(schema = "Party", name = "InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)

public class InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken,
						InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "InvolvedPartyXInvolvedPartyIdentificationTypeID",
	            referencedColumnName = "InvolvedPartyXInvolvedPartyIdentificationTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private InvolvedPartyXInvolvedPartyIdentificationType base;
	
	public InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken()
	{
	
	}
	
	public InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken(java.lang.String involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenID)
	{
		this.id = involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenID;
	}
	
	public String toString()
	{
		return "InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyXInvolvedPartyIdentificationType getBase()
	{
		return this.base;
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
