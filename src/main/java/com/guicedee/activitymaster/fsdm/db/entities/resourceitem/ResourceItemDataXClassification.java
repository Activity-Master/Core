package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemDataXClassificationQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Resource", name = "ResourceItemDataXClassification")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ResourceItemDataXClassification
		extends WarehouseClassificationRelationshipTable<ResourceItemData,
		Classification,
		ResourceItemDataXClassification,
		ResourceItemDataXClassificationQueryBuilder,
		UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ResourceItemDataXClassificationID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
	@JoinColumn(name = "ResourceItemDataID",
	            referencedColumnName = "ResourceItemDataID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ResourceItemData resourceItemDataID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataXClassificationSecurityToken> securities;
	
	public ResourceItemDataXClassification()
	{
	
	}
	
	public ResourceItemDataXClassification(UUID resourceItemDataXClassificationID)
	{
		this.id = resourceItemDataXClassificationID;
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public ResourceItemDataXClassification setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ResourceItemData getResourceItemDataID()
	{
		return this.resourceItemDataID;
	}
	
	public ResourceItemDataXClassification setResourceItemDataID(ResourceItemData resourceItemDataID)
	{
		this.resourceItemDataID = resourceItemDataID;
		return this;
	}
	
	public List<ResourceItemDataXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public ResourceItemDataXClassification setSecurities(List<ResourceItemDataXClassificationSecurityToken> securities)
	{
		this.securities = securities;
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
		ResourceItemDataXClassification that = (ResourceItemDataXClassification) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public ResourceItemData getPrimary()
	{
		return getResourceItemDataID();
	}
	
	@Override
	public Classification getSecondary()
	{
		return getClassificationID();
	}
}
