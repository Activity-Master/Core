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
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.systems.ActiveFlagSystem;
import com.jwebmp.guicedinjection.GuiceContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
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
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)@lombok.Data
public class ActiveFlag
		extends WarehouseNameDescriptionTable<ActiveFlag, ActiveFlagQueryBuilder, Long, ActiveFlagSecurityToken>
		implements INameAndDescription<ActiveFlag>,
				           IContainsClassifications<ActiveFlag, Classification, ActiveFlagXClassification, IActiveFlagClassification<?>, IActiveFlag<ActiveFlag>>,
				           IActivityMasterEntity<ActiveFlag>,
				           IContainsEnterprise<ActiveFlag>,
				           IActiveFlag<ActiveFlag>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ActiveFlagID")
	@Getter
	@Setter
	private Long id;

	@JoinColumn(name = "EnterpriseID",
			referencedColumnName = "EnterpriseID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	@Getter
	@Setter
	private Enterprise enterpriseID;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 100)
	@Column(nullable = false,
			length = 100,
			name = "ActiveFlagName")
	@Getter
	@Setter
	private String name;

	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@NotNull
	@Size(min = 1,
			max = 100)
	@Column(nullable = false,
			length = 100,
			name = "ActiveFlagDescription")
	@Getter
	@Setter
	private String description;

	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@NotNull
	@Column(nullable = false,
			name = "AllowAccess")
	@Getter
	@Setter
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
}
