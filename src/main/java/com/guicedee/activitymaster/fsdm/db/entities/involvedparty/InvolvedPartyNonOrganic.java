package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyNonOrganicQueryBuilder;
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
@SuppressWarnings("unused")
@Entity
@Table(schema = "Party",
        name = "InvolvedPartyNonOrganic")
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
public class InvolvedPartyNonOrganic
        extends WarehouseSCDTable<InvolvedPartyNonOrganic, InvolvedPartyNonOrganicQueryBuilder, UUID, InvolvedPartyNonOrganicSecurityToken>
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(nullable = false,
            name = "InvolvedPartyNonOrganicID")
    @JsonValue

    private java.util.UUID id;

    @JoinColumn(name = "InvolvedPartyNonOrganicID",
            referencedColumnName = "InvolvedPartyID",
            nullable = false,
            insertable = false,
            updatable = false)
    @OneToOne(optional = false,
            fetch = FetchType.LAZY)
    private InvolvedParty involvedParty;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyNonOrganicSecurityToken> securities;

    @Override
    public void configureSecurityEntity(InvolvedPartyNonOrganicSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<InvolvedPartyNonOrganicSecurityToken> getSecurities()
    {
        return securities;
    }

    public InvolvedPartyNonOrganic setSecurities(List<InvolvedPartyNonOrganicSecurityToken> securities)
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
        InvolvedPartyNonOrganic that = (InvolvedPartyNonOrganic) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public String toString()
    {
        return getId() + "";
    }

    public InvolvedParty getInvolvedParty()
    {
        return involvedParty;
    }

    public InvolvedPartyNonOrganic setInvolvedParty(InvolvedParty involvedParty)
    {
        this.involvedParty = involvedParty;
        return this;
    }
}
