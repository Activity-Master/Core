/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.product;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.product.builders.ProductTypeSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.UUID;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Product",
        name = "ProductTypesSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTypeSecurityToken
        extends WarehouseSecurityTable<ProductTypeSecurityToken, ProductTypeSecurityTokenQueryBuilder, UUID>
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "ProductTypesSecurityTokenID")

    private java.util.UUID id;

    @JoinColumn(name = "ProductTypesID",
            referencedColumnName = "ProductTypeID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private ProductType base;

    @Override
    public String toString()
    {
        return "ProductTypesSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
    }

    public ProductType getBase()
    {
        return this.base;
    }

    public ProductTypeSecurityToken setBase(ProductType base)
    {
        this.base = base;
        return this;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (o == this)
        {
            return true;
        }
        if (!(o instanceof ProductTypeSecurityToken))
        {
            return false;
        }
        final ProductTypeSecurityToken other = (ProductTypeSecurityToken) o;
        if (!other.canEqual(this))
        {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        return this$id == null ? other$id == null : this$id.equals(other$id);
    }

    protected boolean canEqual(final Object other)
    {
        return other instanceof ProductTypeSecurityToken;
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}
