package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXResourceItemQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
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
@Table(schema = "Event", name = "EventXResourceItem")
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
public class EventXResourceItem
        extends WarehouseClassificationRelationshipTable<Event,
                                ResourceItem,
                                EventXResourceItem,
                                EventXResourceItemQueryBuilder,
                                UUID,
                                EventXResourceItemSecurityToken
                                >
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "EventXResourceItemID")

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<EventXResourceItemSecurityToken> securities;
    @JoinColumn(name = "EventID",
            referencedColumnName = "EventID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private Event eventID;
    @JoinColumn(name = "ResourceItemID",
            referencedColumnName = "ResourceItemID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private ResourceItem resourceItemID;

    @Override
    public void configureSecurityEntity(EventXResourceItemSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<EventXResourceItemSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public EventXResourceItem setSecurities(List<EventXResourceItemSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public Event getEventID()
    {
        return this.eventID;
    }

    public EventXResourceItem setEventID(Event eventID)
    {
        this.eventID = eventID;
        return this;
    }

    public ResourceItem getResourceItemID()
    {
        return this.resourceItemID;
    }

    public EventXResourceItem setResourceItemID(ResourceItem resourceItemID)
    {
        this.resourceItemID = resourceItemID;
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
        EventXResourceItem that = (EventXResourceItem) o;
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
    public ResourceItem getSecondary()
    {
        return getResourceItemID();
    }
}
