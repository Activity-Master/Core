package com.guicedee.activitymaster.core.db.entities.classifications;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.classifications.builders.ClassificationDataConceptXResourceItemSecurityTokenQueryBuilder;

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
@Table(name = "ClassificationDataConceptXResourceItemSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ClassificationDataConceptXResourceItemSecurityToken
		extends WarehouseSecurityTable<ClassificationDataConceptXResourceItemSecurityToken, ClassificationDataConceptXResourceItemSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationDataConceptXResourceItemSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ClassificationDataConceptXResourceItemID",
			referencedColumnName = "ClassificationDataConceptXResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ClassificationDataConceptXResourceItem base;

	public ClassificationDataConceptXResourceItemSecurityToken()
	{

	}

	public ClassificationDataConceptXResourceItemSecurityToken(Long classificationDataConceptXResourceItemSecurityTokenID)
	{
		this.id = classificationDataConceptXResourceItemSecurityTokenID;
	}

	public String toString()
	{
		return "ClassificationDataConceptXResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public ClassificationDataConceptXResourceItem getBase()
	{
		return this.base;
	}

	public ClassificationDataConceptXResourceItemSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
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
