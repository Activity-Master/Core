package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXArrangementQueryBuilder;
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
@Table(schema = "Event", name = "EventXArrangement")
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
public class EventXArrangement
        extends WarehouseClassificationRelationshipTable<Event,
                                Arrangement,
                                EventXArrangement,
                                EventXArrangementQueryBuilder,
                                UUID,
                                EventXArrangementsSecurityToken
                                >
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "EventXArrangementsID")

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<EventXArrangementsSecurityToken> securities;

    @JoinColumn(name = "ArrangementID",
            referencedColumnName = "ArrangementID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Arrangement arrangementID;

    @JoinColumn(name = "EventID",
            referencedColumnName = "EventID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private Event eventID;

    @Override
    public void configureSecurityEntity(EventXArrangementsSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<EventXArrangementsSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public EventXArrangement setSecurities(List<EventXArrangementsSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public Arrangement getArrangementID()
    {
        return this.arrangementID;
    }

    public EventXArrangement setArrangementID(Arrangement arrangementID)
    {
        this.arrangementID = arrangementID;
        return this;
    }

    public Event getEventID()
    {
        return this.eventID;
    }

    public EventXArrangement setEventID(Event eventID)
    {
        this.eventID = eventID;
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
        EventXArrangement that = (EventXArrangement) o;
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
    public Arrangement getSecondary()
    {
        return getArrangementID();
    }
}
