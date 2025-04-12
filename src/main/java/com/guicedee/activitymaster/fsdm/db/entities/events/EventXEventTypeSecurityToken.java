package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXEventTypeSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.Objects;
import java.util.UUID;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event", name = "EventXEventTypeSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventXEventTypeSecurityToken
        extends WarehouseSecurityTable<EventXEventTypeSecurityToken, EventXEventTypeSecurityTokenQueryBuilder, UUID>
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "EventXEventTypeSecurityTokenID")

    private java.util.UUID id;

    @JoinColumn(name = "EventXEventTypeID",
            referencedColumnName = "EventXEventTypeID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private EventXEventType base;

    public String toString()
    {
        return "EventXEventTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
    }

    public EventXEventType getBase()
    {
        return this.base;
    }

    public EventXEventTypeSecurityToken setBase(EventXEventType base)
    {
        this.base = base;
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
        EventXEventTypeSecurityToken that = (EventXEventTypeSecurityToken) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(getId());
    }
}
