package com.guicedee.activitymaster.fsdm.db.entities.product;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.product.builders.ProductXResourceItemQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
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
@Table(schema = "Product",
        name = "ProductXResourceItem")
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
public class ProductXResourceItem
        extends WarehouseClassificationRelationshipTable<Product,
        ResourceItem,
        ProductXResourceItem,
        ProductXResourceItemQueryBuilder,
        UUID,
        ProductXResourceItemSecurityToken>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "ProductXResourceItemID")

    private java.util.UUID id;
    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ProductXResourceItemSecurityToken> securities;
    @JoinColumn(name = "ProductID",
            referencedColumnName = "ProductID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Product productID;
    @JoinColumn(name = "ResourceItemID",
            referencedColumnName = "ResourceItemID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private ResourceItem resourceItemID;

    @Override
    public void configureSecurityEntity(ProductXResourceItemSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<ProductXResourceItemSecurityToken> getSecurities()
    {
        return securities;
    }

    public ProductXResourceItem setSecurities(List<ProductXResourceItemSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public Product getProductID()
    {
        return productID;
    }

    public ProductXResourceItem setProductID(Product productID)
    {
        this.productID = productID;
        return this;
    }

    public ResourceItem getResourceItemID()
    {
        return resourceItemID;
    }

    public ProductXResourceItem setResourceItemID(ResourceItem resourceItemID)
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
        ProductXResourceItem that = (ProductXResourceItem) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public Product getPrimary()
    {
        return getProductID();
    }

    @Override
    public ResourceItem getSecondary()
    {
        return getResourceItemID();
    }
}
