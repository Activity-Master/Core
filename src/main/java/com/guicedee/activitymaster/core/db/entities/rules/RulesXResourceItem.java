package com.guicedee.activitymaster.core.db.entities.rules;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesXResourceItemQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRules;
import com.guicedee.activitymaster.core.services.dto.IResourceItem;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Rules",
		name = "RulesXResourceItem")
@XmlRootElement
@Access(FIELD)
public class RulesXResourceItem
		extends WarehouseClassificationRelationshipTable<Rules,
				                                                ResourceItem,
				                                                RulesXResourceItem,
				                                                RulesXResourceItemQueryBuilder,
				                                                java.util.UUID,
				                                                RulesXResourceItemSecurityToken,
				                                                IRules<?>, IResourceItem<?>>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "RulesXResourceItemID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<RulesXResourceItemSecurityToken> securities;
	@JoinColumn(name = "RulesID",
			referencedColumnName = "RulesID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Rules rulesID;
	@JoinColumn(name = "ResourceItemID",
			referencedColumnName = "ResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ResourceItem resourceItemID;

	public RulesXResourceItem()
	{

	}

	public RulesXResourceItem(UUID rulesXResourceItemID)
	{
		id = rulesXResourceItemID;
	}

	@Override
	protected RulesXResourceItemSecurityToken configureDefaultsForNewToken(RulesXResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public java.util.UUID getId()
	{
		return id;
	}

	public List<RulesXResourceItemSecurityToken> getSecurities()
	{
		return securities;
	}

	public Rules getRulesID()
	{
		return rulesID;
	}

	public ResourceItem getResourceItemID()
	{
		return resourceItemID;
	}

	@Override
	public RulesXResourceItem setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public RulesXResourceItem setSecurities(List<RulesXResourceItemSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public RulesXResourceItem setRulesID(Rules rulesID)
	{
		this.rulesID = rulesID;
		return this;
	}

	public RulesXResourceItem setResourceItemID(ResourceItem resourceItemID)
	{
		this.resourceItemID = resourceItemID;
		return this;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		RulesXResourceItem that = (RulesXResourceItem) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IRules<?> getPrimary()
	{
		return getRulesID();
	}

	@Override
	public IResourceItem<?> getSecondary()
	{
		return getResourceItemID();
	}
}
