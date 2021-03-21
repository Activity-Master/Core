package com.guicedee.activitymaster.core.db.entities.address;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.address.IAddress;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.geography.IGeography;
import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.api.Passwords;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.address.builders.AddressQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.events.EventXAddress;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXAddress;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings({"unused", "rawtypes"})
@Entity
@Table(schema = "Address",
       name = "Address")
@XmlRootElement
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Address
		extends WarehouseTable<Address, AddressQueryBuilder, java.util.UUID>
		implements IAddress<Address, AddressQueryBuilder>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "AddressID")
	@JsonValue
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
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
	private Classification classificationID;
	
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
	public java.util.UUID getId()
	{
		return id;
	}
	
	@Override
	public Address setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public Address(UUID addressID)
	{
		this.id = addressID;
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
		return new String(new Passwords().integerDecrypt(this.value));
	}
	
	public Classification getClassificationID()
	{
		return this.classificationID;
	}
	
	@Override
	public Address setClassificationID(IClassification<?, ?> classificationID)
	{
		this.classificationID = (Classification) classificationID;
		return this;
	}
	
	public Address setValue(@NotNull String value)
	{
		this.value = new Passwords().integerEncrypt(value.getBytes());
		return this;
	}
	
	public Address setClassificationID(Classification classificationID)
	{
		this.classificationID = classificationID;
		return this;
	}
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
	{
		AddressXClassification axg = (AddressXClassification) linkTable;
		axg.setAddressID(this);
	}
	
	@Override
	public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, Address primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		AddressXResourceItem axg = (AddressXResourceItem) linkTable;
		axg.setAddressID(primary);
		axg.setResourceItemID((ResourceItem) secondary);
		axg.setClassificationID(classificationValue);
		axg.setValue(value);
	}
	
	@Override
	public void configureGeographyAddable(IWarehouseRelationshipTable linkTable, Address primary, IGeography<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		AddressXGeography axg = (AddressXGeography) linkTable;
		axg.setAddressID(primary);
		axg.setGeographyID((Geography) secondary);
		axg.setClassificationID(classificationValue);
		axg.setValue(value);
	}
}
