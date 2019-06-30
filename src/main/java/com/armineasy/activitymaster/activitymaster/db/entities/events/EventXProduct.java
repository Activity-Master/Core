package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXProductQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.product.Product;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@Access(FIELD)
public class EventXProduct
		extends WarehouseClassificationRelationshipTable<Event, Product, EventXProduct, EventXProductQueryBuilder, Long, EventXProductSecurityToken>
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

	public String toString()
	{
		return "EventXProduct(id=" + this.getId() + ", securities=" + this.getSecurities() + ", eventID=" + this.getEventID() + ", productID=" + this.getProductID() + ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof EventXProduct))
		{
			return false;
		}
		final EventXProduct other = (EventXProduct) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof EventXProduct;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
