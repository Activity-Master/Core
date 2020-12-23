package com.guicedee.activitymaster.core.db.entities.events;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXProductQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IEvent;
import com.guicedee.activitymaster.core.services.dto.IProduct;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.UUID;

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
public class EventXProduct
		extends WarehouseClassificationRelationshipTable<Event,
				                                                Product,
				                                                EventXProduct,
				                                                EventXProductQueryBuilder,
				                                                java.util.UUID,
				                                                EventXProductSecurityToken,
				                                                IEvent<?>, IProduct<?>>
{

	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "EventXProductID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

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

	@Override
	protected EventXProductSecurityToken configureDefaultsForNewToken(EventXProductSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	public java.util.UUID getId()
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

	public EventXProduct setId(java.util.UUID id)
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
	public IEvent<?> getPrimary()
	{
		return getEventID();
	}

	@Override
	public IProduct<?> getSecondary()
	{
		return getProductID();
	}
}
