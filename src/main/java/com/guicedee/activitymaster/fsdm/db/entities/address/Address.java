package com.guicedee.activitymaster.fsdm.db.entities.address;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.api.Passwords;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.geography.IGeography;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.builders.AddressQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXAddress;
import com.guicedee.activitymaster.fsdm.db.entities.geography.Geography;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXAddress;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@SuppressWarnings({"unused", "rawtypes"})
@Entity
@Table(schema = "Address",
       name = "Address")
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
public class Address
		extends WarehouseSCDTable<Address, AddressQueryBuilder, UUID, AddressSecurityToken>
		implements IAddress<Address, AddressQueryBuilder>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
	        name = "AddressID")
	@JsonValue

	private java.util.UUID id;
	
@OneToMany(
			mappedBy = "addressID",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
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
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<AddressSecurityToken> securities;
	
@OneToMany(
			mappedBy = "addressID",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<AddressXGeography> geographies;
@OneToMany(
			mappedBy = "addressID",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<AddressXResourceItem> resources;
	
@OneToMany(
			mappedBy = "addressID",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<EventXAddress> events;
@OneToMany(
			mappedBy = "addressID",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<InvolvedPartyXAddress> addresses;

	
	@Override
	public void configureSecurityEntity(AddressSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public List<AddressXClassification> getClassifications()
	{
		return this.classifications;
	}
	
	public Address setClassifications(List<AddressXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}
	
	public List<AddressSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public Address setSecurities(List<AddressSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public List<AddressXGeography> getGeographies()
	{
		return this.geographies;
	}
	
	public Address setGeographies(List<AddressXGeography> geographies)
	{
		this.geographies = geographies;
		return this;
	}
	
	public List<AddressXResourceItem> getResources()
	{
		return this.resources;
	}
	
	public Address setResources(List<AddressXResourceItem> resources)
	{
		this.resources = resources;
		return this;
	}
	
	public List<EventXAddress> getEvents()
	{
		return this.events;
	}
	
	public Address setEvents(List<EventXAddress> events)
	{
		this.events = events;
		return this;
	}
	
	public List<InvolvedPartyXAddress> getAddresses()
	{
		return this.addresses;
	}
	
	public Address setAddresses(List<InvolvedPartyXAddress> addresses)
	{
		this.addresses = addresses;
		return this;
	}
	
	public @NotNull String getValue()
	{
		if ("true".equals(System.getProperty("encrypt", "true")))
		{
			Passwords pass = new Passwords();
			byte[] valueDecrypted = pass.integerDecrypt(this.value);
			return new String(valueDecrypted);
		}
		else
		{
			return this.value;
		}
	}
	
	@Override
	public Address setValue(String value)
	{
		if ("true".equals(System.getProperty("encrypt", "true")))
		{
			this.value = new Passwords().integerEncrypt(value.getBytes());
		}
		else
		{
			this.value = value;
		}
		return this;
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
	public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, Address primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, IEnterprise<?,?> system)
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
		return Objects.hashCode(getId());
	}
}
