package com.armineasy.activitymaster.activitymaster.db.entities.yesno;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.builders.YesNoXClassificationSecurityTokenQueryBuilder;
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
@Table(name = "YesNoXClassificationSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class YesNoXClassificationSecurityToken
		extends WarehouseSecurityTable<YesNoXClassificationSecurityToken, YesNoXClassificationSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "YesNoXClassificationSecurityTokenID")
	private Long id;

	@JoinColumn(name = "YesNoXClassificationID",
			referencedColumnName = "YesNoXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private YesNoXClassification base;

	public YesNoXClassificationSecurityToken()
	{

	}

	public YesNoXClassificationSecurityToken(Long systemXClassificationSecurityTokenID)
	{
		this.id = systemXClassificationSecurityTokenID;
	}

	public String toString()
	{
		return "YesNoXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public YesNoXClassification getBase()
	{
		return this.base;
	}

	public YesNoXClassificationSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public YesNoXClassificationSecurityToken setBase(YesNoXClassification base)
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
		if (!(o instanceof YesNoXClassificationSecurityToken))
		{
			return false;
		}
		final YesNoXClassificationSecurityToken other = (YesNoXClassificationSecurityToken) o;
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
		return other instanceof YesNoXClassificationSecurityToken;
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
