package com.guicedee.activitymaster.fsdm.db.entities.enterprise;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Getter
@Entity
@Table(name = "EnterpriseSecurityToken",
       schema = "dbo")
@XmlRootElement

@Access(AccessType.FIELD)
@EqualsAndHashCode(of="id",callSuper = false)
public class EnterpriseSecurityToken
		extends IWarehouseSecurityTable<EnterpriseSecurityToken, EnterpriseSecurityTokenQueryBuilder, String>
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
}
