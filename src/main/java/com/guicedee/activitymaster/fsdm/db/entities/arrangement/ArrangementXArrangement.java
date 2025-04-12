/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXArrangementQueryBuilder;
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
@Table(schema = "Arrangement", name = "ArrangementXArrangement")
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
public class ArrangementXArrangement
        extends WarehouseClassificationRelationshipTable<Arrangement,
        Arrangement,
        ArrangementXArrangement,
        ArrangementXArrangementQueryBuilder,
        UUID,
        ArrangementXArrangementSecurityToken
        >

{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "ArrangementXArrangementID")

    private java.util.UUID id;

    @JoinColumn(name = "ChildArrangementID",
            referencedColumnName = "ArrangementID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private Arrangement childArrangementID;
    @JoinColumn(name = "ParentArrangementID",
            referencedColumnName = "ArrangementID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private Arrangement parentArrangementID;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ArrangementXArrangementSecurityToken> securities;

    @Override
    public void configureSecurityEntity(ArrangementXArrangementSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public Arrangement getChildArrangementID()
    {
        return this.childArrangementID;
    }

    public ArrangementXArrangement setChildArrangementID(Arrangement childArrangementID)
    {
        this.childArrangementID = childArrangementID;
        return this;
    }

    public Arrangement getParentArrangementID()
    {
        return this.parentArrangementID;
    }

    public ArrangementXArrangement setParentArrangementID(Arrangement parentArrangementID)
    {
        this.parentArrangementID = parentArrangementID;
        return this;
    }

    public List<ArrangementXArrangementSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public ArrangementXArrangement setSecurities(List<ArrangementXArrangementSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    @Override
    public Arrangement getPrimary()
    {
        return getParentArrangementID();
    }

    @Override
    public Arrangement getSecondary()
    {
        return getChildArrangementID();
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
        ArrangementXArrangement that = (ArrangementXArrangement) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(getId());
    }
}
