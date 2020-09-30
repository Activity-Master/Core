package com.guicedee.activitymaster.core.db.entities.arrangement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.events.EventXArrangement;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.hierarchies.ArrangementsHierarchyView;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.arrangement.IArrangementClassification;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IArrangementTypes;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Arrangement",
       name = "Arrangement")
@XmlRootElement

@Access(FIELD)
public class Arrangement
		extends WarehouseTable<Arrangement, ArrangementQueryBuilder, Long, ArrangementSecurityToken>
		implements IContainsClassifications<Arrangement, Classification, ArrangementXClassification, IArrangementClassification<?>, IArrangement<?>, IClassification<?>, Arrangement>,
		           IContainsResourceItems<Arrangement, ResourceItem, ArrangementXResourceItem, IClassificationValue<?>, IArrangement<?>, IResourceItem<?>, Arrangement>,
		           IActivityMasterEntity<Arrangement>,
		           IContainsArrangementTypes<Arrangement, ArrangementType, ArrangementXArrangementType, IArrangementTypes<?>, IArrangement<?>, IArrangementType<?>, Arrangement>,
		           IContainsInvolvedParties<Arrangement, InvolvedParty, ArrangementXInvolvedParty, IClassificationValue<?>, IArrangement<?>, IInvolvedParty<?>, Arrangement>,
		           IHasActiveFlags<Arrangement>,
		           IContainsEnterprise<Arrangement>,
		           IContainsHierarchy<Arrangement,ArrangementXArrangement, ArrangementsHierarchyView,IArrangement<Arrangement>>,
		           IArrangement<Arrangement>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
	        name = "ArrangementID")
	@JsonValue
	private Long id;
	
	@OneToMany(
			mappedBy = "arrangementID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ArrangementXClassification> classifications;
	
	@OneToMany(
			mappedBy = "arrangementID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ArrangementXInvolvedParty> parties;
	@OneToMany(
			mappedBy = "arrangementID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ArrangementXResourceItem> resources;
	@OneToMany(
			mappedBy = "arrangementID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventXArrangement> events;
	
	@OneToMany(
			mappedBy = "childArrangementID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ArrangementXArrangement> arrangementXArrangementList;
	@OneToMany(
			mappedBy = "parentArrangementID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ArrangementXArrangement> arrangementXArrangementList1;
	@OneToMany(
			mappedBy = "arrangementID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ArrangementXProduct> products;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ArrangementSecurityToken> securities;
	
	@OneToMany(
			fetch = FetchType.LAZY,
			mappedBy = "arrangement")
	@JsonIgnore
	private List<ArrangementXArrangementType> types;
	
	public Arrangement()
	{
	
	}
	
	public Arrangement(Long arrangementID)
	{
		this.id = arrangementID;
	}
	
	@Override
	protected ArrangementSecurityToken configureDefaultsForNewToken(ArrangementSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
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
	public void configureResourceItemLinkValue(ArrangementXResourceItem linkTable, Arrangement primary, ResourceItem secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setArrangementID(this);
		linkTable.setResourceItemID(secondary);
	}
	
	@Override
	public void configureResourceItemAddable(ArrangementXResourceItem linkTable, Arrangement primary, ResourceItem secondary, IClassificationValue<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setArrangementID(this);
		linkTable.setResourceItemID(secondary);
	}
	
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
	
	public List<ArrangementXClassification> getClassifications()
	{
		return this.classifications;
	}
	
	public List<ArrangementXInvolvedParty> getParties()
	{
		return this.parties;
	}
	
	public List<ArrangementXResourceItem> getResources()
	{
		return this.resources;
	}
	
	public List<EventXArrangement> getEvents()
	{
		return this.events;
	}
	
	public List<ArrangementXArrangement> getArrangementXArrangementList()
	{
		return this.arrangementXArrangementList;
	}
	
	public List<ArrangementXArrangement> getArrangementXArrangementList1()
	{
		return this.arrangementXArrangementList1;
	}
	
	public List<ArrangementXProduct> getProducts()
	{
		return this.products;
	}
	
	public List<ArrangementSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public List<ArrangementXArrangementType> getTypes()
	{
		return this.types;
	}
	
	public Arrangement setClassifications(List<ArrangementXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}
	
	public Arrangement setParties(List<ArrangementXInvolvedParty> parties)
	{
		this.parties = parties;
		return this;
	}
	
	public Arrangement setResources(List<ArrangementXResourceItem> resources)
	{
		this.resources = resources;
		return this;
	}
	
	public Arrangement setEvents(List<EventXArrangement> events)
	{
		this.events = events;
		return this;
	}
	
	public Arrangement setArrangementXArrangementList(List<ArrangementXArrangement> arrangementXArrangementList)
	{
		this.arrangementXArrangementList = arrangementXArrangementList;
		return this;
	}
	
	public Arrangement setArrangementXArrangementList1(List<ArrangementXArrangement> arrangementXArrangementList1)
	{
		this.arrangementXArrangementList1 = arrangementXArrangementList1;
		return this;
	}
	
	public Arrangement setProducts(List<ArrangementXProduct> products)
	{
		this.products = products;
		return this;
	}
	
	public Arrangement setSecurities(List<ArrangementSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public Arrangement setTypes(List<ArrangementXArrangementType> types)
	{
		this.types = types;
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
		Arrangement that = (Arrangement) o;
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
		return "Arrangement - " + getId();
	}
	
	@Override
	public Long getId()
	{
		return this.id;
	}
	
	@Override
	public Arrangement setId(Long id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public void setMyInvolvedPartyLinkValue(ArrangementXInvolvedParty classificationLink, Arrangement first, InvolvedParty involvedParty, IEnterprise<?> enterprise)
	{
		classificationLink.setArrangementID(first);
		classificationLink.setInvolvedPartyID(involvedParty);
	}
	
	@Override
	public void configureNewHierarchyItem(ArrangementXArrangement newLink, IArrangement<Arrangement> parent, IArrangement<Arrangement> child, String value)
	{
		newLink.setParentArrangementID((Arrangement) parent);
		newLink.setChildArrangementID((Arrangement) child);
		newLink.setValue(value);
	}
}
