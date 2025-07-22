/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXAddressQueryBuilder;
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
@Table(schema = "Event", name = "EventXAddress")
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
public class EventXAddress
        extends WarehouseClassificationRelationshipTable<Event,
                                Address,
                                EventXAddress,
                                EventXAddressQueryBuilder,
                                UUID,
                                EventXAddressSecurityToken
                                >
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "EventXAddressID")

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<EventXAddressSecurityToken> securities;

    @JoinColumn(name = "AddressID",
            referencedColumnName = "AddressID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Address addressID;

    @JoinColumn(name = "EventID",
            referencedColumnName = "EventID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private Event eventID;

    @Override
    public void configureSecurityEntity(EventXAddressSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }


    public EventXAddress setSecurities(List<EventXAddressSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public EventXAddress setAddressID(Address addressID)
    {
        this.addressID = addressID;
        return this;
    }

    public EventXAddress setEventID(Event eventID)
    {
        this.eventID = eventID;
        return this;
    }

    @Override
    public Event getPrimary()
    {
        return getEventID();
    }

    @Override
    public Address getSecondary()
    {
        return getAddressID();
    }

    public List<EventXAddressSecurityToken> getSecurities()
    {
        return securities;
    }

    public Address getAddressID()
    {
        return addressID;
    }

    public Event getEventID()
    {
        return eventID;
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
        EventXAddress that = (EventXAddress) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(getId());
    }
}
