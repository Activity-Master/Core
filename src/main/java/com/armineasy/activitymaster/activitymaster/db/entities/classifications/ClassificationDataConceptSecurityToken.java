package com.armineasy.activitymaster.activitymaster.db.entities.classifications;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders.ClassificationDataConceptSecurityTokenQueryBuilder;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ClassificationDataConceptSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ClassificationDataConceptSecurityToken
		extends WarehouseSecurityTable<ClassificationDataConceptSecurityToken, ClassificationDataConceptSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationDataConceptSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ClassificationDataConceptID",
			referencedColumnName = "ClassificationDataConceptID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ClassificationDataConcept base;

	public ClassificationDataConceptSecurityToken()
	{

	}

	public ClassificationDataConceptSecurityToken(Long classificationDataConceptSecurityTokenID)
	{
		this.id = classificationDataConceptSecurityTokenID;
	}

	public String toString()
	{
		return "ClassificationDataConceptSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public ClassificationDataConcept getBase()
	{
		return this.base;
	}

	public ClassificationDataConceptSecurityToken setId(Long id)
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
