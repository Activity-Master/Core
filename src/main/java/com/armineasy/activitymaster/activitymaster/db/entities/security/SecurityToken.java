package com.armineasy.activitymaster.activitymaster.db.entities.security;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXGeographySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.*;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.*;
import com.armineasy.activitymaster.activitymaster.db.entities.events.*;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXGeographySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.*;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXProductSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemDataSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.builders.SecurityTokenQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemsSecurityToken;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISecurityToken;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.jwebmp.logger.LogFactory;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "SecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class SecurityToken
		extends WarehouseSCDNameDescriptionTable<SecurityToken, SecurityTokenQueryBuilder, Long, SecurityTokensSecurityToken>
		implements IContainsClassifications<SecurityToken, Classification, SecurityTokenXClassification, IResourceItemClassification<?>, SecurityToken>,
				           IActivityMasterEntity<SecurityToken>,
				           ISecurityToken<SecurityToken>
{
	private static final Logger log = LogFactory.getLog("SecurityToken");

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "SecurityTokenID")
	private Long id;
	@Basic(optional = false,
			fetch = FetchType.EAGER)
	@NotNull
	@Size(min = 1,
			max = 128)
	@Column(nullable = false,
			length = 128,
			name = "SecurityToken")
	private String securityToken;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Lob
	@Column(nullable = false,
			name = "SecurityTokenFriendlyName")
	private String name;
	@Basic(optional = false,
			fetch = FetchType.EAGER)
	@NotNull
	@Lob
	@Column(nullable = false,
			name = "SecurityTokenFriendlyDescription")
	private String description;
	@JoinColumn(name = "SecurityTokenClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Classification securityTokenClassificationID;

	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXClassification> classifications;

	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProductSecurityToken> involvedPartyXProductSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ProductXResourceItemSecurityToken> productXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ResourceItemXClassificationSecurityToken> resourceItemXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<EventXClassificationSecurityToken> eventXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ProductSecurityToken> productSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> involvedPartyXInvolvedPartyNameTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<EventXEventTypeSecurityToken> eventXEventTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<EventXProductSecurityToken> eventXProductSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<EventXGeographySecurityToken> eventXGeographySecurityTokenList;

	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNameTypeSecurityToken> involvedPartyNameTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ClassificationXResourceItemSecurityToken> classificationXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<EventXResourceItemSecurityToken> eventXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<AddressSecurityToken> addressSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<SystemsSecurityToken> systemSecurityTokenList;

	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<EventXArrangementsSecurityToken> eventXArrangementsSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXResourceItemSecurityToken> involvedPartyXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataSecurityToken> resourceItemDataSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ProductXProductSecurityToken> productXProductSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ArrangementXInvolvedPartySecurityToken> arrangementXInvolvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<GeographyXResourceItemSecurityToken> geographyXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyIdentificationTypeSecurityToken> involvedPartyIdentificationTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<AddressXResourceItemSecurityToken> addressXResourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartySecurityToken> involvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ProductXClassificationSecurityToken> productXClassificationSecurityTokenList;

	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementTypeSecurityToken> arrangementXArrangementTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<EventXInvolvedPartySecurityToken> eventXInvolvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXClassificationSecurityToken> classificationDataConceptXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ActiveFlagSecurityToken> activeFlagSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicTypeSecurityToken> involvedPartyOrganicTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ArrangementTypeSecurityToken> arrangementTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicSecurityToken> involvedPartyOrganicSecurityTokenList;

	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ArrangementSecurityToken> arrangementSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyTypeSecurityToken> involvedPartyXInvolvedPartyTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptSecurityToken> classificationDataConceptSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyTypeSecurityToken> involvedPartyTypeSecurityTokenList;
	@OneToMany(
			mappedBy = "parentSecurityTokenID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenParentList;
	@OneToMany(
			mappedBy = "childSecurityTokenID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenChildList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXClassificationSecurityToken> involvedPartyXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<EventXAddressSecurityToken> eventXAddressSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<EventSecurityToken> eventSecurityTokenList;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<SecurityTokensSecurityToken> securityTokenAccessList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<SecurityTokensSecurityToken> securities;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<GeographyXClassificationSecurityToken> geographyXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<GeographyXGeographySecurityToken> geographyXGeographySecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementSecurityToken> arrangementXArrangementSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<GeographySecurityToken> geographySecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartySecurityToken> involvedPartyXInvolvedPartySecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ArrangementXClassificationSecurityToken> arrangementXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddressSecurityToken> involvedPartyXAddressSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<AddressXGeographySecurityToken> addressXGeographySecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNonOrganicSecurityToken> involvedPartyNonOrganicSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ResourceItemSecurityToken> resourceItemSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassificationSecurityToken> classificationXClassificationSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<EventTypesSecurityToken> eventTypesSecurityTokenList;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ClassificationSecurityToken> classificationSecurityTokenList;

	public SecurityToken()
	{

	}

	public SecurityToken(Long securityTokenID)
	{
		this.id = securityTokenID;
	}

	public SecurityToken(Long securityTokenID, String securityToken, String securityTokenFriendlyName, String securityTokenFriendlyDescription, String originalSourceSystemUniqueID)
	{
		this.id = securityTokenID;
		this.securityToken = securityToken;
		this.name = securityTokenFriendlyName;
		this.description = securityTokenFriendlyDescription;
	}

	@Override
	public String toString()
	{
		return "Security - " + getSecurityToken();
	}

	@Override
	public void configureForClassification(SecurityTokenXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setSecurityTokenID(this);
	}

	@Override
	protected SecurityTokensSecurityToken configureDefaultsForNewToken(SecurityTokensSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);
		stAdmin.setBase(this);
		return stAdmin;
	}

	public List<SecurityTokenXClassification> getClassifications()
	{
		return this.classifications;
	}

	public List<InvolvedPartyXProductSecurityToken> getInvolvedPartyXProductSecurityTokenList()
	{
		return this.involvedPartyXProductSecurityTokenList;
	}

	public List<ProductXResourceItemSecurityToken> getProductXResourceItemSecurityTokenList()
	{
		return this.productXResourceItemSecurityTokenList;
	}

	public List<ResourceItemXClassificationSecurityToken> getResourceItemXClassificationSecurityTokenList()
	{
		return this.resourceItemXClassificationSecurityTokenList;
	}

	public List<EventXClassificationSecurityToken> getEventXClassificationSecurityTokenList()
	{
		return this.eventXClassificationSecurityTokenList;
	}

	public List<ProductSecurityToken> getProductSecurityTokenList()
	{
		return this.productSecurityTokenList;
	}

	public List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> getInvolvedPartyXInvolvedPartyNameTypeSecurityTokenList()
	{
		return this.involvedPartyXInvolvedPartyNameTypeSecurityTokenList;
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

	public List<EventXGeographySecurityToken> getEventXGeographySecurityTokenList()
	{
		return this.eventXGeographySecurityTokenList;
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

	public List<EventXArrangementsSecurityToken> getEventXArrangementsSecurityTokenList()
	{
		return this.eventXArrangementsSecurityTokenList;
	}

	public List<InvolvedPartyXResourceItemSecurityToken> getInvolvedPartyXResourceItemSecurityTokenList()
	{
		return this.involvedPartyXResourceItemSecurityTokenList;
	}

	public List<ResourceItemDataSecurityToken> getResourceItemDataSecurityTokenList()
	{
		return this.resourceItemDataSecurityTokenList;
	}

	public List<ProductXProductSecurityToken> getProductXProductSecurityTokenList()
	{
		return this.productXProductSecurityTokenList;
	}

	public List<ArrangementXInvolvedPartySecurityToken> getArrangementXInvolvedPartySecurityTokenList()
	{
		return this.arrangementXInvolvedPartySecurityTokenList;
	}

	public List<GeographyXResourceItemSecurityToken> getGeographyXResourceItemSecurityTokenList()
	{
		return this.geographyXResourceItemSecurityTokenList;
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

	public List<ArrangementXArrangementTypeSecurityToken> getArrangementXArrangementTypeSecurityTokenList()
	{
		return this.arrangementXArrangementTypeSecurityTokenList;
	}

	public List<EventXInvolvedPartySecurityToken> getEventXInvolvedPartySecurityTokenList()
	{
		return this.eventXInvolvedPartySecurityTokenList;
	}

	public List<ClassificationDataConceptXClassificationSecurityToken> getClassificationDataConceptXClassificationSecurityTokenList()
	{
		return this.classificationDataConceptXClassificationSecurityTokenList;
	}

	public List<ActiveFlagSecurityToken> getActiveFlagSecurityTokenList()
	{
		return this.activeFlagSecurityTokenList;
	}

	public List<InvolvedPartyOrganicTypeSecurityToken> getInvolvedPartyOrganicTypeSecurityTokenList()
	{
		return this.involvedPartyOrganicTypeSecurityTokenList;
	}

	public List<ArrangementTypeSecurityToken> getArrangementTypeSecurityTokenList()
	{
		return this.arrangementTypeSecurityTokenList;
	}

	public List<InvolvedPartyOrganicSecurityToken> getInvolvedPartyOrganicSecurityTokenList()
	{
		return this.involvedPartyOrganicSecurityTokenList;
	}

	public List<ArrangementSecurityToken> getArrangementSecurityTokenList()
	{
		return this.arrangementSecurityTokenList;
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

	public List<InvolvedPartyTypeSecurityToken> getInvolvedPartyTypeSecurityTokenList()
	{
		return this.involvedPartyTypeSecurityTokenList;
	}

	public List<SecurityTokenXSecurityToken> getSecurityTokenXSecurityTokenParentList()
	{
		return this.securityTokenXSecurityTokenParentList;
	}

	public List<SecurityTokenXSecurityToken> getSecurityTokenXSecurityTokenChildList()
	{
		return this.securityTokenXSecurityTokenChildList;
	}

	public List<InvolvedPartyXClassificationSecurityToken> getInvolvedPartyXClassificationSecurityTokenList()
	{
		return this.involvedPartyXClassificationSecurityTokenList;
	}

	public List<EventXAddressSecurityToken> getEventXAddressSecurityTokenList()
	{
		return this.eventXAddressSecurityTokenList;
	}

	public List<EventSecurityToken> getEventSecurityTokenList()
	{
		return this.eventSecurityTokenList;
	}

	public List<SecurityTokensSecurityToken> getSecurityTokenAccessList()
	{
		return this.securityTokenAccessList;
	}

	public List<SecurityTokensSecurityToken> getSecurities()
	{
		return this.securities;
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

	public List<InvolvedPartyXInvolvedPartySecurityToken> getInvolvedPartyXInvolvedPartySecurityTokenList()
	{
		return this.involvedPartyXInvolvedPartySecurityTokenList;
	}

	public List<ArrangementXClassificationSecurityToken> getArrangementXClassificationSecurityTokenList()
	{
		return this.arrangementXClassificationSecurityTokenList;
	}

	public List<InvolvedPartyXAddressSecurityToken> getInvolvedPartyXAddressSecurityTokenList()
	{
		return this.involvedPartyXAddressSecurityTokenList;
	}

	public List<AddressXGeographySecurityToken> getAddressXGeographySecurityTokenList()
	{
		return this.addressXGeographySecurityTokenList;
	}

	public List<InvolvedPartyNonOrganicSecurityToken> getInvolvedPartyNonOrganicSecurityTokenList()
	{
		return this.involvedPartyNonOrganicSecurityTokenList;
	}

	public List<ResourceItemSecurityToken> getResourceItemSecurityTokenList()
	{
		return this.resourceItemSecurityTokenList;
	}

	public List<ClassificationXClassificationSecurityToken> getClassificationXClassificationSecurityTokenList()
	{
		return this.classificationXClassificationSecurityTokenList;
	}

	public List<EventTypesSecurityToken> getEventTypesSecurityTokenList()
	{
		return this.eventTypesSecurityTokenList;
	}

	public List<ClassificationSecurityToken> getClassificationSecurityTokenList()
	{
		return this.classificationSecurityTokenList;
	}

	public SecurityToken setClassifications(List<SecurityTokenXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}

	public SecurityToken setInvolvedPartyXProductSecurityTokenList(List<InvolvedPartyXProductSecurityToken> involvedPartyXProductSecurityTokenList)
	{
		this.involvedPartyXProductSecurityTokenList = involvedPartyXProductSecurityTokenList;
		return this;
	}

	public SecurityToken setProductXResourceItemSecurityTokenList(List<ProductXResourceItemSecurityToken> productXResourceItemSecurityTokenList)
	{
		this.productXResourceItemSecurityTokenList = productXResourceItemSecurityTokenList;
		return this;
	}

	public SecurityToken setResourceItemXClassificationSecurityTokenList(List<ResourceItemXClassificationSecurityToken> resourceItemXClassificationSecurityTokenList)
	{
		this.resourceItemXClassificationSecurityTokenList = resourceItemXClassificationSecurityTokenList;
		return this;
	}

	public SecurityToken setEventXClassificationSecurityTokenList(List<EventXClassificationSecurityToken> eventXClassificationSecurityTokenList)
	{
		this.eventXClassificationSecurityTokenList = eventXClassificationSecurityTokenList;
		return this;
	}

	public SecurityToken setProductSecurityTokenList(List<ProductSecurityToken> productSecurityTokenList)
	{
		this.productSecurityTokenList = productSecurityTokenList;
		return this;
	}

	public SecurityToken setInvolvedPartyXInvolvedPartyNameTypeSecurityTokenList(List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> involvedPartyXInvolvedPartyNameTypeSecurityTokenList)
	{
		this.involvedPartyXInvolvedPartyNameTypeSecurityTokenList = involvedPartyXInvolvedPartyNameTypeSecurityTokenList;
		return this;
	}

	public SecurityToken setEventXEventTypeSecurityTokenList(List<EventXEventTypeSecurityToken> eventXEventTypeSecurityTokenList)
	{
		this.eventXEventTypeSecurityTokenList = eventXEventTypeSecurityTokenList;
		return this;
	}

	public SecurityToken setArrangementXResourceItemSecurityTokenList(List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList)
	{
		this.arrangementXResourceItemSecurityTokenList = arrangementXResourceItemSecurityTokenList;
		return this;
	}

	public SecurityToken setEventXProductSecurityTokenList(List<EventXProductSecurityToken> eventXProductSecurityTokenList)
	{
		this.eventXProductSecurityTokenList = eventXProductSecurityTokenList;
		return this;
	}

	public SecurityToken setEventXGeographySecurityTokenList(List<EventXGeographySecurityToken> eventXGeographySecurityTokenList)
	{
		this.eventXGeographySecurityTokenList = eventXGeographySecurityTokenList;
		return this;
	}

	public SecurityToken setInvolvedPartyNameTypeSecurityTokenList(List<InvolvedPartyNameTypeSecurityToken> involvedPartyNameTypeSecurityTokenList)
	{
		this.involvedPartyNameTypeSecurityTokenList = involvedPartyNameTypeSecurityTokenList;
		return this;
	}

	public SecurityToken setClassificationXResourceItemSecurityTokenList(List<ClassificationXResourceItemSecurityToken> classificationXResourceItemSecurityTokenList)
	{
		this.classificationXResourceItemSecurityTokenList = classificationXResourceItemSecurityTokenList;
		return this;
	}

	public SecurityToken setEventXResourceItemSecurityTokenList(List<EventXResourceItemSecurityToken> eventXResourceItemSecurityTokenList)
	{
		this.eventXResourceItemSecurityTokenList = eventXResourceItemSecurityTokenList;
		return this;
	}

	public SecurityToken setAddressSecurityTokenList(List<AddressSecurityToken> addressSecurityTokenList)
	{
		this.addressSecurityTokenList = addressSecurityTokenList;
		return this;
	}

	public SecurityToken setSystemSecurityTokenList(List<SystemsSecurityToken> systemSecurityTokenList)
	{
		this.systemSecurityTokenList = systemSecurityTokenList;
		return this;
	}

	public SecurityToken setEventXArrangementsSecurityTokenList(List<EventXArrangementsSecurityToken> eventXArrangementsSecurityTokenList)
	{
		this.eventXArrangementsSecurityTokenList = eventXArrangementsSecurityTokenList;
		return this;
	}

	public SecurityToken setInvolvedPartyXResourceItemSecurityTokenList(List<InvolvedPartyXResourceItemSecurityToken> involvedPartyXResourceItemSecurityTokenList)
	{
		this.involvedPartyXResourceItemSecurityTokenList = involvedPartyXResourceItemSecurityTokenList;
		return this;
	}

	public SecurityToken setResourceItemDataSecurityTokenList(List<ResourceItemDataSecurityToken> resourceItemDataSecurityTokenList)
	{
		this.resourceItemDataSecurityTokenList = resourceItemDataSecurityTokenList;
		return this;
	}

	public SecurityToken setProductXProductSecurityTokenList(List<ProductXProductSecurityToken> productXProductSecurityTokenList)
	{
		this.productXProductSecurityTokenList = productXProductSecurityTokenList;
		return this;
	}

	public SecurityToken setArrangementXInvolvedPartySecurityTokenList(List<ArrangementXInvolvedPartySecurityToken> arrangementXInvolvedPartySecurityTokenList)
	{
		this.arrangementXInvolvedPartySecurityTokenList = arrangementXInvolvedPartySecurityTokenList;
		return this;
	}

	public SecurityToken setGeographyXResourceItemSecurityTokenList(List<GeographyXResourceItemSecurityToken> geographyXResourceItemSecurityTokenList)
	{
		this.geographyXResourceItemSecurityTokenList = geographyXResourceItemSecurityTokenList;
		return this;
	}

	public SecurityToken setInvolvedPartyIdentificationTypeSecurityTokenList(List<InvolvedPartyIdentificationTypeSecurityToken> involvedPartyIdentificationTypeSecurityTokenList)
	{
		this.involvedPartyIdentificationTypeSecurityTokenList = involvedPartyIdentificationTypeSecurityTokenList;
		return this;
	}

	public SecurityToken setAddressXResourceItemSecurityTokenList(List<AddressXResourceItemSecurityToken> addressXResourceItemSecurityTokenList)
	{
		this.addressXResourceItemSecurityTokenList = addressXResourceItemSecurityTokenList;
		return this;
	}

	public SecurityToken setInvolvedPartySecurityTokenList(List<InvolvedPartySecurityToken> involvedPartySecurityTokenList)
	{
		this.involvedPartySecurityTokenList = involvedPartySecurityTokenList;
		return this;
	}

	public SecurityToken setProductXClassificationSecurityTokenList(List<ProductXClassificationSecurityToken> productXClassificationSecurityTokenList)
	{
		this.productXClassificationSecurityTokenList = productXClassificationSecurityTokenList;
		return this;
	}

	public SecurityToken setArrangementXArrangementTypeSecurityTokenList(List<ArrangementXArrangementTypeSecurityToken> arrangementXArrangementTypeSecurityTokenList)
	{
		this.arrangementXArrangementTypeSecurityTokenList = arrangementXArrangementTypeSecurityTokenList;
		return this;
	}

	public SecurityToken setEventXInvolvedPartySecurityTokenList(List<EventXInvolvedPartySecurityToken> eventXInvolvedPartySecurityTokenList)
	{
		this.eventXInvolvedPartySecurityTokenList = eventXInvolvedPartySecurityTokenList;
		return this;
	}

	public SecurityToken setClassificationDataConceptXClassificationSecurityTokenList(List<ClassificationDataConceptXClassificationSecurityToken> classificationDataConceptXClassificationSecurityTokenList)
	{
		this.classificationDataConceptXClassificationSecurityTokenList = classificationDataConceptXClassificationSecurityTokenList;
		return this;
	}

	public SecurityToken setActiveFlagSecurityTokenList(List<ActiveFlagSecurityToken> activeFlagSecurityTokenList)
	{
		this.activeFlagSecurityTokenList = activeFlagSecurityTokenList;
		return this;
	}

	public SecurityToken setInvolvedPartyOrganicTypeSecurityTokenList(List<InvolvedPartyOrganicTypeSecurityToken> involvedPartyOrganicTypeSecurityTokenList)
	{
		this.involvedPartyOrganicTypeSecurityTokenList = involvedPartyOrganicTypeSecurityTokenList;
		return this;
	}

	public SecurityToken setArrangementTypeSecurityTokenList(List<ArrangementTypeSecurityToken> arrangementTypeSecurityTokenList)
	{
		this.arrangementTypeSecurityTokenList = arrangementTypeSecurityTokenList;
		return this;
	}

	public SecurityToken setInvolvedPartyOrganicSecurityTokenList(List<InvolvedPartyOrganicSecurityToken> involvedPartyOrganicSecurityTokenList)
	{
		this.involvedPartyOrganicSecurityTokenList = involvedPartyOrganicSecurityTokenList;
		return this;
	}

	public SecurityToken setArrangementSecurityTokenList(List<ArrangementSecurityToken> arrangementSecurityTokenList)
	{
		this.arrangementSecurityTokenList = arrangementSecurityTokenList;
		return this;
	}

	public SecurityToken setInvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList(List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList)
	{
		this.involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList = involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenList;
		return this;
	}

	public SecurityToken setInvolvedPartyXInvolvedPartyTypeSecurityTokenList(List<InvolvedPartyXInvolvedPartyTypeSecurityToken> involvedPartyXInvolvedPartyTypeSecurityTokenList)
	{
		this.involvedPartyXInvolvedPartyTypeSecurityTokenList = involvedPartyXInvolvedPartyTypeSecurityTokenList;
		return this;
	}

	public SecurityToken setClassificationDataConceptSecurityTokenList(List<ClassificationDataConceptSecurityToken> classificationDataConceptSecurityTokenList)
	{
		this.classificationDataConceptSecurityTokenList = classificationDataConceptSecurityTokenList;
		return this;
	}

	public SecurityToken setInvolvedPartyTypeSecurityTokenList(List<InvolvedPartyTypeSecurityToken> involvedPartyTypeSecurityTokenList)
	{
		this.involvedPartyTypeSecurityTokenList = involvedPartyTypeSecurityTokenList;
		return this;
	}

	public SecurityToken setSecurityTokenXSecurityTokenParentList(List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenParentList)
	{
		this.securityTokenXSecurityTokenParentList = securityTokenXSecurityTokenParentList;
		return this;
	}

	public SecurityToken setSecurityTokenXSecurityTokenChildList(List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenChildList)
	{
		this.securityTokenXSecurityTokenChildList = securityTokenXSecurityTokenChildList;
		return this;
	}

	public SecurityToken setInvolvedPartyXClassificationSecurityTokenList(List<InvolvedPartyXClassificationSecurityToken> involvedPartyXClassificationSecurityTokenList)
	{
		this.involvedPartyXClassificationSecurityTokenList = involvedPartyXClassificationSecurityTokenList;
		return this;
	}

	public SecurityToken setEventXAddressSecurityTokenList(List<EventXAddressSecurityToken> eventXAddressSecurityTokenList)
	{
		this.eventXAddressSecurityTokenList = eventXAddressSecurityTokenList;
		return this;
	}

	public SecurityToken setEventSecurityTokenList(List<EventSecurityToken> eventSecurityTokenList)
	{
		this.eventSecurityTokenList = eventSecurityTokenList;
		return this;
	}

	public SecurityToken setSecurityTokenAccessList(List<SecurityTokensSecurityToken> securityTokenAccessList)
	{
		this.securityTokenAccessList = securityTokenAccessList;
		return this;
	}

	public SecurityToken setSecurities(List<SecurityTokensSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public SecurityToken setGeographyXClassificationSecurityTokenList(List<GeographyXClassificationSecurityToken> geographyXClassificationSecurityTokenList)
	{
		this.geographyXClassificationSecurityTokenList = geographyXClassificationSecurityTokenList;
		return this;
	}

	public SecurityToken setGeographyXGeographySecurityTokenList(List<GeographyXGeographySecurityToken> geographyXGeographySecurityTokenList)
	{
		this.geographyXGeographySecurityTokenList = geographyXGeographySecurityTokenList;
		return this;
	}

	public SecurityToken setArrangementXArrangementSecurityTokenList(List<ArrangementXArrangementSecurityToken> arrangementXArrangementSecurityTokenList)
	{
		this.arrangementXArrangementSecurityTokenList = arrangementXArrangementSecurityTokenList;
		return this;
	}

	public SecurityToken setGeographySecurityTokenList(List<GeographySecurityToken> geographySecurityTokenList)
	{
		this.geographySecurityTokenList = geographySecurityTokenList;
		return this;
	}

	public SecurityToken setInvolvedPartyXInvolvedPartySecurityTokenList(List<InvolvedPartyXInvolvedPartySecurityToken> involvedPartyXInvolvedPartySecurityTokenList)
	{
		this.involvedPartyXInvolvedPartySecurityTokenList = involvedPartyXInvolvedPartySecurityTokenList;
		return this;
	}

	public SecurityToken setArrangementXClassificationSecurityTokenList(List<ArrangementXClassificationSecurityToken> arrangementXClassificationSecurityTokenList)
	{
		this.arrangementXClassificationSecurityTokenList = arrangementXClassificationSecurityTokenList;
		return this;
	}

	public SecurityToken setInvolvedPartyXAddressSecurityTokenList(List<InvolvedPartyXAddressSecurityToken> involvedPartyXAddressSecurityTokenList)
	{
		this.involvedPartyXAddressSecurityTokenList = involvedPartyXAddressSecurityTokenList;
		return this;
	}

	public SecurityToken setAddressXGeographySecurityTokenList(List<AddressXGeographySecurityToken> addressXGeographySecurityTokenList)
	{
		this.addressXGeographySecurityTokenList = addressXGeographySecurityTokenList;
		return this;
	}

	public SecurityToken setInvolvedPartyNonOrganicSecurityTokenList(List<InvolvedPartyNonOrganicSecurityToken> involvedPartyNonOrganicSecurityTokenList)
	{
		this.involvedPartyNonOrganicSecurityTokenList = involvedPartyNonOrganicSecurityTokenList;
		return this;
	}

	public SecurityToken setResourceItemSecurityTokenList(List<ResourceItemSecurityToken> resourceItemSecurityTokenList)
	{
		this.resourceItemSecurityTokenList = resourceItemSecurityTokenList;
		return this;
	}

	public SecurityToken setClassificationXClassificationSecurityTokenList(List<ClassificationXClassificationSecurityToken> classificationXClassificationSecurityTokenList)
	{
		this.classificationXClassificationSecurityTokenList = classificationXClassificationSecurityTokenList;
		return this;
	}

	public SecurityToken setEventTypesSecurityTokenList(List<EventTypesSecurityToken> eventTypesSecurityTokenList)
	{
		this.eventTypesSecurityTokenList = eventTypesSecurityTokenList;
		return this;
	}

	public SecurityToken setClassificationSecurityTokenList(List<ClassificationSecurityToken> classificationSecurityTokenList)
	{
		this.classificationSecurityTokenList = classificationSecurityTokenList;
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
		SecurityToken that = (SecurityToken) o;
		return Objects.equals(getSecurityToken(), that.getSecurityToken());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getSecurityToken());
	}

	public Long getId()
	{
		return this.id;
	}

	public @NotNull @Size(min = 1,
			max = 128) String getSecurityToken()
	{
		return this.securityToken;
	}

	public @NotNull String getName()
	{
		return this.name;
	}

	public @NotNull String getDescription()
	{
		return this.description;
	}

	public Classification getSecurityTokenClassificationID()
	{
		return this.securityTokenClassificationID;
	}

	public SecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public SecurityToken setSecurityToken(@NotNull @Size(min = 1,
			max = 128) String securityToken)
	{
		this.securityToken = securityToken;
		return this;
	}

	public SecurityToken setName(@NotNull String name)
	{
		this.name = name;
		return this;
	}

	public SecurityToken setDescription(@NotNull String description)
	{
		this.description = description;
		return this;
	}

	public SecurityToken setSecurityTokenClassificationID(Classification securityTokenClassificationID)
	{
		this.securityTokenClassificationID = securityTokenClassificationID;
		return this;
	}
}
