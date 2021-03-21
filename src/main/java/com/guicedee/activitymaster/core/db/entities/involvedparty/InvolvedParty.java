package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.address.IAddress;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.client.services.builders.warehouse.products.IProductType;
import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.classifications.types.IdentificationTypes;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXInvolvedParty;
import com.guicedee.activitymaster.core.db.entities.events.EventXInvolvedParty;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.product.ProductType;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.systems.InvolvedPartySystem;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.*;
import java.util.logging.Logger;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static com.guicedee.activitymaster.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings({"unused", "rawtypes"})
@Entity
@Table(schema = "Party",
       name = "InvolvedParty")
@XmlRootElement
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class InvolvedParty
		extends WarehouseTable<InvolvedParty, InvolvedPartyQueryBuilder, java.util.UUID>
		implements IInvolvedParty<InvolvedParty,InvolvedPartyQueryBuilder>
{
	private static final Logger log = Logger.getLogger(InvolvedParty.class.getName());
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyID")
	@JsonValue
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
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
	
	public InvolvedParty(UUID involvedPartyID)
	{
		this.id = involvedPartyID;
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public InvolvedParty moveWebClientUUIDToNewInvolvedParty(IInvolvedParty<?,?> destination, UUID newUUID)
	{
		
		//DataSource ds = GuiceContext.get(Key.get(DataSource.class, ActivityMasterDB.class));
		InvolvedParty dest = (InvolvedParty) destination;
		
	/*	InvolvedParty dest = (InvolvedParty) destination;
		try
		{
			Connection connection = ds.getConnection();
			java.sql.CallableStatement cs = connection.prepareCall("exec MoveToExistingInvolvedParty '" + getId().toString() + "','" + dest.getId()
                                                                                                                   .toString() + "'");
			cs.execute();
			cs.close();
		}
		catch (SQLException throwables)
		{
			throwables.printStackTrace();
		}
		
		remove();*/
		//new InvolvedPartyOrganic(getId()).remove();
	//	new InvolvedPartyNonOrganic(getId()).remove();
		
		
		ISystems<?,?> originatingSystem = get(InvolvedPartySystem.class).getSystem(dest.getEnterprise());
		UUID identityToken = get(InvolvedPartySystem.class).getSystemToken(dest.getEnterprise());
		
		InvolvedParty me = builder().find(getId())
		                            .get()
		                            .orElseThrow();
		
		IInvolvedPartyService<?> partyService = get(IInvolvedPartyService.class);
		IInvolvedPartyIdentificationType partyType =  partyService.findInvolvedPartyIdentificationType("IdentificationTypeWebClientUUID", getSystemID(), identityToken);
		for (var identificationTypeWebClientUUID : partyService.findAllByIdentificationType("IdentificationTypeWebClientUUID", newUUID.toString()))
		{
			identificationTypeWebClientUUID.expire();
		}
		destination.addInvolvedPartyIdentificationType(NoClassification.toString(),partyType,  newUUID.toString(), dest.getSystemID(), identityToken);
		
		return dest;
	}
	
	@Override
	public UUID getSecurityIdentity()
	{
		IInvolvedPartyService<?> partyService = get(IInvolvedPartyService.class);
		String value = this.findInvolvedPartyIdentificationType(NoClassification,IdentificationTypes.IdentificationTypeUUID, null, getSystemID(),true, true,
				get(InvolvedPartySystem.class).getSystemToken(getEnterprise()))
		                   .orElseThrow()
		                   .getValue();
		return UUID.fromString(value);
	}
	
	@Override
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	@Override
	public InvolvedParty setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
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
	public String toString()
	{
		return getId() == null ? "" : getId().toString();
	}
	
	@Override
	public void configureNewHierarchyItem(IWarehouseRelationshipClassificationTable<?, ?, InvolvedParty, InvolvedParty, UUID> newLink, InvolvedParty parent, InvolvedParty child, String value)
	{
	
	}
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable,IClassification<?,?> classificationValue, ISystems<?,?> system)
	{
		InvolvedPartyXClassification i = (InvolvedPartyXClassification) linkTable;
		i.setInvolvedPartyID(this);
	}
	
	@Override
	public void configureInvolvedPartyIdentificationTypeAddable(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IInvolvedPartyIdentificationType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?,?> system)
	{
		InvolvedPartyXInvolvedPartyIdentificationType i = (InvolvedPartyXInvolvedPartyIdentificationType) linkTable;
		i.setInvolvedPartyID(primary);
		i.setInvolvedPartyIdentificationTypeID((InvolvedPartyIdentificationType) secondary);
		i.setClassificationID(classificationValue);
		i.setValue(value);
	}
	
	@Override
	public void configureInvolvedPartyNameTypeAddable(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IInvolvedPartyNameType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?,?> system)
	{
		InvolvedPartyXInvolvedPartyNameType i = (InvolvedPartyXInvolvedPartyNameType) linkTable;
		i.setInvolvedPartyID(primary);
		i.setInvolvedPartyNameTypeID((InvolvedPartyNameType) secondary);
		i.setClassificationID(classificationValue);
		i.setValue(value);
	}
	
	@Override
	public void configureInvolvedPartyTypeAddable(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IInvolvedPartyType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?,?> system)
	{
		InvolvedPartyXInvolvedPartyType i = (InvolvedPartyXInvolvedPartyType) linkTable;
		i.setInvolvedPartyID(primary);
		i.setInvolvedPartyTypeID((InvolvedPartyType) secondary);
		i.setClassificationID(classificationValue);
		i.setValue(value);
	}
	
	@Override
	public void configureProductTypeLinkValue(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IProductType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, IEnterprise<?,?> enterprise)
	{
		InvolvedPartyXProductType p = (InvolvedPartyXProductType) linkTable;
		p.setInvolvedPartyID(primary);
		p.setProductTypeID((ProductType) secondary);
		p.setClassificationID(classificationValue);
		p.setValue(value);
		
	}
	
	@Override
	public void configureProductAddable(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IProduct<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?,?> system)
	{
		InvolvedPartyXProduct p = (InvolvedPartyXProduct) linkTable;
		p.setInvolvedPartyID(primary);
		p.setProductID((Product) secondary);
		p.setClassificationID(classificationValue);
		p.setValue(value);
	}
	
	@Override
	public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?,?> system)
	{
		InvolvedPartyXResourceItem r = (InvolvedPartyXResourceItem) linkTable;
		r.setInvolvedPartyID(primary);
		r.setResourceItemID((ResourceItem) secondary);
		r.setClassificationID(classificationValue);
		r.setValue(value);
	}
	
	@Override
	public void configureAddressLinkValue(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IAddress<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?,?> system)
	{
		InvolvedPartyXAddress a = (InvolvedPartyXAddress) linkTable;
		a.setInvolvedPartyID(primary);
		a.setAddressID((Address) secondary);
		a.setClassificationID(classificationValue);
		a.setValue(value);
	}
	
	@Override
	public void configureRulesAddable(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IRules<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		InvolvedPartyXRules r = (InvolvedPartyXRules) linkTable;
		r.setInvolvedPartyID(primary);
		r.setRulesID((Rules) secondary);
		r.setClassificationID(classificationValue);
		r.setValue(value);
	}
}
