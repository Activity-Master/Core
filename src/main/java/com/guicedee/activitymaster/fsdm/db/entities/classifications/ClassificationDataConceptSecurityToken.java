package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationDataConceptSecurityTokenQueryBuilder;

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
@Table(schema="Classification",name = "ClassificationDataConceptSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ClassificationDataConceptSecurityToken
		extends WarehouseSecurityTable<ClassificationDataConceptSecurityToken, ClassificationDataConceptSecurityTokenQueryBuilder, UUID>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "ClassificationDataConceptSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;

	@JoinColumn(name = "ClassificationDataConceptID",
			referencedColumnName = "ClassificationDataConceptID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ClassificationDataConcept base;

	public ClassificationDataConceptSecurityToken()
	{

	}

	public ClassificationDataConceptSecurityToken(UUID classificationDataConceptSecurityTokenID)
	{
		this.id = classificationDataConceptSecurityTokenID;
	}

	public String toString()
	{
		return "ClassificationDataConceptSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public UUID getId()
	{
		return this.id;
	}

	public ClassificationDataConcept getBase()
	{
		return this.base;
	}

	public ClassificationDataConceptSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}

	public ClassificationDataConceptSecurityToken setBase(ClassificationDataConcept base)
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
		if (!(o instanceof ClassificationDataConceptSecurityToken))
		{
			return false;
		}
		final ClassificationDataConceptSecurityToken other = (ClassificationDataConceptSecurityToken) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof ClassificationDataConceptSecurityToken;
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
