package com.guicedee.activitymaster.core.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesXResourceItemQueryBuilder;
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
       name = "RulesXResourceItem")
@XmlRootElement
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RulesXResourceItem
		extends WarehouseClassificationRelationshipTable<Rules,
		ResourceItem,
		RulesXResourceItem,
		RulesXResourceItemQueryBuilder,
		java.util.UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesXResourceItemID")
	@org.hibernate.annotations.Type(type = "uuid-char")
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
	public Rules getPrimary()
	{
		return getRulesID();
	}
	
	@Override
	public ResourceItem getSecondary()
	{
		return getResourceItemID();
	}
}
