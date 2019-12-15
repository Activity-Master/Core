package com.guicedee.activitymaster.core.db.entities.enterprise;

import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseClassification;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Enterprise")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class Enterprise
		extends WarehouseNameDescriptionTable<Enterprise, EnterpriseQueryBuilder, Long, EnterpriseSecurityToken>
		implements IContainsClassifications<Enterprise, Classification, EnterpriseXClassification, IEnterpriseClassification<?>, IEnterprise<?>, IClassification<?>, Enterprise>,
				           IActivityMasterEntity<Enterprise>,
				           INameAndDescription<Enterprise>,
				           IEnterprise<Enterprise>
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EnterpriseID")
	private Long id;

	@Basic(optional = false,
			fetch = FetchType.EAGER)
	@NotNull
	@Lob
	@Column(nullable = false,
			name = "EnterpriseName")
	private String name;
	@Basic(optional = false,
			fetch = FetchType.EAGER)
	@NotNull
	@Lob
	@Column(nullable = false,
			name = "EnterpriseDesc")
	private String description;

	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EnterpriseSecurityToken> securities;

	/*
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyNameType> involvedPartyXInvolvedPartyNameTypeList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProductSecurityToken> involvedPartyXProductSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<Address> addressList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ProductXResourceItemSecurityToken> productXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<Product> productList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ResourceItemXClassificationSecurityToken> resourceItemXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXResourceItem> involvedPartyXResourceItemList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNameType> involvedPartyNameTypeList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventXClassificationSecurityToken> eventXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ProductSecurityToken> productSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedParty> involvedPartyList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementType> arrangementXArrangementTypeList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> involvedPartyXInvolvedPartyNameTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<Classification> classificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventXEventTypeSecurityToken> eventXEventTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventXProductSecurityToken> eventXProductSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNameTypeSecurityToken> involvedPartyNameTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ClassificationXResourceItemSecurityToken> classificationXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventXResourceItemSecurityToken> eventXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<AddressSecurityToken> addressSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<SystemsSecurityToken> systemSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ActiveFlag> activeFlagList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassification> classificationXClassificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConcept> classificationDataConceptList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<AddressXGeography> addressXGeographyList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXClassification> securityTokenXClassificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventXArrangementsSecurityToken> eventXArrangementsSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXResourceItemSecurityToken> involvedPartyXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ProductXClassification> productXClassificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataSecurityToken> resourceItemDataSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ArrangementXInvolvedParty> arrangementXInvolvedPartyList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItem> arrangementXResourceItemList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ProductXProductSecurityToken> productXProductSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ResourceItemXClassification> resourceItemXClassificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ArrangementXInvolvedPartySecurityToken> arrangementXInvolvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNonOrganic> involvedPartyNonOrganicList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventXInvolvedParty> eventXInvolvedPartyList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ActiveFlagXClassification> activeFlagXClassificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<GeographyXResourceItemSecurityToken> geographyXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EnterpriseXClassification> enterpriseXClassificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventXArrangement> eventXArrangementList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyIdentificationTypeSecurityToken> involvedPartyIdentificationTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<AddressXResourceItemSecurityToken> addressXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartySecurityToken> involvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ProductXClassificationSecurityToken> productXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<SecurityToken> securityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementTypeSecurityToken> arrangementXArrangementTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventXInvolvedPartySecurityToken> eventXInvolvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganic> involvedPartyOrganicList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<SystemXClassification> systemXClassificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<Event> eventList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXClassificationSecurityToken> classificationDataConceptXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ActiveFlagSecurityToken> activeFlagSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ArrangementXClassification> arrangementXClassificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<Geography> geographyList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicTypeSecurityToken> involvedPartyOrganicTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventType> eventTypeList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ResourceItemData> resourceItemDataList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<AddressXResourceItem> addressXResourceItemList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ArrangementTypeSecurityToken> arrangementTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicSecurityToken> involvedPartyOrganicSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangement> arrangementXArrangementList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXClassification> classificationDataConceptXClassificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ArrangementXProduct> arrangementXProductList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ArrangementSecurityToken> arrangementSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicType> involvedPartyOrganicTypeList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyTypeSecurityToken> involvedPartyXInvolvedPartyTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptSecurityToken> classificationDataConceptSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventXResourceItem> eventXResourceItemList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXClassification> involvedPartyXClassificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ArrangementType> arrangementTypeList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<Systems> systemsList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyTypeSecurityToken> involvedPartyTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ProductXResourceItem> productXResourceItemList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXClassificationSecurityToken> involvedPartyXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventXAddressSecurityToken> eventXAddressSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ResourceItem> resourceItemList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventXAddress> eventXAddressList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXResourceItem> classificationDataConceptXResourceItemList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProduct> involvedPartyXProductList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventSecurityToken> eventSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataXClassification> resourceItemDataXClassificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<SecurityTokensSecurityToken> securityTokenAccessList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddress> involvedPartyXAddressList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<GeographyXClassificationSecurityToken> geographyXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<GeographyXGeographySecurityToken> geographyXGeographySecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementSecurityToken> arrangementXArrangementSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<GeographySecurityToken> geographySecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyIdentificationType> involvedPartyXInvolvedPartyIdentificationTypeList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyIdentificationType> involvedPartyIdentificationTypeList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ClassificationXResourceItem> classificationXResourceItemList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventXProduct> eventXProductList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventXEventType> eventXEventTypeList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartySecurityToken> involvedPartyXInvolvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ProductXProduct> productXProductList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyType> involvedPartyTypeList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ArrangementXClassificationSecurityToken> arrangementXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddressSecurityToken> involvedPartyXAddressSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<GeographyXClassification> geographyXClassificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<AddressXGeographySecurityToken> addressXGeographySecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNonOrganicSecurityToken> involvedPartyNonOrganicSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventXClassification> eventXClassificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<Arrangement> arrangementList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ResourceItemSecurityToken> resourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<GeographyXResourceItem> geographyXResourceItemList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassificationSecurityToken> classificationXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EventTypesSecurityToken> eventTypesSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<GeographyXGeography> geographyXGeographyList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ClassificationSecurityToken> classificationSecurityTokenList;
*/
	public Enterprise()
	{

	}

	public Enterprise(Long id)
	{
		this.id = id;
	}

	public Enterprise(Long id, String enterpriseName, String enterpriseDesc)
	{
		this.id = id;
		name = enterpriseName;
		description = enterpriseDesc;
	}

	@Override
	public String toString()
	{
		return "Enterprise - " + getName();
	}

	@Override
	public void configureForClassification(EnterpriseXClassification classificationLink, IEnterprise<?> enterprise)
	{

	}

	@Override
	@SuppressWarnings("unchecked")
	public Enterprise remove()
	{
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Enterprise archive()
	{
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return this;
	}

	public List<EnterpriseSecurityToken> getSecurities()
	{
		return securities;
	}
/*

	public List<InvolvedPartyXInvolvedPartyNameType> getInvolvedPartyXInvolvedPartyNameTypeList()
	{
		return this.involvedPartyXInvolvedPartyNameTypeList;
	}

	public List<InvolvedPartyXProductSecurityToken> getInvolvedPartyXProductSecurityTokenList()
	{
		return this.involvedPartyXProductSecurityTokenList;
	}

	public List<Address> getAddressList()
	{
		return this.addressList;
	}

	public List<ProductXResourceItemSecurityToken> getProductXResourceItemSecurityTokenList()
	{
		return this.productXResourceItemSecurityTokenList;
	}

	public List<Product> getProductList()
	{
		return this.productList;
	}

	public List<ResourceItemXClassificationSecurityToken> getResourceItemXClassificationSecurityTokenList()
	{
		return this.resourceItemXClassificationSecurityTokenList;
	}

	public List<InvolvedPartyXResourceItem> getInvolvedPartyXResourceItemList()
	{
		return this.involvedPartyXResourceItemList;
	}

	public List<InvolvedPartyNameType> getInvolvedPartyNameTypeList()
	{
		return this.involvedPartyNameTypeList;
	}

	public List<EventXClassificationSecurityToken> getEventXClassificationSecurityTokenList()
	{
		return this.eventXClassificationSecurityTokenList;
	}

	public List<ProductSecurityToken> getProductSecurityTokenList()
	{
		return this.productSecurityTokenList;
	}

	public List<InvolvedParty> getInvolvedPartyList()
	{
		return this.involvedPartyList;
	}

	public List<ArrangementXArrangementType> getArrangementXArrangementTypeList()
	{
		return this.arrangementXArrangementTypeList;
	}

	public List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> getInvolvedPartyXInvolvedPartyNameTypeSecurityTokenList()
	{
		return this.involvedPartyXInvolvedPartyNameTypeSecurityTokenList;
	}

	public List<Classification> getClassificationList()
	{
		return this.classificationList;
	}

	public List<EventXEventTypeSecurityToken> getEventXEventTypeSecurityTokenList()
	{
		return this.eventXEventTypeSecurityTokenList;
	}

	public List<ArrangementXResourceItemSecurityToken> getArrangementXResourceItemSecurityTokenList()
	{
		return this.arrangementXResourceItemSecurityTokenList;
	}

	public List<EventXProductSecurityToken> getEventXProductSecurityTokenList()
	{
		return this.eventXProductSecurityTokenList;
	}

	public List<InvolvedPartyNameTypeSecurityToken> getInvolvedPartyNameTypeSecurityTokenList()
	{
		return this.involvedPartyNameTypeSecurityTokenList;
	}

	public List<ClassificationXResourceItemSecurityToken> getClassificationXResourceItemSecurityTokenList()
	{
		return this.classificationXResourceItemSecurityTokenList;
	}

	public List<EventXResourceItemSecurityToken> getEventXResourceItemSecurityTokenList()
	{
		return this.eventXResourceItemSecurityTokenList;
	}

	public List<AddressSecurityToken> getAddressSecurityTokenList()
	{
		return this.addressSecurityTokenList;
	}

	public List<SystemsSecurityToken> getSystemSecurityTokenList()
	{
		return this.systemSecurityTokenList;
	}

	public List<ActiveFlag> getActiveFlagList()
	{
		return this.activeFlagList;
	}

	public List<ClassificationXClassification> getClassificationXClassificationList()
	{
		return this.classificationXClassificationList;
	}

	public List<ClassificationDataConcept> getClassificationDataConceptList()
	{
		return this.classificationDataConceptList;
	}

	public List<AddressXGeography> getAddressXGeographyList()
	{
		return this.addressXGeographyList;
	}

	public List<SecurityTokenXClassification> getSecurityTokenXClassificationList()
	{
		return this.securityTokenXClassificationList;
	}

	public List<EventXArrangementsSecurityToken> getEventXArrangementsSecurityTokenList()
	{
		return this.eventXArrangementsSecurityTokenList;
	}

	public List<InvolvedPartyXResourceItemSecurityToken> getInvolvedPartyXResourceItemSecurityTokenList()
	{
		return this.involvedPartyXResourceItemSecurityTokenList;
	}

	public List<ProductXClassification> getProductXClassificationList()
	{
		return this.productXClassificationList;
	}

	public List<ResourceItemDataSecurityToken> getResourceItemDataSecurityTokenList()
	{
		return this.resourceItemDataSecurityTokenList;
	}

	public List<ArrangementXInvolvedParty> getArrangementXInvolvedPartyList()
	{
		return this.arrangementXInvolvedPartyList;
	}

	public List<ArrangementXResourceItem> getArrangementXResourceItemList()
	{
		return this.arrangementXResourceItemList;
	}

	public List<ProductXProductSecurityToken> getProductXProductSecurityTokenList()
	{
		return this.productXProductSecurityTokenList;
	}

	public List<ResourceItemXClassification> getResourceItemXClassificationList()
	{
		return this.resourceItemXClassificationList;
	}

	public List<ArrangementXInvolvedPartySecurityToken> getArrangementXInvolvedPartySecurityTokenList()
	{
		return this.arrangementXInvolvedPartySecurityTokenList;
	}

	public List<InvolvedPartyNonOrganic> getInvolvedPartyNonOrganicList()
	{
		return this.involvedPartyNonOrganicList;
	}

	public List<EventXInvolvedParty> getEventXInvolvedPartyList()
	{
		return this.eventXInvolvedPartyList;
	}

	public List<ActiveFlagXClassification> getActiveFlagXClassificationList()
	{
		return this.activeFlagXClassificationList;
	}

	public List<GeographyXResourceItemSecurityToken> getGeographyXResourceItemSecurityTokenList()
	{
		return this.geographyXResourceItemSecurityTokenList;
	}

	public List<EnterpriseXClassification> getEnterpriseXClassificationList()
	{
		return this.enterpriseXClassificationList;
	}

	public List<EventXArrangement> getEventXArrangementList()
	{
		return this.eventXArrangementList;
	}

	public List<InvolvedPartyIdentificationTypeSecurityToken> getInvolvedPartyIdentificationTypeSecurityTokenList()
	{
		return this.involvedPartyIdentificationTypeSecurityTokenList;
	}

	public List<AddressXResourceItemSecurityToken> getAddressXResourceItemSecurityTokenList()
	{
		return this.addressXResourceItemSecurityTokenList;
	}

	public List<InvolvedPartySecurityToken> getInvolvedPartySecurityTokenList()
	{
		return this.involvedPartySecurityTokenList;
	}

	public List<ProductXClassificationSecurityToken> getProductXClassificationSecurityTokenList()
	{
		return this.productXClassificationSecurityTokenList;
	}

	public List<SecurityToken> getSecurityTokenList()
	{
		return this.securityTokenList;
	}

	public List<ArrangementXArrangementTypeSecurityToken> getArrangementXArrangementTypeSecurityTokenList()
	{
		return this.arrangementXArrangementTypeSecurityTokenList;
	}

	public List<EventXInvolvedPartySecurityToken> getEventXInvolvedPartySecurityTokenList()
	{
		return this.eventXInvolvedPartySecurityTokenList;
	}

	public List<InvolvedPartyOrganic> getInvolvedPartyOrganicList()
	{
		return this.involvedPartyOrganicList;
	}

	public List<SystemXClassification> getSystemXClassificationList()
	{
		return this.systemXClassificationList;
	}

	public List<Event> getEventList()
	{
		return this.eventList;
	}

	public List<ClassificationDataConceptXClassificationSecurityToken> getClassificationDataConceptXClassificationSecurityTokenList()
	{
		return this.classificationDataConceptXClassificationSecurityTokenList;
	}

	public List<ActiveFlagSecurityToken> getActiveFlagSecurityTokenList()
	{
		return this.activeFlagSecurityTokenList;
	}

	public List<ArrangementXClassification> getArrangementXClassificationList()
	{
		return this.arrangementXClassificationList;
	}

	public List<Geography> getGeographyList()
	{
		return this.geographyList;
	}

	public List<InvolvedPartyOrganicTypeSecurityToken> getInvolvedPartyOrganicTypeSecurityTokenList()
	{
		return this.involvedPartyOrganicTypeSecurityTokenList;
	}

	public List<EventType> getEventTypeList()
	{
		return this.eventTypeList;
	}

	public List<ResourceItemData> getResourceItemDataList()
	{
		return this.resourceItemDataList;
	}

	public List<AddressXResourceItem> getAddressXResourceItemList()
	{
		return this.addressXResourceItemList;
	}

	public List<ArrangementTypeSecurityToken> getArrangementTypeSecurityTokenList()
	{
		return this.arrangementTypeSecurityTokenList;
	}

	public List<InvolvedPartyOrganicSecurityToken> getInvolvedPartyOrganicSecurityTokenList()
	{
		return this.involvedPartyOrganicSecurityTokenList;
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

	public List<InvolvedPartyXInvolvedPartyType> getInvolvedPartyXInvolvedPartyTypeList()
	{
		return this.involvedPartyXInvolvedPartyTypeList;
	}

	public List<ArrangementSecurityToken> getArrangementSecurityTokenList()
	{
		return this.arrangementSecurityTokenList;
	}

	public List<InvolvedPartyOrganicType> getInvolvedPartyOrganicTypeList()
	{
		return this.involvedPartyOrganicTypeList;
	}

	public List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> getInvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList()
	{
		return this.involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList;
	}

	public List<InvolvedPartyXInvolvedPartyTypeSecurityToken> getInvolvedPartyXInvolvedPartyTypeSecurityTokenList()
	{
		return this.involvedPartyXInvolvedPartyTypeSecurityTokenList;
	}

	public List<ClassificationDataConceptSecurityToken> getClassificationDataConceptSecurityTokenList()
	{
		return this.classificationDataConceptSecurityTokenList;
	}

	public List<EventXResourceItem> getEventXResourceItemList()
	{
		return this.eventXResourceItemList;
	}

	public List<InvolvedPartyXClassification> getInvolvedPartyXClassificationList()
	{
		return this.involvedPartyXClassificationList;
	}

	public List<ArrangementType> getArrangementTypeList()
	{
		return this.arrangementTypeList;
	}

	public List<Systems> getSystemsList()
	{
		return this.systemsList;
	}

	public List<InvolvedPartyTypeSecurityToken> getInvolvedPartyTypeSecurityTokenList()
	{
		return this.involvedPartyTypeSecurityTokenList;
	}

	public List<SecurityTokenXSecurityToken> getSecurityTokenXSecurityTokenList()
	{
		return this.securityTokenXSecurityTokenList;
	}

	public List<ProductXResourceItem> getProductXResourceItemList()
	{
		return this.productXResourceItemList;
	}

	public List<InvolvedPartyXClassificationSecurityToken> getInvolvedPartyXClassificationSecurityTokenList()
	{
		return this.involvedPartyXClassificationSecurityTokenList;
	}

	public List<EventXAddressSecurityToken> getEventXAddressSecurityTokenList()
	{
		return this.eventXAddressSecurityTokenList;
	}

	public List<ResourceItem> getResourceItemList()
	{
		return this.resourceItemList;
	}

	public List<EventXAddress> getEventXAddressList()
	{
		return this.eventXAddressList;
	}

	public List<ClassificationDataConceptXResourceItem> getClassificationDataConceptXResourceItemList()
	{
		return this.classificationDataConceptXResourceItemList;
	}

	public List<InvolvedPartyXProduct> getInvolvedPartyXProductList()
	{
		return this.involvedPartyXProductList;
	}

	public List<EventSecurityToken> getEventSecurityTokenList()
	{
		return this.eventSecurityTokenList;
	}

	public List<ResourceItemDataXClassification> getResourceItemDataXClassificationList()
	{
		return this.resourceItemDataXClassificationList;
	}

	public List<SecurityTokensSecurityToken> getSecurityTokenAccessList()
	{
		return this.securityTokenAccessList;
	}

	public List<InvolvedPartyXAddress> getInvolvedPartyXAddressList()
	{
		return this.involvedPartyXAddressList;
	}

	public List<GeographyXClassificationSecurityToken> getGeographyXClassificationSecurityTokenList()
	{
		return this.geographyXClassificationSecurityTokenList;
	}

	public List<GeographyXGeographySecurityToken> getGeographyXGeographySecurityTokenList()
	{
		return this.geographyXGeographySecurityTokenList;
	}

	public List<ArrangementXArrangementSecurityToken> getArrangementXArrangementSecurityTokenList()
	{
		return this.arrangementXArrangementSecurityTokenList;
	}

	public List<GeographySecurityToken> getGeographySecurityTokenList()
	{
		return this.geographySecurityTokenList;
	}

	public List<InvolvedPartyXInvolvedPartyIdentificationType> getInvolvedPartyXInvolvedPartyIdentificationTypeList()
	{
		return this.involvedPartyXInvolvedPartyIdentificationTypeList;
	}

	public List<InvolvedPartyIdentificationType> getInvolvedPartyIdentificationTypeList()
	{
		return this.involvedPartyIdentificationTypeList;
	}

	public List<ClassificationXResourceItem> getClassificationXResourceItemList()
	{
		return this.classificationXResourceItemList;
	}

	public List<EventXProduct> getEventXProductList()
	{
		return this.eventXProductList;
	}

	public List<EventXEventType> getEventXEventTypeList()
	{
		return this.eventXEventTypeList;
	}

	public List<InvolvedPartyXInvolvedPartySecurityToken> getInvolvedPartyXInvolvedPartySecurityTokenList()
	{
		return this.involvedPartyXInvolvedPartySecurityTokenList;
	}

	public List<ProductXProduct> getProductXProductList()
	{
		return this.productXProductList;
	}

	public List<InvolvedPartyType> getInvolvedPartyTypeList()
	{
		return this.involvedPartyTypeList;
	}

	public List<ArrangementXClassificationSecurityToken> getArrangementXClassificationSecurityTokenList()
	{
		return this.arrangementXClassificationSecurityTokenList;
	}

	public List<InvolvedPartyXAddressSecurityToken> getInvolvedPartyXAddressSecurityTokenList()
	{
		return this.involvedPartyXAddressSecurityTokenList;
	}

	public List<GeographyXClassification> getGeographyXClassificationList()
	{
		return this.geographyXClassificationList;
	}

	public List<AddressXGeographySecurityToken> getAddressXGeographySecurityTokenList()
	{
		return this.addressXGeographySecurityTokenList;
	}

	public List<InvolvedPartyNonOrganicSecurityToken> getInvolvedPartyNonOrganicSecurityTokenList()
	{
		return this.involvedPartyNonOrganicSecurityTokenList;
	}

	public List<EventXClassification> getEventXClassificationList()
	{
		return this.eventXClassificationList;
	}

	public List<Arrangement> getArrangementList()
	{
		return this.arrangementList;
	}

	public List<ResourceItemSecurityToken> getResourceItemSecurityTokenList()
	{
		return this.resourceItemSecurityTokenList;
	}

	public List<GeographyXResourceItem> getGeographyXResourceItemList()
	{
		return this.geographyXResourceItemList;
	}

	public List<ClassificationXClassificationSecurityToken> getClassificationXClassificationSecurityTokenList()
	{
		return this.classificationXClassificationSecurityTokenList;
	}

	public List<EventTypesSecurityToken> getEventTypesSecurityTokenList()
	{
		return this.eventTypesSecurityTokenList;
	}

	public List<GeographyXGeography> getGeographyXGeographyList()
	{
		return this.geographyXGeographyList;
	}

	public List<ClassificationSecurityToken> getClassificationSecurityTokenList()
	{
		return this.classificationSecurityTokenList;
	}

	public Enterprise setSecurities(List<EnterpriseSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public Enterprise setInvolvedPartyXInvolvedPartyNameTypeList(List<InvolvedPartyXInvolvedPartyNameType> involvedPartyXInvolvedPartyNameTypeList)
	{
		this.involvedPartyXInvolvedPartyNameTypeList = involvedPartyXInvolvedPartyNameTypeList;
		return this;
	}

	public Enterprise setInvolvedPartyXProductSecurityTokenList(List<InvolvedPartyXProductSecurityToken> involvedPartyXProductSecurityTokenList)
	{
		this.involvedPartyXProductSecurityTokenList = involvedPartyXProductSecurityTokenList;
		return this;
	}

	public Enterprise setAddressList(List<Address> addressList)
	{
		this.addressList = addressList;
		return this;
	}

	public Enterprise setProductXResourceItemSecurityTokenList(List<ProductXResourceItemSecurityToken> productXResourceItemSecurityTokenList)
	{
		this.productXResourceItemSecurityTokenList = productXResourceItemSecurityTokenList;
		return this;
	}

	public Enterprise setProductList(List<Product> productList)
	{
		this.productList = productList;
		return this;
	}

	public Enterprise setResourceItemXClassificationSecurityTokenList(List<ResourceItemXClassificationSecurityToken> resourceItemXClassificationSecurityTokenList)
	{
		this.resourceItemXClassificationSecurityTokenList = resourceItemXClassificationSecurityTokenList;
		return this;
	}

	public Enterprise setInvolvedPartyXResourceItemList(List<InvolvedPartyXResourceItem> involvedPartyXResourceItemList)
	{
		this.involvedPartyXResourceItemList = involvedPartyXResourceItemList;
		return this;
	}

	public Enterprise setInvolvedPartyNameTypeList(List<InvolvedPartyNameType> involvedPartyNameTypeList)
	{
		this.involvedPartyNameTypeList = involvedPartyNameTypeList;
		return this;
	}

	public Enterprise setEventXClassificationSecurityTokenList(List<EventXClassificationSecurityToken> eventXClassificationSecurityTokenList)
	{
		this.eventXClassificationSecurityTokenList = eventXClassificationSecurityTokenList;
		return this;
	}

	public Enterprise setProductSecurityTokenList(List<ProductSecurityToken> productSecurityTokenList)
	{
		this.productSecurityTokenList = productSecurityTokenList;
		return this;
	}

	public Enterprise setInvolvedPartyList(List<InvolvedParty> involvedPartyList)
	{
		this.involvedPartyList = involvedPartyList;
		return this;
	}

	public Enterprise setArrangementXArrangementTypeList(List<ArrangementXArrangementType> arrangementXArrangementTypeList)
	{
		this.arrangementXArrangementTypeList = arrangementXArrangementTypeList;
		return this;
	}

	public Enterprise setInvolvedPartyXInvolvedPartyNameTypeSecurityTokenList(List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> involvedPartyXInvolvedPartyNameTypeSecurityTokenList)
	{
		this.involvedPartyXInvolvedPartyNameTypeSecurityTokenList = involvedPartyXInvolvedPartyNameTypeSecurityTokenList;
		return this;
	}

	public Enterprise setClassificationList(List<Classification> classificationList)
	{
		this.classificationList = classificationList;
		return this;
	}

	public Enterprise setEventXEventTypeSecurityTokenList(List<EventXEventTypeSecurityToken> eventXEventTypeSecurityTokenList)
	{
		this.eventXEventTypeSecurityTokenList = eventXEventTypeSecurityTokenList;
		return this;
	}

	public Enterprise setArrangementXResourceItemSecurityTokenList(List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList)
	{
		this.arrangementXResourceItemSecurityTokenList = arrangementXResourceItemSecurityTokenList;
		return this;
	}

	public Enterprise setEventXProductSecurityTokenList(List<EventXProductSecurityToken> eventXProductSecurityTokenList)
	{
		this.eventXProductSecurityTokenList = eventXProductSecurityTokenList;
		return this;
	}

	public Enterprise setInvolvedPartyNameTypeSecurityTokenList(List<InvolvedPartyNameTypeSecurityToken> involvedPartyNameTypeSecurityTokenList)
	{
		this.involvedPartyNameTypeSecurityTokenList = involvedPartyNameTypeSecurityTokenList;
		return this;
	}

	public Enterprise setClassificationXResourceItemSecurityTokenList(List<ClassificationXResourceItemSecurityToken> classificationXResourceItemSecurityTokenList)
	{
		this.classificationXResourceItemSecurityTokenList = classificationXResourceItemSecurityTokenList;
		return this;
	}

	public Enterprise setEventXResourceItemSecurityTokenList(List<EventXResourceItemSecurityToken> eventXResourceItemSecurityTokenList)
	{
		this.eventXResourceItemSecurityTokenList = eventXResourceItemSecurityTokenList;
		return this;
	}

	public Enterprise setAddressSecurityTokenList(List<AddressSecurityToken> addressSecurityTokenList)
	{
		this.addressSecurityTokenList = addressSecurityTokenList;
		return this;
	}

	public Enterprise setSystemSecurityTokenList(List<SystemsSecurityToken> systemSecurityTokenList)
	{
		this.systemSecurityTokenList = systemSecurityTokenList;
		return this;
	}

	public Enterprise setActiveFlagList(List<ActiveFlag> activeFlagList)
	{
		this.activeFlagList = activeFlagList;
		return this;
	}

	public Enterprise setClassificationXClassificationList(List<ClassificationXClassification> classificationXClassificationList)
	{
		this.classificationXClassificationList = classificationXClassificationList;
		return this;
	}

	public Enterprise setClassificationDataConceptList(List<ClassificationDataConcept> classificationDataConceptList)
	{
		this.classificationDataConceptList = classificationDataConceptList;
		return this;
	}

	public Enterprise setAddressXGeographyList(List<AddressXGeography> addressXGeographyList)
	{
		this.addressXGeographyList = addressXGeographyList;
		return this;
	}

	public Enterprise setSecurityTokenXClassificationList(List<SecurityTokenXClassification> securityTokenXClassificationList)
	{
		this.securityTokenXClassificationList = securityTokenXClassificationList;
		return this;
	}

	public Enterprise setEventXArrangementsSecurityTokenList(List<EventXArrangementsSecurityToken> eventXArrangementsSecurityTokenList)
	{
		this.eventXArrangementsSecurityTokenList = eventXArrangementsSecurityTokenList;
		return this;
	}

	public Enterprise setInvolvedPartyXResourceItemSecurityTokenList(List<InvolvedPartyXResourceItemSecurityToken> involvedPartyXResourceItemSecurityTokenList)
	{
		this.involvedPartyXResourceItemSecurityTokenList = involvedPartyXResourceItemSecurityTokenList;
		return this;
	}

	public Enterprise setProductXClassificationList(List<ProductXClassification> productXClassificationList)
	{
		this.productXClassificationList = productXClassificationList;
		return this;
	}

	public Enterprise setResourceItemDataSecurityTokenList(List<ResourceItemDataSecurityToken> resourceItemDataSecurityTokenList)
	{
		this.resourceItemDataSecurityTokenList = resourceItemDataSecurityTokenList;
		return this;
	}

	public Enterprise setArrangementXInvolvedPartyList(List<ArrangementXInvolvedParty> arrangementXInvolvedPartyList)
	{
		this.arrangementXInvolvedPartyList = arrangementXInvolvedPartyList;
		return this;
	}

	public Enterprise setArrangementXResourceItemList(List<ArrangementXResourceItem> arrangementXResourceItemList)
	{
		this.arrangementXResourceItemList = arrangementXResourceItemList;
		return this;
	}

	public Enterprise setProductXProductSecurityTokenList(List<ProductXProductSecurityToken> productXProductSecurityTokenList)
	{
		this.productXProductSecurityTokenList = productXProductSecurityTokenList;
		return this;
	}

	public Enterprise setResourceItemXClassificationList(List<ResourceItemXClassification> resourceItemXClassificationList)
	{
		this.resourceItemXClassificationList = resourceItemXClassificationList;
		return this;
	}

	public Enterprise setArrangementXInvolvedPartySecurityTokenList(List<ArrangementXInvolvedPartySecurityToken> arrangementXInvolvedPartySecurityTokenList)
	{
		this.arrangementXInvolvedPartySecurityTokenList = arrangementXInvolvedPartySecurityTokenList;
		return this;
	}

	public Enterprise setInvolvedPartyNonOrganicList(List<InvolvedPartyNonOrganic> involvedPartyNonOrganicList)
	{
		this.involvedPartyNonOrganicList = involvedPartyNonOrganicList;
		return this;
	}

	public Enterprise setEventXInvolvedPartyList(List<EventXInvolvedParty> eventXInvolvedPartyList)
	{
		this.eventXInvolvedPartyList = eventXInvolvedPartyList;
		return this;
	}

	public Enterprise setActiveFlagXClassificationList(List<ActiveFlagXClassification> activeFlagXClassificationList)
	{
		this.activeFlagXClassificationList = activeFlagXClassificationList;
		return this;
	}

	public Enterprise setGeographyXResourceItemSecurityTokenList(List<GeographyXResourceItemSecurityToken> geographyXResourceItemSecurityTokenList)
	{
		this.geographyXResourceItemSecurityTokenList = geographyXResourceItemSecurityTokenList;
		return this;
	}

	public Enterprise setEnterpriseXClassificationList(List<EnterpriseXClassification> enterpriseXClassificationList)
	{
		this.enterpriseXClassificationList = enterpriseXClassificationList;
		return this;
	}

	public Enterprise setEventXArrangementList(List<EventXArrangement> eventXArrangementList)
	{
		this.eventXArrangementList = eventXArrangementList;
		return this;
	}

	public Enterprise setInvolvedPartyIdentificationTypeSecurityTokenList(List<InvolvedPartyIdentificationTypeSecurityToken> involvedPartyIdentificationTypeSecurityTokenList)
	{
		this.involvedPartyIdentificationTypeSecurityTokenList = involvedPartyIdentificationTypeSecurityTokenList;
		return this;
	}

	public Enterprise setAddressXResourceItemSecurityTokenList(List<AddressXResourceItemSecurityToken> addressXResourceItemSecurityTokenList)
	{
		this.addressXResourceItemSecurityTokenList = addressXResourceItemSecurityTokenList;
		return this;
	}

	public Enterprise setInvolvedPartySecurityTokenList(List<InvolvedPartySecurityToken> involvedPartySecurityTokenList)
	{
		this.involvedPartySecurityTokenList = involvedPartySecurityTokenList;
		return this;
	}

	public Enterprise setProductXClassificationSecurityTokenList(List<ProductXClassificationSecurityToken> productXClassificationSecurityTokenList)
	{
		this.productXClassificationSecurityTokenList = productXClassificationSecurityTokenList;
		return this;
	}

	public Enterprise setSecurityTokenList(List<SecurityToken> securityTokenList)
	{
		this.securityTokenList = securityTokenList;
		return this;
	}

	public Enterprise setArrangementXArrangementTypeSecurityTokenList(List<ArrangementXArrangementTypeSecurityToken> arrangementXArrangementTypeSecurityTokenList)
	{
		this.arrangementXArrangementTypeSecurityTokenList = arrangementXArrangementTypeSecurityTokenList;
		return this;
	}

	public Enterprise setEventXInvolvedPartySecurityTokenList(List<EventXInvolvedPartySecurityToken> eventXInvolvedPartySecurityTokenList)
	{
		this.eventXInvolvedPartySecurityTokenList = eventXInvolvedPartySecurityTokenList;
		return this;
	}

	public Enterprise setInvolvedPartyOrganicList(List<InvolvedPartyOrganic> involvedPartyOrganicList)
	{
		this.involvedPartyOrganicList = involvedPartyOrganicList;
		return this;
	}

	public Enterprise setSystemXClassificationList(List<SystemXClassification> systemXClassificationList)
	{
		this.systemXClassificationList = systemXClassificationList;
		return this;
	}

	public Enterprise setEventList(List<Event> eventList)
	{
		this.eventList = eventList;
		return this;
	}

	public Enterprise setClassificationDataConceptXClassificationSecurityTokenList(List<ClassificationDataConceptXClassificationSecurityToken> classificationDataConceptXClassificationSecurityTokenList)
	{
		this.classificationDataConceptXClassificationSecurityTokenList = classificationDataConceptXClassificationSecurityTokenList;
		return this;
	}

	public Enterprise setActiveFlagSecurityTokenList(List<ActiveFlagSecurityToken> activeFlagSecurityTokenList)
	{
		this.activeFlagSecurityTokenList = activeFlagSecurityTokenList;
		return this;
	}

	public Enterprise setArrangementXClassificationList(List<ArrangementXClassification> arrangementXClassificationList)
	{
		this.arrangementXClassificationList = arrangementXClassificationList;
		return this;
	}

	public Enterprise setGeographyList(List<Geography> geographyList)
	{
		this.geographyList = geographyList;
		return this;
	}

	public Enterprise setInvolvedPartyOrganicTypeSecurityTokenList(List<InvolvedPartyOrganicTypeSecurityToken> involvedPartyOrganicTypeSecurityTokenList)
	{
		this.involvedPartyOrganicTypeSecurityTokenList = involvedPartyOrganicTypeSecurityTokenList;
		return this;
	}

	public Enterprise setEventTypeList(List<EventType> eventTypeList)
	{
		this.eventTypeList = eventTypeList;
		return this;
	}

	public Enterprise setResourceItemDataList(List<ResourceItemData> resourceItemDataList)
	{
		this.resourceItemDataList = resourceItemDataList;
		return this;
	}

	public Enterprise setAddressXResourceItemList(List<AddressXResourceItem> addressXResourceItemList)
	{
		this.addressXResourceItemList = addressXResourceItemList;
		return this;
	}

	public Enterprise setArrangementTypeSecurityTokenList(List<ArrangementTypeSecurityToken> arrangementTypeSecurityTokenList)
	{
		this.arrangementTypeSecurityTokenList = arrangementTypeSecurityTokenList;
		return this;
	}

	public Enterprise setInvolvedPartyOrganicSecurityTokenList(List<InvolvedPartyOrganicSecurityToken> involvedPartyOrganicSecurityTokenList)
	{
		this.involvedPartyOrganicSecurityTokenList = involvedPartyOrganicSecurityTokenList;
		return this;
	}

	public Enterprise setArrangementXArrangementList(List<ArrangementXArrangement> arrangementXArrangementList)
	{
		this.arrangementXArrangementList = arrangementXArrangementList;
		return this;
	}

	public Enterprise setClassificationDataConceptXClassificationList(List<ClassificationDataConceptXClassification> classificationDataConceptXClassificationList)
	{
		this.classificationDataConceptXClassificationList = classificationDataConceptXClassificationList;
		return this;
	}

	public Enterprise setArrangementXProductList(List<ArrangementXProduct> arrangementXProductList)
	{
		this.arrangementXProductList = arrangementXProductList;
		return this;
	}

	public Enterprise setInvolvedPartyXInvolvedPartyList(List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList)
	{
		this.involvedPartyXInvolvedPartyList = involvedPartyXInvolvedPartyList;
		return this;
	}

	public Enterprise setInvolvedPartyXInvolvedPartyTypeList(List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList)
	{
		this.involvedPartyXInvolvedPartyTypeList = involvedPartyXInvolvedPartyTypeList;
		return this;
	}

	public Enterprise setArrangementSecurityTokenList(List<ArrangementSecurityToken> arrangementSecurityTokenList)
	{
		this.arrangementSecurityTokenList = arrangementSecurityTokenList;
		return this;
	}

	public Enterprise setInvolvedPartyOrganicTypeList(List<InvolvedPartyOrganicType> involvedPartyOrganicTypeList)
	{
		this.involvedPartyOrganicTypeList = involvedPartyOrganicTypeList;
		return this;
	}

	public Enterprise setInvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList(List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList)
	{
		this.involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList = involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList;
		return this;
	}

	public Enterprise setInvolvedPartyXInvolvedPartyTypeSecurityTokenList(List<InvolvedPartyXInvolvedPartyTypeSecurityToken> involvedPartyXInvolvedPartyTypeSecurityTokenList)
	{
		this.involvedPartyXInvolvedPartyTypeSecurityTokenList = involvedPartyXInvolvedPartyTypeSecurityTokenList;
		return this;
	}

	public Enterprise setClassificationDataConceptSecurityTokenList(List<ClassificationDataConceptSecurityToken> classificationDataConceptSecurityTokenList)
	{
		this.classificationDataConceptSecurityTokenList = classificationDataConceptSecurityTokenList;
		return this;
	}

	public Enterprise setEventXResourceItemList(List<EventXResourceItem> eventXResourceItemList)
	{
		this.eventXResourceItemList = eventXResourceItemList;
		return this;
	}

	public Enterprise setInvolvedPartyXClassificationList(List<InvolvedPartyXClassification> involvedPartyXClassificationList)
	{
		this.involvedPartyXClassificationList = involvedPartyXClassificationList;
		return this;
	}

	public Enterprise setArrangementTypeList(List<ArrangementType> arrangementTypeList)
	{
		this.arrangementTypeList = arrangementTypeList;
		return this;
	}

	public Enterprise setSystemsList(List<Systems> systemsList)
	{
		this.systemsList = systemsList;
		return this;
	}

	public Enterprise setInvolvedPartyTypeSecurityTokenList(List<InvolvedPartyTypeSecurityToken> involvedPartyTypeSecurityTokenList)
	{
		this.involvedPartyTypeSecurityTokenList = involvedPartyTypeSecurityTokenList;
		return this;
	}

	public Enterprise setSecurityTokenXSecurityTokenList(List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenList)
	{
		this.securityTokenXSecurityTokenList = securityTokenXSecurityTokenList;
		return this;
	}

	public Enterprise setProductXResourceItemList(List<ProductXResourceItem> productXResourceItemList)
	{
		this.productXResourceItemList = productXResourceItemList;
		return this;
	}

	public Enterprise setInvolvedPartyXClassificationSecurityTokenList(List<InvolvedPartyXClassificationSecurityToken> involvedPartyXClassificationSecurityTokenList)
	{
		this.involvedPartyXClassificationSecurityTokenList = involvedPartyXClassificationSecurityTokenList;
		return this;
	}

	public Enterprise setEventXAddressSecurityTokenList(List<EventXAddressSecurityToken> eventXAddressSecurityTokenList)
	{
		this.eventXAddressSecurityTokenList = eventXAddressSecurityTokenList;
		return this;
	}

	public Enterprise setResourceItemList(List<ResourceItem> resourceItemList)
	{
		this.resourceItemList = resourceItemList;
		return this;
	}

	public Enterprise setEventXAddressList(List<EventXAddress> eventXAddressList)
	{
		this.eventXAddressList = eventXAddressList;
		return this;
	}

	public Enterprise setClassificationDataConceptXResourceItemList(List<ClassificationDataConceptXResourceItem> classificationDataConceptXResourceItemList)
	{
		this.classificationDataConceptXResourceItemList = classificationDataConceptXResourceItemList;
		return this;
	}

	public Enterprise setInvolvedPartyXProductList(List<InvolvedPartyXProduct> involvedPartyXProductList)
	{
		this.involvedPartyXProductList = involvedPartyXProductList;
		return this;
	}

	public Enterprise setEventSecurityTokenList(List<EventSecurityToken> eventSecurityTokenList)
	{
		this.eventSecurityTokenList = eventSecurityTokenList;
		return this;
	}

	public Enterprise setResourceItemDataXClassificationList(List<ResourceItemDataXClassification> resourceItemDataXClassificationList)
	{
		this.resourceItemDataXClassificationList = resourceItemDataXClassificationList;
		return this;
	}

	public Enterprise setSecurityTokenAccessList(List<SecurityTokensSecurityToken> securityTokenAccessList)
	{
		this.securityTokenAccessList = securityTokenAccessList;
		return this;
	}

	public Enterprise setInvolvedPartyXAddressList(List<InvolvedPartyXAddress> involvedPartyXAddressList)
	{
		this.involvedPartyXAddressList = involvedPartyXAddressList;
		return this;
	}

	public Enterprise setGeographyXClassificationSecurityTokenList(List<GeographyXClassificationSecurityToken> geographyXClassificationSecurityTokenList)
	{
		this.geographyXClassificationSecurityTokenList = geographyXClassificationSecurityTokenList;
		return this;
	}

	public Enterprise setGeographyXGeographySecurityTokenList(List<GeographyXGeographySecurityToken> geographyXGeographySecurityTokenList)
	{
		this.geographyXGeographySecurityTokenList = geographyXGeographySecurityTokenList;
		return this;
	}

	public Enterprise setArrangementXArrangementSecurityTokenList(List<ArrangementXArrangementSecurityToken> arrangementXArrangementSecurityTokenList)
	{
		this.arrangementXArrangementSecurityTokenList = arrangementXArrangementSecurityTokenList;
		return this;
	}

	public Enterprise setGeographySecurityTokenList(List<GeographySecurityToken> geographySecurityTokenList)
	{
		this.geographySecurityTokenList = geographySecurityTokenList;
		return this;
	}

	public Enterprise setInvolvedPartyXInvolvedPartyIdentificationTypeList(List<InvolvedPartyXInvolvedPartyIdentificationType> involvedPartyXInvolvedPartyIdentificationTypeList)
	{
		this.involvedPartyXInvolvedPartyIdentificationTypeList = involvedPartyXInvolvedPartyIdentificationTypeList;
		return this;
	}

	public Enterprise setInvolvedPartyIdentificationTypeList(List<InvolvedPartyIdentificationType> involvedPartyIdentificationTypeList)
	{
		this.involvedPartyIdentificationTypeList = involvedPartyIdentificationTypeList;
		return this;
	}

	public Enterprise setClassificationXResourceItemList(List<ClassificationXResourceItem> classificationXResourceItemList)
	{
		this.classificationXResourceItemList = classificationXResourceItemList;
		return this;
	}

	public Enterprise setEventXProductList(List<EventXProduct> eventXProductList)
	{
		this.eventXProductList = eventXProductList;
		return this;
	}

	public Enterprise setEventXEventTypeList(List<EventXEventType> eventXEventTypeList)
	{
		this.eventXEventTypeList = eventXEventTypeList;
		return this;
	}

	public Enterprise setInvolvedPartyXInvolvedPartySecurityTokenList(List<InvolvedPartyXInvolvedPartySecurityToken> involvedPartyXInvolvedPartySecurityTokenList)
	{
		this.involvedPartyXInvolvedPartySecurityTokenList = involvedPartyXInvolvedPartySecurityTokenList;
		return this;
	}

	public Enterprise setProductXProductList(List<ProductXProduct> productXProductList)
	{
		this.productXProductList = productXProductList;
		return this;
	}

	public Enterprise setInvolvedPartyTypeList(List<InvolvedPartyType> involvedPartyTypeList)
	{
		this.involvedPartyTypeList = involvedPartyTypeList;
		return this;
	}

	public Enterprise setArrangementXClassificationSecurityTokenList(List<ArrangementXClassificationSecurityToken> arrangementXClassificationSecurityTokenList)
	{
		this.arrangementXClassificationSecurityTokenList = arrangementXClassificationSecurityTokenList;
		return this;
	}

	public Enterprise setInvolvedPartyXAddressSecurityTokenList(List<InvolvedPartyXAddressSecurityToken> involvedPartyXAddressSecurityTokenList)
	{
		this.involvedPartyXAddressSecurityTokenList = involvedPartyXAddressSecurityTokenList;
		return this;
	}

	public Enterprise setGeographyXClassificationList(List<GeographyXClassification> geographyXClassificationList)
	{
		this.geographyXClassificationList = geographyXClassificationList;
		return this;
	}

	public Enterprise setAddressXGeographySecurityTokenList(List<AddressXGeographySecurityToken> addressXGeographySecurityTokenList)
	{
		this.addressXGeographySecurityTokenList = addressXGeographySecurityTokenList;
		return this;
	}

	public Enterprise setInvolvedPartyNonOrganicSecurityTokenList(List<InvolvedPartyNonOrganicSecurityToken> involvedPartyNonOrganicSecurityTokenList)
	{
		this.involvedPartyNonOrganicSecurityTokenList = involvedPartyNonOrganicSecurityTokenList;
		return this;
	}

	public Enterprise setEventXClassificationList(List<EventXClassification> eventXClassificationList)
	{
		this.eventXClassificationList = eventXClassificationList;
		return this;
	}

	public Enterprise setArrangementList(List<Arrangement> arrangementList)
	{
		this.arrangementList = arrangementList;
		return this;
	}

	public Enterprise setResourceItemSecurityTokenList(List<ResourceItemSecurityToken> resourceItemSecurityTokenList)
	{
		this.resourceItemSecurityTokenList = resourceItemSecurityTokenList;
		return this;
	}

	public Enterprise setGeographyXResourceItemList(List<GeographyXResourceItem> geographyXResourceItemList)
	{
		this.geographyXResourceItemList = geographyXResourceItemList;
		return this;
	}

	public Enterprise setClassificationXClassificationSecurityTokenList(List<ClassificationXClassificationSecurityToken> classificationXClassificationSecurityTokenList)
	{
		this.classificationXClassificationSecurityTokenList = classificationXClassificationSecurityTokenList;
		return this;
	}

	public Enterprise setEventTypesSecurityTokenList(List<EventTypesSecurityToken> eventTypesSecurityTokenList)
	{
		this.eventTypesSecurityTokenList = eventTypesSecurityTokenList;
		return this;
	}

	public Enterprise setGeographyXGeographyList(List<GeographyXGeography> geographyXGeographyList)
	{
		this.geographyXGeographyList = geographyXGeographyList;
		return this;
	}

	public Enterprise setClassificationSecurityTokenList(List<ClassificationSecurityToken> classificationSecurityTokenList)
	{
		this.classificationSecurityTokenList = classificationSecurityTokenList;
		return this;
	}
*/

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
		Enterprise that = (Enterprise) o;
		return Objects.equals(getName(), that.getName());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public @NotNull String getName()
	{
		return name;
	}

	@Override
	public @NotNull String getDescription()
	{
		return description;
	}

	@Override
	public Enterprise setId(Long id)
	{
		this.id = id;
		return this;
	}

	@Override
	public Enterprise setName(@NotNull String name)
	{
		this.name = name;
		return this;
	}

	@Override
	public Enterprise setDescription(@NotNull String description)
	{
		this.description = description;
		return this;
	}
}
