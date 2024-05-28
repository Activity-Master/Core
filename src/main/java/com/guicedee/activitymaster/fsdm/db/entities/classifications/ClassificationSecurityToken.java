package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationsSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.Objects;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Classification", name = "ClassificationSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
public class ClassificationSecurityToken
		extends WarehouseSecurityTable<ClassificationSecurityToken, ClassificationsSecurityTokenQueryBuilder, String>
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
	
	public String getId()
	{
		return id;
	}
	
	public Classification getBase()
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
		ClassificationSecurityToken that = (ClassificationSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
