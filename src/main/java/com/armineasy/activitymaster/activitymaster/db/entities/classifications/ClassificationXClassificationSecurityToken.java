package com.armineasy.activitymaster.activitymaster.db.entities.classifications;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders.ClassificationXClassificationSecurityTokenQueryBuilder;
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
@Table(name = "ClassificationXClassificationSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ClassificationXClassificationSecurityToken
		extends WarehouseSecurityTable<ClassificationXClassificationSecurityToken, ClassificationXClassificationSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationXClassificationSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ClassificationXClassificationID",
			referencedColumnName = "ClassificationXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ClassificationXClassification base;

	public ClassificationXClassificationSecurityToken()
	{

	}

	public ClassificationXClassificationSecurityToken(Long classificationXClassificationSecurityTokenID)
	{
		this.id = classificationXClassificationSecurityTokenID;
	}

	public String toString()
	{
		return "ClassificationXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public ClassificationXClassification getBase()
	{
		return this.base;
	}

	public ClassificationXClassificationSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
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
