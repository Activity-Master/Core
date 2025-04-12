package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXGeographyQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.geography.Geography;
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
@Table(schema = "Event", name = "EventXGeography")
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
public class EventXGeography
        extends WarehouseClassificationRelationshipTable<Event,
        Geography,
        EventXGeography,
        EventXGeographyQueryBuilder,
        UUID,
        EventXGeographySecurityToken
        >
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "EventXGeographyID")

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<EventXGeographySecurityToken> securities;

    @JoinColumn(name = "EventID",
            referencedColumnName = "EventID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private Event eventID;
    @JoinColumn(name = "GeographyID",
            referencedColumnName = "GeographyID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Geography geographyID;

    @Override
    public void configureSecurityEntity(EventXGeographySecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<EventXGeographySecurityToken> getSecurities()
    {
        return this.securities;
    }

    public EventXGeography setSecurities(List<EventXGeographySecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public Event getEventID()
    {
        return this.eventID;
    }

    public EventXGeography setEventID(Event eventID)
    {
        this.eventID = eventID;
        return this;
    }

    public Geography getGeographyID()
    {
        return this.geographyID;
    }

    public EventXGeography setGeographyID(Geography geographyID)
    {
        this.geographyID = geographyID;
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
        EventXGeography that = (EventXGeography) o;
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
    public Geography getSecondary()
    {
        return getGeographyID();
    }
}
