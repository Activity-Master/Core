package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationDataConceptXClassificationSecurityTokenQueryBuilder;
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
@Table(schema = "Classification", name = "ClassificationDataConceptXClassificationSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
public class ClassificationDataConceptXClassificationSecurityToken
		extends WarehouseSecurityTable<ClassificationDataConceptXClassificationSecurityToken, ClassificationDataConceptXClassificationSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationDataConceptXClassificationSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ClassificationDataConceptXClassificationID",
	            referencedColumnName = "ClassificationDataConceptXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ClassificationDataConceptXClassification base;
	
	public ClassificationDataConceptXClassificationSecurityToken()
	{
	
	}
	
	public ClassificationDataConceptXClassificationSecurityToken(java.lang.String classificationDataConceptXClassificationSecurityTokenID)
	{
		this.id = classificationDataConceptXClassificationSecurityTokenID;
	}
	
	public String toString()
	{
		return "ClassificationDataConceptXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public ClassificationDataConceptXClassificationSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ClassificationDataConceptXClassificationSecurityToken setBase(ClassificationDataConceptXClassification base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public ClassificationDataConceptXClassification getBase()
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
		ClassificationDataConceptXClassificationSecurityToken that = (ClassificationDataConceptXClassificationSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
