package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesXClassificationQueryBuilder;
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
@Table(schema = "Rules", name = "RulesXClassification")
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
public class RulesXClassification
        extends WarehouseClassificationRelationshipTable<Rules,
        Classification,
        RulesXClassification,
        RulesXClassificationQueryBuilder,
        UUID,
        RulesXClassificationSecurityToken>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "RulesXClassificationID")

    private java.util.UUID id;

    @JoinColumn(name = "RulesID",
            referencedColumnName = "RulesID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Rules rulesID;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<RulesXClassificationSecurityToken> securities;

    @Override
    public void configureSecurityEntity(RulesXClassificationSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }


    public Rules getRulesID()
    {
        return this.rulesID;
    }

    public RulesXClassification setRulesID(Rules rulesID)
    {
        this.rulesID = rulesID;
        return this;
    }

    public List<RulesXClassificationSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public RulesXClassification setSecurities(List<RulesXClassificationSecurityToken> securities)
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
        RulesXClassification that = (RulesXClassification) o;
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
    public Classification getSecondary()
    {
        return getSecondary();
    }
}
