package com.armineasy.activitymaster.activitymaster.db.entities.enterprise;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.address.*;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.*;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.*;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.events.*;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.*;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.*;
import com.armineasy.activitymaster.activitymaster.db.entities.product.*;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.*;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokensSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemsSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNo;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNoXClassification;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.INameAndDescription;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Enterprise")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "name",
		callSuper = false)
public class Enterprise
		extends WarehouseNameDescriptionTable<Enterprise, EnterpriseQueryBuilder, Long, EnterpriseSecurityToken>
		implements IContainsClassifications<Enterprise, Classification, EnterpriseXClassification, IEnterpriseClassification>,
				           IActivityMasterEntity<Enterprise>,
				           INameAndDescription<Enterprise>,
				           IEnterprise<Enterprise>
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EnterpriseID")
	@Getter(onMethod = @__(@XmlTransient))
	@Setter
	private Long id;

	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@NotNull
	@Lob
	@Column(nullable = false,
			name = "EnterpriseName")
	@Getter(onMethod = @__(@XmlTransient))
	@Setter
	private String name;
	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@NotNull
	@Lob
	@Column(nullable = false,
			name = "EnterpriseDesc")
	@Getter(onMethod = @__(@XmlTransient))
	@Setter
	private String description;

	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EnterpriseSecurityToken> securities;


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
	private List<YesNoXClassification> yesNoXClassificationList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<GeographyXGeography> geographyXGeographyList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ClassificationSecurityToken> classificationSecurityTokenList;
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<YesNo> yesNoList;

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
		this.name = enterpriseName;
		this.description = enterpriseDesc;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	public void configureForClassification(EnterpriseXClassification classificationLink, Enterprise enterprise)
	{
		classificationLink.setEnterpriseID(this);
	}
}
