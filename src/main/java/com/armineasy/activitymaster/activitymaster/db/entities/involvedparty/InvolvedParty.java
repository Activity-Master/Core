package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXInvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXInvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.*;
import com.armineasy.activitymaster.activitymaster.services.classifications.involvedparty.IInvolvedPartyClassification;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.*;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.*;
import com.armineasy.activitymaster.activitymaster.systems.InvolvedPartySystem;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

import static com.armineasy.activitymaster.activitymaster.services.types.IdentificationTypes.*;
import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "InvolvedParty")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class InvolvedParty
		extends WarehouseTable<InvolvedParty, InvolvedPartyQueryBuilder, Long, InvolvedPartySecurityToken>
		implements IContainsClassifications<InvolvedParty, Classification, InvolvedPartyXClassification, IInvolvedPartyClassification<?>, IInvolvedParty<?>,IClassification<?>, InvolvedParty>,
				           IContainsResourceItems<InvolvedParty, ResourceItem, InvolvedPartyXResourceItem, IResourceItemClassification<?>,IInvolvedParty<?>,IResourceItem<?>, InvolvedParty>,
				           IContainsInvolvedPartyIdentificationTypes<InvolvedParty, InvolvedPartyIdentificationType, InvolvedPartyXInvolvedPartyIdentificationType, IIdentificationType<?>,IInvolvedParty<?>,IInvolvedPartyIdentificationType<?>, InvolvedParty>,
				           IContainsInvolvedPartyNameTypes<InvolvedParty, InvolvedPartyNameType, InvolvedPartyXInvolvedPartyNameType, INameType<?>,IInvolvedParty<?>,IInvolvedPartyNameType<?>, InvolvedParty>,
				           IContainsInvolvedPartyTypes<InvolvedParty, InvolvedPartyType, InvolvedPartyXInvolvedPartyType, ITypeValue<?>,IInvolvedParty<?>,IInvolvedPartyType<?>, InvolvedParty>,
				           IContainsAddresses<InvolvedParty, Address, InvolvedPartyXAddress>,
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
	private Long id;

	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXClassification> classifications;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyNameType> names;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXResourceItem> resources;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	private List<ArrangementXInvolvedParty> arrangements;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	private List<EventXInvolvedParty> events;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartySecurityToken> securities;


	@OneToOne(
			mappedBy = "involvedParty",
			fetch = FetchType.LAZY)
	private InvolvedPartyOrganic involvedPartyOrganic;
	@OneToOne(
			mappedBy = "involvedParty",
			fetch = FetchType.LAZY)
	private InvolvedPartyNonOrganic involvedPartyNonOrganic;


	@OneToMany(
			mappedBy = "childInvolvedPartyID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList;
	@OneToMany(
			mappedBy = "parentInvolvedPartyID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList1;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyType> types;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProduct> products;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddress> addresses;
	@OneToMany(
			mappedBy = "involvedPartyID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyIdentificationType> identities;

	public InvolvedParty()
	{

	}

	public InvolvedParty(Long involvedPartyID)
	{
		this.id = involvedPartyID;
	}

	@Override
	public UUID getSecurityIdentity()
	{
		String value = find(IdentificationTypeUUID, InvolvedPartySystem.getNewSystem()
		                                                               .get(getEnterpriseID()), InvolvedPartySystem.getSystemTokens()
		                                                                                                           .get(getEnterpriseID())).orElseThrow()
		                                                                                                                                   .getValue();
		return UUID.fromString(value);
	}

	@Override
	protected InvolvedPartySecurityToken configureDefaultsForNewToken(InvolvedPartySecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
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
	public void setMyAddressLinkValue(InvolvedPartyXAddress classificationLink, Address geography, IEnterprise<?> enterprise)
	{
		classificationLink.setInvolvedPartyID(this);
		classificationLink.setAddressID(geography);
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
