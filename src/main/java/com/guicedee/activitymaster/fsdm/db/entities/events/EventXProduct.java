package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXProductQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Event",name = "EventXProduct")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class EventXProduct
		extends WarehouseClassificationRelationshipTable<Event,
				                                                Product,
				                                                EventXProduct,
				                                                EventXProductQueryBuilder,
				                                                UUID>
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "EventXProductID")@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
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

	public EventXProduct()
	{

	}

	public EventXProduct(UUID eventXProductID)
	{
		this.id = eventXProductID;
	}
	
	public UUID getId()
	{
		return this.id;
	}

	public List<EventXProductSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public Event getEventID()
	{
		return this.eventID;
	}

	public Product getProductID()
	{
		return this.productID;
	}

	public EventXProduct setId(UUID id)
	{
		this.id = id;
		return this;
	}

	public EventXProduct setSecurities(List<EventXProductSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public EventXProduct setEventID(Event eventID)
	{
		this.eventID = eventID;
		return this;
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
