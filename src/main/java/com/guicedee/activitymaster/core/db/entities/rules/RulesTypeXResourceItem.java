package com.guicedee.activitymaster.core.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesTypeXResourceItemQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Rules",
       name = "RulesTypeXResourceItem")
@XmlRootElement
@Access(FIELD)@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RulesTypeXResourceItem
		extends WarehouseClassificationRelationshipTable<RulesType,
		ResourceItem,
		RulesTypeXResourceItem,
		RulesTypeXResourceItemQueryBuilder,
		UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
	        name = "RulesTypeXResourceItemID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<RulesTypeXResourceItemSecurityToken> securities;
	@JoinColumn(name = "RulesTypeID",
	            referencedColumnName = "RulesTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private RulesType rulesTypeID;
	@JoinColumn(name = "ResourceItemID",
	            referencedColumnName = "ResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private ResourceItem resourceItemID;
	
	public RulesTypeXResourceItem()
	{
	
	}
	
	public RulesTypeXResourceItem(UUID rulesXResourceItemID)
	{
		id = rulesXResourceItemID;
	}
	
	@Override
	public UUID getId()
	{
		return id;
	}
	
	public List<RulesTypeXResourceItemSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public RulesType getRulesTypeID()
	{
		return rulesTypeID;
	}
	
	public ResourceItem getResourceItemID()
	{
		return resourceItemID;
	}
	
	@Override
	public RulesTypeXResourceItem setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public RulesTypeXResourceItem setSecurities(List<RulesTypeXResourceItemSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public RulesTypeXResourceItem setRulesTypeID(RulesType rulesTypeID)
	{
		this.rulesTypeID = rulesTypeID;
		return this;
	}
	
	public RulesTypeXResourceItem setResourceItemID(ResourceItem resourceItemID)
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
		RulesTypeXResourceItem that = (RulesTypeXResourceItem) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public RulesType getPrimary()
	{
		return getRulesTypeID();
	}
	
	@Override
	public ResourceItem getSecondary()
	{
		return getResourceItemID();
	}
}
