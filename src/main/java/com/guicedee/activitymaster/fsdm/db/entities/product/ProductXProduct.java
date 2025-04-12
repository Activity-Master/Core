package com.guicedee.activitymaster.fsdm.db.entities.product;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.product.builders.ProductXProductQueryBuilder;
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
        name = "ProductXProduct")
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
public class ProductXProduct
        extends WarehouseClassificationRelationshipTable<Product,
        Product,
        ProductXProduct,
        ProductXProductQueryBuilder,
        UUID, ProductXProductSecurityToken>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "ProductXProductID")

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ProductXProductSecurityToken> securities;

    @JoinColumn(name = "ChildProductID",
            referencedColumnName = "ProductID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Product childProductID;
    @JoinColumn(name = "ParentProductID",
            referencedColumnName = "ProductID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Product parentProductID;

    @Override
    public void configureSecurityEntity(ProductXProductSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<ProductXProductSecurityToken> getSecurities()
    {
        return securities;
    }

    public ProductXProduct setSecurities(List<ProductXProductSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public Product getChildProductID()
    {
        return childProductID;
    }

    public ProductXProduct setChildProductID(Product childProductID)
    {
        this.childProductID = childProductID;
        return this;
    }

    public Product getParentProductID()
    {
        return parentProductID;
    }

    public ProductXProduct setParentProductID(Product parentProductID)
    {
        this.parentProductID = parentProductID;
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
        ProductXProduct that = (ProductXProduct) o;
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
        return getParentProductID();
    }

    @Override
    public Product getSecondary()
    {
        return getChildProductID();
    }
}
