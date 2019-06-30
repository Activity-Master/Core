package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXResourceItemQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ArrangementXResourceItem")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ArrangementXResourceItem
		extends WarehouseClassificationRelationshipTable<Arrangement, ResourceItem, ArrangementXResourceItem, ArrangementXResourceItemQueryBuilder, Long, ArrangementXResourceItemSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementXResourceItemID")
	private Long id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList;

	@JoinColumn(name = "ArrangementID",
			referencedColumnName = "ArrangementID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Arrangement arrangementID;

	@JoinColumn(name = "ResourceItemID",
			referencedColumnName = "ResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ResourceItem resourceItemID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItemSecurityToken> securities;

	public ArrangementXResourceItem()
	{

	}

	public ArrangementXResourceItem(Long arrangementXResourceItemID)
	{
		this.id = arrangementXResourceItemID;
	}

	@Override
	protected ArrangementXResourceItemSecurityToken configureDefaultsForNewToken(ArrangementXResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "ArrangementXResourceItem(id=" + this.getId() + ", arrangementXResourceItemSecurityTokenList=" + this.getArrangementXResourceItemSecurityTokenList() +
		       ", arrangementID=" + this.getArrangementID() + ", resourceItemID=" + this.getResourceItemID() + ", securities=" + this.getSecurities() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public List<ArrangementXResourceItemSecurityToken> getArrangementXResourceItemSecurityTokenList()
	{
		return this.arrangementXResourceItemSecurityTokenList;
	}

	public Arrangement getArrangementID()
	{
		return this.arrangementID;
	}

	public ResourceItem getResourceItemID()
	{
		return this.resourceItemID;
	}

	public List<ArrangementXResourceItemSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public ArrangementXResourceItem setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ArrangementXResourceItem setArrangementXResourceItemSecurityTokenList(List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList)
	{
		this.arrangementXResourceItemSecurityTokenList = arrangementXResourceItemSecurityTokenList;
		return this;
	}

	public ArrangementXResourceItem setArrangementID(Arrangement arrangementID)
	{
		this.arrangementID = arrangementID;
		return this;
	}

	public ArrangementXResourceItem setResourceItemID(ResourceItem resourceItemID)
	{
		this.resourceItemID = resourceItemID;
		return this;
	}

	public ArrangementXResourceItem setSecurities(List<ArrangementXResourceItemSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ArrangementXResourceItem))
		{
			return false;
		}
		final ArrangementXResourceItem other = (ArrangementXResourceItem) o;
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
		return other instanceof ArrangementXResourceItem;
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
