/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXProductQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
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
@Table(schema = "Party", name = "InvolvedPartyXProduct")
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
public class InvolvedPartyXProduct
        extends WarehouseClassificationRelationshipTable<InvolvedParty,
        Product,
        InvolvedPartyXProduct,
        InvolvedPartyXProductQueryBuilder,
        UUID,
        InvolvedPartyXProductSecurityToken>
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "InvolvedPartyXProductID")

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyXProductSecurityToken> securities;

    @JoinColumn(name = "InvolvedPartyID",
            referencedColumnName = "InvolvedPartyID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private InvolvedParty involvedPartyID;

    @OneToOne(mappedBy = "involvedPartyXProduct1",
            fetch = FetchType.LAZY)
    private InvolvedPartyXProduct involvedPartyXProduct;
    @JoinColumn(name = "InvolvedPartyXProductID",
            referencedColumnName = "InvolvedPartyXProductID",
            nullable = false,
            insertable = false,
            updatable = false)
    @OneToOne(optional = false,
            fetch = FetchType.LAZY)
    private InvolvedPartyXProduct involvedPartyXProduct1;
    @JoinColumn(name = "ProductID",
            referencedColumnName = "ProductID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Product productID;

    @Override
    public void configureSecurityEntity(InvolvedPartyXProductSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }


    public List<InvolvedPartyXProductSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public InvolvedPartyXProduct setSecurities(List<InvolvedPartyXProductSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public InvolvedParty getInvolvedPartyID()
    {
        return this.involvedPartyID;
    }

    public InvolvedPartyXProduct setInvolvedPartyID(InvolvedParty involvedPartyID)
    {
        this.involvedPartyID = involvedPartyID;
        return this;
    }

    public InvolvedPartyXProduct getInvolvedPartyXProduct()
    {
        return this.involvedPartyXProduct;
    }

    public InvolvedPartyXProduct setInvolvedPartyXProduct(InvolvedPartyXProduct involvedPartyXProduct)
    {
        this.involvedPartyXProduct = involvedPartyXProduct;
        return this;
    }

    public InvolvedPartyXProduct getInvolvedPartyXProduct1()
    {
        return this.involvedPartyXProduct1;
    }

    public InvolvedPartyXProduct setInvolvedPartyXProduct1(InvolvedPartyXProduct involvedPartyXProduct1)
    {
        this.involvedPartyXProduct1 = involvedPartyXProduct1;
        return this;
    }

    public Product getProductID()
    {
        return this.productID;
    }

    public InvolvedPartyXProduct setProductID(Product productID)
    {
        this.productID = productID;
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
        InvolvedPartyXProduct that = (InvolvedPartyXProduct) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public InvolvedParty getPrimary()
    {
        return getInvolvedPartyID();
    }

    @Override
    public Product getSecondary()
    {
        return getProductID();
    }
}
