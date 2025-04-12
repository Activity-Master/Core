package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXArrangementSecurityTokenQueryBuilder;
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
@Table(schema = "Event", name = "EventXArrangementsSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventXArrangementsSecurityToken
        extends WarehouseSecurityTable<EventXArrangementsSecurityToken, EventXArrangementSecurityTokenQueryBuilder, UUID>
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "EventXArrangementsSecurityTokenID")

    private java.util.UUID id;

    @JoinColumn(name = "EventXArrangementsID",
            referencedColumnName = "EventXArrangementsID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private EventXArrangement base;

    public String toString()
    {
        return "EventXArrangementsSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
    }


    public EventXArrangementsSecurityToken setBase(EventXArrangement base)
    {
        this.base = base;
        return this;
    }
	
    public EventXArrangement getBase()
    {
        return base;
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
        EventXArrangementsSecurityToken that = (EventXArrangementsSecurityToken) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(getId());
    }
}
