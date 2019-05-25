package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXInvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXInvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.capabilities.*;
import com.armineasy.activitymaster.activitymaster.services.classifications.involvedparty.IInvolvedPartyClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.java.Log;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

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
@Log
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class InvolvedParty
		extends WarehouseTable<InvolvedParty, InvolvedPartyQueryBuilder, Long, InvolvedPartySecurityToken>
		implements IContainsClassifications<InvolvedParty, Classification, InvolvedPartyXClassification, IInvolvedPartyClassification>,
				           IContainsResourceItems<InvolvedParty, ResourceItem, InvolvedPartyXResourceItem>,
				           IContainsInvolvedPartyIdentificationTypes<InvolvedParty, InvolvedPartyIdentificationType, InvolvedPartyXInvolvedPartyIdentificationType>,
				           IContainsInvolvedPartyNameTypes<InvolvedParty, InvolvedPartyNameType, InvolvedPartyXInvolvedPartyNameType>,
				           IContainsInvolvedPartyTypes<InvolvedParty, InvolvedPartyType, InvolvedPartyXInvolvedPartyType>,
				           IContainsAddresses<InvolvedParty, Address, InvolvedPartyXAddress>,
				           IActivityMasterEntity<InvolvedParty>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyID")
	@Getter
	@Setter
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
	protected InvolvedPartySecurityToken configureDefaultsForNewToken(InvolvedPartySecurityToken stAdmin, IEnterprise enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(InvolvedPartyXClassification classificationLink, Enterprise enterprise)
	{
		classificationLink.setInvolvedPartyID(this);
	}

	@Override
	public void setMyResourceItemLinkValue(InvolvedPartyXResourceItem classificationLink, ResourceItem resourceItem, Enterprise enterprise)
	{
		classificationLink.setResourceItemID(resourceItem);
		classificationLink.setInvolvedPartyID(this);
	}

	@Override
	public void setMyInvolvedPartyIdentificationTypeLinkValue(InvolvedPartyXInvolvedPartyIdentificationType classificationLink, InvolvedPartyIdentificationType identificationType, Enterprise enterprise)
	{
		classificationLink.setInvolvedPartyID(this);
		classificationLink.setInvolvedPartyIdentificationTypeID(identificationType);
	}

	@Override
	public void setMyInvolvedPartyNameTypeLinkValue(InvolvedPartyXInvolvedPartyNameType classificationLink, InvolvedPartyNameType identificationType, Enterprise enterprise)
	{
		classificationLink.setInvolvedPartyID(this);
		classificationLink.setInvolvedPartyNameTypeID(identificationType);
	}

	@Override
	public void setMyInvolvedPartyTypeLinkValue(InvolvedPartyXInvolvedPartyType classificationLink, InvolvedPartyType identificationType, Enterprise enterprise)
	{
		classificationLink.setInvolvedPartyID(this);
		classificationLink.setInvolvedPartyTypeID(identificationType);
	}

	@Override
	public void setMyAddressLinkValue(InvolvedPartyXAddress classificationLink, Address geography, Enterprise enterprise)
	{
		classificationLink.setInvolvedPartyID(this);
		classificationLink.setAddressID(geography);
	}
}
