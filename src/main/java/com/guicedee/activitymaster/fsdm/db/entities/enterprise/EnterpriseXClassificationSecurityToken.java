package com.guicedee.activitymaster.fsdm.db.entities.enterprise;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseXClassificationSecurityTokenQueryBuilder;
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
@Table(name = "EnterpriseXClassificationSecurityToken")
@XmlRootElement

@Access(FIELD)
public class EnterpriseXClassificationSecurityToken
		extends WarehouseSecurityTable<EnterpriseXClassificationSecurityToken, EnterpriseXClassificationSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EnterpriseXClassificationSecurityTokenID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@JoinColumn(name = "EnterpriseXClassificationID",
	            referencedColumnName = "EnterpriseXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private EnterpriseXClassification base;
	
	public EnterpriseXClassificationSecurityToken()
	{
	
	}
	
	public EnterpriseXClassificationSecurityToken(UUID classificationXResourceItemSecurityTokenID)
	{
		this.id = classificationXResourceItemSecurityTokenID;
	}
	
	public String toString()
	{
		return "EnterpriseXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public EnterpriseXClassificationSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public EnterpriseXClassification getBase()
	{
		return this.base;
	}
	
	public EnterpriseXClassificationSecurityToken setBase(EnterpriseXClassification base)
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
		if (!(o instanceof EnterpriseXClassificationSecurityToken))
		{
			return false;
		}
		final EnterpriseXClassificationSecurityToken other = (EnterpriseXClassificationSecurityToken) o;
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
		return other instanceof EnterpriseXClassificationSecurityToken;
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
