package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesXRulesQueryBuilder;
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
@Table(schema = "Rules",
        name = "RulesXRules")
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
public class RulesXRules
        extends WarehouseClassificationRelationshipTable<Rules,
                                Rules,
                                RulesXRules,
                                RulesXRulesQueryBuilder,
                                UUID,
                                RulesXRulesSecurityToken>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "RulesXRulesID")

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<RulesXRulesSecurityToken> securities;

    @JoinColumn(name = "ChildRulesID",
            referencedColumnName = "RulesID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Rules childRulesID;
    @JoinColumn(name = "ParentRulesID",
            referencedColumnName = "RulesID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Rules parentRulesID;

    @Override
    public void configureSecurityEntity(RulesXRulesSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<RulesXRulesSecurityToken> getSecurities()
    {
        return securities;
    }

    public RulesXRules setSecurities(List<RulesXRulesSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public Rules getChildRulesID()
    {
        return childRulesID;
    }

    public RulesXRules setChildRulesID(Rules childRulesID)
    {
        this.childRulesID = childRulesID;
        return this;
    }

    public Rules getParentRulesID()
    {
        return parentRulesID;
    }

    public RulesXRules setParentRulesID(Rules parentRulesID)
    {
        this.parentRulesID = parentRulesID;
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
        RulesXRules that = (RulesXRules) o;
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
        return getParentRulesID();
    }

    @Override
    public Rules getSecondary()
    {
        return getChildRulesID();
    }
}
