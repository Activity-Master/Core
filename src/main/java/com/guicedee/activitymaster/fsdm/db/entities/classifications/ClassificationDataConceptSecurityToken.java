package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationDataConceptSecurityTokenQueryBuilder;
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
@Table(schema = "Classification", name = "ClassificationDataConceptSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@EqualsAndHashCode(of = "id", callSuper = false)
public class ClassificationDataConceptSecurityToken
		extends IWarehouseSecurityTable<ClassificationDataConceptSecurityToken, ClassificationDataConceptSecurityTokenQueryBuilder, String>
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
}
