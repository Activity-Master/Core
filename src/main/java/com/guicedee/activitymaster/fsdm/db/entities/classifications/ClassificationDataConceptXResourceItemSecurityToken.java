package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationDataConceptXResourceItemSecurityTokenQueryBuilder;
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
@Table(schema = "Classification", name = "ClassificationDataConceptXResourceItemSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
public class ClassificationDataConceptXResourceItemSecurityToken
		extends WarehouseSecurityTable<ClassificationDataConceptXResourceItemSecurityToken, ClassificationDataConceptXResourceItemSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationDataConceptXResourceItemSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ClassificationDataConceptXResourceItemID",
	            referencedColumnName = "ClassificationDataConceptXResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ClassificationDataConceptXResourceItem base;
	
	public ClassificationDataConceptXResourceItemSecurityToken()
	{
	
	}
	
	public ClassificationDataConceptXResourceItemSecurityToken(java.lang.String classificationDataConceptXResourceItemSecurityTokenID)
	{
		this.id = classificationDataConceptXResourceItemSecurityTokenID;
	}
	
	public String toString()
	{
		return "ClassificationDataConceptXResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public ClassificationDataConceptXResourceItemSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ClassificationDataConceptXResourceItemSecurityToken setBase(ClassificationDataConceptXResourceItem base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public ClassificationDataConceptXResourceItem getBase()
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
		ClassificationDataConceptXResourceItemSecurityToken that = (ClassificationDataConceptXResourceItemSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
