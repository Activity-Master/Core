package com.guicedee.activitymaster.fsdm.db.entities.enterprise;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EnterpriseSecurityToken",
       schema = "dbo")
@XmlRootElement

@Access(AccessType.FIELD)
public class EnterpriseSecurityToken
		extends WarehouseSecurityTable<EnterpriseSecurityToken, EnterpriseSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EnterpriseSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "EnterpriseID",
	            referencedColumnName = "EnterpriseID",
	            nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Enterprise base;
	
	public EnterpriseSecurityToken()
	{
	
	}
	
	public EnterpriseSecurityToken(java.lang.String enterpriseSecurityTokenID)
	{
		this.id = enterpriseSecurityTokenID;
	}
	
	public String toString()
	{
		return "EnterpriseSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public EnterpriseSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public EnterpriseSecurityToken setBase(Enterprise base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public Enterprise getBase()
	{
		return base;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		EnterpriseSecurityToken that = (EnterpriseSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
