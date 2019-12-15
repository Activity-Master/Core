package com.guicedee.activitymaster.core.db.entities.classifications;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.classifications.builders.ClassificationQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.hierarchies.ClassificationHierarchyView;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsHierarchy;
import com.guicedee.activitymaster.core.services.capabilities.IContainsResourceItems;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IResourceItem;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.guicedinjection.GuiceContext;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Classification",
		name = "Classification")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class Classification
		extends WarehouseTable<Classification, ClassificationQueryBuilder, Long, ClassificationSecurityToken>
		implements IContainsHierarchy<Classification, ClassificationXClassification, ClassificationHierarchyView>,
				           IContainsResourceItems<Classification, ResourceItem, ClassificationXResourceItem, IResourceItemClassification<?>, IClassification<?>, IResourceItem<?>, Classification>,
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
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ClassificationSecurityToken> securities;

/*

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
	private List<GeographyXGeography> geographyXGeographyList;*/

	public Classification()
	{

	}

	public Classification(Long classificationID)
	{
		id = classificationID;
	}

	public Classification(Long classificationID, String classificationName, String classificationDesc, short classificationSequenceNumber)
	{
		id = classificationID;
		name = classificationName;
		description = classificationDesc;
		this.classificationSequenceNumber = classificationSequenceNumber;
	}

	@Override
	public String toString()
	{
		return "Classification - " + getName() + " - " + getDescription();
	}

	@Override
	protected ClassificationSecurityToken configureDefaultsForNewToken(ClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
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

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getDescription()
	{
		return description;
	}

	public @NotNull Short getClassificationSequenceNumber()
	{
		return classificationSequenceNumber;
	}

	public ClassificationDataConcept getConcept()
	{
		return concept;
	}

	@Override
	public Classification setId(Long id)
	{
		this.id = id;
		return this;
	}

	@Override
	public Classification setName(String name)
	{
		this.name = name;
		return this;
	}

	@Override
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
