/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXResourceItemQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
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
@Table(schema = "Party",
        name = "InvolvedPartyXResourceItem")
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
public class InvolvedPartyXResourceItem
        extends WarehouseClassificationRelationshipTable<InvolvedParty,
                                ResourceItem,
                                InvolvedPartyXResourceItem,
                                InvolvedPartyXResourceItemQueryBuilder,
                                UUID,
                                InvolvedPartyXResourceItemSecurityToken>

{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "InvolvedPartyXResourceItemID")

    private java.util.UUID id;

    @JoinColumn(name = "InvolvedPartyID",
            referencedColumnName = "InvolvedPartyID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private InvolvedParty involvedPartyID;
    @JoinColumn(name = "ResourceItemID",
            referencedColumnName = "ResourceItemID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private ResourceItem resourceItemID;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyXResourceItemSecurityToken> securities;

    @Override
    public void configureSecurityEntity(InvolvedPartyXResourceItemSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public InvolvedParty getInvolvedPartyID()
    {
        return this.involvedPartyID;
    }

    public InvolvedPartyXResourceItem setInvolvedPartyID(InvolvedParty involvedPartyID)
    {
        this.involvedPartyID = involvedPartyID;
        return this;
    }

    public ResourceItem getResourceItemID()
    {
        return this.resourceItemID;
    }

    public InvolvedPartyXResourceItem setResourceItemID(ResourceItem resourceItemID)
    {
        this.resourceItemID = resourceItemID;
        return this;
    }

    public List<InvolvedPartyXResourceItemSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public InvolvedPartyXResourceItem setSecurities(List<InvolvedPartyXResourceItemSecurityToken> securities)
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
        InvolvedPartyXResourceItem that = (InvolvedPartyXResourceItem) o;
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
    public ResourceItem getSecondary()
    {
        return getResourceItemID();
    }
}
