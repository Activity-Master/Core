package com.guicedee.activitymaster.core.db.entities.enterprise;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.enterprise.builders.EnterpriseXClassificationSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static javax.persistence.AccessType.*;

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
		extends WarehouseSecurityTable<EnterpriseXClassificationSecurityToken, EnterpriseXClassificationSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EnterpriseXClassificationSecurityTokenID")
	private Long id;

	@JoinColumn(name = "EnterpriseXClassificationID",
			referencedColumnName = "EnterpriseXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private EnterpriseXClassification base;

	public EnterpriseXClassificationSecurityToken()
	{

	}

	public EnterpriseXClassificationSecurityToken(Long classificationXResourceItemSecurityTokenID)
	{
		this.id = classificationXResourceItemSecurityTokenID;
	}

	public String toString()
	{
		return "EnterpriseXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public EnterpriseXClassification getBase()
	{
		return this.base;
	}

	public EnterpriseXClassificationSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
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
