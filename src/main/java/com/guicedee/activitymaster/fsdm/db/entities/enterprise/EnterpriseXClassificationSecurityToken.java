package com.guicedee.activitymaster.fsdm.db.entities.enterprise;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseXClassificationSecurityTokenQueryBuilder;
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
@Table(name = "EnterpriseXClassificationSecurityToken",
       schema = "dbo")
@XmlRootElement

@Access(AccessType.FIELD)
@EqualsAndHashCode(of = "id", callSuper = false)
public class EnterpriseXClassificationSecurityToken
		extends IWarehouseSecurityTable<EnterpriseXClassificationSecurityToken, EnterpriseXClassificationSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EnterpriseXClassificationSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "EnterpriseXClassificationID",
	            referencedColumnName = "EnterpriseXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private EnterpriseXClassification base;
	
	public EnterpriseXClassificationSecurityToken()
	{
	
	}
	
	public EnterpriseXClassificationSecurityToken(java.lang.String classificationXResourceItemSecurityTokenID)
	{
		this.id = classificationXResourceItemSecurityTokenID;
	}
	
	public String toString()
	{
		return "EnterpriseXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public EnterpriseXClassificationSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public EnterpriseXClassificationSecurityToken setBase(EnterpriseXClassification base)
	{
		this.base = base;
		return this;
	}
	
}
