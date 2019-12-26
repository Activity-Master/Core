package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.entityassist.enumerations.Operand;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXInvolvedParty;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXInvolvedParty_;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.events.EventXInvolvedParty;
import com.guicedee.activitymaster.core.db.entities.events.EventXInvolvedParty_;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.classifications.involvedparty.IInvolvedPartyClassification;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.systems.InvolvedPartySystem;
import com.guicedee.activitymaster.core.services.enumtypes.IIdentificationType;
import com.guicedee.activitymaster.core.services.enumtypes.INameType;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;
import com.guicedee.activitymaster.core.services.types.IdentificationTypes;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

import static com.entityassist.enumerations.Operand.*;
import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Party",
		name = "InvolvedParty")
@XmlRootElement
@Access(FIELD)
public class InvolvedParty
		extends WarehouseTable<InvolvedParty, InvolvedPartyQueryBuilder, Long, InvolvedPartySecurityToken>
		implements IContainsClassifications<InvolvedParty, Classification, InvolvedPartyXClassification, IInvolvedPartyClassification<?>, IInvolvedParty<?>, IClassification<?>, InvolvedParty>,
				           IContainsResourceItems<InvolvedParty, ResourceItem, InvolvedPartyXResourceItem, IResourceItemClassification<?>, IInvolvedParty<?>, IResourceItem<?>, InvolvedParty>,
				           IContainsInvolvedPartyIdentificationTypes<InvolvedParty, InvolvedPartyIdentificationType, InvolvedPartyXInvolvedPartyIdentificationType, IIdentificationType<?>, IInvolvedParty<?>, IInvolvedPartyIdentificationType<?>, InvolvedParty>,
				           IContainsInvolvedPartyNameTypes<InvolvedParty, InvolvedPartyNameType, InvolvedPartyXInvolvedPartyNameType, INameType<?>, IInvolvedParty<?>, IInvolvedPartyNameType<?>, InvolvedParty>,
				           IContainsInvolvedPartyTypes<InvolvedParty, InvolvedPartyType, InvolvedPartyXInvolvedPartyType, ITypeValue<?>, IInvolvedParty<?>, IInvolvedPartyType<?>, InvolvedParty>,
				           IContainsAddresses<InvolvedParty, Address, InvolvedPartyXAddress, IAddressClassification<?>, IInvolvedParty<?>, IAddress<?>, InvolvedParty>,
				           IActivityMasterEntity<InvolvedParty>,
				           IContainsEnterprise<InvolvedParty>,
				           IInvolvedParty<InvolvedParty>
{
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(InvolvedParty.class.getName());
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyID")
	@JsonValue
	private Long id;

	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyXClassification> classifications;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyXInvolvedPartyNameType> names;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyXResourceItem> resources;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ArrangementXInvolvedParty> arrangements;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventXInvolvedParty> events;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartySecurityToken> securities;

	@OneToOne(
			mappedBy = "involvedParty",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private InvolvedPartyOrganic involvedPartyOrganic;
	@OneToOne(
			mappedBy = "involvedParty",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private InvolvedPartyNonOrganic involvedPartyNonOrganic;

	@OneToMany(
			mappedBy = "childInvolvedPartyID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList;
	@OneToMany(
			mappedBy = "parentInvolvedPartyID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList1;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyXInvolvedPartyType> types;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyXProduct> products;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyXAddress> addresses;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyXInvolvedPartyIdentificationType> identities;

	public InvolvedParty()
	{

	}

	public InvolvedParty(Long involvedPartyID)
	{
		this.id = involvedPartyID;
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public InvolvedParty move(IInvolvedParty<?> destination)
	{
		InvolvedParty dest = (InvolvedParty) destination;
		dest.setOriginalSourceSystemUniqueID(getId().toString());

		ISystems<?> originatingSystem = InvolvedPartySystem.getSystemsMap()
		                                                   .get(dest.getEnterprise());
		UUID identityToken = InvolvedPartySystem.getSystemTokens()
		                                        .get(dest.getEnterprise());

		InvolvedParty me = builder().getEntityManager()
		                            .find(InvolvedParty.class, getId());

		List<InvolvedPartyXClassification> classifications = new InvolvedPartyXClassification()
				                                                    .builder()
				                                                    .inDateRange()
				                                                    .inActiveRange(getEnterprise(),identityToken)
				                                                    .where(InvolvedPartyXClassification_.involvedPartyID, Equals, this)
				                                                    .getAll();
		for (InvolvedPartyXClassification item : classifications)
		{
			item.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                              .getDeletedFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                  .get(getEnterprise())));
			item.setEffectiveToDate(LocalDateTime.now());
			item.update();

			InvolvedPartyXClassification newItem = new InvolvedPartyXClassification();
			newItem.setValue(item.getValue());
			newItem.setEnterpriseID(item.getEnterpriseID());
			newItem.setOriginalSourceSystemID(item.getOriginalSourceSystemID());
			newItem.setSystemID(item.getSystemID());
			newItem.setEnterpriseID(item.getEnterpriseID());
			newItem.setClassificationID(item.getClassificationID());
			newItem.setInvolvedPartyID(dest);
			newItem.setEffectiveFromDate(LocalDateTime.now());
			newItem.setEffectiveToDate(EndOfTime);
			newItem.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                                 .getActiveFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                    .get(getEnterprise())));
			newItem.persist();
		}
		//not names?
		/*for (InvolvedPartyXInvolvedPartyNameType item : me.names)
		{

		}*/
		List<InvolvedPartyXResourceItem> resources = new InvolvedPartyXResourceItem()
				                                                     .builder()
				                                                     .inDateRange()
				                                                     .inActiveRange(getEnterprise(),identityToken)
				                                                     .where(InvolvedPartyXResourceItem_.involvedPartyID, Equals, this)
				                                                     .getAll();
		for (InvolvedPartyXResourceItem item : resources)
		{
			item.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                              .getDeletedFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                  .get(getEnterprise())));
			item.setEffectiveToDate(LocalDateTime.now());
			item.update();

			InvolvedPartyXResourceItem newItem = new InvolvedPartyXResourceItem();
			newItem.setValue(item.getValue());
			newItem.setEnterpriseID(item.getEnterpriseID());
			newItem.setOriginalSourceSystemID(item.getOriginalSourceSystemID());
			newItem.setSystemID(item.getSystemID());
			newItem.setResourceItemID(item.getResourceItemID());
			newItem.setEnterpriseID(item.getEnterpriseID());
			newItem.setClassificationID(item.getClassificationID());
			newItem.setInvolvedPartyID(dest);
			newItem.setEffectiveFromDate(LocalDateTime.now());
			newItem.setEffectiveToDate(EndOfTime);
			newItem.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                                 .getActiveFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                    .get(getEnterprise())));
			newItem.persist();
		}
		List<ArrangementXInvolvedParty> arrangements = new ArrangementXInvolvedParty()
				                                             .builder()
				                                             .inDateRange()
				                                             .inActiveRange(getEnterprise(),identityToken)
				                                             .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, this)
				                                             .getAll();
		for (ArrangementXInvolvedParty item : arrangements)
		{
			item.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                              .getDeletedFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                  .get(getEnterprise())));
			item.setEffectiveToDate(LocalDateTime.now());
			item.update();

			ArrangementXInvolvedParty newItem = new ArrangementXInvolvedParty();
			newItem.setValue(item.getValue());
			newItem.setEnterpriseID(item.getEnterpriseID());
			newItem.setOriginalSourceSystemID(item.getOriginalSourceSystemID());
			newItem.setSystemID(item.getSystemID());
			newItem.setEnterpriseID(item.getEnterpriseID());
			newItem.setClassificationID(item.getClassificationID());
			newItem.setInvolvedPartyID(dest);
			newItem.setEffectiveFromDate(LocalDateTime.now());
			newItem.setEffectiveToDate(EndOfTime);
			newItem.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                                 .getActiveFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                    .get(getEnterprise())));
			newItem.persist();
		}
		List<EventXInvolvedParty> events = new EventXInvolvedParty()
				                                               .builder()
				                                               .inDateRange()
				                                               .inActiveRange(getEnterprise(),identityToken)
				                                               .where(EventXInvolvedParty_.involvedPartyID, Equals, this)
				                                               .getAll();
		for (EventXInvolvedParty item : events)
		{
			item.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                              .getDeletedFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                  .get(getEnterprise())));
			item.setEffectiveToDate(LocalDateTime.now());
			item.update();

			EventXInvolvedParty newItem = new EventXInvolvedParty();
			newItem.setValue(item.getValue());
			newItem.setEnterpriseID(item.getEnterpriseID());
			newItem.setOriginalSourceSystemID(item.getOriginalSourceSystemID());
			newItem.setSystemID(item.getSystemID());
			newItem.setEnterpriseID(item.getEnterpriseID());
			newItem.setClassificationID(item.getClassificationID());
			newItem.setInvolvedPartyID(dest);
			newItem.setEffectiveFromDate(LocalDateTime.now());
			newItem.setEffectiveToDate(EndOfTime);
			newItem.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                                 .getActiveFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                    .get(getEnterprise())));
			newItem.persist();
		}
		List<InvolvedPartyXInvolvedPartyType> types = new InvolvedPartyXInvolvedPartyType()
				                                   .builder()
				                                   .inDateRange()
				                                   .inActiveRange(getEnterprise(),identityToken)
				                                   .where(InvolvedPartyXInvolvedPartyType_.involvedPartyID, Equals, this)
				                                   .getAll();
		for (InvolvedPartyXInvolvedPartyType item : types)
		{
			item.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                              .getDeletedFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                  .get(getEnterprise())));
			item.setEffectiveToDate(LocalDateTime.now());
			item.update();

			InvolvedPartyXInvolvedPartyType newItem = new InvolvedPartyXInvolvedPartyType();
			newItem.setValue(item.getValue());
			newItem.setEnterpriseID(item.getEnterpriseID());
			newItem.setOriginalSourceSystemID(item.getOriginalSourceSystemID());
			newItem.setSystemID(item.getSystemID());
			newItem.setEnterpriseID(item.getEnterpriseID());
			//	newItem.setClassificationID(item.getClassificationID());
			newItem.setInvolvedPartyID(dest);
			newItem.setEffectiveFromDate(LocalDateTime.now());
			newItem.setEffectiveToDate(EndOfTime);
			newItem.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                                 .getActiveFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                    .get(getEnterprise())));
			newItem.persist();
		}
		List<InvolvedPartyXAddress> addresses = new InvolvedPartyXAddress()
				                                              .builder()
				                                              .inDateRange()
				                                              .inActiveRange(getEnterprise(),identityToken)
				                                              .where(InvolvedPartyXAddress_.involvedPartyID, Equals, this)
				                                              .getAll();
		for (InvolvedPartyXAddress item : addresses)
		{
			item.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                              .getDeletedFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                  .get(getEnterprise())));
			item.setEffectiveToDate(LocalDateTime.now());
			item.update();

			InvolvedPartyXAddress newItem = new InvolvedPartyXAddress();
			newItem.setValue(item.getValue());
			newItem.setAddressID(item.getAddressID());
			newItem.setEnterpriseID(item.getEnterpriseID());
			newItem.setOriginalSourceSystemID(item.getOriginalSourceSystemID());
			newItem.setSystemID(item.getSystemID());
			newItem.setEnterpriseID(item.getEnterpriseID());
			newItem.setClassificationID(item.getClassificationID());
			newItem.setInvolvedPartyID(dest);
			newItem.setEffectiveFromDate(LocalDateTime.now());
			newItem.setEffectiveToDate(EndOfTime);
			newItem.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                                 .getActiveFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                    .get(getEnterprise())));
			newItem.persist();
		}
		List<InvolvedPartyXProduct> products = new InvolvedPartyXProduct()
				                                        .builder()
				                                        .inDateRange()
				                                        .inActiveRange(getEnterprise(),identityToken)
				                                        .where(InvolvedPartyXProduct_.involvedPartyID, Equals, this)
				                                        .getAll();
		for (InvolvedPartyXProduct item : products)
		{
			item.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                              .getDeletedFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                  .get(getEnterprise())));
			item.setEffectiveToDate(LocalDateTime.now());
			item.update();

			InvolvedPartyXProduct newItem = new InvolvedPartyXProduct();
			newItem.setValue(item.getValue());
			newItem.setProductID(item.getProductID());
			newItem.setEnterpriseID(item.getEnterpriseID());
			newItem.setOriginalSourceSystemID(item.getOriginalSourceSystemID());
			newItem.setSystemID(item.getSystemID());
			newItem.setEnterpriseID(item.getEnterpriseID());
			newItem.setClassificationID(item.getClassificationID());
			newItem.setInvolvedPartyID(dest);
			newItem.setEffectiveFromDate(LocalDateTime.now());
			newItem.setEffectiveToDate(EndOfTime);
			newItem.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                                 .getActiveFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                    .get(getEnterprise())));
			newItem.persist();
		}
		List<InvolvedPartyXInvolvedPartyIdentificationType> identities = new InvolvedPartyXInvolvedPartyIdentificationType()
				                                       .builder()
				                                       .inDateRange()
				                                       .inActiveRange(getEnterprise(),identityToken)
				                                       .where(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyID, Equals, this)
				                                       .getAll();
		for (InvolvedPartyXInvolvedPartyIdentificationType item : identities)
		{
			item.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                              .getDeletedFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                  .get(getEnterprise())));
			item.setEffectiveToDate(LocalDateTime.now());
			item.update();

			InvolvedPartyXInvolvedPartyIdentificationType newItem = new InvolvedPartyXInvolvedPartyIdentificationType();
			newItem.setValue(item.getValue());
			newItem.setInvolvedPartyIdentificationTypeID(item.getInvolvedPartyIdentificationTypeID());
			newItem.setEnterpriseID(item.getEnterpriseID());
			newItem.setOriginalSourceSystemID(item.getOriginalSourceSystemID());
			newItem.setSystemID(item.getSystemID());
			newItem.setEnterpriseID(item.getEnterpriseID());
			//	newItem.setClassificationID(item.getClassificationID());
			newItem.setInvolvedPartyID(dest);
			newItem.setEffectiveFromDate(LocalDateTime.now());
			newItem.setEffectiveToDate(EndOfTime);
			newItem.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                                 .getActiveFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                    .get(getEnterprise())));
			newItem.persist();
		}

		if (me.involvedPartyOrganic != null)
		{
			InvolvedPartyOrganic item = me.involvedPartyOrganic;
			item.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                              .getDeletedFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                  .get(getEnterprise())));
			item.setEffectiveToDate(LocalDateTime.now());
			item.update();
		}
		else if (me.involvedPartyNonOrganic != null)
		{
			InvolvedPartyNonOrganic item = me.involvedPartyNonOrganic;
			item.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                              .getDeletedFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
			                                                                                                  .get(getEnterprise())));
			item.setEffectiveToDate(LocalDateTime.now());
			item.update();
		}
		setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                         .getDeletedFlag(getEnterprise(), InvolvedPartySystem.getSystemTokens()
		                                                                                             .get(getEnterprise())));
		setEffectiveToDate(LocalDateTime.now());
		update();
		return dest;
	}

	@Override
	public UUID getSecurityIdentity()
	{
		String value = find(IdentificationTypes.IdentificationTypeUUID, InvolvedPartySystem.getSystemsMap()
		                                                                                   .get(getEnterpriseID()), InvolvedPartySystem.getSystemTokens()
		                                                                                                                               .get(getEnterpriseID())).orElseThrow()
		                                                                                                                                                       .getValue();
		return UUID.fromString(value);
	}

	@Override
	protected InvolvedPartySecurityToken configureDefaultsForNewToken(InvolvedPartySecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(InvolvedPartyXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setInvolvedPartyID(this);
	}

	@Override
	public void configureResourceItemLinkValue(InvolvedPartyXResourceItem linkTable, InvolvedParty primary, ResourceItem secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setResourceItemID(secondary);
		linkTable.setInvolvedPartyID(this);
	}

	@Override
	public void configureAddressLinkValue(InvolvedPartyXAddress linkTable, InvolvedParty primary, Address secondary, IClassification classificationValue, String value, IEnterprise enterprise)
	{
		linkTable.setInvolvedPartyID(this);
		linkTable.setAddressID(secondary);
	}

	@Override
	public void configureInvolvedPartyIdentificationType(InvolvedPartyXInvolvedPartyIdentificationType linkTable, InvolvedParty primary, InvolvedPartyIdentificationType secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setInvolvedPartyID(this);
		linkTable.setInvolvedPartyIdentificationTypeID(secondary);
	}

	@Override
	public void configureInvolvedPartyNameTypeLinkValue(InvolvedPartyXInvolvedPartyNameType linkTable, InvolvedParty primary, InvolvedPartyNameType secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setInvolvedPartyID(this);
		linkTable.setInvolvedPartyNameTypeID(secondary);
	}

	@Override
	public void configureInvolvedPartyTypeLinkValue(InvolvedPartyXInvolvedPartyType linkTable, InvolvedParty primary, InvolvedPartyType secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setInvolvedPartyID(this);
		linkTable.setInvolvedPartyTypeID(secondary);
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
		InvolvedParty that = (InvolvedParty) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public String toString()
	{
		return "InvolvedParty - " + getId();
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedParty setId(Long id)
	{
		this.id = id;
		return this;
	}

}
