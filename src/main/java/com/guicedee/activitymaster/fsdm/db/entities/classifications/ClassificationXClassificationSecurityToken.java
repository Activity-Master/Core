package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationXClassificationSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Classification", name = "ClassificationXClassificationSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class ClassificationXClassificationSecurityToken
		extends WarehouseSecurityTable<ClassificationXClassificationSecurityToken, ClassificationXClassificationSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationXClassificationSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
	@JoinColumn(name = "ClassificationXClassificationID",
	            referencedColumnName = "ClassificationXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ClassificationXClassification base;
	
	public ClassificationXClassificationSecurityToken()
	{
	
	}
	
	public ClassificationXClassificationSecurityToken(UUID classificationXClassificationSecurityTokenID)
	{
		this.id = classificationXClassificationSecurityTokenID;
	}
	
	public String toString()
	{
		return "ClassificationXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public ClassificationXClassificationSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ClassificationXClassification getBase()
	{
		return this.base;
	}
	
	public ClassificationXClassificationSecurityToken setBase(ClassificationXClassification base)
	{
		this.base = base;
		return this;
	}
	
	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ClassificationXClassificationSecurityToken))
		{
			return false;
		}
		final ClassificationXClassificationSecurityToken other = (ClassificationXClassificationSecurityToken) o;
		if (!other.canEqual(this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		return this$id == null ? other$id == null : this$id.equals(other$id);
	}
	
	protected boolean canEqual(final Object other)
	{
		return other instanceof ClassificationXClassificationSecurityToken;
	}
	
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
