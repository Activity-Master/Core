package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXClassificationQueryBuilder;
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
@Table(schema = "Party", name = "InvolvedPartyXClassification")
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
public class InvolvedPartyXClassification
        extends WarehouseClassificationRelationshipTable<InvolvedParty,
                                Classification,
                                InvolvedPartyXClassification,
                                InvolvedPartyXClassificationQueryBuilder,
                                UUID,
                                InvolvedPartyXClassificationSecurityToken
                                >
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "InvolvedPartyXClassificationID")

    private java.util.UUID id;
    @JoinColumn(name = "InvolvedPartyID",
            referencedColumnName = "InvolvedPartyID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.EAGER)
    private InvolvedParty involvedPartyID;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyXClassificationSecurityToken> securities;

    @Override
    public void configureSecurityEntity(InvolvedPartyXClassificationSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public InvolvedParty getInvolvedPartyID()
    {
        return this.involvedPartyID;
    }

    public InvolvedPartyXClassification setInvolvedPartyID(InvolvedParty involvedPartyID)
    {
        this.involvedPartyID = involvedPartyID;
        return this;
    }

    public List<InvolvedPartyXClassificationSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public InvolvedPartyXClassification setSecurities(List<InvolvedPartyXClassificationSecurityToken> securities)
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
        InvolvedPartyXClassification that = (InvolvedPartyXClassification) o;
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
    public Classification getSecondary()
    {
        return getClassificationID();
    }
}
