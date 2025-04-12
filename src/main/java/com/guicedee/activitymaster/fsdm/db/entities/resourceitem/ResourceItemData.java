package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceData;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemDataQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Resource",
        name = "ResourceItemData")
@XmlRootElement
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceItemData
        extends WarehouseSCDTable<ResourceItemData, ResourceItemDataQueryBuilder, UUID, ResourceItemDataSecurityToken>
        implements IResourceData<ResourceItemData, ResourceItemDataQueryBuilder, ResourceItemDataSecurityToken>
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(nullable = false,
            name = "ResourceItemDataID")
    private UUID id;
    @Column(nullable = false,
            name = "ResourceItemData")
    private byte[] resourceItemData;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ResourceItemDataSecurityToken> securities;

    @JoinColumn(name = "ResourceItemID",
            referencedColumnName = "ResourceItemID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private ResourceItem resource;

    @OneToMany(
            mappedBy = "resourceItemDataID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ResourceItemDataXClassification> classifications;

    @Override
    public void configureSecurityEntity(ResourceItemDataSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<ResourceItemDataSecurityToken> getSecurities()
    {
        return securities;
    }

    public ResourceItemData setSecurities(List<ResourceItemDataSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public List<ResourceItemDataXClassification> getClassifications()
    {
        return classifications;
    }

    public ResourceItemData setClassifications(List<ResourceItemDataXClassification> classifications)
    {
        this.classifications = classifications;
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
        ResourceItemData itemData = (ResourceItemData) o;
        return Objects.equals(getId(), itemData.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public String toString()
    {
        return getId() + "";
    }

    @Override
    public byte[] getResourceItemData()
    {
        return getResource().unzip(resourceItemData);
    }

    public ResourceItemData setResourceItemData(byte[] resourceItemData)
    {
        this.resourceItemData = resourceItemData;
        return this;
    }

    public ResourceItem getResource()
    {
        return resource;
    }

    public ResourceItemData setResource(ResourceItem resource)
    {
        this.resource = resource;
        return this;
    }

    @Override
    public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
    {

    }

}