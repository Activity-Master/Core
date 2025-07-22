package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesXInvolvedPartyQueryBuilder;
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
@Table(schema = "Rules", name = "RulesXInvolvedParty")
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
public class RulesXInvolvedParty
        extends WarehouseClassificationRelationshipTable<Rules,
                                InvolvedParty,
                                RulesXInvolvedParty,
                                RulesXInvolvedPartyQueryBuilder,
                                UUID,
                                RulesXInvolvedPartySecurityToken>
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "RulesXInvolvedPartyID")

    private java.util.UUID id;

    @JoinColumn(name = "RulesID",
            referencedColumnName = "RulesID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Rules rulesID;
    @JoinColumn(name = "InvolvedPartyID",
            referencedColumnName = "InvolvedPartyID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private InvolvedParty involvedPartyID;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<RulesXInvolvedPartySecurityToken> securities;
    @Override
    public void configureSecurityEntity(RulesXInvolvedPartySecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public Rules getRulesID()
    {
        return this.rulesID;
    }

    public RulesXInvolvedParty setRulesID(Rules RulesID)
    {
        this.rulesID = RulesID;
        return this;
    }

    public InvolvedParty getInvolvedPartyID()
    {
        return this.involvedPartyID;
    }

    public RulesXInvolvedParty setInvolvedPartyID(InvolvedParty involvedPartyID)
    {
        this.involvedPartyID = involvedPartyID;
        return this;
    }

    public List<RulesXInvolvedPartySecurityToken> getSecurities()
    {
        return this.securities;
    }

    public RulesXInvolvedParty setSecurities(List<RulesXInvolvedPartySecurityToken> securities)
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
        RulesXInvolvedParty that = (RulesXInvolvedParty) o;
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
    public InvolvedParty getSecondary()
    {
        return getInvolvedPartyID();
    }
}
