package com.guicedee.activitymaster.fsdm.db.entities.product;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.product.builders.ProductTypeXClassificationQueryBuilder;
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
@Table(schema = "Product", name = "ProductTypeXClassification")
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
public class ProductTypeXClassification
        extends WarehouseClassificationRelationshipTable<ProductType,
                                Classification,
                                ProductTypeXClassification,
                                ProductTypeXClassificationQueryBuilder,
                                UUID,
                                ProductTypeXClassificationSecurityToken>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "ProductTypeXClassificationID")

    private java.util.UUID id;

    @JoinColumn(name = "ProductTypeID",
            referencedColumnName = "ProductTypeID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private ProductType productTypeID;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ProductTypeXClassificationSecurityToken> securities;

    @Override
    public void configureSecurityEntity(ProductTypeXClassificationSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public ProductType getProductTypeID()
    {
        return this.productTypeID;
    }

    public ProductTypeXClassification setProductTypeID(ProductType productTypeID)
    {
        this.productTypeID = productTypeID;
        return this;
    }

    public List<ProductTypeXClassificationSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public ProductTypeXClassification setSecurities(List<ProductTypeXClassificationSecurityToken> securities)
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
        ProductTypeXClassification that = (ProductTypeXClassification) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public ProductType getPrimary()
    {
        return getProductTypeID();
    }

    @Override
    public Classification getSecondary()
    {
        return getSecondary();
    }
}
