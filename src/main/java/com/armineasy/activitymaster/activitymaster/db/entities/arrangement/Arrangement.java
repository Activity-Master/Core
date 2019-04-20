package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXArrangement;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;
import com.armineasy.activitymaster.activitymaster.services.classifications.arrangement.IArrangementClassification;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@Table(name = "Arrangement")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class Arrangement
		extends WarehouseTable<Arrangement, ArrangementQueryBuilder, Long, ArrangementSecurityToken>
		implements IContainsClassifications<Arrangement, Classification, ArrangementXClassification, IArrangementClassification>,
				           IContainsResourceItems<Arrangement, ResourceItem,ArrangementXResourceItem>
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
	protected ArrangementSecurityToken configureDefaultsForNewToken(ArrangementSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(ArrangementXClassification classificationLink, Enterprise enterprise)
	{
		classificationLink.setArrangementID(this);
	}

	@Override
	public void setMyResourceItemLinkValue(ArrangementXResourceItem classificationLink, ResourceItem resourceItem, Enterprise enterprise)
	{
		classificationLink.setArrangementID(this);
		classificationLink.setResourceItemID(resourceItem);
	}
}
