package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationXResourceItemSecurityTokenQueryBuilder;
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
@Table(schema = "Classification", name = "ClassificationXResourceItemSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class ClassificationXResourceItemSecurityToken
		extends WarehouseSecurityTable<ClassificationXResourceItemSecurityToken, ClassificationXResourceItemSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationXResourceItemSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
	@JoinColumn(name = "ClassificationXResourceItemID",
	            referencedColumnName = "ClassificationXResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ClassificationXResourceItem base;
	
	public ClassificationXResourceItemSecurityToken()
	{
	
	}
	
	public ClassificationXResourceItemSecurityToken(UUID classificationXResourceItemSecurityTokenID)
	{
		this.id = classificationXResourceItemSecurityTokenID;
	}
	
	public String toString()
	{
		return "ClassificationXResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public ClassificationXResourceItemSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ClassificationXResourceItem getBase()
	{
		return this.base;
	}
	
	public ClassificationXResourceItemSecurityToken setBase(ClassificationXResourceItem base)
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
		if (!(o instanceof ClassificationXResourceItemSecurityToken))
		{
			return false;
		}
		final ClassificationXResourceItemSecurityToken other = (ClassificationXResourceItemSecurityToken) o;
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
		return other instanceof ClassificationXResourceItemSecurityToken;
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
