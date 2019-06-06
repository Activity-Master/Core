package com.armineasy.activitymaster.activitymaster.db.entities.systems;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.address.*;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.*;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.*;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.EnterpriseXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.events.*;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.*;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.*;
import com.armineasy.activitymaster.activitymaster.db.entities.product.*;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.*;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokensSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.builders.SystemsQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNo;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNoXClassification;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsEnterprise;
import com.armineasy.activitymaster.activitymaster.services.capabilities.INameAndDescription;
import com.armineasy.activitymaster.activitymaster.services.classifications.systems.ISystemsClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Systems")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = {"name","enterpriseID"},
		callSuper = false)
public class Systems
		extends WarehouseNameDescriptionTable<Systems, SystemsQueryBuilder, Long, SystemsSecurityToken>
		implements IContainsClassifications<Systems, Classification, SystemXClassification, ISystemsClassification<?>>,
				           IActivityMasterEntity<Systems>,
				           INameAndDescription<Systems>,
				           IContainsEnterprise<Systems>,
				           ISystems<Systems>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "SystemID")
	@Getter
	@Setter
	private Long id;
	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@NotNull
	@Size(min = 1,
			max = 150)
	@Column(nullable = false,
			length = 150,
			name = "SystemName")
	@Getter
	@Setter
	private String name;
	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@NotNull
	@Size()
	@Column(nullable = false,
			length = 250,
			name = "SystemDesc")
	@Getter
	@Setter
	private String description;
	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@NotNull
	@Size(min = 1,
			max = 250)
	@Column(nullable = false,
			length = 250,
			name = "SystemHistoryName")
	@Getter
	@Setter
	private String systemHistoryName;

	@JoinColumn(name = "EnterpriseID",
			referencedColumnName = "EnterpriseID",
			nullable = false)
	@ManyToOne(optional = false)
	@Getter
	@Setter
	private Enterprise enterpriseID;

	@JoinColumn(name = "ActiveFlagID",
			referencedColumnName = "ActiveFlagID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	@Getter
	@Setter
	private ActiveFlag activeFlagID;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyNameType> involvedPartyXInvolvedPartyNameTypeList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyNameType> involvedPartyXInvolvedPartyNameTypeList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProductSecurityToken> involvedPartyXProductSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProductSecurityToken> involvedPartyXProductSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<Address> addressList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<Address> addressList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ProductXResourceItemSecurityToken> productXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ProductXResourceItemSecurityToken> productXResourceItemSecurityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<Product> productList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<Product> productList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ResourceItemXClassificationSecurityToken> resourceItemXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ResourceItemXClassificationSecurityToken> resourceItemXClassificationSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXResourceItem> involvedPartyXResourceItemList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXResourceItem> involvedPartyXResourceItemList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNameType> involvedPartyNameTypeList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNameType> involvedPartyNameTypeList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventXClassificationSecurityToken> eventXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventXClassificationSecurityToken> eventXClassificationSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ProductSecurityToken> productSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ProductSecurityToken> productSecurityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedParty> involvedPartyList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedParty> involvedPartyList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementType> arrangementXArrangementTypeList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementType> arrangementXArrangementTypeList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> involvedPartyXInvolvedPartyNameTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> involvedPartyXInvolvedPartyNameTypeSecurityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<Classification> classificationList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<Classification> classificationList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventXEventTypeSecurityToken> eventXEventTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventXEventTypeSecurityToken> eventXEventTypeSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventXProductSecurityToken> eventXProductSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventXProductSecurityToken> eventXProductSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNameTypeSecurityToken> involvedPartyNameTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNameTypeSecurityToken> involvedPartyNameTypeSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ClassificationXResourceItemSecurityToken> classificationXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ClassificationXResourceItemSecurityToken> classificationXResourceItemSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventXResourceItemSecurityToken> eventXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventXResourceItemSecurityToken> eventXResourceItemSecurityTokenList1;

	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<AddressSecurityToken> addressSecurityTokenList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<AddressSecurityToken> addressSecurityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<SystemsSecurityToken> securities;

	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<SystemsSecurityToken> systemSecurityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassification> classificationXClassificationList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassification> classificationXClassificationList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConcept> classificationDataConceptList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConcept> classificationDataConceptList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<AddressXGeography> addressXGeographyList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<AddressXGeography> addressXGeographyList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXClassification> securityTokenXClassificationList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXClassification> securityTokenXClassificationList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventXArrangementsSecurityToken> eventXArrangementsSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventXArrangementsSecurityToken> eventXArrangementsSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXResourceItemSecurityToken> involvedPartyXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXResourceItemSecurityToken> involvedPartyXResourceItemSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ProductXClassification> productXClassificationList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ProductXClassification> productXClassificationList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataSecurityToken> resourceItemDataSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataSecurityToken> resourceItemDataSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXInvolvedParty> arrangementXInvolvedPartyList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXInvolvedParty> arrangementXInvolvedPartyList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItem> arrangementXResourceItemList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItem> arrangementXResourceItemList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ProductXProductSecurityToken> productXProductSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ProductXProductSecurityToken> productXProductSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ResourceItemXClassification> resourceItemXClassificationList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ResourceItemXClassification> resourceItemXClassificationList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXInvolvedPartySecurityToken> arrangementXInvolvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXInvolvedPartySecurityToken> arrangementXInvolvedPartySecurityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNonOrganic> involvedPartyNonOrganicList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNonOrganic> involvedPartyNonOrganicList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventXInvolvedParty> eventXInvolvedPartyList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventXInvolvedParty> eventXInvolvedPartyList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ActiveFlagXClassification> activeFlagXClassificationList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ActiveFlagXClassification> activeFlagXClassificationList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<GeographyXResourceItemSecurityToken> geographyXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<GeographyXResourceItemSecurityToken> geographyXResourceItemSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EnterpriseXClassification> enterpriseXClassificationList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EnterpriseXClassification> enterpriseXClassificationList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventXArrangement> eventXArrangementList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventXArrangement> eventXArrangementList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyIdentificationTypeSecurityToken> involvedPartyIdentificationTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyIdentificationTypeSecurityToken> involvedPartyIdentificationTypeSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<AddressXResourceItemSecurityToken> addressXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<AddressXResourceItemSecurityToken> addressXResourceItemSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartySecurityToken> involvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartySecurityToken> involvedPartySecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ProductXClassificationSecurityToken> productXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ProductXClassificationSecurityToken> productXClassificationSecurityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<SecurityToken> securityTokenList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<SecurityToken> securityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementTypeSecurityToken> arrangementXArrangementTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementTypeSecurityToken> arrangementXArrangementTypeSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventXInvolvedPartySecurityToken> eventXInvolvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventXInvolvedPartySecurityToken> eventXInvolvedPartySecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganic> involvedPartyOrganicList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganic> involvedPartyOrganicList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<SystemXClassification> classifications;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<SystemXClassification> systemXClassificationList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<Event> eventList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<Event> eventList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ActiveFlagSecurityToken> activeFlagSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ActiveFlagSecurityToken> activeFlagSecurityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXClassification> arrangementXClassificationList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXClassification> arrangementXClassificationList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<Geography> geographyList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<Geography> geographyList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicTypeSecurityToken> involvedPartyOrganicTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicTypeSecurityToken> involvedPartyOrganicTypeSecurityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventType> eventTypeList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventType> eventTypeList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ResourceItemData> resourceItemDataList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ResourceItemData> resourceItemDataList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<AddressXResourceItem> addressXResourceItemList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<AddressXResourceItem> addressXResourceItemList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ArrangementTypeSecurityToken> arrangementTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ArrangementTypeSecurityToken> arrangementTypeSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicSecurityToken> involvedPartyOrganicSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicSecurityToken> involvedPartyOrganicSecurityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangement> arrangementXArrangementList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangement> arrangementXArrangementList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXClassification> classificationDataConceptXClassificationList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXProduct> arrangementXProductList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXProduct> arrangementXProductList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ArrangementSecurityToken> arrangementSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ArrangementSecurityToken> arrangementSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicType> involvedPartyOrganicTypeList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyTypeSecurityToken> involvedPartyXInvolvedPartyTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyTypeSecurityToken> involvedPartyXInvolvedPartyTypeSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptSecurityToken> classificationDataConceptSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptSecurityToken> classificationDataConceptSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventXResourceItem> eventXResourceItemList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventXResourceItem> eventXResourceItemList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXClassification> involvedPartyXClassificationList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXClassification> involvedPartyXClassificationList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ArrangementType> arrangementTypeList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ArrangementType> arrangementTypeList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyTypeSecurityToken> involvedPartyTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyTypeSecurityToken> involvedPartyTypeSecurityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ProductXResourceItem> productXResourceItemList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ProductXResourceItem> productXResourceItemList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXClassificationSecurityToken> involvedPartyXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXClassificationSecurityToken> involvedPartyXClassificationSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventXAddressSecurityToken> eventXAddressSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventXAddressSecurityToken> eventXAddressSecurityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ResourceItem> resourceItemList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ResourceItem> resourceItemList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventXAddress> eventXAddressList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventXAddress> eventXAddressList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXResourceItem> classificationDataConceptXResourceItemList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProduct> involvedPartyXProductList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProduct> involvedPartyXProductList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventSecurityToken> eventSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventSecurityToken> eventSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataXClassification> resourceItemDataXClassificationList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataXClassification> resourceItemDataXClassificationList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<SecurityTokensSecurityToken> securityTokenAccessList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<SecurityTokensSecurityToken> securityTokenAccessList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddress> involvedPartyXAddressList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddress> involvedPartyXAddressList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<GeographyXClassificationSecurityToken> geographyXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<GeographyXClassificationSecurityToken> geographyXClassificationSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<GeographyXGeographySecurityToken> geographyXGeographySecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<GeographyXGeographySecurityToken> geographyXGeographySecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementSecurityToken> arrangementXArrangementSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementSecurityToken> arrangementXArrangementSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<GeographySecurityToken> geographySecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<GeographySecurityToken> geographySecurityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyIdentificationType> involvedPartyXInvolvedPartyIdentificationTypeList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyIdentificationType> involvedPartyXInvolvedPartyIdentificationTypeList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyIdentificationType> involvedPartyIdentificationTypeList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyIdentificationType> involvedPartyIdentificationTypeList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ClassificationXResourceItem> classificationXResourceItemList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ClassificationXResourceItem> classificationXResourceItemList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventXProduct> eventXProductList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventXProduct> eventXProductList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventXEventType> eventXEventTypeList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartySecurityToken> involvedPartyXInvolvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartySecurityToken> involvedPartyXInvolvedPartySecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ProductXProduct> productXProductList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ProductXProduct> productXProductList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyType> involvedPartyTypeList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyType> involvedPartyTypeList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXClassificationSecurityToken> arrangementXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXClassificationSecurityToken> arrangementXClassificationSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddressSecurityToken> involvedPartyXAddressSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddressSecurityToken> involvedPartyXAddressSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<GeographyXClassification> geographyXClassificationList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<GeographyXClassification> geographyXClassificationList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<AddressXGeographySecurityToken> addressXGeographySecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<AddressXGeographySecurityToken> addressXGeographySecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNonOrganicSecurityToken> involvedPartyNonOrganicSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNonOrganicSecurityToken> involvedPartyNonOrganicSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventXClassification> eventXClassificationList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventXClassification> eventXClassificationList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<Arrangement> arrangementList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<Arrangement> arrangementList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ResourceItemSecurityToken> resourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ResourceItemSecurityToken> resourceItemSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<GeographyXResourceItem> geographyXResourceItemList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<GeographyXResourceItem> geographyXResourceItemList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassificationSecurityToken> classificationXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassificationSecurityToken> classificationXClassificationSecurityTokenList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<EventTypesSecurityToken> eventTypesSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<EventTypesSecurityToken> eventTypesSecurityTokenList1;

	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<YesNoXClassification> yesNoXClassificationList;

	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<GeographyXGeography> geographyXGeographyList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<GeographyXGeography> geographyXGeographyList1;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<ClassificationSecurityToken> classificationSecurityTokenList;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<ClassificationSecurityToken> classificationSecurityTokenList1;
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	private List<YesNo> yesNoList;
	@OneToMany(
			mappedBy = "originalSourceSystemID",
			fetch = FetchType.LAZY)
	private List<YesNo> yesNoList1;

	public Systems()
	{

	}

	public Systems(Long systemID)
	{
		this.id = systemID;
	}

	public Systems(Long systemID, String systemName, String systemDesc, String systemHistoryName)
	{
		this.id = systemID;
		this.name = systemName;
		this.description = systemDesc;
		this.systemHistoryName = systemHistoryName;
	}

	@Override
	protected SystemsSecurityToken configureDefaultsForNewToken(SystemsSecurityToken stAdmin, IEnterprise enterprise, ISystems activityMasterSystem)
	{
		SystemsSecurityToken token = super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);
		stAdmin.setSystemID(this);
		return token;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	public void configureForClassification(SystemXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setSystemID(this);
	}
}
