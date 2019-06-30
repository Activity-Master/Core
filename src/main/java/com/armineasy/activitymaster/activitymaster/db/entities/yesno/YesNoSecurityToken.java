package com.armineasy.activitymaster.activitymaster.db.entities.yesno;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.builders.YesNoSecurityTokenQueryBuilder;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "YesNoSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class YesNoSecurityToken
		extends WarehouseSecurityTable<YesNoSecurityToken, YesNoSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "YesNoSecurityTokenID")
	private Long id;

	@JoinColumn(name = "YesNoID",
			referencedColumnName = "YesNoID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private YesNo base;

	public YesNoSecurityToken()
	{

	}

	public YesNoSecurityToken(Long systemsSecurityTokenID)
	{
		this.id = systemsSecurityTokenID;
	}

	public String toString()
	{
		return "YesNoSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public YesNo getBase()
	{
		return this.base;
	}

	public YesNoSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public YesNoSecurityToken setBase(YesNo base)
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
		if (!(o instanceof YesNoSecurityToken))
		{
			return false;
		}
		final YesNoSecurityToken other = (YesNoSecurityToken) o;
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
		return other instanceof YesNoSecurityToken;
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
