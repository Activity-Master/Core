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
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.jwebmp.guicedinjection.GuiceContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Classification")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Access(FIELD)@lombok.Data
public class Classification
		extends WarehouseTable<Classification, ClassificationQueryBuilder, Long, ClassificationSecurityToken>
		implements IContainsHierarchy<Classification, ClassificationXClassification, ClassificationHierarchyView>,
				           IContainsResourceItems<Classification, ResourceItem, ClassificationXResourceItem>,
				           IActivityMasterEntity<Classification>,
				           IClassification<Classification>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationID")
	@Getter
	@Setter
	private Long id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 100)
	@Column(nullable = false,
			length = 100,
			name = "ClassificationName")
	@Getter
	@Setter
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "ClassificationDesc")
	@Getter
	@Setter
	private String description;
	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@NotNull
	@Column(nullable = false,
			name = "ClassificationSequenceNumber")
	@OrderBy
	@Getter
	@Setter
	private Short classificationSequenceNumber;
	@JoinColumn(name = "ClassificationDataConceptID",
			referencedColumnName = "ClassificationDataConceptID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	@Getter
	@Setter
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
		return getName() + " - " + getDescription();
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
	public void setMyResourceItemLinkValue(ClassificationXResourceItem classificationLink, ResourceItem resourceItem, IEnterprise<?> enterprise)
	{
		classificationLink.setClassificationID(this);
		classificationLink.setResourceItemID(resourceItem);
	}

	@Override
	public void configureNewHierarchyItem(ClassificationXClassification newLink, Classification parent, Classification child, String value)
	{
		newLink.setParentClassificationID(this);
		newLink.setChildClassificationID(child);
		newLink.setEnterpriseID(getEnterpriseID());
	}
}
