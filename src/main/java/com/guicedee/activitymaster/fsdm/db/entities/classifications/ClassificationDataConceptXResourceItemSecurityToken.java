package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationDataConceptXResourceItemSecurityTokenQueryBuilder;
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
@Table(schema = "Classification", name = "ClassificationDataConceptXResourceItemSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
@EqualsAndHashCode(of="id",callSuper = false)
public class ClassificationDataConceptXResourceItemSecurityToken
		extends IWarehouseSecurityTable<ClassificationDataConceptXResourceItemSecurityToken, ClassificationDataConceptXResourceItemSecurityTokenQueryBuilder, String>
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
}
