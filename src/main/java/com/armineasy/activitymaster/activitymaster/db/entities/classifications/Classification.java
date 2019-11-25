package com.armineasy.activitymaster.activitymaster.db.entities.classifications;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXGeography;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.*;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders.ClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.EnterpriseXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.events.*;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.Geography;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXGeography;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.*;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemDataXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNoXClassification;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.ClassificationHierarchyView;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsHierarchy;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IResourceItem;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.guicedee.guicedinjection.GuiceContext;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Classification")
@XmlRootElement
@Access(FIELD)
public class Classification
		extends WarehouseTable<Classification, ClassificationQueryBuilder, Long, ClassificationSecurityToken>
		implements IContainsHierarchy<Classification, ClassificationXClassification, ClassificationHierarchyView>,
				           IContainsResourceItems<Classification, ResourceItem, ClassificationXResourceItem, IResourceItemClassification<?>, IClassification<?>, IResourceItem<?>,						                                 Classification>,
				           IActivityMasterEntity<Classification>,
				           IClassification<Classification>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationID")
	private Long id;

	@Basic(optional = false,
			fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 100)
	@Column(nullable = false,
			length = 100,
			name = "ClassificationName")
	private String name;
	@Basic(optional = false,
			fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "ClassificationDesc")
	private String description;
	@Basic(optional = false,
			fetch = EAGER)
	@NotNull
	@Column(nullable = false,
			name = "ClassificationSequenceNumber")
	@OrderBy
	private Short classificationSequenceNumber;
	@JoinColumn(name = "ClassificationDataConceptID",
			referencedColumnName = "ClassificationDataConceptID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.EAGER)
	private ClassificationDataConcept concept;

	@OneToMany(
			mappedBy = "classification",
			fetch = FetchType.LAZY)
	private List<Address> addressList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXResourceItem> involvedPartyXResourceItemList;

	@OneToMany(
			mappedBy = "childClassificationID",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassification> classificationXClassificationList;

	@OneToMany(
			mappedBy = "parentClassificationID",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassification> classificationXClassificationList2;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<AddressXGeography> addressXGeographyList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXClassification> securityTokenXClassificationList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<ProductXClassification> productXClassificationList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<ArrangementXInvolvedParty> arrangementXInvolvedPartyList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItem> arrangementXResourceItemList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<ResourceItemXClassification> resourceItemXClassificationList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<EventXInvolvedParty> eventXInvolvedPartyList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<ActiveFlagXClassification> activeFlagXClassificationList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<EnterpriseXClassification> enterpriseXClassificationList;
	@OneToMany(mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<EventXArrangement> eventXArrangementList;
	@OneToMany(
			mappedBy = "securityTokenClassificationID",
			fetch = FetchType.LAZY)
	private List<SecurityToken> securityTokenList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<SystemXClassification> systemXClassificationList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<ArrangementXClassification> arrangementXClassificationList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	//@JoinColumn(name = "ClassificationID", referencedColumnName = "ClassificationID", nullable = false)
	private List<AddressXClassification> addressXClassificationList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<Geography> geographyList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<AddressXResourceItem> addressXResourceItemList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangement> arrangementXArrangementList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXClassification> classificationDataConceptXClassificationList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<ArrangementXProduct> arrangementXProductList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<EventXResourceItem> eventXResourceItemList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXResourceItem> classificationDataConceptXResourceItemList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXClassification> involvedPartyXClassificationList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<ProductXResourceItem> productXResourceItemList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<EventXAddress> eventXAddressList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProduct> involvedPartyXProductList;
	@OneToMany(
			mappedBy = "valueTypeClassificationID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProduct> involvedPartyXProductList1;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataXClassification> resourceItemDataXClassificationList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddress> involvedPartyXAddressList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<ClassificationXResourceItem> classificationXResourceItemList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<EventXProduct> eventXProductList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<GeographyXClassification> geographyXClassificationList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<EventXClassification> eventXClassificationList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<GeographyXResourceItem> geographyXResourceItemList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<YesNoXClassification> yesNoXClassificationList;
	@OneToMany(
			mappedBy = "classificationID",
			fetch = FetchType.LAZY)
	private List<GeographyXGeography> geographyXGeographyList;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ClassificationSecurityToken> securities;

	public Classification()
	{

	}

	public Classification(Long classificationID)
	{
		this.id = classificationID;
	}

	public Classification(Long classificationID, String classificationName, String classificationDesc, short classificationSequenceNumber)
	{
		this.id = classificationID;
		this.name = classificationName;
		this.description = classificationDesc;
		this.classificationSequenceNumber = classificationSequenceNumber;
	}

	@Override
	public String toString()
	{
		return "Classification - " + getName() + " - " + getDescription();
	}

	@Override
	protected ClassificationSecurityToken configureDefaultsForNewToken(ClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		ClassificationSecurityToken token = super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);
		token.setBase(this);
		return token;
	}

	public void configureForClassification(ClassificationXClassification classificationLink, IEnterprise<?> enterprise)
	{
		Classification hierarchyClassification = (Classification) GuiceContext.get(ClassificationService.class)
		                                                                      .getHierarchyType(classificationLink.getEnterpriseID());
		Classification incomingClassification = classificationLink.getClassificationID();

		classificationLink.setChildClassificationID(incomingClassification);
		classificationLink.setParentClassificationID(this);
		classificationLink.setClassificationID(hierarchyClassification);
	}


	@Override
	public void configureNewHierarchyItem(ClassificationXClassification newLink, Classification parent, Classification child, String value)
	{
		newLink.setParentClassificationID(this);
		newLink.setChildClassificationID(child);
		newLink.setEnterpriseID(getEnterpriseID());
	}

	public List<Address> getAddressList()
	{
		return this.addressList;
	}

	public List<InvolvedPartyXResourceItem> getInvolvedPartyXResourceItemList()
	{
		return this.involvedPartyXResourceItemList;
	}

	public List<ClassificationXClassification> getClassificationXClassificationList()
	{
		return this.classificationXClassificationList;
	}

	public List<ClassificationXClassification> getClassificationXClassificationList2()
	{
		return this.classificationXClassificationList2;
	}

	public List<AddressXGeography> getAddressXGeographyList()
	{
		return this.addressXGeographyList;
	}

	public List<SecurityTokenXClassification> getSecurityTokenXClassificationList()
	{
		return this.securityTokenXClassificationList;
	}

	public List<ProductXClassification> getProductXClassificationList()
	{
		return this.productXClassificationList;
	}

	public List<ArrangementXInvolvedParty> getArrangementXInvolvedPartyList()
	{
		return this.arrangementXInvolvedPartyList;
	}

	public List<ArrangementXResourceItem> getArrangementXResourceItemList()
	{
		return this.arrangementXResourceItemList;
	}

	public List<ResourceItemXClassification> getResourceItemXClassificationList()
	{
		return this.resourceItemXClassificationList;
	}

	public List<EventXInvolvedParty> getEventXInvolvedPartyList()
	{
		return this.eventXInvolvedPartyList;
	}

	public List<ActiveFlagXClassification> getActiveFlagXClassificationList()
	{
		return this.activeFlagXClassificationList;
	}

	public List<EnterpriseXClassification> getEnterpriseXClassificationList()
	{
		return this.enterpriseXClassificationList;
	}

	public List<EventXArrangement> getEventXArrangementList()
	{
		return this.eventXArrangementList;
	}

	public List<SecurityToken> getSecurityTokenList()
	{
		return this.securityTokenList;
	}

	public List<SystemXClassification> getSystemXClassificationList()
	{
		return this.systemXClassificationList;
	}

	public List<ArrangementXClassification> getArrangementXClassificationList()
	{
		return this.arrangementXClassificationList;
	}

	public List<AddressXClassification> getAddressXClassificationList()
	{
		return this.addressXClassificationList;
	}

	public List<Geography> getGeographyList()
	{
		return this.geographyList;
	}

	public List<AddressXResourceItem> getAddressXResourceItemList()
	{
		return this.addressXResourceItemList;
	}

	public List<ArrangementXArrangement> getArrangementXArrangementList()
	{
		return this.arrangementXArrangementList;
	}

	public List<ClassificationDataConceptXClassification> getClassificationDataConceptXClassificationList()
	{
		return this.classificationDataConceptXClassificationList;
	}

	public List<ArrangementXProduct> getArrangementXProductList()
	{
		return this.arrangementXProductList;
	}

	public List<InvolvedPartyXInvolvedParty> getInvolvedPartyXInvolvedPartyList()
	{
		return this.involvedPartyXInvolvedPartyList;
	}

	public List<EventXResourceItem> getEventXResourceItemList()
	{
		return this.eventXResourceItemList;
	}

	public List<ClassificationDataConceptXResourceItem> getClassificationDataConceptXResourceItemList()
	{
		return this.classificationDataConceptXResourceItemList;
	}

	public List<InvolvedPartyXClassification> getInvolvedPartyXClassificationList()
	{
		return this.involvedPartyXClassificationList;
	}

	public List<SecurityTokenXSecurityToken> getSecurityTokenXSecurityTokenList()
	{
		return this.securityTokenXSecurityTokenList;
	}

	public List<ProductXResourceItem> getProductXResourceItemList()
	{
		return this.productXResourceItemList;
	}

	public List<EventXAddress> getEventXAddressList()
	{
		return this.eventXAddressList;
	}

	public List<InvolvedPartyXProduct> getInvolvedPartyXProductList()
	{
		return this.involvedPartyXProductList;
	}

	public List<InvolvedPartyXProduct> getInvolvedPartyXProductList1()
	{
		return this.involvedPartyXProductList1;
	}

	public List<ResourceItemDataXClassification> getResourceItemDataXClassificationList()
	{
		return this.resourceItemDataXClassificationList;
	}

	public List<InvolvedPartyXAddress> getInvolvedPartyXAddressList()
	{
		return this.involvedPartyXAddressList;
	}

	public List<ClassificationXResourceItem> getClassificationXResourceItemList()
	{
		return this.classificationXResourceItemList;
	}

	public List<EventXProduct> getEventXProductList()
	{
		return this.eventXProductList;
	}

	public List<GeographyXClassification> getGeographyXClassificationList()
	{
		return this.geographyXClassificationList;
	}

	public List<EventXClassification> getEventXClassificationList()
	{
		return this.eventXClassificationList;
	}

	public List<GeographyXResourceItem> getGeographyXResourceItemList()
	{
		return this.geographyXResourceItemList;
	}

	public List<YesNoXClassification> getYesNoXClassificationList()
	{
		return this.yesNoXClassificationList;
	}

	public List<GeographyXGeography> getGeographyXGeographyList()
	{
		return this.geographyXGeographyList;
	}

	public List<ClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public Classification setAddressList(List<Address> addressList)
	{
		this.addressList = addressList;
		return this;
	}

	public Classification setInvolvedPartyXResourceItemList(List<InvolvedPartyXResourceItem> involvedPartyXResourceItemList)
	{
		this.involvedPartyXResourceItemList = involvedPartyXResourceItemList;
		return this;
	}

	public Classification setClassificationXClassificationList(List<ClassificationXClassification> classificationXClassificationList)
	{
		this.classificationXClassificationList = classificationXClassificationList;
		return this;
	}

	public Classification setClassificationXClassificationList2(List<ClassificationXClassification> classificationXClassificationList2)
	{
		this.classificationXClassificationList2 = classificationXClassificationList2;
		return this;
	}

	public Classification setAddressXGeographyList(List<AddressXGeography> addressXGeographyList)
	{
		this.addressXGeographyList = addressXGeographyList;
		return this;
	}

	public Classification setSecurityTokenXClassificationList(List<SecurityTokenXClassification> securityTokenXClassificationList)
	{
		this.securityTokenXClassificationList = securityTokenXClassificationList;
		return this;
	}

	public Classification setProductXClassificationList(List<ProductXClassification> productXClassificationList)
	{
		this.productXClassificationList = productXClassificationList;
		return this;
	}

	public Classification setArrangementXInvolvedPartyList(List<ArrangementXInvolvedParty> arrangementXInvolvedPartyList)
	{
		this.arrangementXInvolvedPartyList = arrangementXInvolvedPartyList;
		return this;
	}

	public Classification setArrangementXResourceItemList(List<ArrangementXResourceItem> arrangementXResourceItemList)
	{
		this.arrangementXResourceItemList = arrangementXResourceItemList;
		return this;
	}

	public Classification setResourceItemXClassificationList(List<ResourceItemXClassification> resourceItemXClassificationList)
	{
		this.resourceItemXClassificationList = resourceItemXClassificationList;
		return this;
	}

	public Classification setEventXInvolvedPartyList(List<EventXInvolvedParty> eventXInvolvedPartyList)
	{
		this.eventXInvolvedPartyList = eventXInvolvedPartyList;
		return this;
	}

	public Classification setActiveFlagXClassificationList(List<ActiveFlagXClassification> activeFlagXClassificationList)
	{
		this.activeFlagXClassificationList = activeFlagXClassificationList;
		return this;
	}

	public Classification setEnterpriseXClassificationList(List<EnterpriseXClassification> enterpriseXClassificationList)
	{
		this.enterpriseXClassificationList = enterpriseXClassificationList;
		return this;
	}

	public Classification setEventXArrangementList(List<EventXArrangement> eventXArrangementList)
	{
		this.eventXArrangementList = eventXArrangementList;
		return this;
	}

	public Classification setSecurityTokenList(List<SecurityToken> securityTokenList)
	{
		this.securityTokenList = securityTokenList;
		return this;
	}

	public Classification setSystemXClassificationList(List<SystemXClassification> systemXClassificationList)
	{
		this.systemXClassificationList = systemXClassificationList;
		return this;
	}

	public Classification setArrangementXClassificationList(List<ArrangementXClassification> arrangementXClassificationList)
	{
		this.arrangementXClassificationList = arrangementXClassificationList;
		return this;
	}

	public Classification setAddressXClassificationList(List<AddressXClassification> addressXClassificationList)
	{
		this.addressXClassificationList = addressXClassificationList;
		return this;
	}

	public Classification setGeographyList(List<Geography> geographyList)
	{
		this.geographyList = geographyList;
		return this;
	}

	public Classification setAddressXResourceItemList(List<AddressXResourceItem> addressXResourceItemList)
	{
		this.addressXResourceItemList = addressXResourceItemList;
		return this;
	}

	public Classification setArrangementXArrangementList(List<ArrangementXArrangement> arrangementXArrangementList)
	{
		this.arrangementXArrangementList = arrangementXArrangementList;
		return this;
	}

	public Classification setClassificationDataConceptXClassificationList(List<ClassificationDataConceptXClassification> classificationDataConceptXClassificationList)
	{
		this.classificationDataConceptXClassificationList = classificationDataConceptXClassificationList;
		return this;
	}

	public Classification setArrangementXProductList(List<ArrangementXProduct> arrangementXProductList)
	{
		this.arrangementXProductList = arrangementXProductList;
		return this;
	}

	public Classification setInvolvedPartyXInvolvedPartyList(List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList)
	{
		this.involvedPartyXInvolvedPartyList = involvedPartyXInvolvedPartyList;
		return this;
	}

	public Classification setEventXResourceItemList(List<EventXResourceItem> eventXResourceItemList)
	{
		this.eventXResourceItemList = eventXResourceItemList;
		return this;
	}

	public Classification setClassificationDataConceptXResourceItemList(List<ClassificationDataConceptXResourceItem> classificationDataConceptXResourceItemList)
	{
		this.classificationDataConceptXResourceItemList = classificationDataConceptXResourceItemList;
		return this;
	}

	public Classification setInvolvedPartyXClassificationList(List<InvolvedPartyXClassification> involvedPartyXClassificationList)
	{
		this.involvedPartyXClassificationList = involvedPartyXClassificationList;
		return this;
	}

	public Classification setSecurityTokenXSecurityTokenList(List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenList)
	{
		this.securityTokenXSecurityTokenList = securityTokenXSecurityTokenList;
		return this;
	}

	public Classification setProductXResourceItemList(List<ProductXResourceItem> productXResourceItemList)
	{
		this.productXResourceItemList = productXResourceItemList;
		return this;
	}

	public Classification setEventXAddressList(List<EventXAddress> eventXAddressList)
	{
		this.eventXAddressList = eventXAddressList;
		return this;
	}

	public Classification setInvolvedPartyXProductList(List<InvolvedPartyXProduct> involvedPartyXProductList)
	{
		this.involvedPartyXProductList = involvedPartyXProductList;
		return this;
	}

	public Classification setInvolvedPartyXProductList1(List<InvolvedPartyXProduct> involvedPartyXProductList1)
	{
		this.involvedPartyXProductList1 = involvedPartyXProductList1;
		return this;
	}

	public Classification setResourceItemDataXClassificationList(List<ResourceItemDataXClassification> resourceItemDataXClassificationList)
	{
		this.resourceItemDataXClassificationList = resourceItemDataXClassificationList;
		return this;
	}

	public Classification setInvolvedPartyXAddressList(List<InvolvedPartyXAddress> involvedPartyXAddressList)
	{
		this.involvedPartyXAddressList = involvedPartyXAddressList;
		return this;
	}

	public Classification setClassificationXResourceItemList(List<ClassificationXResourceItem> classificationXResourceItemList)
	{
		this.classificationXResourceItemList = classificationXResourceItemList;
		return this;
	}

	public Classification setEventXProductList(List<EventXProduct> eventXProductList)
	{
		this.eventXProductList = eventXProductList;
		return this;
	}

	public Classification setGeographyXClassificationList(List<GeographyXClassification> geographyXClassificationList)
	{
		this.geographyXClassificationList = geographyXClassificationList;
		return this;
	}

	public Classification setEventXClassificationList(List<EventXClassification> eventXClassificationList)
	{
		this.eventXClassificationList = eventXClassificationList;
		return this;
	}

	public Classification setGeographyXResourceItemList(List<GeographyXResourceItem> geographyXResourceItemList)
	{
		this.geographyXResourceItemList = geographyXResourceItemList;
		return this;
	}

	public Classification setYesNoXClassificationList(List<YesNoXClassification> yesNoXClassificationList)
	{
		this.yesNoXClassificationList = yesNoXClassificationList;
		return this;
	}

	public Classification setGeographyXGeographyList(List<GeographyXGeography> geographyXGeographyList)
	{
		this.geographyXGeographyList = geographyXGeographyList;
		return this;
	}

	public Classification setSecurities(List<ClassificationSecurityToken> securities)
	{
		this.securities = securities;
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
		Classification that = (Classification) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	public Long getId()
	{
		return this.id;
	}

	public @NotNull @Size(min = 1,
			max = 100) String getName()
	{
		return this.name;
	}

	public @NotNull @Size(min = 1,
			max = 500) String getDescription()
	{
		return this.description;
	}

	public @NotNull Short getClassificationSequenceNumber()
	{
		return this.classificationSequenceNumber;
	}

	public ClassificationDataConcept getConcept()
	{
		return this.concept;
	}

	public Classification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public Classification setName(@NotNull @Size(min = 1,
			max = 100) String name)
	{
		this.name = name;
		return this;
	}

	public Classification setDescription(@NotNull @Size(min = 1,
			max = 500) String description)
	{
		this.description = description;
		return this;
	}

	public Classification setClassificationSequenceNumber(@NotNull Short classificationSequenceNumber)
	{
		this.classificationSequenceNumber = classificationSequenceNumber;
		return this;
	}

	public Classification setConcept(ClassificationDataConcept concept)
	{
		this.concept = concept;
		return this;
	}

	@Override
	public void configureResourceItemLinkValue(ClassificationXResourceItem linkTable, Classification primary, ResourceItem secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setClassificationID(this);
		linkTable.setResourceItemID(secondary);
	}
}
