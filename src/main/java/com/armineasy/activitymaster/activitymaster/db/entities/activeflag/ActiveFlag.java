package com.armineasy.activitymaster.activitymaster.db.entities.activeflag;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.builders.ActiveFlagQueryBuilder;
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
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemsSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNo;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNoXClassification;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsEnterprise;
import com.armineasy.activitymaster.activitymaster.services.capabilities.INameAndDescription;
import com.armineasy.activitymaster.activitymaster.services.classifications.activeflag.IActiveFlagClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IActiveFlag;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings({"Duplicates", "unused"})
@Entity
@Table(name = "ActiveFlag")
@XmlRootElement

@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class ActiveFlag
		extends WarehouseNameDescriptionTable<ActiveFlag, ActiveFlagQueryBuilder, Long, ActiveFlagSecurityToken>
		implements INameAndDescription<ActiveFlag>,
				           IContainsClassifications<ActiveFlag, Classification, ActiveFlagXClassification, IActiveFlagClassification<?>,IActiveFlag<?>, IClassification<?>,  IActiveFlag<ActiveFlag>>,
				           IActivityMasterEntity<ActiveFlag>,
				           IContainsEnterprise<ActiveFlag>,
				           IActiveFlag<ActiveFlag>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ActiveFlagID")
	private Long id;

	@JoinColumn(name = "EnterpriseID",
			referencedColumnName = "EnterpriseID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Enterprise enterpriseID;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 100)
	@Column(nullable = false,
			length = 100,
			name = "ActiveFlagName")
	private String name;

	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@NotNull
	@Size(min = 1,
			max = 100)
	@Column(nullable = false,
			length = 100,
			name = "ActiveFlagDescription")
	private String description;

	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@NotNull
	@Column(nullable = false,
			name = "AllowAccess")
	private boolean allowAccess;

	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyNameType> involvedPartyXInvolvedPartyNameTypeList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProductSecurityToken> involvedPartyXProductSecurityTokenList;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ActiveFlagSecurityToken> securities;

	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<Address> addressList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ProductXResourceItemSecurityToken> productXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<Product> productList;

	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ResourceItemXClassificationSecurityToken> resourceItemXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXResourceItem> involvedPartyXResourceItemList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNameType> involvedPartyNameTypeList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventXClassificationSecurityToken> eventXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ProductSecurityToken> productSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedParty> involvedPartyList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementType> arrangementXArrangementTypeList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> involvedPartyXInvolvedPartyNameTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<Classification> classificationList;

	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventXEventTypeSecurityToken> eventXEventTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventXProductSecurityToken> eventXProductSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNameTypeSecurityToken> involvedPartyNameTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ClassificationXResourceItemSecurityToken> classificationXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventXResourceItemSecurityToken> eventXResourceItemSecurityTokenList;

	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<AddressSecurityToken> addressSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<SystemsSecurityToken> systemSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassification> classificationXClassificationList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConcept> classificationDataConceptList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<AddressXGeography> addressXGeographyList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXClassification> securityTokenXClassificationList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventXArrangementsSecurityToken> eventXArrangementsSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXResourceItemSecurityToken> involvedPartyXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ProductXClassification> productXClassificationList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataSecurityToken> resourceItemDataSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ArrangementXInvolvedParty> arrangementXInvolvedPartyList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItem> arrangementXResourceItemList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ProductXProductSecurityToken> productXProductSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ResourceItemXClassification> resourceItemXClassificationList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ArrangementXInvolvedPartySecurityToken> arrangementXInvolvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNonOrganic> involvedPartyNonOrganicList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventXInvolvedParty> eventXInvolvedPartyList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ActiveFlagXClassification> classifications;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<GeographyXResourceItemSecurityToken> geographyXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EnterpriseXClassification> enterpriseXClassificationList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventXArrangement> eventXArrangementList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyIdentificationTypeSecurityToken> involvedPartyIdentificationTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<AddressXResourceItemSecurityToken> addressXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartySecurityToken> involvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ProductXClassificationSecurityToken> productXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<SecurityToken> securityTokenList;

	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementTypeSecurityToken> arrangementXArrangementTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventXInvolvedPartySecurityToken> eventXInvolvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganic> involvedPartyOrganicList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<SystemXClassification> systemXClassificationList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<Event> eventList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXClassificationSecurityToken> classificationDataConceptXClassificationSecurityTokenList;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ActiveFlagSecurityToken> activeFlagSecurityTokenList;

	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ActiveFlagSecurityToken> activeFlagSecurityTokenList1;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ArrangementXClassification> arrangementXClassificationList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<Geography> geographyList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicTypeSecurityToken> involvedPartyOrganicTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventType> eventTypeList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ResourceItemData> resourceItemDataList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<AddressXResourceItem> addressXResourceItemList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ArrangementTypeSecurityToken> arrangementTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicSecurityToken> involvedPartyOrganicSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangement> arrangementXArrangementList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXClassification> classificationDataConceptXClassificationList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ArrangementXProduct> arrangementXProductList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList;

	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ArrangementSecurityToken> arrangementSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicType> involvedPartyOrganicTypeList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyTypeSecurityToken> involvedPartyXInvolvedPartyTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptSecurityToken> classificationDataConceptSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventXResourceItem> eventXResourceItemList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXClassification> involvedPartyXClassificationList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ArrangementType> arrangementTypeList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<Systems> systemsList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyTypeSecurityToken> involvedPartyTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ProductXResourceItem> productXResourceItemList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXClassificationSecurityToken> involvedPartyXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventXAddressSecurityToken> eventXAddressSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ResourceItem> resourceItemList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventXAddress> eventXAddressList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXResourceItem> classificationDataConceptXResourceItemList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProduct> involvedPartyXProductList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventSecurityToken> eventSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataXClassification> resourceItemDataXClassificationList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<SecurityTokensSecurityToken> securityTokenAccessList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddress> involvedPartyXAddressList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<GeographyXClassificationSecurityToken> geographyXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<GeographyXGeographySecurityToken> geographyXGeographySecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementSecurityToken> arrangementXArrangementSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<GeographySecurityToken> geographySecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyIdentificationType> involvedPartyXInvolvedPartyIdentificationTypeList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyIdentificationType> involvedPartyIdentificationTypeList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ClassificationXResourceItem> classificationXResourceItemList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventXProduct> eventXProductList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventXEventType> eventXEventTypeList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartySecurityToken> involvedPartyXInvolvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ProductXProduct> productXProductList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyType> involvedPartyTypeList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ArrangementXClassificationSecurityToken> arrangementXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddressSecurityToken> involvedPartyXAddressSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<GeographyXClassification> geographyXClassificationList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<AddressXGeographySecurityToken> addressXGeographySecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNonOrganicSecurityToken> involvedPartyNonOrganicSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventXClassification> eventXClassificationList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<Arrangement> arrangementList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ResourceItemSecurityToken> resourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<GeographyXResourceItem> geographyXResourceItemList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassificationSecurityToken> classificationXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<EventTypesSecurityToken> eventTypesSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<YesNoXClassification> yesNoXClassificationList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<GeographyXGeography> geographyXGeographyList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<ClassificationSecurityToken> classificationSecurityTokenList;
	@OneToMany(
			mappedBy = "activeFlagID",
			fetch = FetchType.LAZY)
	private List<YesNo> yesNoList;

	public ActiveFlag()
	{

	}

	public ActiveFlag(Long id)
	{
		this.id = id;
	}

	public ActiveFlag(Long id, String activeFlagName, boolean allowAccess)
	{
		this.id = id;
		this.name = activeFlagName;
		this.allowAccess = allowAccess;
	}


	@SuppressWarnings("unchecked")
	public ActiveFlag remove()
	{
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return this;
	}


	@SuppressWarnings("unchecked")
	public ActiveFlag archive()
	{
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return this;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	@NotNull
	@SuppressWarnings("unchecked")
	protected Class<ActiveFlagSecurityToken> findPersistentSecurityClass()
	{
		return (Class<ActiveFlagSecurityToken>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
	}

	@Override
	protected ActiveFlagSecurityToken configureDefaultsForNewToken(ActiveFlagSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		stAdmin.setSystemID((Systems) activityMasterSystem);
		stAdmin.setActiveFlagID(((Systems) activityMasterSystem).getActiveFlagID());
		stAdmin.setBase(this);
		stAdmin.setOriginalSourceSystemID((Systems) activityMasterSystem);
		stAdmin.setOriginalSourceSystemUniqueID("");
		stAdmin.setEnterpriseID((Enterprise) enterprise);
		return stAdmin;
	}

	@Override
	public void configureForClassification(ActiveFlagXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setActiveFlagID(this);
	}

	public List<InvolvedPartyXInvolvedPartyNameType> getInvolvedPartyXInvolvedPartyNameTypeList()
	{
		return this.involvedPartyXInvolvedPartyNameTypeList;
	}

	public List<InvolvedPartyXProductSecurityToken> getInvolvedPartyXProductSecurityTokenList()
	{
		return this.involvedPartyXProductSecurityTokenList;
	}

	public List<ActiveFlagSecurityToken> getSecurities()
	{
		return this.securities;
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

	public List<ActiveFlagXClassification> getClassifications()
	{
		return this.classifications;
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

	public List<ActiveFlagSecurityToken> getActiveFlagSecurityTokenList1()
	{
		return this.activeFlagSecurityTokenList1;
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

	public List<YesNoXClassification> getYesNoXClassificationList()
	{
		return this.yesNoXClassificationList;
	}

	public List<GeographyXGeography> getGeographyXGeographyList()
	{
		return this.geographyXGeographyList;
	}

	public List<ClassificationSecurityToken> getClassificationSecurityTokenList()
	{
		return this.classificationSecurityTokenList;
	}

	public List<YesNo> getYesNoList()
	{
		return this.yesNoList;
	}

	public ActiveFlag setInvolvedPartyXInvolvedPartyNameTypeList(List<InvolvedPartyXInvolvedPartyNameType> involvedPartyXInvolvedPartyNameTypeList)
	{
		this.involvedPartyXInvolvedPartyNameTypeList = involvedPartyXInvolvedPartyNameTypeList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXProductSecurityTokenList(List<InvolvedPartyXProductSecurityToken> involvedPartyXProductSecurityTokenList)
	{
		this.involvedPartyXProductSecurityTokenList = involvedPartyXProductSecurityTokenList;
		return this;
	}

	public ActiveFlag setSecurities(List<ActiveFlagSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public ActiveFlag setAddressList(List<Address> addressList)
	{
		this.addressList = addressList;
		return this;
	}

	public ActiveFlag setProductXResourceItemSecurityTokenList(List<ProductXResourceItemSecurityToken> productXResourceItemSecurityTokenList)
	{
		this.productXResourceItemSecurityTokenList = productXResourceItemSecurityTokenList;
		return this;
	}

	public ActiveFlag setProductList(List<Product> productList)
	{
		this.productList = productList;
		return this;
	}

	public ActiveFlag setResourceItemXClassificationSecurityTokenList(List<ResourceItemXClassificationSecurityToken> resourceItemXClassificationSecurityTokenList)
	{
		this.resourceItemXClassificationSecurityTokenList = resourceItemXClassificationSecurityTokenList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXResourceItemList(List<InvolvedPartyXResourceItem> involvedPartyXResourceItemList)
	{
		this.involvedPartyXResourceItemList = involvedPartyXResourceItemList;
		return this;
	}

	public ActiveFlag setInvolvedPartyNameTypeList(List<InvolvedPartyNameType> involvedPartyNameTypeList)
	{
		this.involvedPartyNameTypeList = involvedPartyNameTypeList;
		return this;
	}

	public ActiveFlag setEventXClassificationSecurityTokenList(List<EventXClassificationSecurityToken> eventXClassificationSecurityTokenList)
	{
		this.eventXClassificationSecurityTokenList = eventXClassificationSecurityTokenList;
		return this;
	}

	public ActiveFlag setProductSecurityTokenList(List<ProductSecurityToken> productSecurityTokenList)
	{
		this.productSecurityTokenList = productSecurityTokenList;
		return this;
	}

	public ActiveFlag setInvolvedPartyList(List<InvolvedParty> involvedPartyList)
	{
		this.involvedPartyList = involvedPartyList;
		return this;
	}

	public ActiveFlag setArrangementXArrangementTypeList(List<ArrangementXArrangementType> arrangementXArrangementTypeList)
	{
		this.arrangementXArrangementTypeList = arrangementXArrangementTypeList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXInvolvedPartyNameTypeSecurityTokenList(List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> involvedPartyXInvolvedPartyNameTypeSecurityTokenList)
	{
		this.involvedPartyXInvolvedPartyNameTypeSecurityTokenList = involvedPartyXInvolvedPartyNameTypeSecurityTokenList;
		return this;
	}

	public ActiveFlag setClassificationList(List<Classification> classificationList)
	{
		this.classificationList = classificationList;
		return this;
	}

	public ActiveFlag setEventXEventTypeSecurityTokenList(List<EventXEventTypeSecurityToken> eventXEventTypeSecurityTokenList)
	{
		this.eventXEventTypeSecurityTokenList = eventXEventTypeSecurityTokenList;
		return this;
	}

	public ActiveFlag setArrangementXResourceItemSecurityTokenList(List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList)
	{
		this.arrangementXResourceItemSecurityTokenList = arrangementXResourceItemSecurityTokenList;
		return this;
	}

	public ActiveFlag setEventXProductSecurityTokenList(List<EventXProductSecurityToken> eventXProductSecurityTokenList)
	{
		this.eventXProductSecurityTokenList = eventXProductSecurityTokenList;
		return this;
	}

	public ActiveFlag setInvolvedPartyNameTypeSecurityTokenList(List<InvolvedPartyNameTypeSecurityToken> involvedPartyNameTypeSecurityTokenList)
	{
		this.involvedPartyNameTypeSecurityTokenList = involvedPartyNameTypeSecurityTokenList;
		return this;
	}

	public ActiveFlag setClassificationXResourceItemSecurityTokenList(List<ClassificationXResourceItemSecurityToken> classificationXResourceItemSecurityTokenList)
	{
		this.classificationXResourceItemSecurityTokenList = classificationXResourceItemSecurityTokenList;
		return this;
	}

	public ActiveFlag setEventXResourceItemSecurityTokenList(List<EventXResourceItemSecurityToken> eventXResourceItemSecurityTokenList)
	{
		this.eventXResourceItemSecurityTokenList = eventXResourceItemSecurityTokenList;
		return this;
	}

	public ActiveFlag setAddressSecurityTokenList(List<AddressSecurityToken> addressSecurityTokenList)
	{
		this.addressSecurityTokenList = addressSecurityTokenList;
		return this;
	}

	public ActiveFlag setSystemSecurityTokenList(List<SystemsSecurityToken> systemSecurityTokenList)
	{
		this.systemSecurityTokenList = systemSecurityTokenList;
		return this;
	}

	public ActiveFlag setClassificationXClassificationList(List<ClassificationXClassification> classificationXClassificationList)
	{
		this.classificationXClassificationList = classificationXClassificationList;
		return this;
	}

	public ActiveFlag setClassificationDataConceptList(List<ClassificationDataConcept> classificationDataConceptList)
	{
		this.classificationDataConceptList = classificationDataConceptList;
		return this;
	}

	public ActiveFlag setAddressXGeographyList(List<AddressXGeography> addressXGeographyList)
	{
		this.addressXGeographyList = addressXGeographyList;
		return this;
	}

	public ActiveFlag setSecurityTokenXClassificationList(List<SecurityTokenXClassification> securityTokenXClassificationList)
	{
		this.securityTokenXClassificationList = securityTokenXClassificationList;
		return this;
	}

	public ActiveFlag setEventXArrangementsSecurityTokenList(List<EventXArrangementsSecurityToken> eventXArrangementsSecurityTokenList)
	{
		this.eventXArrangementsSecurityTokenList = eventXArrangementsSecurityTokenList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXResourceItemSecurityTokenList(List<InvolvedPartyXResourceItemSecurityToken> involvedPartyXResourceItemSecurityTokenList)
	{
		this.involvedPartyXResourceItemSecurityTokenList = involvedPartyXResourceItemSecurityTokenList;
		return this;
	}

	public ActiveFlag setProductXClassificationList(List<ProductXClassification> productXClassificationList)
	{
		this.productXClassificationList = productXClassificationList;
		return this;
	}

	public ActiveFlag setResourceItemDataSecurityTokenList(List<ResourceItemDataSecurityToken> resourceItemDataSecurityTokenList)
	{
		this.resourceItemDataSecurityTokenList = resourceItemDataSecurityTokenList;
		return this;
	}

	public ActiveFlag setArrangementXInvolvedPartyList(List<ArrangementXInvolvedParty> arrangementXInvolvedPartyList)
	{
		this.arrangementXInvolvedPartyList = arrangementXInvolvedPartyList;
		return this;
	}

	public ActiveFlag setArrangementXResourceItemList(List<ArrangementXResourceItem> arrangementXResourceItemList)
	{
		this.arrangementXResourceItemList = arrangementXResourceItemList;
		return this;
	}

	public ActiveFlag setProductXProductSecurityTokenList(List<ProductXProductSecurityToken> productXProductSecurityTokenList)
	{
		this.productXProductSecurityTokenList = productXProductSecurityTokenList;
		return this;
	}

	public ActiveFlag setResourceItemXClassificationList(List<ResourceItemXClassification> resourceItemXClassificationList)
	{
		this.resourceItemXClassificationList = resourceItemXClassificationList;
		return this;
	}

	public ActiveFlag setArrangementXInvolvedPartySecurityTokenList(List<ArrangementXInvolvedPartySecurityToken> arrangementXInvolvedPartySecurityTokenList)
	{
		this.arrangementXInvolvedPartySecurityTokenList = arrangementXInvolvedPartySecurityTokenList;
		return this;
	}

	public ActiveFlag setInvolvedPartyNonOrganicList(List<InvolvedPartyNonOrganic> involvedPartyNonOrganicList)
	{
		this.involvedPartyNonOrganicList = involvedPartyNonOrganicList;
		return this;
	}

	public ActiveFlag setEventXInvolvedPartyList(List<EventXInvolvedParty> eventXInvolvedPartyList)
	{
		this.eventXInvolvedPartyList = eventXInvolvedPartyList;
		return this;
	}

	public ActiveFlag setClassifications(List<ActiveFlagXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}

	public ActiveFlag setGeographyXResourceItemSecurityTokenList(List<GeographyXResourceItemSecurityToken> geographyXResourceItemSecurityTokenList)
	{
		this.geographyXResourceItemSecurityTokenList = geographyXResourceItemSecurityTokenList;
		return this;
	}

	public ActiveFlag setEnterpriseXClassificationList(List<EnterpriseXClassification> enterpriseXClassificationList)
	{
		this.enterpriseXClassificationList = enterpriseXClassificationList;
		return this;
	}

	public ActiveFlag setEventXArrangementList(List<EventXArrangement> eventXArrangementList)
	{
		this.eventXArrangementList = eventXArrangementList;
		return this;
	}

	public ActiveFlag setInvolvedPartyIdentificationTypeSecurityTokenList(List<InvolvedPartyIdentificationTypeSecurityToken> involvedPartyIdentificationTypeSecurityTokenList)
	{
		this.involvedPartyIdentificationTypeSecurityTokenList = involvedPartyIdentificationTypeSecurityTokenList;
		return this;
	}

	public ActiveFlag setAddressXResourceItemSecurityTokenList(List<AddressXResourceItemSecurityToken> addressXResourceItemSecurityTokenList)
	{
		this.addressXResourceItemSecurityTokenList = addressXResourceItemSecurityTokenList;
		return this;
	}

	public ActiveFlag setInvolvedPartySecurityTokenList(List<InvolvedPartySecurityToken> involvedPartySecurityTokenList)
	{
		this.involvedPartySecurityTokenList = involvedPartySecurityTokenList;
		return this;
	}

	public ActiveFlag setProductXClassificationSecurityTokenList(List<ProductXClassificationSecurityToken> productXClassificationSecurityTokenList)
	{
		this.productXClassificationSecurityTokenList = productXClassificationSecurityTokenList;
		return this;
	}

	public ActiveFlag setSecurityTokenList(List<SecurityToken> securityTokenList)
	{
		this.securityTokenList = securityTokenList;
		return this;
	}

	public ActiveFlag setArrangementXArrangementTypeSecurityTokenList(List<ArrangementXArrangementTypeSecurityToken> arrangementXArrangementTypeSecurityTokenList)
	{
		this.arrangementXArrangementTypeSecurityTokenList = arrangementXArrangementTypeSecurityTokenList;
		return this;
	}

	public ActiveFlag setEventXInvolvedPartySecurityTokenList(List<EventXInvolvedPartySecurityToken> eventXInvolvedPartySecurityTokenList)
	{
		this.eventXInvolvedPartySecurityTokenList = eventXInvolvedPartySecurityTokenList;
		return this;
	}

	public ActiveFlag setInvolvedPartyOrganicList(List<InvolvedPartyOrganic> involvedPartyOrganicList)
	{
		this.involvedPartyOrganicList = involvedPartyOrganicList;
		return this;
	}

	public ActiveFlag setSystemXClassificationList(List<SystemXClassification> systemXClassificationList)
	{
		this.systemXClassificationList = systemXClassificationList;
		return this;
	}

	public ActiveFlag setEventList(List<Event> eventList)
	{
		this.eventList = eventList;
		return this;
	}

	public ActiveFlag setClassificationDataConceptXClassificationSecurityTokenList(List<ClassificationDataConceptXClassificationSecurityToken> classificationDataConceptXClassificationSecurityTokenList)
	{
		this.classificationDataConceptXClassificationSecurityTokenList = classificationDataConceptXClassificationSecurityTokenList;
		return this;
	}

	public ActiveFlag setActiveFlagSecurityTokenList(List<ActiveFlagSecurityToken> activeFlagSecurityTokenList)
	{
		this.activeFlagSecurityTokenList = activeFlagSecurityTokenList;
		return this;
	}

	public ActiveFlag setActiveFlagSecurityTokenList1(List<ActiveFlagSecurityToken> activeFlagSecurityTokenList1)
	{
		this.activeFlagSecurityTokenList1 = activeFlagSecurityTokenList1;
		return this;
	}

	public ActiveFlag setArrangementXClassificationList(List<ArrangementXClassification> arrangementXClassificationList)
	{
		this.arrangementXClassificationList = arrangementXClassificationList;
		return this;
	}

	public ActiveFlag setGeographyList(List<Geography> geographyList)
	{
		this.geographyList = geographyList;
		return this;
	}

	public ActiveFlag setInvolvedPartyOrganicTypeSecurityTokenList(List<InvolvedPartyOrganicTypeSecurityToken> involvedPartyOrganicTypeSecurityTokenList)
	{
		this.involvedPartyOrganicTypeSecurityTokenList = involvedPartyOrganicTypeSecurityTokenList;
		return this;
	}

	public ActiveFlag setEventTypeList(List<EventType> eventTypeList)
	{
		this.eventTypeList = eventTypeList;
		return this;
	}

	public ActiveFlag setResourceItemDataList(List<ResourceItemData> resourceItemDataList)
	{
		this.resourceItemDataList = resourceItemDataList;
		return this;
	}

	public ActiveFlag setAddressXResourceItemList(List<AddressXResourceItem> addressXResourceItemList)
	{
		this.addressXResourceItemList = addressXResourceItemList;
		return this;
	}

	public ActiveFlag setArrangementTypeSecurityTokenList(List<ArrangementTypeSecurityToken> arrangementTypeSecurityTokenList)
	{
		this.arrangementTypeSecurityTokenList = arrangementTypeSecurityTokenList;
		return this;
	}

	public ActiveFlag setInvolvedPartyOrganicSecurityTokenList(List<InvolvedPartyOrganicSecurityToken> involvedPartyOrganicSecurityTokenList)
	{
		this.involvedPartyOrganicSecurityTokenList = involvedPartyOrganicSecurityTokenList;
		return this;
	}

	public ActiveFlag setArrangementXArrangementList(List<ArrangementXArrangement> arrangementXArrangementList)
	{
		this.arrangementXArrangementList = arrangementXArrangementList;
		return this;
	}

	public ActiveFlag setClassificationDataConceptXClassificationList(List<ClassificationDataConceptXClassification> classificationDataConceptXClassificationList)
	{
		this.classificationDataConceptXClassificationList = classificationDataConceptXClassificationList;
		return this;
	}

	public ActiveFlag setArrangementXProductList(List<ArrangementXProduct> arrangementXProductList)
	{
		this.arrangementXProductList = arrangementXProductList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXInvolvedPartyList(List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList)
	{
		this.involvedPartyXInvolvedPartyList = involvedPartyXInvolvedPartyList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXInvolvedPartyTypeList(List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList)
	{
		this.involvedPartyXInvolvedPartyTypeList = involvedPartyXInvolvedPartyTypeList;
		return this;
	}

	public ActiveFlag setArrangementSecurityTokenList(List<ArrangementSecurityToken> arrangementSecurityTokenList)
	{
		this.arrangementSecurityTokenList = arrangementSecurityTokenList;
		return this;
	}

	public ActiveFlag setInvolvedPartyOrganicTypeList(List<InvolvedPartyOrganicType> involvedPartyOrganicTypeList)
	{
		this.involvedPartyOrganicTypeList = involvedPartyOrganicTypeList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList(List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList)
	{
		this.involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList = involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXInvolvedPartyTypeSecurityTokenList(List<InvolvedPartyXInvolvedPartyTypeSecurityToken> involvedPartyXInvolvedPartyTypeSecurityTokenList)
	{
		this.involvedPartyXInvolvedPartyTypeSecurityTokenList = involvedPartyXInvolvedPartyTypeSecurityTokenList;
		return this;
	}

	public ActiveFlag setClassificationDataConceptSecurityTokenList(List<ClassificationDataConceptSecurityToken> classificationDataConceptSecurityTokenList)
	{
		this.classificationDataConceptSecurityTokenList = classificationDataConceptSecurityTokenList;
		return this;
	}

	public ActiveFlag setEventXResourceItemList(List<EventXResourceItem> eventXResourceItemList)
	{
		this.eventXResourceItemList = eventXResourceItemList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXClassificationList(List<InvolvedPartyXClassification> involvedPartyXClassificationList)
	{
		this.involvedPartyXClassificationList = involvedPartyXClassificationList;
		return this;
	}

	public ActiveFlag setArrangementTypeList(List<ArrangementType> arrangementTypeList)
	{
		this.arrangementTypeList = arrangementTypeList;
		return this;
	}

	public ActiveFlag setSystemsList(List<Systems> systemsList)
	{
		this.systemsList = systemsList;
		return this;
	}

	public ActiveFlag setInvolvedPartyTypeSecurityTokenList(List<InvolvedPartyTypeSecurityToken> involvedPartyTypeSecurityTokenList)
	{
		this.involvedPartyTypeSecurityTokenList = involvedPartyTypeSecurityTokenList;
		return this;
	}

	public ActiveFlag setSecurityTokenXSecurityTokenList(List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenList)
	{
		this.securityTokenXSecurityTokenList = securityTokenXSecurityTokenList;
		return this;
	}

	public ActiveFlag setProductXResourceItemList(List<ProductXResourceItem> productXResourceItemList)
	{
		this.productXResourceItemList = productXResourceItemList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXClassificationSecurityTokenList(List<InvolvedPartyXClassificationSecurityToken> involvedPartyXClassificationSecurityTokenList)
	{
		this.involvedPartyXClassificationSecurityTokenList = involvedPartyXClassificationSecurityTokenList;
		return this;
	}

	public ActiveFlag setEventXAddressSecurityTokenList(List<EventXAddressSecurityToken> eventXAddressSecurityTokenList)
	{
		this.eventXAddressSecurityTokenList = eventXAddressSecurityTokenList;
		return this;
	}

	public ActiveFlag setResourceItemList(List<ResourceItem> resourceItemList)
	{
		this.resourceItemList = resourceItemList;
		return this;
	}

	public ActiveFlag setEventXAddressList(List<EventXAddress> eventXAddressList)
	{
		this.eventXAddressList = eventXAddressList;
		return this;
	}

	public ActiveFlag setClassificationDataConceptXResourceItemList(List<ClassificationDataConceptXResourceItem> classificationDataConceptXResourceItemList)
	{
		this.classificationDataConceptXResourceItemList = classificationDataConceptXResourceItemList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXProductList(List<InvolvedPartyXProduct> involvedPartyXProductList)
	{
		this.involvedPartyXProductList = involvedPartyXProductList;
		return this;
	}

	public ActiveFlag setEventSecurityTokenList(List<EventSecurityToken> eventSecurityTokenList)
	{
		this.eventSecurityTokenList = eventSecurityTokenList;
		return this;
	}

	public ActiveFlag setResourceItemDataXClassificationList(List<ResourceItemDataXClassification> resourceItemDataXClassificationList)
	{
		this.resourceItemDataXClassificationList = resourceItemDataXClassificationList;
		return this;
	}

	public ActiveFlag setSecurityTokenAccessList(List<SecurityTokensSecurityToken> securityTokenAccessList)
	{
		this.securityTokenAccessList = securityTokenAccessList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXAddressList(List<InvolvedPartyXAddress> involvedPartyXAddressList)
	{
		this.involvedPartyXAddressList = involvedPartyXAddressList;
		return this;
	}

	public ActiveFlag setGeographyXClassificationSecurityTokenList(List<GeographyXClassificationSecurityToken> geographyXClassificationSecurityTokenList)
	{
		this.geographyXClassificationSecurityTokenList = geographyXClassificationSecurityTokenList;
		return this;
	}

	public ActiveFlag setGeographyXGeographySecurityTokenList(List<GeographyXGeographySecurityToken> geographyXGeographySecurityTokenList)
	{
		this.geographyXGeographySecurityTokenList = geographyXGeographySecurityTokenList;
		return this;
	}

	public ActiveFlag setArrangementXArrangementSecurityTokenList(List<ArrangementXArrangementSecurityToken> arrangementXArrangementSecurityTokenList)
	{
		this.arrangementXArrangementSecurityTokenList = arrangementXArrangementSecurityTokenList;
		return this;
	}

	public ActiveFlag setGeographySecurityTokenList(List<GeographySecurityToken> geographySecurityTokenList)
	{
		this.geographySecurityTokenList = geographySecurityTokenList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXInvolvedPartyIdentificationTypeList(List<InvolvedPartyXInvolvedPartyIdentificationType> involvedPartyXInvolvedPartyIdentificationTypeList)
	{
		this.involvedPartyXInvolvedPartyIdentificationTypeList = involvedPartyXInvolvedPartyIdentificationTypeList;
		return this;
	}

	public ActiveFlag setInvolvedPartyIdentificationTypeList(List<InvolvedPartyIdentificationType> involvedPartyIdentificationTypeList)
	{
		this.involvedPartyIdentificationTypeList = involvedPartyIdentificationTypeList;
		return this;
	}

	public ActiveFlag setClassificationXResourceItemList(List<ClassificationXResourceItem> classificationXResourceItemList)
	{
		this.classificationXResourceItemList = classificationXResourceItemList;
		return this;
	}

	public ActiveFlag setEventXProductList(List<EventXProduct> eventXProductList)
	{
		this.eventXProductList = eventXProductList;
		return this;
	}

	public ActiveFlag setEventXEventTypeList(List<EventXEventType> eventXEventTypeList)
	{
		this.eventXEventTypeList = eventXEventTypeList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXInvolvedPartySecurityTokenList(List<InvolvedPartyXInvolvedPartySecurityToken> involvedPartyXInvolvedPartySecurityTokenList)
	{
		this.involvedPartyXInvolvedPartySecurityTokenList = involvedPartyXInvolvedPartySecurityTokenList;
		return this;
	}

	public ActiveFlag setProductXProductList(List<ProductXProduct> productXProductList)
	{
		this.productXProductList = productXProductList;
		return this;
	}

	public ActiveFlag setInvolvedPartyTypeList(List<InvolvedPartyType> involvedPartyTypeList)
	{
		this.involvedPartyTypeList = involvedPartyTypeList;
		return this;
	}

	public ActiveFlag setArrangementXClassificationSecurityTokenList(List<ArrangementXClassificationSecurityToken> arrangementXClassificationSecurityTokenList)
	{
		this.arrangementXClassificationSecurityTokenList = arrangementXClassificationSecurityTokenList;
		return this;
	}

	public ActiveFlag setInvolvedPartyXAddressSecurityTokenList(List<InvolvedPartyXAddressSecurityToken> involvedPartyXAddressSecurityTokenList)
	{
		this.involvedPartyXAddressSecurityTokenList = involvedPartyXAddressSecurityTokenList;
		return this;
	}

	public ActiveFlag setGeographyXClassificationList(List<GeographyXClassification> geographyXClassificationList)
	{
		this.geographyXClassificationList = geographyXClassificationList;
		return this;
	}

	public ActiveFlag setAddressXGeographySecurityTokenList(List<AddressXGeographySecurityToken> addressXGeographySecurityTokenList)
	{
		this.addressXGeographySecurityTokenList = addressXGeographySecurityTokenList;
		return this;
	}

	public ActiveFlag setInvolvedPartyNonOrganicSecurityTokenList(List<InvolvedPartyNonOrganicSecurityToken> involvedPartyNonOrganicSecurityTokenList)
	{
		this.involvedPartyNonOrganicSecurityTokenList = involvedPartyNonOrganicSecurityTokenList;
		return this;
	}

	public ActiveFlag setEventXClassificationList(List<EventXClassification> eventXClassificationList)
	{
		this.eventXClassificationList = eventXClassificationList;
		return this;
	}

	public ActiveFlag setArrangementList(List<Arrangement> arrangementList)
	{
		this.arrangementList = arrangementList;
		return this;
	}

	public ActiveFlag setResourceItemSecurityTokenList(List<ResourceItemSecurityToken> resourceItemSecurityTokenList)
	{
		this.resourceItemSecurityTokenList = resourceItemSecurityTokenList;
		return this;
	}

	public ActiveFlag setGeographyXResourceItemList(List<GeographyXResourceItem> geographyXResourceItemList)
	{
		this.geographyXResourceItemList = geographyXResourceItemList;
		return this;
	}

	public ActiveFlag setClassificationXClassificationSecurityTokenList(List<ClassificationXClassificationSecurityToken> classificationXClassificationSecurityTokenList)
	{
		this.classificationXClassificationSecurityTokenList = classificationXClassificationSecurityTokenList;
		return this;
	}

	public ActiveFlag setEventTypesSecurityTokenList(List<EventTypesSecurityToken> eventTypesSecurityTokenList)
	{
		this.eventTypesSecurityTokenList = eventTypesSecurityTokenList;
		return this;
	}

	public ActiveFlag setYesNoXClassificationList(List<YesNoXClassification> yesNoXClassificationList)
	{
		this.yesNoXClassificationList = yesNoXClassificationList;
		return this;
	}

	public ActiveFlag setGeographyXGeographyList(List<GeographyXGeography> geographyXGeographyList)
	{
		this.geographyXGeographyList = geographyXGeographyList;
		return this;
	}

	public ActiveFlag setClassificationSecurityTokenList(List<ClassificationSecurityToken> classificationSecurityTokenList)
	{
		this.classificationSecurityTokenList = classificationSecurityTokenList;
		return this;
	}

	public ActiveFlag setYesNoList(List<YesNo> yesNoList)
	{
		this.yesNoList = yesNoList;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ActiveFlag))
		{
			return false;
		}
		final ActiveFlag other = (ActiveFlag) o;
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
		return other instanceof ActiveFlag;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}

	public Long getId()
	{
		return this.id;
	}

	public Enterprise getEnterpriseID()
	{
		return this.enterpriseID;
	}

	public @NotNull @Size(min = 1,
			max = 100) String getName()
	{
		return this.name;
	}

	public @NotNull @Size(min = 1,
			max = 100) String getDescription()
	{
		return this.description;
	}

	public @NotNull boolean isAllowAccess()
	{
		return this.allowAccess;
	}

	public ActiveFlag setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ActiveFlag setEnterpriseID(Enterprise enterpriseID)
	{
		this.enterpriseID = enterpriseID;
		return this;
	}

	public ActiveFlag setName(@NotNull @Size(min = 1,
			max = 100) String name)
	{
		this.name = name;
		return this;
	}

	public ActiveFlag setDescription(@NotNull @Size(min = 1,
			max = 100) String description)
	{
		this.description = description;
		return this;
	}

	public ActiveFlag setAllowAccess(@NotNull boolean allowAccess)
	{
		this.allowAccess = allowAccess;
		return this;
	}
}
