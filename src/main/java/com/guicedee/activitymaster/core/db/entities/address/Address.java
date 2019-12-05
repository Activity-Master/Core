package com.guicedee.activitymaster.core.db.entities.address;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.address.builders.AddressQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.events.EventXAddress;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXAddress;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.dto.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Address")
@XmlRootElement

@Access(FIELD)
public class Address
		extends WarehouseTable<Address, AddressQueryBuilder, Long, AddressSecurityToken>
		implements IContainsClassifications<Address, Classification, AddressXClassification, IAddressClassification<?>, IAddress<?>, IClassification<?>, Address>,
				           IContainsGeographies<Address, Geography, AddressXGeography>,
				           IContainsResourceItems<Address, ResourceItem, AddressXResourceItem, IResourceItemClassification<?>,IAddress<?>, IResourceItem<?>, Address>,
				           IActivityMasterEntity<Address>,
				           IContainsEnterprise<Address>,
				           IContainsActiveFlags<Address>,
				           IAddress<Address>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "AddressID")
	private Long id;

	@OneToMany(
			mappedBy = "addressID",
			fetch = FetchType.LAZY)
	private List<AddressXClassification> classifications;


	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
			name = "Value")
	private String value;

	@JoinColumn(name = "ClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Classification classification;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<AddressSecurityToken> securities;

	@OneToMany(
			mappedBy = "addressID",
			fetch = FetchType.LAZY)
	private List<AddressXGeography> geographies;
	@OneToMany(
			mappedBy = "addressID",
			fetch = FetchType.LAZY)
	private List<AddressXResourceItem> resources;

	@OneToMany(
			mappedBy = "addressID",
			fetch = FetchType.LAZY)
	private List<EventXAddress> events;
	@OneToMany(
			mappedBy = "addressID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddress> addresses;

	public Address()
	{

	}

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public Address setId(Long id)
	{
		this.id = id;
		return this;
	}

	public Address(Long addressID)
	{
		this.id = addressID;
	}



	@Override
	protected AddressSecurityToken configureDefaultsForNewToken(AddressSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(AddressXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setAddressID(this);
	}

	@Override
	public void setMyGeographyLinkValue(AddressXGeography classificationLink, Geography geography, IEnterprise<?> enterprise)
	{
		classificationLink.setAddressID(this);
		classificationLink.setGeographyID(geography);
	}

	@Override
	public void configureResourceItemLinkValue(AddressXResourceItem linkTable, Address primary, ResourceItem secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setAddressID(this);
		linkTable.setResourceItemID(secondary);
	}

	public List<AddressXClassification> getClassifications()
	{
		return this.classifications;
	}

	public List<AddressSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public List<AddressXGeography> getGeographies()
	{
		return this.geographies;
	}

	public List<AddressXResourceItem> getResources()
	{
		return this.resources;
	}

	public List<EventXAddress> getEvents()
	{
		return this.events;
	}

	public List<InvolvedPartyXAddress> getAddresses()
	{
		return this.addresses;
	}

	public Address setClassifications(List<AddressXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}

	public Address setSecurities(List<AddressSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public Address setGeographies(List<AddressXGeography> geographies)
	{
		this.geographies = geographies;
		return this;
	}

	public Address setResources(List<AddressXResourceItem> resources)
	{
		this.resources = resources;
		return this;
	}

	public Address setEvents(List<EventXAddress> events)
	{
		this.events = events;
		return this;
	}

	public Address setAddresses(List<InvolvedPartyXAddress> addresses)
	{
		this.addresses = addresses;
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
		Address address = (Address) o;
		return Objects.equals(getId(), address.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	public @NotNull String getValue()
	{
		return this.value;
	}

	public Classification getClassification()
	{
		return this.classification;
	}

	public Address setValue(@NotNull String value)
	{
		this.value = value;
		return this;
	}

	public Address setClassification(Classification classification)
	{
		this.classification = classification;
		return this;
	}


}
