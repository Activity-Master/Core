package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationsSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Getter
@Entity
@Table(schema = "Classification", name = "ClassificationSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
@EqualsAndHashCode(of="id",callSuper = false)
public class ClassificationSecurityToken
		extends IWarehouseSecurityTable<ClassificationSecurityToken, ClassificationsSecurityTokenQueryBuilder, String>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ClassificationID",
	            referencedColumnName = "ClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Classification base;
	
	public ClassificationSecurityToken()
	{
	
	}
	
	public ClassificationSecurityToken(java.lang.String classificationSecurityTokenID)
	{
		this.id = classificationSecurityTokenID;
	}
	
	public String toString()
	{
		return "ClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public ClassificationSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ClassificationSecurityToken setBase(Classification base)
	{
		this.base = base;
		return this;
	}
}
