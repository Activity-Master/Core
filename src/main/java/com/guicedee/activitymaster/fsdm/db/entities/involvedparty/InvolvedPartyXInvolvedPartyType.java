package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyTypeQueryBuilder;
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
        name = "InvolvedPartyXInvolvedPartyType")
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
public class InvolvedPartyXInvolvedPartyType
        extends WarehouseClassificationRelationshipTypesTable<InvolvedParty,
        InvolvedPartyType,
        InvolvedPartyXInvolvedPartyType,
        InvolvedPartyXInvolvedPartyTypeQueryBuilder,
        UUID,
        InvolvedPartyXInvolvedPartyTypeSecurityToken>
{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "InvolvedPartyXInvolvedPartyTypeID")

    private java.util.UUID id;

    @JoinColumn(name = "InvolvedPartyID",
            referencedColumnName = "InvolvedPartyID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private InvolvedParty involvedPartyID;
    @JoinColumn(name = "InvolvedPartyTypeID",
            referencedColumnName = "InvolvedPartyTypeID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private InvolvedPartyType involvedPartyTypeID;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyXInvolvedPartyTypeSecurityToken> securities;

    @Override
    public void configureSecurityEntity(InvolvedPartyXInvolvedPartyTypeSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }


    public InvolvedParty getInvolvedPartyID()
    {
        return this.involvedPartyID;
    }

    public InvolvedPartyXInvolvedPartyType setInvolvedPartyID(InvolvedParty involvedPartyID)
    {
        this.involvedPartyID = involvedPartyID;
        return this;
    }

    public InvolvedPartyType getInvolvedPartyTypeID()
    {
        return this.involvedPartyTypeID;
    }

    public InvolvedPartyXInvolvedPartyType setInvolvedPartyTypeID(InvolvedPartyType involvedPartyTypeID)
    {
        this.involvedPartyTypeID = involvedPartyTypeID;
        return this;
    }

    public List<InvolvedPartyXInvolvedPartyTypeSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public InvolvedPartyXInvolvedPartyType setSecurities(List<InvolvedPartyXInvolvedPartyTypeSecurityToken> securities)
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
        InvolvedPartyXInvolvedPartyType that = (InvolvedPartyXInvolvedPartyType) o;
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
    public InvolvedPartyType getSecondary()
    {
        return getInvolvedPartyTypeID();
    }
}
