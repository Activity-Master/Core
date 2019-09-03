package com.armineasy.activitymaster.activitymaster.db.entities.classifications;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders.ClassificationXResourceItemSecurityTokenQueryBuilder;


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
@Table(name = "ClassificationXResourceItemSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ClassificationXResourceItemSecurityToken
		extends WarehouseSecurityTable<ClassificationXResourceItemSecurityToken, ClassificationXResourceItemSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationXResourceItemSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ClassificationXResourceItemID",
			referencedColumnName = "ClassificationXResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ClassificationXResourceItem base;

	public ClassificationXResourceItemSecurityToken()
	{

	}

	public ClassificationXResourceItemSecurityToken(Long classificationXResourceItemSecurityTokenID)
	{
		this.id = classificationXResourceItemSecurityTokenID;
	}

	public String toString()
	{
		return "ClassificationXResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public ClassificationXResourceItem getBase()
	{
		return this.base;
	}

	public ClassificationXResourceItemSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
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
