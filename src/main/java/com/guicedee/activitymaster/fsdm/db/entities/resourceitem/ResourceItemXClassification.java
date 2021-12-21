package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemXClassificationQueryBuilder;
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
@Table(schema = "Resource", name = "ResourceItemXClassification")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ResourceItemXClassification
		extends WarehouseClassificationRelationshipTable<ResourceItem,
		Classification,
		ResourceItemXClassification,
		ResourceItemXClassificationQueryBuilder,
		UUID>
		implements Serializable,
		           IWarehouseRelationshipClassificationTable<ResourceItemXClassification, ResourceItemXClassificationQueryBuilder, ResourceItem, Classification, UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ResourceItemXClassificationID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ResourceItemXClassificationSecurityToken> securities;
	
	@JoinColumn(name = "ResourceItemID",
	            referencedColumnName = "ResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private ResourceItem resourceItemID;
	
	public ResourceItemXClassification()
	{
	
	}
	
	public ResourceItemXClassification(UUID resourceItemXClassificationID)
	{
		this.id = resourceItemXClassificationID;
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public ResourceItemXClassification setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public List<ResourceItemXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public ResourceItemXClassification setSecurities(List<ResourceItemXClassificationSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public ResourceItem getResourceItemID()
	{
		return this.resourceItemID;
	}
	
	public ResourceItemXClassification setResourceItemID(ResourceItem resourceItemID)
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
		ResourceItemXClassification that = (ResourceItemXClassification) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public ResourceItem getPrimary()
	{
		return getResourceItemID();
	}
	
	@Override
	public Classification getSecondary()
	{
		return getClassificationID();
	}
}
