package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXProductQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event", name = "EventXProduct")
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
public class EventXProduct
        extends WarehouseClassificationRelationshipTable<Event,
        Product,
        EventXProduct,
        EventXProductQueryBuilder,
        UUID,
        EventXProductSecurityToken
        >
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "EventXProductID")

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<EventXProductSecurityToken> securities;

    @JoinColumn(name = "EventID",
            referencedColumnName = "EventID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private Event eventID;
    @JoinColumn(name = "ProductID",
            referencedColumnName = "ProductID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private Product productID;

    @Override
    public void configureSecurityEntity(EventXProductSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<EventXProductSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public EventXProduct setSecurities(List<EventXProductSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public Event getEventID()
    {
        return this.eventID;
    }

    public EventXProduct setEventID(Event eventID)
    {
        this.eventID = eventID;
        return this;
    }

    public Product getProductID()
    {
        return this.productID;
    }

    public EventXProduct setProductID(Product productID)
    {
        this.productID = productID;
        return this;
    }

    @Override
    public Event getPrimary()
    {
        return getEventID();
    }

    @Override
    public Product getSecondary()
    {
        return getProductID();
    }
}
