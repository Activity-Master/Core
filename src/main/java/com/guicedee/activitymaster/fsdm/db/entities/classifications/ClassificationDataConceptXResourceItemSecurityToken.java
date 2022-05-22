package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationDataConceptXResourceItemSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

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
		extends WarehouseSecurityTable<ClassificationDataConceptXResourceItemSecurityToken, ClassificationDataConceptXResourceItemSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationDataConceptXResourceItemSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
	@JoinColumn(name = "ClassificationDataConceptXResourceItemID",
	            referencedColumnName = "ClassificationDataConceptXResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ClassificationDataConceptXResourceItem base;
	
	public ClassificationDataConceptXResourceItemSecurityToken()
	{
	
	}
	
	public ClassificationDataConceptXResourceItemSecurityToken(UUID classificationDataConceptXResourceItemSecurityTokenID)
	{
		this.id = classificationDataConceptXResourceItemSecurityTokenID;
	}
	
	public String toString()
	{
		return "ClassificationDataConceptXResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public ClassificationDataConceptXResourceItemSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ClassificationDataConceptXResourceItem getBase()
	{
		return this.base;
	}
	
	public ClassificationDataConceptXResourceItemSecurityToken setBase(ClassificationDataConceptXResourceItem base)
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
		if (!(o instanceof ClassificationDataConceptXResourceItemSecurityToken))
		{
			return false;
		}
		final ClassificationDataConceptXResourceItemSecurityToken other = (ClassificationDataConceptXResourceItemSecurityToken) o;
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
		return other instanceof ClassificationDataConceptXResourceItemSecurityToken;
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
