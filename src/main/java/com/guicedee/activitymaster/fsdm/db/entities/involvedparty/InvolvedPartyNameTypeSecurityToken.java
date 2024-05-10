package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyNameTypeSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Party", name = "InvolvedPartyNameTypeSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class InvolvedPartyNameTypeSecurityToken
		extends IWarehouseSecurityTable<InvolvedPartyNameTypeSecurityToken, InvolvedPartyNameTypeSecurityTokenQueryBuilder, String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyNameTypeSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "InvolvedPartyNameTypeID",
	            referencedColumnName = "InvolvedPartyNameTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private InvolvedPartyNameType base;
	
	public InvolvedPartyNameTypeSecurityToken()
	{
	
	}
	
	public InvolvedPartyNameTypeSecurityToken(java.lang.String involvedPartyNameTypeSecurityTokenID)
	{
		this.id = involvedPartyNameTypeSecurityTokenID;
	}
	
	public String toString()
	{
		return "InvolvedPartyNameTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public InvolvedPartyNameTypeSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyNameType getBase()
	{
		return this.base;
	}
	
	public InvolvedPartyNameTypeSecurityToken setBase(InvolvedPartyNameType base)
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
		if (!(o instanceof InvolvedPartyNameTypeSecurityToken))
		{
			return false;
		}
		final InvolvedPartyNameTypeSecurityToken other = (InvolvedPartyNameTypeSecurityToken) o;
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
		return other instanceof InvolvedPartyNameTypeSecurityToken;
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
