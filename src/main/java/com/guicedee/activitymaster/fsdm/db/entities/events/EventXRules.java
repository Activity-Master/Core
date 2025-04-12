package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXRulesQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;
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
@Table(schema = "Event", name = "EventXRules")
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
public class EventXRules
        extends WarehouseClassificationRelationshipTable<Event,
        Rules,
        EventXRules,
        EventXRulesQueryBuilder,
        UUID,
        EventXRulesSecurityToken
        >
{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "EventXRulesID")

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<EventXRulesSecurityToken> securities;
    @JoinColumn(name = "EventID",
            referencedColumnName = "EventID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private Event eventID;
    @JoinColumn(name = "RulesID",
            referencedColumnName = "RulesID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Rules rulesID;

    @Override
    public void configureSecurityEntity(EventXRulesSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<EventXRulesSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public EventXRules setSecurities(List<EventXRulesSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public Event getEventID()
    {
        return this.eventID;
    }

    public EventXRules setEventID(Event eventID)
    {
        this.eventID = eventID;
        return this;
    }

    public Rules getRulesID()
    {
        return this.rulesID;
    }

    public EventXRules setRulesID(Rules resourceItemID)
    {
        this.rulesID = resourceItemID;
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
        EventXRules that = (EventXRules) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public Event getPrimary()
    {
        return getEventID();
    }

    @Override
    public Rules getSecondary()
    {
        return getRulesID();
    }
}
