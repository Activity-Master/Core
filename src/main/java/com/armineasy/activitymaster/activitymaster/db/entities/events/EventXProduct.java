package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXProductQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.product.Product;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IEvent;
import com.armineasy.activitymaster.activitymaster.services.dto.IProduct;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EventXProduct")
@XmlRootElement

@Access(FIELD)
public class EventXProduct
		extends WarehouseClassificationRelationshipTable<Event,
				                                                Product,
				                                                EventXProduct,
				                                                EventXProductQueryBuilder,
				                                                Long,
				                                                EventXProductSecurityToken,
				                                                IEvent<?>, IProduct<?>>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXProductID")
	private Long id;

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

	public EventXProduct(Long eventXProductID)
	{
		this.id = eventXProductID;
	}

	@Override
	protected EventXProductSecurityToken configureDefaultsForNewToken(EventXProductSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	public Long getId()
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

	public EventXProduct setId(Long id)
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
