package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationXClassificationSecurityTokenQueryBuilder;
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
@Table(schema = "Classification", name = "ClassificationXClassificationSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@EqualsAndHashCode(of = "id", callSuper = false)
public class ClassificationXClassificationSecurityToken
		extends IWarehouseSecurityTable<ClassificationXClassificationSecurityToken, ClassificationXClassificationSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationXClassificationSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ClassificationXClassificationID",
	            referencedColumnName = "ClassificationXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ClassificationXClassification base;
	
	public ClassificationXClassificationSecurityToken()
	{
	
	}
	
	public ClassificationXClassificationSecurityToken(java.lang.String classificationXClassificationSecurityTokenID)
	{
		this.id = classificationXClassificationSecurityTokenID;
	}
	
	public String toString()
	{
		return "ClassificationXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public ClassificationXClassificationSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ClassificationXClassificationSecurityToken setBase(ClassificationXClassification base)
	{
		this.base = base;
		return this;
	}
}
