package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationDataConceptSecurityTokenQueryBuilder;
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
@Table(schema = "Classification", name = "ClassificationDataConceptSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
public class ClassificationDataConceptSecurityToken
		extends WarehouseSecurityTable<ClassificationDataConceptSecurityToken, ClassificationDataConceptSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationDataConceptSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ClassificationDataConceptID",
	            referencedColumnName = "ClassificationDataConceptID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ClassificationDataConcept base;
	
	public ClassificationDataConceptSecurityToken()
	{
	
	}
	
	public ClassificationDataConceptSecurityToken(java.lang.String classificationDataConceptSecurityTokenID)
	{
		this.id = classificationDataConceptSecurityTokenID;
	}
	
	public String toString()
	{
		return "ClassificationDataConceptSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public ClassificationDataConceptSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ClassificationDataConceptSecurityToken setBase(ClassificationDataConcept base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public ClassificationDataConcept getBase()
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
		ClassificationDataConceptSecurityToken that = (ClassificationDataConceptSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
