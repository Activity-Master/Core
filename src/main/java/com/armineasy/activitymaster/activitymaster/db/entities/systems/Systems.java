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
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.systems.ActiveFlagSystem;
import com.jwebmp.guicedinjection.GuiceContext;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
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
@Table(name = "Systems")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class Systems
		extends WarehouseNameDescriptionTable<Systems, SystemsQueryBuilder, Long, SystemsSecurityToken>
		implements IContainsClassifications<Systems, Classification, SystemXClassification, ISystemsClassification<?>, ISystems<Systems>>,
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
	private Long id;
	@Basic(optional = false,
			fetch = FetchType.EAGER)
	@NotNull
	@Size(min = 1,
			max = 150)
	@Column(nullable = false,
			length = 150,
			name = "SystemName")
	private String name;
	@Basic(optional = false,
			fetch = FetchType.EAGER)
	@NotNull
	@Size()
	@Column(nullable = false,
			length = 250,
			name = "SystemDesc")
	private String description;
	@Basic(optional = false,
			fetch = FetchType.EAGER)
	@NotNull
	@Size(min = 1,
			max = 250)
	@Column(nullable = false,
			length = 250,
			name = "SystemHistoryName")
	private String systemHistoryName;

	@JoinColumn(name = "EnterpriseID",
			referencedColumnName = "EnterpriseID",
			nullable = false)
	@ManyToOne(optional = false)
	private Enterprise enterpriseID;

	@JoinColumn(name = "ActiveFlagID",
			referencedColumnName = "ActiveFlagID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
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
	protected SystemsSecurityToken configureDefaultsForNewToken(SystemsSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		SystemsSecurityToken token = super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);
		stAdmin.setSystemID(this);
		return token;
	}

	@Override
	public String toString()
	{
		return "System - " + getName();
	}

	@Override
	public void configureForClassification(SystemXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setSystemID(this);
	}


	@SuppressWarnings("unchecked")
	public Systems remove()
	{
		setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
		                            .getDeletedFlag(getEnterpriseID(), ActiveFlagSystem.getSystemTokens()
		                                                                               .get(getEnterpriseID())));
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return this;
	}


	@SuppressWarnings("unchecked")
	public Systems archive()
	{
		setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
		                            .getArchivedFlag(getEnterpriseID(), ActiveFlagSystem.getSystemTokens()
		                                                                                .get(getEnterpriseID())));
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return this;
	}

	public List<InvolvedPartyXInvolvedPartyNameType> getInvolvedPartyXInvolvedPartyNameTypeList()
	{
		return this.involvedPartyXInvolvedPartyNameTypeList;
	}

	public List<InvolvedPartyXInvolvedPartyNameType> getInvolvedPartyXInvolvedPartyNameTypeList1()
	{
		return this.involvedPartyXInvolvedPartyNameTypeList1;
	}

	public List<InvolvedPartyXProductSecurityToken> getInvolvedPartyXProductSecurityTokenList()
	{
		return this.involvedPartyXProductSecurityTokenList;
	}

	public List<InvolvedPartyXProductSecurityToken> getInvolvedPartyXProductSecurityTokenList1()
	{
		return this.involvedPartyXProductSecurityTokenList1;
	}

	public List<Address> getAddressList()
	{
		return this.addressList;
	}

	public List<Address> getAddressList1()
	{
		return this.addressList1;
	}

	public List<ProductXResourceItemSecurityToken> getProductXResourceItemSecurityTokenList()
	{
		return this.productXResourceItemSecurityTokenList;
	}

	public List<ProductXResourceItemSecurityToken> getProductXResourceItemSecurityTokenList1()
	{
		return this.productXResourceItemSecurityTokenList1;
	}

	public List<Product> getProductList()
	{
		return this.productList;
	}

	public List<Product> getProductList1()
	{
		return this.productList1;
	}

	public List<ResourceItemXClassificationSecurityToken> getResourceItemXClassificationSecurityTokenList()
	{
		return this.resourceItemXClassificationSecurityTokenList;
	}

	public List<ResourceItemXClassificationSecurityToken> getResourceItemXClassificationSecurityTokenList1()
	{
		return this.resourceItemXClassificationSecurityTokenList1;
	}

	public List<InvolvedPartyXResourceItem> getInvolvedPartyXResourceItemList()
	{
		return this.involvedPartyXResourceItemList;
	}

	public List<InvolvedPartyXResourceItem> getInvolvedPartyXResourceItemList1()
	{
		return this.involvedPartyXResourceItemList1;
	}

	public List<InvolvedPartyNameType> getInvolvedPartyNameTypeList()
	{
		return this.involvedPartyNameTypeList;
	}

	public List<InvolvedPartyNameType> getInvolvedPartyNameTypeList1()
	{
		return this.involvedPartyNameTypeList1;
	}

	public List<EventXClassificationSecurityToken> getEventXClassificationSecurityTokenList()
	{
		return this.eventXClassificationSecurityTokenList;
	}

	public List<EventXClassificationSecurityToken> getEventXClassificationSecurityTokenList1()
	{
		return this.eventXClassificationSecurityTokenList1;
	}

	public List<ProductSecurityToken> getProductSecurityTokenList()
	{
		return this.productSecurityTokenList;
	}

	public List<ProductSecurityToken> getProductSecurityTokenList1()
	{
		return this.productSecurityTokenList1;
	}

	public List<InvolvedParty> getInvolvedPartyList()
	{
		return this.involvedPartyList;
	}

	public List<InvolvedParty> getInvolvedPartyList1()
	{
		return this.involvedPartyList1;
	}

	public List<ArrangementXArrangementType> getArrangementXArrangementTypeList()
	{
		return this.arrangementXArrangementTypeList;
	}

	public List<ArrangementXArrangementType> getArrangementXArrangementTypeList1()
	{
		return this.arrangementXArrangementTypeList1;
	}

	public List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> getInvolvedPartyXInvolvedPartyNameTypeSecurityTokenList()
	{
		return this.involvedPartyXInvolvedPartyNameTypeSecurityTokenList;
	}

	public List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> getInvolvedPartyXInvolvedPartyNameTypeSecurityTokenList1()
	{
		return this.involvedPartyXInvolvedPartyNameTypeSecurityTokenList1;
	}

	public List<Classification> getClassificationList()
	{
		return this.classificationList;
	}

	public List<Classification> getClassificationList1()
	{
		return this.classificationList1;
	}

	public List<EventXEventTypeSecurityToken> getEventXEventTypeSecurityTokenList()
	{
		return this.eventXEventTypeSecurityTokenList;
	}

	public List<EventXEventTypeSecurityToken> getEventXEventTypeSecurityTokenList1()
	{
		return this.eventXEventTypeSecurityTokenList1;
	}

	public List<ArrangementXResourceItemSecurityToken> getArrangementXResourceItemSecurityTokenList()
	{
		return this.arrangementXResourceItemSecurityTokenList;
	}

	public List<ArrangementXResourceItemSecurityToken> getArrangementXResourceItemSecurityTokenList1()
	{
		return this.arrangementXResourceItemSecurityTokenList1;
	}

	public List<EventXProductSecurityToken> getEventXProductSecurityTokenList()
	{
		return this.eventXProductSecurityTokenList;
	}

	public List<EventXProductSecurityToken> getEventXProductSecurityTokenList1()
	{
		return this.eventXProductSecurityTokenList1;
	}

	public List<InvolvedPartyNameTypeSecurityToken> getInvolvedPartyNameTypeSecurityTokenList()
	{
		return this.involvedPartyNameTypeSecurityTokenList;
	}

	public List<InvolvedPartyNameTypeSecurityToken> getInvolvedPartyNameTypeSecurityTokenList1()
	{
		return this.involvedPartyNameTypeSecurityTokenList1;
	}

	public List<ClassificationXResourceItemSecurityToken> getClassificationXResourceItemSecurityTokenList()
	{
		return this.classificationXResourceItemSecurityTokenList;
	}

	public List<ClassificationXResourceItemSecurityToken> getClassificationXResourceItemSecurityTokenList1()
	{
		return this.classificationXResourceItemSecurityTokenList1;
	}

	public List<EventXResourceItemSecurityToken> getEventXResourceItemSecurityTokenList()
	{
		return this.eventXResourceItemSecurityTokenList;
	}

	public List<EventXResourceItemSecurityToken> getEventXResourceItemSecurityTokenList1()
	{
		return this.eventXResourceItemSecurityTokenList1;
	}

	public List<AddressSecurityToken> getAddressSecurityTokenList()
	{
		return this.addressSecurityTokenList;
	}

	public List<AddressSecurityToken> getAddressSecurityTokenList1()
	{
		return this.addressSecurityTokenList1;
	}

	public List<SystemsSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public List<SystemsSecurityToken> getSystemSecurityTokenList1()
	{
		return this.systemSecurityTokenList1;
	}

	public List<ClassificationXClassification> getClassificationXClassificationList()
	{
		return this.classificationXClassificationList;
	}

	public List<ClassificationXClassification> getClassificationXClassificationList1()
	{
		return this.classificationXClassificationList1;
	}

	public List<ClassificationDataConcept> getClassificationDataConceptList()
	{
		return this.classificationDataConceptList;
	}

	public List<ClassificationDataConcept> getClassificationDataConceptList1()
	{
		return this.classificationDataConceptList1;
	}

	public List<AddressXGeography> getAddressXGeographyList()
	{
		return this.addressXGeographyList;
	}

	public List<AddressXGeography> getAddressXGeographyList1()
	{
		return this.addressXGeographyList1;
	}

	public List<SecurityTokenXClassification> getSecurityTokenXClassificationList()
	{
		return this.securityTokenXClassificationList;
	}

	public List<SecurityTokenXClassification> getSecurityTokenXClassificationList1()
	{
		return this.securityTokenXClassificationList1;
	}

	public List<EventXArrangementsSecurityToken> getEventXArrangementsSecurityTokenList()
	{
		return this.eventXArrangementsSecurityTokenList;
	}

	public List<EventXArrangementsSecurityToken> getEventXArrangementsSecurityTokenList1()
	{
		return this.eventXArrangementsSecurityTokenList1;
	}

	public List<InvolvedPartyXResourceItemSecurityToken> getInvolvedPartyXResourceItemSecurityTokenList()
	{
		return this.involvedPartyXResourceItemSecurityTokenList;
	}

	public List<InvolvedPartyXResourceItemSecurityToken> getInvolvedPartyXResourceItemSecurityTokenList1()
	{
		return this.involvedPartyXResourceItemSecurityTokenList1;
	}

	public List<ProductXClassification> getProductXClassificationList()
	{
		return this.productXClassificationList;
	}

	public List<ProductXClassification> getProductXClassificationList1()
	{
		return this.productXClassificationList1;
	}

	public List<ResourceItemDataSecurityToken> getResourceItemDataSecurityTokenList()
	{
		return this.resourceItemDataSecurityTokenList;
	}

	public List<ResourceItemDataSecurityToken> getResourceItemDataSecurityTokenList1()
	{
		return this.resourceItemDataSecurityTokenList1;
	}

	public List<ArrangementXInvolvedParty> getArrangementXInvolvedPartyList()
	{
		return this.arrangementXInvolvedPartyList;
	}

	public List<ArrangementXInvolvedParty> getArrangementXInvolvedPartyList1()
	{
		return this.arrangementXInvolvedPartyList1;
	}

	public List<ArrangementXResourceItem> getArrangementXResourceItemList()
	{
		return this.arrangementXResourceItemList;
	}

	public List<ArrangementXResourceItem> getArrangementXResourceItemList1()
	{
		return this.arrangementXResourceItemList1;
	}

	public List<ProductXProductSecurityToken> getProductXProductSecurityTokenList()
	{
		return this.productXProductSecurityTokenList;
	}

	public List<ProductXProductSecurityToken> getProductXProductSecurityTokenList1()
	{
		return this.productXProductSecurityTokenList1;
	}

	public List<ResourceItemXClassification> getResourceItemXClassificationList()
	{
		return this.resourceItemXClassificationList;
	}

	public List<ResourceItemXClassification> getResourceItemXClassificationList1()
	{
		return this.resourceItemXClassificationList1;
	}

	public List<ArrangementXInvolvedPartySecurityToken> getArrangementXInvolvedPartySecurityTokenList()
	{
		return this.arrangementXInvolvedPartySecurityTokenList;
	}

	public List<ArrangementXInvolvedPartySecurityToken> getArrangementXInvolvedPartySecurityTokenList1()
	{
		return this.arrangementXInvolvedPartySecurityTokenList1;
	}

	public List<InvolvedPartyNonOrganic> getInvolvedPartyNonOrganicList()
	{
		return this.involvedPartyNonOrganicList;
	}

	public List<InvolvedPartyNonOrganic> getInvolvedPartyNonOrganicList1()
	{
		return this.involvedPartyNonOrganicList1;
	}

	public List<EventXInvolvedParty> getEventXInvolvedPartyList()
	{
		return this.eventXInvolvedPartyList;
	}

	public List<EventXInvolvedParty> getEventXInvolvedPartyList1()
	{
		return this.eventXInvolvedPartyList1;
	}

	public List<ActiveFlagXClassification> getActiveFlagXClassificationList()
	{
		return this.activeFlagXClassificationList;
	}

	public List<ActiveFlagXClassification> getActiveFlagXClassificationList1()
	{
		return this.activeFlagXClassificationList1;
	}

	public List<GeographyXResourceItemSecurityToken> getGeographyXResourceItemSecurityTokenList()
	{
		return this.geographyXResourceItemSecurityTokenList;
	}

	public List<GeographyXResourceItemSecurityToken> getGeographyXResourceItemSecurityTokenList1()
	{
		return this.geographyXResourceItemSecurityTokenList1;
	}

	public List<EnterpriseXClassification> getEnterpriseXClassificationList()
	{
		return this.enterpriseXClassificationList;
	}

	public List<EnterpriseXClassification> getEnterpriseXClassificationList1()
	{
		return this.enterpriseXClassificationList1;
	}

	public List<EventXArrangement> getEventXArrangementList()
	{
		return this.eventXArrangementList;
	}

	public List<EventXArrangement> getEventXArrangementList1()
	{
		return this.eventXArrangementList1;
	}

	public List<InvolvedPartyIdentificationTypeSecurityToken> getInvolvedPartyIdentificationTypeSecurityTokenList()
	{
		return this.involvedPartyIdentificationTypeSecurityTokenList;
	}

	public List<InvolvedPartyIdentificationTypeSecurityToken> getInvolvedPartyIdentificationTypeSecurityTokenList1()
	{
		return this.involvedPartyIdentificationTypeSecurityTokenList1;
	}

	public List<AddressXResourceItemSecurityToken> getAddressXResourceItemSecurityTokenList()
	{
		return this.addressXResourceItemSecurityTokenList;
	}

	public List<AddressXResourceItemSecurityToken> getAddressXResourceItemSecurityTokenList1()
	{
		return this.addressXResourceItemSecurityTokenList1;
	}

	public List<InvolvedPartySecurityToken> getInvolvedPartySecurityTokenList()
	{
		return this.involvedPartySecurityTokenList;
	}

	public List<InvolvedPartySecurityToken> getInvolvedPartySecurityTokenList1()
	{
		return this.involvedPartySecurityTokenList1;
	}

	public List<ProductXClassificationSecurityToken> getProductXClassificationSecurityTokenList()
	{
		return this.productXClassificationSecurityTokenList;
	}

	public List<ProductXClassificationSecurityToken> getProductXClassificationSecurityTokenList1()
	{
		return this.productXClassificationSecurityTokenList1;
	}

	public List<SecurityToken> getSecurityTokenList()
	{
		return this.securityTokenList;
	}

	public List<SecurityToken> getSecurityTokenList1()
	{
		return this.securityTokenList1;
	}

	public List<ArrangementXArrangementTypeSecurityToken> getArrangementXArrangementTypeSecurityTokenList()
	{
		return this.arrangementXArrangementTypeSecurityTokenList;
	}

	public List<ArrangementXArrangementTypeSecurityToken> getArrangementXArrangementTypeSecurityTokenList1()
	{
		return this.arrangementXArrangementTypeSecurityTokenList1;
	}

	public List<EventXInvolvedPartySecurityToken> getEventXInvolvedPartySecurityTokenList()
	{
		return this.eventXInvolvedPartySecurityTokenList;
	}

	public List<EventXInvolvedPartySecurityToken> getEventXInvolvedPartySecurityTokenList1()
	{
		return this.eventXInvolvedPartySecurityTokenList1;
	}

	public List<InvolvedPartyOrganic> getInvolvedPartyOrganicList()
	{
		return this.involvedPartyOrganicList;
	}

	public List<InvolvedPartyOrganic> getInvolvedPartyOrganicList1()
	{
		return this.involvedPartyOrganicList1;
	}

	public List<SystemXClassification> getClassifications()
	{
		return this.classifications;
	}

	public List<SystemXClassification> getSystemXClassificationList1()
	{
		return this.systemXClassificationList1;
	}

	public List<Event> getEventList()
	{
		return this.eventList;
	}

	public List<Event> getEventList1()
	{
		return this.eventList1;
	}

	public List<ActiveFlagSecurityToken> getActiveFlagSecurityTokenList()
	{
		return this.activeFlagSecurityTokenList;
	}

	public List<ActiveFlagSecurityToken> getActiveFlagSecurityTokenList1()
	{
		return this.activeFlagSecurityTokenList1;
	}

	public List<ArrangementXClassification> getArrangementXClassificationList()
	{
		return this.arrangementXClassificationList;
	}

	public List<ArrangementXClassification> getArrangementXClassificationList1()
	{
		return this.arrangementXClassificationList1;
	}

	public List<Geography> getGeographyList()
	{
		return this.geographyList;
	}

	public List<Geography> getGeographyList1()
	{
		return this.geographyList1;
	}

	public List<InvolvedPartyOrganicTypeSecurityToken> getInvolvedPartyOrganicTypeSecurityTokenList()
	{
		return this.involvedPartyOrganicTypeSecurityTokenList;
	}

	public List<InvolvedPartyOrganicTypeSecurityToken> getInvolvedPartyOrganicTypeSecurityTokenList1()
	{
		return this.involvedPartyOrganicTypeSecurityTokenList1;
	}

	public List<EventType> getEventTypeList()
	{
		return this.eventTypeList;
	}

	public List<EventType> getEventTypeList1()
	{
		return this.eventTypeList1;
	}

	public List<ResourceItemData> getResourceItemDataList()
	{
		return this.resourceItemDataList;
	}

	public List<ResourceItemData> getResourceItemDataList1()
	{
		return this.resourceItemDataList1;
	}

	public List<AddressXResourceItem> getAddressXResourceItemList()
	{
		return this.addressXResourceItemList;
	}

	public List<AddressXResourceItem> getAddressXResourceItemList1()
	{
		return this.addressXResourceItemList1;
	}

	public List<ArrangementTypeSecurityToken> getArrangementTypeSecurityTokenList()
	{
		return this.arrangementTypeSecurityTokenList;
	}

	public List<ArrangementTypeSecurityToken> getArrangementTypeSecurityTokenList1()
	{
		return this.arrangementTypeSecurityTokenList1;
	}

	public List<InvolvedPartyOrganicSecurityToken> getInvolvedPartyOrganicSecurityTokenList()
	{
		return this.involvedPartyOrganicSecurityTokenList;
	}

	public List<InvolvedPartyOrganicSecurityToken> getInvolvedPartyOrganicSecurityTokenList1()
	{
		return this.involvedPartyOrganicSecurityTokenList1;
	}

	public List<ArrangementXArrangement> getArrangementXArrangementList()
	{
		return this.arrangementXArrangementList;
	}

	public List<ArrangementXArrangement> getArrangementXArrangementList1()
	{
		return this.arrangementXArrangementList1;
	}

	public List<ClassificationDataConceptXClassification> getClassificationDataConceptXClassificationList()
	{
		return this.classificationDataConceptXClassificationList;
	}

	public List<ArrangementXProduct> getArrangementXProductList()
	{
		return this.arrangementXProductList;
	}

	public List<ArrangementXProduct> getArrangementXProductList1()
	{
		return this.arrangementXProductList1;
	}

	public List<InvolvedPartyXInvolvedParty> getInvolvedPartyXInvolvedPartyList()
	{
		return this.involvedPartyXInvolvedPartyList;
	}

	public List<InvolvedPartyXInvolvedParty> getInvolvedPartyXInvolvedPartyList1()
	{
		return this.involvedPartyXInvolvedPartyList1;
	}

	public List<InvolvedPartyXInvolvedPartyType> getInvolvedPartyXInvolvedPartyTypeList()
	{
		return this.involvedPartyXInvolvedPartyTypeList;
	}

	public List<InvolvedPartyXInvolvedPartyType> getInvolvedPartyXInvolvedPartyTypeList1()
	{
		return this.involvedPartyXInvolvedPartyTypeList1;
	}

	public List<ArrangementSecurityToken> getArrangementSecurityTokenList()
	{
		return this.arrangementSecurityTokenList;
	}

	public List<ArrangementSecurityToken> getArrangementSecurityTokenList1()
	{
		return this.arrangementSecurityTokenList1;
	}

	public List<InvolvedPartyOrganicType> getInvolvedPartyOrganicTypeList()
	{
		return this.involvedPartyOrganicTypeList;
	}

	public List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> getInvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList()
	{
		return this.involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList;
	}

	public List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> getInvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList1()
	{
		return this.involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList1;
	}

	public List<InvolvedPartyXInvolvedPartyTypeSecurityToken> getInvolvedPartyXInvolvedPartyTypeSecurityTokenList()
	{
		return this.involvedPartyXInvolvedPartyTypeSecurityTokenList;
	}

	public List<InvolvedPartyXInvolvedPartyTypeSecurityToken> getInvolvedPartyXInvolvedPartyTypeSecurityTokenList1()
	{
		return this.involvedPartyXInvolvedPartyTypeSecurityTokenList1;
	}

	public List<ClassificationDataConceptSecurityToken> getClassificationDataConceptSecurityTokenList()
	{
		return this.classificationDataConceptSecurityTokenList;
	}

	public List<ClassificationDataConceptSecurityToken> getClassificationDataConceptSecurityTokenList1()
	{
		return this.classificationDataConceptSecurityTokenList1;
	}

	public List<EventXResourceItem> getEventXResourceItemList()
	{
		return this.eventXResourceItemList;
	}

	public List<EventXResourceItem> getEventXResourceItemList1()
	{
		return this.eventXResourceItemList1;
	}

	public List<InvolvedPartyXClassification> getInvolvedPartyXClassificationList()
	{
		return this.involvedPartyXClassificationList;
	}

	public List<InvolvedPartyXClassification> getInvolvedPartyXClassificationList1()
	{
		return this.involvedPartyXClassificationList1;
	}

	public List<ArrangementType> getArrangementTypeList()
	{
		return this.arrangementTypeList;
	}

	public List<ArrangementType> getArrangementTypeList1()
	{
		return this.arrangementTypeList1;
	}

	public List<InvolvedPartyTypeSecurityToken> getInvolvedPartyTypeSecurityTokenList()
	{
		return this.involvedPartyTypeSecurityTokenList;
	}

	public List<InvolvedPartyTypeSecurityToken> getInvolvedPartyTypeSecurityTokenList1()
	{
		return this.involvedPartyTypeSecurityTokenList1;
	}

	public List<SecurityTokenXSecurityToken> getSecurityTokenXSecurityTokenList()
	{
		return this.securityTokenXSecurityTokenList;
	}

	public List<SecurityTokenXSecurityToken> getSecurityTokenXSecurityTokenList1()
	{
		return this.securityTokenXSecurityTokenList1;
	}

	public List<ProductXResourceItem> getProductXResourceItemList()
	{
		return this.productXResourceItemList;
	}

	public List<ProductXResourceItem> getProductXResourceItemList1()
	{
		return this.productXResourceItemList1;
	}

	public List<InvolvedPartyXClassificationSecurityToken> getInvolvedPartyXClassificationSecurityTokenList()
	{
		return this.involvedPartyXClassificationSecurityTokenList;
	}

	public List<InvolvedPartyXClassificationSecurityToken> getInvolvedPartyXClassificationSecurityTokenList1()
	{
		return this.involvedPartyXClassificationSecurityTokenList1;
	}

	public List<EventXAddressSecurityToken> getEventXAddressSecurityTokenList()
	{
		return this.eventXAddressSecurityTokenList;
	}

	public List<EventXAddressSecurityToken> getEventXAddressSecurityTokenList1()
	{
		return this.eventXAddressSecurityTokenList1;
	}

	public List<ResourceItem> getResourceItemList()
	{
		return this.resourceItemList;
	}

	public List<ResourceItem> getResourceItemList1()
	{
		return this.resourceItemList1;
	}

	public List<EventXAddress> getEventXAddressList()
	{
		return this.eventXAddressList;
	}

	public List<EventXAddress> getEventXAddressList1()
	{
		return this.eventXAddressList1;
	}

	public List<ClassificationDataConceptXResourceItem> getClassificationDataConceptXResourceItemList()
	{
		return this.classificationDataConceptXResourceItemList;
	}

	public List<InvolvedPartyXProduct> getInvolvedPartyXProductList()
	{
		return this.involvedPartyXProductList;
	}

	public List<InvolvedPartyXProduct> getInvolvedPartyXProductList1()
	{
		return this.involvedPartyXProductList1;
	}

	public List<EventSecurityToken> getEventSecurityTokenList()
	{
		return this.eventSecurityTokenList;
	}

	public List<EventSecurityToken> getEventSecurityTokenList1()
	{
		return this.eventSecurityTokenList1;
	}

	public List<ResourceItemDataXClassification> getResourceItemDataXClassificationList()
	{
		return this.resourceItemDataXClassificationList;
	}

	public List<ResourceItemDataXClassification> getResourceItemDataXClassificationList1()
	{
		return this.resourceItemDataXClassificationList1;
	}

	public List<SecurityTokensSecurityToken> getSecurityTokenAccessList()
	{
		return this.securityTokenAccessList;
	}

	public List<SecurityTokensSecurityToken> getSecurityTokenAccessList1()
	{
		return this.securityTokenAccessList1;
	}

	public List<InvolvedPartyXAddress> getInvolvedPartyXAddressList()
	{
		return this.involvedPartyXAddressList;
	}

	public List<InvolvedPartyXAddress> getInvolvedPartyXAddressList1()
	{
		return this.involvedPartyXAddressList1;
	}

	public List<GeographyXClassificationSecurityToken> getGeographyXClassificationSecurityTokenList()
	{
		return this.geographyXClassificationSecurityTokenList;
	}

	public List<GeographyXClassificationSecurityToken> getGeographyXClassificationSecurityTokenList1()
	{
		return this.geographyXClassificationSecurityTokenList1;
	}

	public List<GeographyXGeographySecurityToken> getGeographyXGeographySecurityTokenList()
	{
		return this.geographyXGeographySecurityTokenList;
	}

	public List<GeographyXGeographySecurityToken> getGeographyXGeographySecurityTokenList1()
	{
		return this.geographyXGeographySecurityTokenList1;
	}

	public List<ArrangementXArrangementSecurityToken> getArrangementXArrangementSecurityTokenList()
	{
		return this.arrangementXArrangementSecurityTokenList;
	}

	public List<ArrangementXArrangementSecurityToken> getArrangementXArrangementSecurityTokenList1()
	{
		return this.arrangementXArrangementSecurityTokenList1;
	}

	public List<GeographySecurityToken> getGeographySecurityTokenList()
	{
		return this.geographySecurityTokenList;
	}

	public List<GeographySecurityToken> getGeographySecurityTokenList1()
	{
		return this.geographySecurityTokenList1;
	}

	public List<InvolvedPartyXInvolvedPartyIdentificationType> getInvolvedPartyXInvolvedPartyIdentificationTypeList()
	{
		return this.involvedPartyXInvolvedPartyIdentificationTypeList;
	}

	public List<InvolvedPartyXInvolvedPartyIdentificationType> getInvolvedPartyXInvolvedPartyIdentificationTypeList1()
	{
		return this.involvedPartyXInvolvedPartyIdentificationTypeList1;
	}

	public List<InvolvedPartyIdentificationType> getInvolvedPartyIdentificationTypeList()
	{
		return this.involvedPartyIdentificationTypeList;
	}

	public List<InvolvedPartyIdentificationType> getInvolvedPartyIdentificationTypeList1()
	{
		return this.involvedPartyIdentificationTypeList1;
	}

	public List<ClassificationXResourceItem> getClassificationXResourceItemList()
	{
		return this.classificationXResourceItemList;
	}

	public List<ClassificationXResourceItem> getClassificationXResourceItemList1()
	{
		return this.classificationXResourceItemList1;
	}

	public List<EventXProduct> getEventXProductList()
	{
		return this.eventXProductList;
	}

	public List<EventXProduct> getEventXProductList1()
	{
		return this.eventXProductList1;
	}

	public List<EventXEventType> getEventXEventTypeList()
	{
		return this.eventXEventTypeList;
	}

	public List<InvolvedPartyXInvolvedPartySecurityToken> getInvolvedPartyXInvolvedPartySecurityTokenList()
	{
		return this.involvedPartyXInvolvedPartySecurityTokenList;
	}

	public List<InvolvedPartyXInvolvedPartySecurityToken> getInvolvedPartyXInvolvedPartySecurityTokenList1()
	{
		return this.involvedPartyXInvolvedPartySecurityTokenList1;
	}

	public List<ProductXProduct> getProductXProductList()
	{
		return this.productXProductList;
	}

	public List<ProductXProduct> getProductXProductList1()
	{
		return this.productXProductList1;
	}

	public List<InvolvedPartyType> getInvolvedPartyTypeList()
	{
		return this.involvedPartyTypeList;
	}

	public List<InvolvedPartyType> getInvolvedPartyTypeList1()
	{
		return this.involvedPartyTypeList1;
	}

	public List<ArrangementXClassificationSecurityToken> getArrangementXClassificationSecurityTokenList()
	{
		return this.arrangementXClassificationSecurityTokenList;
	}

	public List<ArrangementXClassificationSecurityToken> getArrangementXClassificationSecurityTokenList1()
	{
		return this.arrangementXClassificationSecurityTokenList1;
	}

	public List<InvolvedPartyXAddressSecurityToken> getInvolvedPartyXAddressSecurityTokenList()
	{
		return this.involvedPartyXAddressSecurityTokenList;
	}

	public List<InvolvedPartyXAddressSecurityToken> getInvolvedPartyXAddressSecurityTokenList1()
	{
		return this.involvedPartyXAddressSecurityTokenList1;
	}

	public List<GeographyXClassification> getGeographyXClassificationList()
	{
		return this.geographyXClassificationList;
	}

	public List<GeographyXClassification> getGeographyXClassificationList1()
	{
		return this.geographyXClassificationList1;
	}

	public List<AddressXGeographySecurityToken> getAddressXGeographySecurityTokenList()
	{
		return this.addressXGeographySecurityTokenList;
	}

	public List<AddressXGeographySecurityToken> getAddressXGeographySecurityTokenList1()
	{
		return this.addressXGeographySecurityTokenList1;
	}

	public List<InvolvedPartyNonOrganicSecurityToken> getInvolvedPartyNonOrganicSecurityTokenList()
	{
		return this.involvedPartyNonOrganicSecurityTokenList;
	}

	public List<InvolvedPartyNonOrganicSecurityToken> getInvolvedPartyNonOrganicSecurityTokenList1()
	{
		return this.involvedPartyNonOrganicSecurityTokenList1;
	}

	public List<EventXClassification> getEventXClassificationList()
	{
		return this.eventXClassificationList;
	}

	public List<EventXClassification> getEventXClassificationList1()
	{
		return this.eventXClassificationList1;
	}

	public List<Arrangement> getArrangementList()
	{
		return this.arrangementList;
	}

	public List<Arrangement> getArrangementList1()
	{
		return this.arrangementList1;
	}

	public List<ResourceItemSecurityToken> getResourceItemSecurityTokenList()
	{
		return this.resourceItemSecurityTokenList;
	}

	public List<ResourceItemSecurityToken> getResourceItemSecurityTokenList1()
	{
		return this.resourceItemSecurityTokenList1;
	}

	public List<GeographyXResourceItem> getGeographyXResourceItemList()
	{
		return this.geographyXResourceItemList;
	}

	public List<GeographyXResourceItem> getGeographyXResourceItemList1()
	{
		return this.geographyXResourceItemList1;
	}

	public List<ClassificationXClassificationSecurityToken> getClassificationXClassificationSecurityTokenList()
	{
		return this.classificationXClassificationSecurityTokenList;
	}

	public List<ClassificationXClassificationSecurityToken> getClassificationXClassificationSecurityTokenList1()
	{
		return this.classificationXClassificationSecurityTokenList1;
	}

	public List<EventTypesSecurityToken> getEventTypesSecurityTokenList()
	{
		return this.eventTypesSecurityTokenList;
	}

	public List<EventTypesSecurityToken> getEventTypesSecurityTokenList1()
	{
		return this.eventTypesSecurityTokenList1;
	}

	public List<YesNoXClassification> getYesNoXClassificationList()
	{
		return this.yesNoXClassificationList;
	}

	public List<GeographyXGeography> getGeographyXGeographyList()
	{
		return this.geographyXGeographyList;
	}

	public List<GeographyXGeography> getGeographyXGeographyList1()
	{
		return this.geographyXGeographyList1;
	}

	public List<ClassificationSecurityToken> getClassificationSecurityTokenList()
	{
		return this.classificationSecurityTokenList;
	}

	public List<ClassificationSecurityToken> getClassificationSecurityTokenList1()
	{
		return this.classificationSecurityTokenList1;
	}

	public List<YesNo> getYesNoList()
	{
		return this.yesNoList;
	}

	public List<YesNo> getYesNoList1()
	{
		return this.yesNoList1;
	}

	public Systems setInvolvedPartyXInvolvedPartyNameTypeList(List<InvolvedPartyXInvolvedPartyNameType> involvedPartyXInvolvedPartyNameTypeList)
	{
		this.involvedPartyXInvolvedPartyNameTypeList = involvedPartyXInvolvedPartyNameTypeList;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartyNameTypeList1(List<InvolvedPartyXInvolvedPartyNameType> involvedPartyXInvolvedPartyNameTypeList1)
	{
		this.involvedPartyXInvolvedPartyNameTypeList1 = involvedPartyXInvolvedPartyNameTypeList1;
		return this;
	}

	public Systems setInvolvedPartyXProductSecurityTokenList(List<InvolvedPartyXProductSecurityToken> involvedPartyXProductSecurityTokenList)
	{
		this.involvedPartyXProductSecurityTokenList = involvedPartyXProductSecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartyXProductSecurityTokenList1(List<InvolvedPartyXProductSecurityToken> involvedPartyXProductSecurityTokenList1)
	{
		this.involvedPartyXProductSecurityTokenList1 = involvedPartyXProductSecurityTokenList1;
		return this;
	}

	public Systems setAddressList(List<Address> addressList)
	{
		this.addressList = addressList;
		return this;
	}

	public Systems setAddressList1(List<Address> addressList1)
	{
		this.addressList1 = addressList1;
		return this;
	}

	public Systems setProductXResourceItemSecurityTokenList(List<ProductXResourceItemSecurityToken> productXResourceItemSecurityTokenList)
	{
		this.productXResourceItemSecurityTokenList = productXResourceItemSecurityTokenList;
		return this;
	}

	public Systems setProductXResourceItemSecurityTokenList1(List<ProductXResourceItemSecurityToken> productXResourceItemSecurityTokenList1)
	{
		this.productXResourceItemSecurityTokenList1 = productXResourceItemSecurityTokenList1;
		return this;
	}

	public Systems setProductList(List<Product> productList)
	{
		this.productList = productList;
		return this;
	}

	public Systems setProductList1(List<Product> productList1)
	{
		this.productList1 = productList1;
		return this;
	}

	public Systems setResourceItemXClassificationSecurityTokenList(List<ResourceItemXClassificationSecurityToken> resourceItemXClassificationSecurityTokenList)
	{
		this.resourceItemXClassificationSecurityTokenList = resourceItemXClassificationSecurityTokenList;
		return this;
	}

	public Systems setResourceItemXClassificationSecurityTokenList1(List<ResourceItemXClassificationSecurityToken> resourceItemXClassificationSecurityTokenList1)
	{
		this.resourceItemXClassificationSecurityTokenList1 = resourceItemXClassificationSecurityTokenList1;
		return this;
	}

	public Systems setInvolvedPartyXResourceItemList(List<InvolvedPartyXResourceItem> involvedPartyXResourceItemList)
	{
		this.involvedPartyXResourceItemList = involvedPartyXResourceItemList;
		return this;
	}

	public Systems setInvolvedPartyXResourceItemList1(List<InvolvedPartyXResourceItem> involvedPartyXResourceItemList1)
	{
		this.involvedPartyXResourceItemList1 = involvedPartyXResourceItemList1;
		return this;
	}

	public Systems setInvolvedPartyNameTypeList(List<InvolvedPartyNameType> involvedPartyNameTypeList)
	{
		this.involvedPartyNameTypeList = involvedPartyNameTypeList;
		return this;
	}

	public Systems setInvolvedPartyNameTypeList1(List<InvolvedPartyNameType> involvedPartyNameTypeList1)
	{
		this.involvedPartyNameTypeList1 = involvedPartyNameTypeList1;
		return this;
	}

	public Systems setEventXClassificationSecurityTokenList(List<EventXClassificationSecurityToken> eventXClassificationSecurityTokenList)
	{
		this.eventXClassificationSecurityTokenList = eventXClassificationSecurityTokenList;
		return this;
	}

	public Systems setEventXClassificationSecurityTokenList1(List<EventXClassificationSecurityToken> eventXClassificationSecurityTokenList1)
	{
		this.eventXClassificationSecurityTokenList1 = eventXClassificationSecurityTokenList1;
		return this;
	}

	public Systems setProductSecurityTokenList(List<ProductSecurityToken> productSecurityTokenList)
	{
		this.productSecurityTokenList = productSecurityTokenList;
		return this;
	}

	public Systems setProductSecurityTokenList1(List<ProductSecurityToken> productSecurityTokenList1)
	{
		this.productSecurityTokenList1 = productSecurityTokenList1;
		return this;
	}

	public Systems setInvolvedPartyList(List<InvolvedParty> involvedPartyList)
	{
		this.involvedPartyList = involvedPartyList;
		return this;
	}

	public Systems setInvolvedPartyList1(List<InvolvedParty> involvedPartyList1)
	{
		this.involvedPartyList1 = involvedPartyList1;
		return this;
	}

	public Systems setArrangementXArrangementTypeList(List<ArrangementXArrangementType> arrangementXArrangementTypeList)
	{
		this.arrangementXArrangementTypeList = arrangementXArrangementTypeList;
		return this;
	}

	public Systems setArrangementXArrangementTypeList1(List<ArrangementXArrangementType> arrangementXArrangementTypeList1)
	{
		this.arrangementXArrangementTypeList1 = arrangementXArrangementTypeList1;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartyNameTypeSecurityTokenList(List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> involvedPartyXInvolvedPartyNameTypeSecurityTokenList)
	{
		this.involvedPartyXInvolvedPartyNameTypeSecurityTokenList = involvedPartyXInvolvedPartyNameTypeSecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartyNameTypeSecurityTokenList1(List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> involvedPartyXInvolvedPartyNameTypeSecurityTokenList1)
	{
		this.involvedPartyXInvolvedPartyNameTypeSecurityTokenList1 = involvedPartyXInvolvedPartyNameTypeSecurityTokenList1;
		return this;
	}

	public Systems setClassificationList(List<Classification> classificationList)
	{
		this.classificationList = classificationList;
		return this;
	}

	public Systems setClassificationList1(List<Classification> classificationList1)
	{
		this.classificationList1 = classificationList1;
		return this;
	}

	public Systems setEventXEventTypeSecurityTokenList(List<EventXEventTypeSecurityToken> eventXEventTypeSecurityTokenList)
	{
		this.eventXEventTypeSecurityTokenList = eventXEventTypeSecurityTokenList;
		return this;
	}

	public Systems setEventXEventTypeSecurityTokenList1(List<EventXEventTypeSecurityToken> eventXEventTypeSecurityTokenList1)
	{
		this.eventXEventTypeSecurityTokenList1 = eventXEventTypeSecurityTokenList1;
		return this;
	}

	public Systems setArrangementXResourceItemSecurityTokenList(List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList)
	{
		this.arrangementXResourceItemSecurityTokenList = arrangementXResourceItemSecurityTokenList;
		return this;
	}

	public Systems setArrangementXResourceItemSecurityTokenList1(List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList1)
	{
		this.arrangementXResourceItemSecurityTokenList1 = arrangementXResourceItemSecurityTokenList1;
		return this;
	}

	public Systems setEventXProductSecurityTokenList(List<EventXProductSecurityToken> eventXProductSecurityTokenList)
	{
		this.eventXProductSecurityTokenList = eventXProductSecurityTokenList;
		return this;
	}

	public Systems setEventXProductSecurityTokenList1(List<EventXProductSecurityToken> eventXProductSecurityTokenList1)
	{
		this.eventXProductSecurityTokenList1 = eventXProductSecurityTokenList1;
		return this;
	}

	public Systems setInvolvedPartyNameTypeSecurityTokenList(List<InvolvedPartyNameTypeSecurityToken> involvedPartyNameTypeSecurityTokenList)
	{
		this.involvedPartyNameTypeSecurityTokenList = involvedPartyNameTypeSecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartyNameTypeSecurityTokenList1(List<InvolvedPartyNameTypeSecurityToken> involvedPartyNameTypeSecurityTokenList1)
	{
		this.involvedPartyNameTypeSecurityTokenList1 = involvedPartyNameTypeSecurityTokenList1;
		return this;
	}

	public Systems setClassificationXResourceItemSecurityTokenList(List<ClassificationXResourceItemSecurityToken> classificationXResourceItemSecurityTokenList)
	{
		this.classificationXResourceItemSecurityTokenList = classificationXResourceItemSecurityTokenList;
		return this;
	}

	public Systems setClassificationXResourceItemSecurityTokenList1(List<ClassificationXResourceItemSecurityToken> classificationXResourceItemSecurityTokenList1)
	{
		this.classificationXResourceItemSecurityTokenList1 = classificationXResourceItemSecurityTokenList1;
		return this;
	}

	public Systems setEventXResourceItemSecurityTokenList(List<EventXResourceItemSecurityToken> eventXResourceItemSecurityTokenList)
	{
		this.eventXResourceItemSecurityTokenList = eventXResourceItemSecurityTokenList;
		return this;
	}

	public Systems setEventXResourceItemSecurityTokenList1(List<EventXResourceItemSecurityToken> eventXResourceItemSecurityTokenList1)
	{
		this.eventXResourceItemSecurityTokenList1 = eventXResourceItemSecurityTokenList1;
		return this;
	}

	public Systems setAddressSecurityTokenList(List<AddressSecurityToken> addressSecurityTokenList)
	{
		this.addressSecurityTokenList = addressSecurityTokenList;
		return this;
	}

	public Systems setAddressSecurityTokenList1(List<AddressSecurityToken> addressSecurityTokenList1)
	{
		this.addressSecurityTokenList1 = addressSecurityTokenList1;
		return this;
	}

	public Systems setSecurities(List<SystemsSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public Systems setSystemSecurityTokenList1(List<SystemsSecurityToken> systemSecurityTokenList1)
	{
		this.systemSecurityTokenList1 = systemSecurityTokenList1;
		return this;
	}

	public Systems setClassificationXClassificationList(List<ClassificationXClassification> classificationXClassificationList)
	{
		this.classificationXClassificationList = classificationXClassificationList;
		return this;
	}

	public Systems setClassificationXClassificationList1(List<ClassificationXClassification> classificationXClassificationList1)
	{
		this.classificationXClassificationList1 = classificationXClassificationList1;
		return this;
	}

	public Systems setClassificationDataConceptList(List<ClassificationDataConcept> classificationDataConceptList)
	{
		this.classificationDataConceptList = classificationDataConceptList;
		return this;
	}

	public Systems setClassificationDataConceptList1(List<ClassificationDataConcept> classificationDataConceptList1)
	{
		this.classificationDataConceptList1 = classificationDataConceptList1;
		return this;
	}

	public Systems setAddressXGeographyList(List<AddressXGeography> addressXGeographyList)
	{
		this.addressXGeographyList = addressXGeographyList;
		return this;
	}

	public Systems setAddressXGeographyList1(List<AddressXGeography> addressXGeographyList1)
	{
		this.addressXGeographyList1 = addressXGeographyList1;
		return this;
	}

	public Systems setSecurityTokenXClassificationList(List<SecurityTokenXClassification> securityTokenXClassificationList)
	{
		this.securityTokenXClassificationList = securityTokenXClassificationList;
		return this;
	}

	public Systems setSecurityTokenXClassificationList1(List<SecurityTokenXClassification> securityTokenXClassificationList1)
	{
		this.securityTokenXClassificationList1 = securityTokenXClassificationList1;
		return this;
	}

	public Systems setEventXArrangementsSecurityTokenList(List<EventXArrangementsSecurityToken> eventXArrangementsSecurityTokenList)
	{
		this.eventXArrangementsSecurityTokenList = eventXArrangementsSecurityTokenList;
		return this;
	}

	public Systems setEventXArrangementsSecurityTokenList1(List<EventXArrangementsSecurityToken> eventXArrangementsSecurityTokenList1)
	{
		this.eventXArrangementsSecurityTokenList1 = eventXArrangementsSecurityTokenList1;
		return this;
	}

	public Systems setInvolvedPartyXResourceItemSecurityTokenList(List<InvolvedPartyXResourceItemSecurityToken> involvedPartyXResourceItemSecurityTokenList)
	{
		this.involvedPartyXResourceItemSecurityTokenList = involvedPartyXResourceItemSecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartyXResourceItemSecurityTokenList1(List<InvolvedPartyXResourceItemSecurityToken> involvedPartyXResourceItemSecurityTokenList1)
	{
		this.involvedPartyXResourceItemSecurityTokenList1 = involvedPartyXResourceItemSecurityTokenList1;
		return this;
	}

	public Systems setProductXClassificationList(List<ProductXClassification> productXClassificationList)
	{
		this.productXClassificationList = productXClassificationList;
		return this;
	}

	public Systems setProductXClassificationList1(List<ProductXClassification> productXClassificationList1)
	{
		this.productXClassificationList1 = productXClassificationList1;
		return this;
	}

	public Systems setResourceItemDataSecurityTokenList(List<ResourceItemDataSecurityToken> resourceItemDataSecurityTokenList)
	{
		this.resourceItemDataSecurityTokenList = resourceItemDataSecurityTokenList;
		return this;
	}

	public Systems setResourceItemDataSecurityTokenList1(List<ResourceItemDataSecurityToken> resourceItemDataSecurityTokenList1)
	{
		this.resourceItemDataSecurityTokenList1 = resourceItemDataSecurityTokenList1;
		return this;
	}

	public Systems setArrangementXInvolvedPartyList(List<ArrangementXInvolvedParty> arrangementXInvolvedPartyList)
	{
		this.arrangementXInvolvedPartyList = arrangementXInvolvedPartyList;
		return this;
	}

	public Systems setArrangementXInvolvedPartyList1(List<ArrangementXInvolvedParty> arrangementXInvolvedPartyList1)
	{
		this.arrangementXInvolvedPartyList1 = arrangementXInvolvedPartyList1;
		return this;
	}

	public Systems setArrangementXResourceItemList(List<ArrangementXResourceItem> arrangementXResourceItemList)
	{
		this.arrangementXResourceItemList = arrangementXResourceItemList;
		return this;
	}

	public Systems setArrangementXResourceItemList1(List<ArrangementXResourceItem> arrangementXResourceItemList1)
	{
		this.arrangementXResourceItemList1 = arrangementXResourceItemList1;
		return this;
	}

	public Systems setProductXProductSecurityTokenList(List<ProductXProductSecurityToken> productXProductSecurityTokenList)
	{
		this.productXProductSecurityTokenList = productXProductSecurityTokenList;
		return this;
	}

	public Systems setProductXProductSecurityTokenList1(List<ProductXProductSecurityToken> productXProductSecurityTokenList1)
	{
		this.productXProductSecurityTokenList1 = productXProductSecurityTokenList1;
		return this;
	}

	public Systems setResourceItemXClassificationList(List<ResourceItemXClassification> resourceItemXClassificationList)
	{
		this.resourceItemXClassificationList = resourceItemXClassificationList;
		return this;
	}

	public Systems setResourceItemXClassificationList1(List<ResourceItemXClassification> resourceItemXClassificationList1)
	{
		this.resourceItemXClassificationList1 = resourceItemXClassificationList1;
		return this;
	}

	public Systems setArrangementXInvolvedPartySecurityTokenList(List<ArrangementXInvolvedPartySecurityToken> arrangementXInvolvedPartySecurityTokenList)
	{
		this.arrangementXInvolvedPartySecurityTokenList = arrangementXInvolvedPartySecurityTokenList;
		return this;
	}

	public Systems setArrangementXInvolvedPartySecurityTokenList1(List<ArrangementXInvolvedPartySecurityToken> arrangementXInvolvedPartySecurityTokenList1)
	{
		this.arrangementXInvolvedPartySecurityTokenList1 = arrangementXInvolvedPartySecurityTokenList1;
		return this;
	}

	public Systems setInvolvedPartyNonOrganicList(List<InvolvedPartyNonOrganic> involvedPartyNonOrganicList)
	{
		this.involvedPartyNonOrganicList = involvedPartyNonOrganicList;
		return this;
	}

	public Systems setInvolvedPartyNonOrganicList1(List<InvolvedPartyNonOrganic> involvedPartyNonOrganicList1)
	{
		this.involvedPartyNonOrganicList1 = involvedPartyNonOrganicList1;
		return this;
	}

	public Systems setEventXInvolvedPartyList(List<EventXInvolvedParty> eventXInvolvedPartyList)
	{
		this.eventXInvolvedPartyList = eventXInvolvedPartyList;
		return this;
	}

	public Systems setEventXInvolvedPartyList1(List<EventXInvolvedParty> eventXInvolvedPartyList1)
	{
		this.eventXInvolvedPartyList1 = eventXInvolvedPartyList1;
		return this;
	}

	public Systems setActiveFlagXClassificationList(List<ActiveFlagXClassification> activeFlagXClassificationList)
	{
		this.activeFlagXClassificationList = activeFlagXClassificationList;
		return this;
	}

	public Systems setActiveFlagXClassificationList1(List<ActiveFlagXClassification> activeFlagXClassificationList1)
	{
		this.activeFlagXClassificationList1 = activeFlagXClassificationList1;
		return this;
	}

	public Systems setGeographyXResourceItemSecurityTokenList(List<GeographyXResourceItemSecurityToken> geographyXResourceItemSecurityTokenList)
	{
		this.geographyXResourceItemSecurityTokenList = geographyXResourceItemSecurityTokenList;
		return this;
	}

	public Systems setGeographyXResourceItemSecurityTokenList1(List<GeographyXResourceItemSecurityToken> geographyXResourceItemSecurityTokenList1)
	{
		this.geographyXResourceItemSecurityTokenList1 = geographyXResourceItemSecurityTokenList1;
		return this;
	}

	public Systems setEnterpriseXClassificationList(List<EnterpriseXClassification> enterpriseXClassificationList)
	{
		this.enterpriseXClassificationList = enterpriseXClassificationList;
		return this;
	}

	public Systems setEnterpriseXClassificationList1(List<EnterpriseXClassification> enterpriseXClassificationList1)
	{
		this.enterpriseXClassificationList1 = enterpriseXClassificationList1;
		return this;
	}

	public Systems setEventXArrangementList(List<EventXArrangement> eventXArrangementList)
	{
		this.eventXArrangementList = eventXArrangementList;
		return this;
	}

	public Systems setEventXArrangementList1(List<EventXArrangement> eventXArrangementList1)
	{
		this.eventXArrangementList1 = eventXArrangementList1;
		return this;
	}

	public Systems setInvolvedPartyIdentificationTypeSecurityTokenList(List<InvolvedPartyIdentificationTypeSecurityToken> involvedPartyIdentificationTypeSecurityTokenList)
	{
		this.involvedPartyIdentificationTypeSecurityTokenList = involvedPartyIdentificationTypeSecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartyIdentificationTypeSecurityTokenList1(List<InvolvedPartyIdentificationTypeSecurityToken> involvedPartyIdentificationTypeSecurityTokenList1)
	{
		this.involvedPartyIdentificationTypeSecurityTokenList1 = involvedPartyIdentificationTypeSecurityTokenList1;
		return this;
	}

	public Systems setAddressXResourceItemSecurityTokenList(List<AddressXResourceItemSecurityToken> addressXResourceItemSecurityTokenList)
	{
		this.addressXResourceItemSecurityTokenList = addressXResourceItemSecurityTokenList;
		return this;
	}

	public Systems setAddressXResourceItemSecurityTokenList1(List<AddressXResourceItemSecurityToken> addressXResourceItemSecurityTokenList1)
	{
		this.addressXResourceItemSecurityTokenList1 = addressXResourceItemSecurityTokenList1;
		return this;
	}

	public Systems setInvolvedPartySecurityTokenList(List<InvolvedPartySecurityToken> involvedPartySecurityTokenList)
	{
		this.involvedPartySecurityTokenList = involvedPartySecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartySecurityTokenList1(List<InvolvedPartySecurityToken> involvedPartySecurityTokenList1)
	{
		this.involvedPartySecurityTokenList1 = involvedPartySecurityTokenList1;
		return this;
	}

	public Systems setProductXClassificationSecurityTokenList(List<ProductXClassificationSecurityToken> productXClassificationSecurityTokenList)
	{
		this.productXClassificationSecurityTokenList = productXClassificationSecurityTokenList;
		return this;
	}

	public Systems setProductXClassificationSecurityTokenList1(List<ProductXClassificationSecurityToken> productXClassificationSecurityTokenList1)
	{
		this.productXClassificationSecurityTokenList1 = productXClassificationSecurityTokenList1;
		return this;
	}

	public Systems setSecurityTokenList(List<SecurityToken> securityTokenList)
	{
		this.securityTokenList = securityTokenList;
		return this;
	}

	public Systems setSecurityTokenList1(List<SecurityToken> securityTokenList1)
	{
		this.securityTokenList1 = securityTokenList1;
		return this;
	}

	public Systems setArrangementXArrangementTypeSecurityTokenList(List<ArrangementXArrangementTypeSecurityToken> arrangementXArrangementTypeSecurityTokenList)
	{
		this.arrangementXArrangementTypeSecurityTokenList = arrangementXArrangementTypeSecurityTokenList;
		return this;
	}

	public Systems setArrangementXArrangementTypeSecurityTokenList1(List<ArrangementXArrangementTypeSecurityToken> arrangementXArrangementTypeSecurityTokenList1)
	{
		this.arrangementXArrangementTypeSecurityTokenList1 = arrangementXArrangementTypeSecurityTokenList1;
		return this;
	}

	public Systems setEventXInvolvedPartySecurityTokenList(List<EventXInvolvedPartySecurityToken> eventXInvolvedPartySecurityTokenList)
	{
		this.eventXInvolvedPartySecurityTokenList = eventXInvolvedPartySecurityTokenList;
		return this;
	}

	public Systems setEventXInvolvedPartySecurityTokenList1(List<EventXInvolvedPartySecurityToken> eventXInvolvedPartySecurityTokenList1)
	{
		this.eventXInvolvedPartySecurityTokenList1 = eventXInvolvedPartySecurityTokenList1;
		return this;
	}

	public Systems setInvolvedPartyOrganicList(List<InvolvedPartyOrganic> involvedPartyOrganicList)
	{
		this.involvedPartyOrganicList = involvedPartyOrganicList;
		return this;
	}

	public Systems setInvolvedPartyOrganicList1(List<InvolvedPartyOrganic> involvedPartyOrganicList1)
	{
		this.involvedPartyOrganicList1 = involvedPartyOrganicList1;
		return this;
	}

	public Systems setClassifications(List<SystemXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}

	public Systems setSystemXClassificationList1(List<SystemXClassification> systemXClassificationList1)
	{
		this.systemXClassificationList1 = systemXClassificationList1;
		return this;
	}

	public Systems setEventList(List<Event> eventList)
	{
		this.eventList = eventList;
		return this;
	}

	public Systems setEventList1(List<Event> eventList1)
	{
		this.eventList1 = eventList1;
		return this;
	}

	public Systems setActiveFlagSecurityTokenList(List<ActiveFlagSecurityToken> activeFlagSecurityTokenList)
	{
		this.activeFlagSecurityTokenList = activeFlagSecurityTokenList;
		return this;
	}

	public Systems setActiveFlagSecurityTokenList1(List<ActiveFlagSecurityToken> activeFlagSecurityTokenList1)
	{
		this.activeFlagSecurityTokenList1 = activeFlagSecurityTokenList1;
		return this;
	}

	public Systems setArrangementXClassificationList(List<ArrangementXClassification> arrangementXClassificationList)
	{
		this.arrangementXClassificationList = arrangementXClassificationList;
		return this;
	}

	public Systems setArrangementXClassificationList1(List<ArrangementXClassification> arrangementXClassificationList1)
	{
		this.arrangementXClassificationList1 = arrangementXClassificationList1;
		return this;
	}

	public Systems setGeographyList(List<Geography> geographyList)
	{
		this.geographyList = geographyList;
		return this;
	}

	public Systems setGeographyList1(List<Geography> geographyList1)
	{
		this.geographyList1 = geographyList1;
		return this;
	}

	public Systems setInvolvedPartyOrganicTypeSecurityTokenList(List<InvolvedPartyOrganicTypeSecurityToken> involvedPartyOrganicTypeSecurityTokenList)
	{
		this.involvedPartyOrganicTypeSecurityTokenList = involvedPartyOrganicTypeSecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartyOrganicTypeSecurityTokenList1(List<InvolvedPartyOrganicTypeSecurityToken> involvedPartyOrganicTypeSecurityTokenList1)
	{
		this.involvedPartyOrganicTypeSecurityTokenList1 = involvedPartyOrganicTypeSecurityTokenList1;
		return this;
	}

	public Systems setEventTypeList(List<EventType> eventTypeList)
	{
		this.eventTypeList = eventTypeList;
		return this;
	}

	public Systems setEventTypeList1(List<EventType> eventTypeList1)
	{
		this.eventTypeList1 = eventTypeList1;
		return this;
	}

	public Systems setResourceItemDataList(List<ResourceItemData> resourceItemDataList)
	{
		this.resourceItemDataList = resourceItemDataList;
		return this;
	}

	public Systems setResourceItemDataList1(List<ResourceItemData> resourceItemDataList1)
	{
		this.resourceItemDataList1 = resourceItemDataList1;
		return this;
	}

	public Systems setAddressXResourceItemList(List<AddressXResourceItem> addressXResourceItemList)
	{
		this.addressXResourceItemList = addressXResourceItemList;
		return this;
	}

	public Systems setAddressXResourceItemList1(List<AddressXResourceItem> addressXResourceItemList1)
	{
		this.addressXResourceItemList1 = addressXResourceItemList1;
		return this;
	}

	public Systems setArrangementTypeSecurityTokenList(List<ArrangementTypeSecurityToken> arrangementTypeSecurityTokenList)
	{
		this.arrangementTypeSecurityTokenList = arrangementTypeSecurityTokenList;
		return this;
	}

	public Systems setArrangementTypeSecurityTokenList1(List<ArrangementTypeSecurityToken> arrangementTypeSecurityTokenList1)
	{
		this.arrangementTypeSecurityTokenList1 = arrangementTypeSecurityTokenList1;
		return this;
	}

	public Systems setInvolvedPartyOrganicSecurityTokenList(List<InvolvedPartyOrganicSecurityToken> involvedPartyOrganicSecurityTokenList)
	{
		this.involvedPartyOrganicSecurityTokenList = involvedPartyOrganicSecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartyOrganicSecurityTokenList1(List<InvolvedPartyOrganicSecurityToken> involvedPartyOrganicSecurityTokenList1)
	{
		this.involvedPartyOrganicSecurityTokenList1 = involvedPartyOrganicSecurityTokenList1;
		return this;
	}

	public Systems setArrangementXArrangementList(List<ArrangementXArrangement> arrangementXArrangementList)
	{
		this.arrangementXArrangementList = arrangementXArrangementList;
		return this;
	}

	public Systems setArrangementXArrangementList1(List<ArrangementXArrangement> arrangementXArrangementList1)
	{
		this.arrangementXArrangementList1 = arrangementXArrangementList1;
		return this;
	}

	public Systems setClassificationDataConceptXClassificationList(List<ClassificationDataConceptXClassification> classificationDataConceptXClassificationList)
	{
		this.classificationDataConceptXClassificationList = classificationDataConceptXClassificationList;
		return this;
	}

	public Systems setArrangementXProductList(List<ArrangementXProduct> arrangementXProductList)
	{
		this.arrangementXProductList = arrangementXProductList;
		return this;
	}

	public Systems setArrangementXProductList1(List<ArrangementXProduct> arrangementXProductList1)
	{
		this.arrangementXProductList1 = arrangementXProductList1;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartyList(List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList)
	{
		this.involvedPartyXInvolvedPartyList = involvedPartyXInvolvedPartyList;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartyList1(List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList1)
	{
		this.involvedPartyXInvolvedPartyList1 = involvedPartyXInvolvedPartyList1;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartyTypeList(List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList)
	{
		this.involvedPartyXInvolvedPartyTypeList = involvedPartyXInvolvedPartyTypeList;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartyTypeList1(List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList1)
	{
		this.involvedPartyXInvolvedPartyTypeList1 = involvedPartyXInvolvedPartyTypeList1;
		return this;
	}

	public Systems setArrangementSecurityTokenList(List<ArrangementSecurityToken> arrangementSecurityTokenList)
	{
		this.arrangementSecurityTokenList = arrangementSecurityTokenList;
		return this;
	}

	public Systems setArrangementSecurityTokenList1(List<ArrangementSecurityToken> arrangementSecurityTokenList1)
	{
		this.arrangementSecurityTokenList1 = arrangementSecurityTokenList1;
		return this;
	}

	public Systems setInvolvedPartyOrganicTypeList(List<InvolvedPartyOrganicType> involvedPartyOrganicTypeList)
	{
		this.involvedPartyOrganicTypeList = involvedPartyOrganicTypeList;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList(List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList)
	{
		this.involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList = involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList1(List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList1)
	{
		this.involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList1 = involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList1;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartyTypeSecurityTokenList(List<InvolvedPartyXInvolvedPartyTypeSecurityToken> involvedPartyXInvolvedPartyTypeSecurityTokenList)
	{
		this.involvedPartyXInvolvedPartyTypeSecurityTokenList = involvedPartyXInvolvedPartyTypeSecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartyTypeSecurityTokenList1(List<InvolvedPartyXInvolvedPartyTypeSecurityToken> involvedPartyXInvolvedPartyTypeSecurityTokenList1)
	{
		this.involvedPartyXInvolvedPartyTypeSecurityTokenList1 = involvedPartyXInvolvedPartyTypeSecurityTokenList1;
		return this;
	}

	public Systems setClassificationDataConceptSecurityTokenList(List<ClassificationDataConceptSecurityToken> classificationDataConceptSecurityTokenList)
	{
		this.classificationDataConceptSecurityTokenList = classificationDataConceptSecurityTokenList;
		return this;
	}

	public Systems setClassificationDataConceptSecurityTokenList1(List<ClassificationDataConceptSecurityToken> classificationDataConceptSecurityTokenList1)
	{
		this.classificationDataConceptSecurityTokenList1 = classificationDataConceptSecurityTokenList1;
		return this;
	}

	public Systems setEventXResourceItemList(List<EventXResourceItem> eventXResourceItemList)
	{
		this.eventXResourceItemList = eventXResourceItemList;
		return this;
	}

	public Systems setEventXResourceItemList1(List<EventXResourceItem> eventXResourceItemList1)
	{
		this.eventXResourceItemList1 = eventXResourceItemList1;
		return this;
	}

	public Systems setInvolvedPartyXClassificationList(List<InvolvedPartyXClassification> involvedPartyXClassificationList)
	{
		this.involvedPartyXClassificationList = involvedPartyXClassificationList;
		return this;
	}

	public Systems setInvolvedPartyXClassificationList1(List<InvolvedPartyXClassification> involvedPartyXClassificationList1)
	{
		this.involvedPartyXClassificationList1 = involvedPartyXClassificationList1;
		return this;
	}

	public Systems setArrangementTypeList(List<ArrangementType> arrangementTypeList)
	{
		this.arrangementTypeList = arrangementTypeList;
		return this;
	}

	public Systems setArrangementTypeList1(List<ArrangementType> arrangementTypeList1)
	{
		this.arrangementTypeList1 = arrangementTypeList1;
		return this;
	}

	public Systems setInvolvedPartyTypeSecurityTokenList(List<InvolvedPartyTypeSecurityToken> involvedPartyTypeSecurityTokenList)
	{
		this.involvedPartyTypeSecurityTokenList = involvedPartyTypeSecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartyTypeSecurityTokenList1(List<InvolvedPartyTypeSecurityToken> involvedPartyTypeSecurityTokenList1)
	{
		this.involvedPartyTypeSecurityTokenList1 = involvedPartyTypeSecurityTokenList1;
		return this;
	}

	public Systems setSecurityTokenXSecurityTokenList(List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenList)
	{
		this.securityTokenXSecurityTokenList = securityTokenXSecurityTokenList;
		return this;
	}

	public Systems setSecurityTokenXSecurityTokenList1(List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenList1)
	{
		this.securityTokenXSecurityTokenList1 = securityTokenXSecurityTokenList1;
		return this;
	}

	public Systems setProductXResourceItemList(List<ProductXResourceItem> productXResourceItemList)
	{
		this.productXResourceItemList = productXResourceItemList;
		return this;
	}

	public Systems setProductXResourceItemList1(List<ProductXResourceItem> productXResourceItemList1)
	{
		this.productXResourceItemList1 = productXResourceItemList1;
		return this;
	}

	public Systems setInvolvedPartyXClassificationSecurityTokenList(List<InvolvedPartyXClassificationSecurityToken> involvedPartyXClassificationSecurityTokenList)
	{
		this.involvedPartyXClassificationSecurityTokenList = involvedPartyXClassificationSecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartyXClassificationSecurityTokenList1(List<InvolvedPartyXClassificationSecurityToken> involvedPartyXClassificationSecurityTokenList1)
	{
		this.involvedPartyXClassificationSecurityTokenList1 = involvedPartyXClassificationSecurityTokenList1;
		return this;
	}

	public Systems setEventXAddressSecurityTokenList(List<EventXAddressSecurityToken> eventXAddressSecurityTokenList)
	{
		this.eventXAddressSecurityTokenList = eventXAddressSecurityTokenList;
		return this;
	}

	public Systems setEventXAddressSecurityTokenList1(List<EventXAddressSecurityToken> eventXAddressSecurityTokenList1)
	{
		this.eventXAddressSecurityTokenList1 = eventXAddressSecurityTokenList1;
		return this;
	}

	public Systems setResourceItemList(List<ResourceItem> resourceItemList)
	{
		this.resourceItemList = resourceItemList;
		return this;
	}

	public Systems setResourceItemList1(List<ResourceItem> resourceItemList1)
	{
		this.resourceItemList1 = resourceItemList1;
		return this;
	}

	public Systems setEventXAddressList(List<EventXAddress> eventXAddressList)
	{
		this.eventXAddressList = eventXAddressList;
		return this;
	}

	public Systems setEventXAddressList1(List<EventXAddress> eventXAddressList1)
	{
		this.eventXAddressList1 = eventXAddressList1;
		return this;
	}

	public Systems setClassificationDataConceptXResourceItemList(List<ClassificationDataConceptXResourceItem> classificationDataConceptXResourceItemList)
	{
		this.classificationDataConceptXResourceItemList = classificationDataConceptXResourceItemList;
		return this;
	}

	public Systems setInvolvedPartyXProductList(List<InvolvedPartyXProduct> involvedPartyXProductList)
	{
		this.involvedPartyXProductList = involvedPartyXProductList;
		return this;
	}

	public Systems setInvolvedPartyXProductList1(List<InvolvedPartyXProduct> involvedPartyXProductList1)
	{
		this.involvedPartyXProductList1 = involvedPartyXProductList1;
		return this;
	}

	public Systems setEventSecurityTokenList(List<EventSecurityToken> eventSecurityTokenList)
	{
		this.eventSecurityTokenList = eventSecurityTokenList;
		return this;
	}

	public Systems setEventSecurityTokenList1(List<EventSecurityToken> eventSecurityTokenList1)
	{
		this.eventSecurityTokenList1 = eventSecurityTokenList1;
		return this;
	}

	public Systems setResourceItemDataXClassificationList(List<ResourceItemDataXClassification> resourceItemDataXClassificationList)
	{
		this.resourceItemDataXClassificationList = resourceItemDataXClassificationList;
		return this;
	}

	public Systems setResourceItemDataXClassificationList1(List<ResourceItemDataXClassification> resourceItemDataXClassificationList1)
	{
		this.resourceItemDataXClassificationList1 = resourceItemDataXClassificationList1;
		return this;
	}

	public Systems setSecurityTokenAccessList(List<SecurityTokensSecurityToken> securityTokenAccessList)
	{
		this.securityTokenAccessList = securityTokenAccessList;
		return this;
	}

	public Systems setSecurityTokenAccessList1(List<SecurityTokensSecurityToken> securityTokenAccessList1)
	{
		this.securityTokenAccessList1 = securityTokenAccessList1;
		return this;
	}

	public Systems setInvolvedPartyXAddressList(List<InvolvedPartyXAddress> involvedPartyXAddressList)
	{
		this.involvedPartyXAddressList = involvedPartyXAddressList;
		return this;
	}

	public Systems setInvolvedPartyXAddressList1(List<InvolvedPartyXAddress> involvedPartyXAddressList1)
	{
		this.involvedPartyXAddressList1 = involvedPartyXAddressList1;
		return this;
	}

	public Systems setGeographyXClassificationSecurityTokenList(List<GeographyXClassificationSecurityToken> geographyXClassificationSecurityTokenList)
	{
		this.geographyXClassificationSecurityTokenList = geographyXClassificationSecurityTokenList;
		return this;
	}

	public Systems setGeographyXClassificationSecurityTokenList1(List<GeographyXClassificationSecurityToken> geographyXClassificationSecurityTokenList1)
	{
		this.geographyXClassificationSecurityTokenList1 = geographyXClassificationSecurityTokenList1;
		return this;
	}

	public Systems setGeographyXGeographySecurityTokenList(List<GeographyXGeographySecurityToken> geographyXGeographySecurityTokenList)
	{
		this.geographyXGeographySecurityTokenList = geographyXGeographySecurityTokenList;
		return this;
	}

	public Systems setGeographyXGeographySecurityTokenList1(List<GeographyXGeographySecurityToken> geographyXGeographySecurityTokenList1)
	{
		this.geographyXGeographySecurityTokenList1 = geographyXGeographySecurityTokenList1;
		return this;
	}

	public Systems setArrangementXArrangementSecurityTokenList(List<ArrangementXArrangementSecurityToken> arrangementXArrangementSecurityTokenList)
	{
		this.arrangementXArrangementSecurityTokenList = arrangementXArrangementSecurityTokenList;
		return this;
	}

	public Systems setArrangementXArrangementSecurityTokenList1(List<ArrangementXArrangementSecurityToken> arrangementXArrangementSecurityTokenList1)
	{
		this.arrangementXArrangementSecurityTokenList1 = arrangementXArrangementSecurityTokenList1;
		return this;
	}

	public Systems setGeographySecurityTokenList(List<GeographySecurityToken> geographySecurityTokenList)
	{
		this.geographySecurityTokenList = geographySecurityTokenList;
		return this;
	}

	public Systems setGeographySecurityTokenList1(List<GeographySecurityToken> geographySecurityTokenList1)
	{
		this.geographySecurityTokenList1 = geographySecurityTokenList1;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartyIdentificationTypeList(List<InvolvedPartyXInvolvedPartyIdentificationType> involvedPartyXInvolvedPartyIdentificationTypeList)
	{
		this.involvedPartyXInvolvedPartyIdentificationTypeList = involvedPartyXInvolvedPartyIdentificationTypeList;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartyIdentificationTypeList1(List<InvolvedPartyXInvolvedPartyIdentificationType> involvedPartyXInvolvedPartyIdentificationTypeList1)
	{
		this.involvedPartyXInvolvedPartyIdentificationTypeList1 = involvedPartyXInvolvedPartyIdentificationTypeList1;
		return this;
	}

	public Systems setInvolvedPartyIdentificationTypeList(List<InvolvedPartyIdentificationType> involvedPartyIdentificationTypeList)
	{
		this.involvedPartyIdentificationTypeList = involvedPartyIdentificationTypeList;
		return this;
	}

	public Systems setInvolvedPartyIdentificationTypeList1(List<InvolvedPartyIdentificationType> involvedPartyIdentificationTypeList1)
	{
		this.involvedPartyIdentificationTypeList1 = involvedPartyIdentificationTypeList1;
		return this;
	}

	public Systems setClassificationXResourceItemList(List<ClassificationXResourceItem> classificationXResourceItemList)
	{
		this.classificationXResourceItemList = classificationXResourceItemList;
		return this;
	}

	public Systems setClassificationXResourceItemList1(List<ClassificationXResourceItem> classificationXResourceItemList1)
	{
		this.classificationXResourceItemList1 = classificationXResourceItemList1;
		return this;
	}

	public Systems setEventXProductList(List<EventXProduct> eventXProductList)
	{
		this.eventXProductList = eventXProductList;
		return this;
	}

	public Systems setEventXProductList1(List<EventXProduct> eventXProductList1)
	{
		this.eventXProductList1 = eventXProductList1;
		return this;
	}

	public Systems setEventXEventTypeList(List<EventXEventType> eventXEventTypeList)
	{
		this.eventXEventTypeList = eventXEventTypeList;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartySecurityTokenList(List<InvolvedPartyXInvolvedPartySecurityToken> involvedPartyXInvolvedPartySecurityTokenList)
	{
		this.involvedPartyXInvolvedPartySecurityTokenList = involvedPartyXInvolvedPartySecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartyXInvolvedPartySecurityTokenList1(List<InvolvedPartyXInvolvedPartySecurityToken> involvedPartyXInvolvedPartySecurityTokenList1)
	{
		this.involvedPartyXInvolvedPartySecurityTokenList1 = involvedPartyXInvolvedPartySecurityTokenList1;
		return this;
	}

	public Systems setProductXProductList(List<ProductXProduct> productXProductList)
	{
		this.productXProductList = productXProductList;
		return this;
	}

	public Systems setProductXProductList1(List<ProductXProduct> productXProductList1)
	{
		this.productXProductList1 = productXProductList1;
		return this;
	}

	public Systems setInvolvedPartyTypeList(List<InvolvedPartyType> involvedPartyTypeList)
	{
		this.involvedPartyTypeList = involvedPartyTypeList;
		return this;
	}

	public Systems setInvolvedPartyTypeList1(List<InvolvedPartyType> involvedPartyTypeList1)
	{
		this.involvedPartyTypeList1 = involvedPartyTypeList1;
		return this;
	}

	public Systems setArrangementXClassificationSecurityTokenList(List<ArrangementXClassificationSecurityToken> arrangementXClassificationSecurityTokenList)
	{
		this.arrangementXClassificationSecurityTokenList = arrangementXClassificationSecurityTokenList;
		return this;
	}

	public Systems setArrangementXClassificationSecurityTokenList1(List<ArrangementXClassificationSecurityToken> arrangementXClassificationSecurityTokenList1)
	{
		this.arrangementXClassificationSecurityTokenList1 = arrangementXClassificationSecurityTokenList1;
		return this;
	}

	public Systems setInvolvedPartyXAddressSecurityTokenList(List<InvolvedPartyXAddressSecurityToken> involvedPartyXAddressSecurityTokenList)
	{
		this.involvedPartyXAddressSecurityTokenList = involvedPartyXAddressSecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartyXAddressSecurityTokenList1(List<InvolvedPartyXAddressSecurityToken> involvedPartyXAddressSecurityTokenList1)
	{
		this.involvedPartyXAddressSecurityTokenList1 = involvedPartyXAddressSecurityTokenList1;
		return this;
	}

	public Systems setGeographyXClassificationList(List<GeographyXClassification> geographyXClassificationList)
	{
		this.geographyXClassificationList = geographyXClassificationList;
		return this;
	}

	public Systems setGeographyXClassificationList1(List<GeographyXClassification> geographyXClassificationList1)
	{
		this.geographyXClassificationList1 = geographyXClassificationList1;
		return this;
	}

	public Systems setAddressXGeographySecurityTokenList(List<AddressXGeographySecurityToken> addressXGeographySecurityTokenList)
	{
		this.addressXGeographySecurityTokenList = addressXGeographySecurityTokenList;
		return this;
	}

	public Systems setAddressXGeographySecurityTokenList1(List<AddressXGeographySecurityToken> addressXGeographySecurityTokenList1)
	{
		this.addressXGeographySecurityTokenList1 = addressXGeographySecurityTokenList1;
		return this;
	}

	public Systems setInvolvedPartyNonOrganicSecurityTokenList(List<InvolvedPartyNonOrganicSecurityToken> involvedPartyNonOrganicSecurityTokenList)
	{
		this.involvedPartyNonOrganicSecurityTokenList = involvedPartyNonOrganicSecurityTokenList;
		return this;
	}

	public Systems setInvolvedPartyNonOrganicSecurityTokenList1(List<InvolvedPartyNonOrganicSecurityToken> involvedPartyNonOrganicSecurityTokenList1)
	{
		this.involvedPartyNonOrganicSecurityTokenList1 = involvedPartyNonOrganicSecurityTokenList1;
		return this;
	}

	public Systems setEventXClassificationList(List<EventXClassification> eventXClassificationList)
	{
		this.eventXClassificationList = eventXClassificationList;
		return this;
	}

	public Systems setEventXClassificationList1(List<EventXClassification> eventXClassificationList1)
	{
		this.eventXClassificationList1 = eventXClassificationList1;
		return this;
	}

	public Systems setArrangementList(List<Arrangement> arrangementList)
	{
		this.arrangementList = arrangementList;
		return this;
	}

	public Systems setArrangementList1(List<Arrangement> arrangementList1)
	{
		this.arrangementList1 = arrangementList1;
		return this;
	}

	public Systems setResourceItemSecurityTokenList(List<ResourceItemSecurityToken> resourceItemSecurityTokenList)
	{
		this.resourceItemSecurityTokenList = resourceItemSecurityTokenList;
		return this;
	}

	public Systems setResourceItemSecurityTokenList1(List<ResourceItemSecurityToken> resourceItemSecurityTokenList1)
	{
		this.resourceItemSecurityTokenList1 = resourceItemSecurityTokenList1;
		return this;
	}

	public Systems setGeographyXResourceItemList(List<GeographyXResourceItem> geographyXResourceItemList)
	{
		this.geographyXResourceItemList = geographyXResourceItemList;
		return this;
	}

	public Systems setGeographyXResourceItemList1(List<GeographyXResourceItem> geographyXResourceItemList1)
	{
		this.geographyXResourceItemList1 = geographyXResourceItemList1;
		return this;
	}

	public Systems setClassificationXClassificationSecurityTokenList(List<ClassificationXClassificationSecurityToken> classificationXClassificationSecurityTokenList)
	{
		this.classificationXClassificationSecurityTokenList = classificationXClassificationSecurityTokenList;
		return this;
	}

	public Systems setClassificationXClassificationSecurityTokenList1(List<ClassificationXClassificationSecurityToken> classificationXClassificationSecurityTokenList1)
	{
		this.classificationXClassificationSecurityTokenList1 = classificationXClassificationSecurityTokenList1;
		return this;
	}

	public Systems setEventTypesSecurityTokenList(List<EventTypesSecurityToken> eventTypesSecurityTokenList)
	{
		this.eventTypesSecurityTokenList = eventTypesSecurityTokenList;
		return this;
	}

	public Systems setEventTypesSecurityTokenList1(List<EventTypesSecurityToken> eventTypesSecurityTokenList1)
	{
		this.eventTypesSecurityTokenList1 = eventTypesSecurityTokenList1;
		return this;
	}

	public Systems setYesNoXClassificationList(List<YesNoXClassification> yesNoXClassificationList)
	{
		this.yesNoXClassificationList = yesNoXClassificationList;
		return this;
	}

	public Systems setGeographyXGeographyList(List<GeographyXGeography> geographyXGeographyList)
	{
		this.geographyXGeographyList = geographyXGeographyList;
		return this;
	}

	public Systems setGeographyXGeographyList1(List<GeographyXGeography> geographyXGeographyList1)
	{
		this.geographyXGeographyList1 = geographyXGeographyList1;
		return this;
	}

	public Systems setClassificationSecurityTokenList(List<ClassificationSecurityToken> classificationSecurityTokenList)
	{
		this.classificationSecurityTokenList = classificationSecurityTokenList;
		return this;
	}

	public Systems setClassificationSecurityTokenList1(List<ClassificationSecurityToken> classificationSecurityTokenList1)
	{
		this.classificationSecurityTokenList1 = classificationSecurityTokenList1;
		return this;
	}

	public Systems setYesNoList(List<YesNo> yesNoList)
	{
		this.yesNoList = yesNoList;
		return this;
	}

	public Systems setYesNoList1(List<YesNo> yesNoList1)
	{
		this.yesNoList1 = yesNoList1;
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
		Systems systems = (Systems) o;
		return Objects.equals(getName(), systems.getName());
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
			max = 150) String getName()
	{
		return this.name;
	}

	public @NotNull @Size() String getDescription()
	{
		return this.description;
	}

	public @NotNull @Size(min = 1,
			max = 250) String getSystemHistoryName()
	{
		return this.systemHistoryName;
	}

	public Enterprise getEnterpriseID()
	{
		return this.enterpriseID;
	}

	public ActiveFlag getActiveFlagID()
	{
		return this.activeFlagID;
	}

	public Systems setId(Long id)
	{
		this.id = id;
		return this;
	}

	public Systems setName(@NotNull @Size(min = 1,
			max = 150) String name)
	{
		this.name = name;
		return this;
	}

	public Systems setDescription(@NotNull @Size() String description)
	{
		this.description = description;
		return this;
	}

	public Systems setSystemHistoryName(@NotNull @Size(min = 1,
			max = 250) String systemHistoryName)
	{
		this.systemHistoryName = systemHistoryName;
		return this;
	}

	public Systems setEnterpriseID(Enterprise enterpriseID)
	{
		this.enterpriseID = enterpriseID;
		return this;
	}

	public Systems setActiveFlagID(ActiveFlag activeFlagID)
	{
		this.activeFlagID = activeFlagID;
		return this;
	}
}
