package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXArrangement;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.*;
import com.armineasy.activitymaster.activitymaster.services.classifications.arrangement.IArrangementClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IArrangement;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IArrangementTypes;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
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
@Table(name = "Arrangement")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Access(FIELD)
@lombok.Data
public class Arrangement
		extends WarehouseTable<Arrangement, ArrangementQueryBuilder, Long, ArrangementSecurityToken>
		implements IContainsClassifications<Arrangement, Classification, ArrangementXClassification, IArrangementClassification<?>, Arrangement>,
				           IContainsResourceItems<Arrangement, ResourceItem, ArrangementXResourceItem>,
				           IActivityMasterEntity<Arrangement>,
				           IContainsArrangementTypes<Arrangement, ArrangementType, ArrangementXArrangementType, IArrangementTypes<?>, Arrangement>,
				           IContainsInvolvedParties<Arrangement, InvolvedParty, ArrangementXInvolvedParty>,
				           IArrangement<Arrangement>,
				           IContainsActiveFlags<Arrangement>,
				           IContainsEnterprise<Arrangement>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementID")
	@Getter
	@Setter
	private Long id;

	@OneToMany(
			mappedBy = "arrangementID",
			fetch = FetchType.LAZY)
	private List<ArrangementXClassification> classifications;

	@OneToMany(
			mappedBy = "arrangementID",
			fetch = FetchType.LAZY)
	private List<ArrangementXInvolvedParty> parties;
	@OneToMany(
			mappedBy = "arrangementID",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItem> resources;
	@OneToMany(
			mappedBy = "arrangementID",
			fetch = FetchType.LAZY)
	private List<EventXArrangement> events;

	@OneToMany(
			mappedBy = "childArrangementID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangement> arrangementXArrangementList;
	@OneToMany(
			mappedBy = "parentArrangementID",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangement> arrangementXArrangementList1;
	@OneToMany(
			mappedBy = "arrangementID",
			fetch = FetchType.LAZY)
	private List<ArrangementXProduct> products;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementSecurityToken> securities;

	@OneToMany(
			fetch = FetchType.LAZY,
			mappedBy = "arrangement")
	private List<ArrangementXArrangementType> types;

	public Arrangement()
	{

	}

	public Arrangement(Long arrangementID)
	{
		this.id = arrangementID;
	}

	@Override
	protected ArrangementSecurityToken configureDefaultsForNewToken(ArrangementSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(ArrangementXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setArrangementID(this);
	}

	@Override
	public void setMyResourceItemLinkValue(ArrangementXResourceItem classificationLink, ResourceItem resourceItem, IEnterprise<?> enterprise)
	{
		classificationLink.setArrangementID(this);
		classificationLink.setResourceItemID(resourceItem);
	}

	@Override
	public void setMyInvolvedPartyLinkValue(ArrangementXInvolvedParty classificationLink, InvolvedParty involvedParty, IEnterprise<?> enterprise)
	{
		classificationLink.setArrangementID(this);
		classificationLink.setInvolvedPartyID(involvedParty);
	}

	@Override
	public void configureArrangementType(ArrangementXArrangementType linkTable, Arrangement primary, ArrangementType secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setArrangement(primary);
		linkTable.setType(secondary);
	}

	@Override
	public Arrangement remove()
	{
		if (classifications != null)
		{
			for (ArrangementXClassification classification : classifications)
			{
				classification.remove();
			}
		}
		if (products != null)
		{
			for (ArrangementXProduct product : products)
			{
				product.remove();
			}
		}
		if (securities != null)
		{
			for (ArrangementSecurityToken security : securities)
			{
				security.remove();
			}
		}
		if (types != null)
		{
			for (ArrangementXArrangementType type : types)
			{
				type.remove();
			}
		}
		if (arrangementXArrangementList != null)
		{
			for (ArrangementXArrangement arrangementXArrangement : arrangementXArrangementList)
			{
				arrangementXArrangement.remove();
			}
		}
		if (arrangementXArrangementList1 != null)
		{
			for (ArrangementXArrangement arrangementXArrangement : arrangementXArrangementList1)
			{
				arrangementXArrangement.remove();
			}
		}
		return super.remove();
	}
}
