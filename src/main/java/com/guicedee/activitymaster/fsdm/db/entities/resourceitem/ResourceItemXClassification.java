package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemXClassificationQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Resource", name = "ResourceItemXClassification")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceItemXClassification
        extends WarehouseClassificationRelationshipTable<ResourceItem,
                                Classification,
                                ResourceItemXClassification,
                                ResourceItemXClassificationQueryBuilder,
                                UUID, ResourceItemXClassificationSecurityToken>
        implements Serializable,
        IWarehouseRelationshipClassificationTable<ResourceItemXClassification, ResourceItemXClassificationQueryBuilder, ResourceItem, Classification,UUID, ResourceItemXClassificationSecurityToken>
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "ResourceItemXClassificationID")

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ResourceItemXClassificationSecurityToken> securities;

    @JoinColumn(name = "ResourceItemID",
            referencedColumnName = "ResourceItemID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private ResourceItem resourceItemID;

    @Override
    public void configureSecurityEntity(ResourceItemXClassificationSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
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
