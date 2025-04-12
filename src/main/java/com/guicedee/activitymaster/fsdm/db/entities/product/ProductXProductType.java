package com.guicedee.activitymaster.fsdm.db.entities.product;


import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.entities.product.builders.ProductXProductTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
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
        name = "ProductXProductType")
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
public class ProductXProductType
        extends WarehouseClassificationRelationshipTypesTable<Product,
        ProductType,
        ProductXProductType,
        ProductXProductTypeQueryBuilder,
        UUID, ProductXProductTypeSecurityToken>
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "ProductXProductTypeID")

    private java.util.UUID id;
    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ProductXProductTypeSecurityToken> securities;
    @JoinColumn(name = "ProductID",
            referencedColumnName = "ProductID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Product productID;
    @JoinColumn(name = "ProductTypeID",
            referencedColumnName = "ProductTypeID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private ProductType productTypeID;

    @Override
    public void configureSecurityEntity(ProductXProductTypeSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<ProductXProductTypeSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public ProductXProductType setSecurities(List<ProductXProductTypeSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public Product getProductID()
    {
        return this.productID;
    }

    public ProductXProductType setProductID(Product productID)
    {
        this.productID = productID;
        return this;
    }

    public ProductType getProductTypeID()
    {
        return this.productTypeID;
    }

    public ProductXProductType setProductTypeID(ProductType productTypeID)
    {
        this.productTypeID = productTypeID;
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
        ProductXProductType that = (ProductXProductType) o;
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
    public ProductType getSecondary()
    {
        return getProductTypeID();
    }
}
