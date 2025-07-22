/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.geography;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.geography.builders.GeographyXGeographyQueryBuilder;
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
@Table(schema = "Geography", name = "GeographyXGeography")
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
public class GeographyXGeography
        extends WarehouseClassificationRelationshipTable<Geography,
                                Geography,
                                GeographyXGeography,
                                GeographyXGeographyQueryBuilder,
                                UUID,
                                GeographyXGeographySecurityToken
                                >
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "GeographyXGeographyID")

    private java.util.UUID id;
    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<GeographyXGeographySecurityToken> securities;

    @JoinColumn(name = "ParentGeographyID",
            referencedColumnName = "GeographyID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Geography parentGeographyID;
    @JoinColumn(name = "ChildGeographyID",
            referencedColumnName = "GeographyID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Geography childGeographyID;

    @Override
    public void configureSecurityEntity(GeographyXGeographySecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<GeographyXGeographySecurityToken> getSecurities()
    {
        return this.securities;
    }

    public GeographyXGeography setSecurities(List<GeographyXGeographySecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public Geography getParentGeographyID()
    {
        return this.parentGeographyID;
    }

    public GeographyXGeography setParentGeographyID(Geography parentGeographyID)
    {
        this.parentGeographyID = parentGeographyID;
        return this;
    }

    public Geography getChildGeographyID()
    {
        return this.childGeographyID;
    }

    public GeographyXGeography setChildGeographyID(Geography childGeographyID)
    {
        this.childGeographyID = childGeographyID;
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
        GeographyXGeography that = (GeographyXGeography) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public Geography getPrimary()
    {
        return getParentGeographyID();
    }

    @Override
    public Geography getSecondary()
    {
        return getChildGeographyID();
    }
}
