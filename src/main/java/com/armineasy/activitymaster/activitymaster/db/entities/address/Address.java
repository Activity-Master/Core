package com.armineasy.activitymaster.activitymaster.db.entities.address;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.builders.AddressQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXAddress;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.Geography;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXAddress;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsGeographies;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;
import com.armineasy.activitymaster.activitymaster.services.classifications.address.IAddressClassification;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Address")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class Address
		extends WarehouseTable<Address, AddressQueryBuilder, Long, AddressSecurityToken>
		implements IContainsClassifications<Address, Classification, AddressXClassification, IAddressClassification>,
				           IContainsGeographies<Address, Geography, AddressXGeography>,
				           IContainsResourceItems<Address, ResourceItem, AddressXResourceItem>
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
	@Getter
	@Setter
	private String value;

	@JoinColumn(name = "ClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	@Getter
	@Setter
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
	protected AddressSecurityToken configureDefaultsForNewToken(AddressSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(AddressXClassification classificationLink, Enterprise enterprise)
	{
		classificationLink.setAddressID(this);
	}

	@Override
	public void setMyGeographyLinkValue(AddressXGeography classificationLink, Geography geography, Enterprise enterprise)
	{
		classificationLink.setAddressID(this);
		classificationLink.setGeographyID(geography);
	}

	@Override
	public void setMyResourceItemLinkValue(AddressXResourceItem classificationLink, ResourceItem resourceItem, Enterprise enterprise)
	{
		classificationLink.setAddressID(this);
		classificationLink.setResourceItemID(resourceItem);
	}
}
