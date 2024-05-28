package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationXResourceItemSecurityTokenQueryBuilder;
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
@Table(schema = "Classification", name = "ClassificationXResourceItemSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
public class ClassificationXResourceItemSecurityToken
		extends WarehouseSecurityTable<ClassificationXResourceItemSecurityToken, ClassificationXResourceItemSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationXResourceItemSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ClassificationXResourceItemID",
	            referencedColumnName = "ClassificationXResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ClassificationXResourceItem base;
	
	public ClassificationXResourceItemSecurityToken()
	{
	
	}
	
	public ClassificationXResourceItemSecurityToken(java.lang.String classificationXResourceItemSecurityTokenID)
	{
		this.id = classificationXResourceItemSecurityTokenID;
	}
	
	public String toString()
	{
		return "ClassificationXResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public ClassificationXResourceItemSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ClassificationXResourceItemSecurityToken setBase(ClassificationXResourceItem base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public ClassificationXResourceItem getBase()
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
		ClassificationXResourceItemSecurityToken that = (ClassificationXResourceItemSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
