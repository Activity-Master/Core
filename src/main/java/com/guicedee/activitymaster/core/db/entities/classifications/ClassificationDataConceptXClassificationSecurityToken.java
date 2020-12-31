package com.guicedee.activitymaster.core.db.entities.classifications;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.classifications.builders.ClassificationDataConceptXClassificationSecurityTokenQueryBuilder;

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
@Table(schema="Classification",name = "ClassificationDataConceptXClassificationSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ClassificationDataConceptXClassificationSecurityToken
		extends WarehouseSecurityTable<ClassificationDataConceptXClassificationSecurityToken, ClassificationDataConceptXClassificationSecurityTokenQueryBuilder, java.util.UUID>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "ClassificationDataConceptXClassificationSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "ClassificationDataConceptXClassificationID",
			referencedColumnName = "ClassificationDataConceptXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ClassificationDataConceptXClassification base;

	public ClassificationDataConceptXClassificationSecurityToken()
	{

	}

	public ClassificationDataConceptXClassificationSecurityToken(UUID classificationDataConceptXClassificationSecurityTokenID)
	{
		this.id = classificationDataConceptXClassificationSecurityTokenID;
	}

	public String toString()
	{
		return "ClassificationDataConceptXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public ClassificationDataConceptXClassification getBase()
	{
		return this.base;
	}

	public ClassificationDataConceptXClassificationSecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public ClassificationDataConceptXClassificationSecurityToken setBase(ClassificationDataConceptXClassification base)
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
		if (!(o instanceof ClassificationDataConceptXClassificationSecurityToken))
		{
			return false;
		}
		final ClassificationDataConceptXClassificationSecurityToken other = (ClassificationDataConceptXClassificationSecurityToken) o;
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
		return other instanceof ClassificationDataConceptXClassificationSecurityToken;
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
