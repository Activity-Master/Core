package com.guicedee.activitymaster.fsdm.db.entities.rules;


import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesXRulesTypeQueryBuilder;
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
@Table(schema = "Rules",
        name = "RulesXRulesType")
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
public class RulesXRulesType
        extends WarehouseClassificationRelationshipTable<Rules,
                                RulesType,
                                RulesXRulesType,
                                RulesXRulesTypeQueryBuilder,
                                UUID,
                                RulesXRulesTypeSecurityToken>
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "RulesXRulesTypeID")

    private java.util.UUID id;
    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<RulesXRulesTypeSecurityToken> securities;
    @JoinColumn(name = "RulesID",
            referencedColumnName = "RulesID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Rules rulesID;
    @JoinColumn(name = "RulesTypeID",
            referencedColumnName = "RulesTypeID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private RulesType rulesTypeID;

    @Override
    public void configureSecurityEntity(RulesXRulesTypeSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<RulesXRulesTypeSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public RulesXRulesType setSecurities(List<RulesXRulesTypeSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public Rules getRulesID()
    {
        return this.rulesID;
    }

    public RulesXRulesType setRulesID(Rules rulesID)
    {
        this.rulesID = rulesID;
        return this;
    }

    public RulesType getRulesTypeID()
    {
        return this.rulesTypeID;
    }

    public RulesXRulesType setRulesTypeID(RulesType rulesTypeID)
    {
        this.rulesTypeID = rulesTypeID;
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
        RulesXRulesType that = (RulesXRulesType) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public Rules getPrimary()
    {
        return getRulesID();
    }

    @Override
    public RulesType getSecondary()
    {
        return getRulesTypeID();
    }
}
