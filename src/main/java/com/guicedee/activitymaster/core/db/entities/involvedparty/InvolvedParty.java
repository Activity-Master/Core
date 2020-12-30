package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXInvolvedParty;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.events.EventXInvolvedParty;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.db.hierarchies.InvolvedPartyHierarchyView;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.involvedparty.IInvolvedPartyClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.*;
import com.guicedee.activitymaster.core.services.system.IInvolvedPartyService;
import com.guicedee.activitymaster.core.services.types.IdentificationTypes;
import com.guicedee.activitymaster.core.systems.InvolvedPartySystem;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.*;
import java.util.logging.Logger;

import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;
import static jakarta.persistence.AccessType.*;

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
		extends WarehouseTable<InvolvedParty, InvolvedPartyQueryBuilder, java.util.UUID, InvolvedPartySecurityToken>
		implements IContainsClassifications<InvolvedParty, Classification, InvolvedPartyXClassification, IInvolvedPartyClassification<?>, IInvolvedParty<?>, IClassification<?>, InvolvedParty>,
		           IContainsResourceItems<InvolvedParty, ResourceItem, InvolvedPartyXResourceItem, IClassificationValue<?>, IInvolvedParty<?>, IResourceItem<?>, InvolvedParty>,
		           IContainsInvolvedPartyIdentificationTypes<InvolvedParty, InvolvedPartyIdentificationType, InvolvedPartyXInvolvedPartyIdentificationType,IClassification<?>, IIdentificationType<?>, IInvolvedParty<?>, IInvolvedPartyIdentificationType<?>, InvolvedParty>,
		           IContainsInvolvedPartyNameTypes<InvolvedParty, InvolvedPartyNameType, InvolvedPartyXInvolvedPartyNameType,IClassification<?>, INameType<?>, IInvolvedParty<?>, IInvolvedPartyNameType<?>, InvolvedParty>,
		           IContainsInvolvedPartyTypes<InvolvedParty, InvolvedPartyType, InvolvedPartyXInvolvedPartyType,IClassification<?>, ITypeValue<?>, IInvolvedParty<?>, IInvolvedPartyType<?>, InvolvedParty>,
		           IContainsAddresses<InvolvedParty, Address, InvolvedPartyXAddress, IAddressClassification<?>, IInvolvedParty<?>, IAddress<?>, InvolvedParty>,
		           IActivityMasterEntity<InvolvedParty>,
		           IContainsEnterprise<InvolvedParty>,
		           IInvolvedParty<InvolvedParty>,
		           IContainsHierarchy<InvolvedParty, InvolvedPartyXInvolvedParty, InvolvedPartyHierarchyView,IInvolvedParty<?>, IInvolvedParty<?>>,
		           IContainsProducts<InvolvedParty, Product,InvolvedPartyXProduct, IClassificationValue<?>,IInvolvedParty<?>,IProduct<?>,InvolvedParty>,
		           IContainsRules<InvolvedParty, Rules,InvolvedPartyXRules,IClassification<?>,IInvolvedParty<?>,IRules<?>>
{
	private static final Logger log = Logger.getLogger(InvolvedParty.class.getName());
	@Id

	@Column(nullable = false,
	        name = "InvolvedPartyID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
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
/*

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
*/
	
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
	
	public InvolvedParty(UUID involvedPartyID)
	{
		this.id = involvedPartyID;
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public InvolvedParty moveWebClientUUIDToNewInvolvedParty(IInvolvedParty<?> destination, UUID newUUID)
	{
		InvolvedParty dest = (InvolvedParty) destination;
		dest.setOriginalSourceSystemUniqueID(getId().toString());
		
		ISystems<?> originatingSystem = get(InvolvedPartySystem.class).getSystem(dest.getEnterprise());
		UUID identityToken = get(InvolvedPartySystem.class).getSystemToken(dest.getEnterprise());
		
		InvolvedParty me = builder().find(getId())
		                            .get()
		                            .orElseThrow();
		
		IInvolvedPartyService<?> partyService = get(IInvolvedPartyService.class);
		IIdentificationType<?> partyType = (IIdentificationType<?>) partyService.findIdentificationType("IdentificationTypeWebClientUUID", getEnterprise(), identityToken);
		for (var identificationTypeWebClientUUID : partyService.findAllByIdentificationType("IdentificationTypeWebClientUUID", newUUID.toString()))
		{
			identificationTypeWebClientUUID.expire();
		}
		destination.addIdentificationType(partyType, newUUID.toString(), destination.getEnterprise(), identityToken);
		return dest;
	}
	
	@Override
	public UUID getSecurityIdentity()
	{
		String value = this.findIdentificationType(IdentificationTypes.IdentificationTypeUUID, Classifications.NoClassification.classificationName(),true,true, getEnterprise(),
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
	public void configureResourceItemAddable(InvolvedPartyXResourceItem linkTable, InvolvedParty primary, ResourceItem secondary, IClassificationValue<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setResourceItemID(secondary);
		linkTable.setInvolvedPartyID(this);
	}
	
	@Override
	public void configureAddressLinkValue(InvolvedPartyXAddress linkTable, InvolvedParty primary, Address secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setInvolvedPartyID(this);
		linkTable.setAddressID(secondary);
	}
	
	@Override
	public IIdentificationType<?> stringToIdentificationType(String typeName, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IInvolvedPartyService<?> service = GuiceContext.get(IInvolvedPartyService.class);
		return (IIdentificationType<?>) service.findIdentificationType(typeName, enterprise, identityToken);
	}
	
	@Override
	public void configureAddableIdentificationType(InvolvedPartyXInvolvedPartyIdentificationType linkTable, InvolvedParty primary, InvolvedPartyIdentificationType secondary, IClassification<?> classificationValue, IIdentificationType<?> type, String value, IEnterprise<?> enterprise)
	{
		linkTable.setInvolvedPartyID(primary);
		linkTable.setInvolvedPartyIdentificationTypeID(secondary);
		linkTable.setClassificationID((Classification) classificationValue);
		linkTable.setValue(value);
		linkTable.setEnterpriseID((Enterprise) enterprise);
	}
	
	@Override
	public INameType<?> stringToNameType(String typeName, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IInvolvedPartyService<?> service = GuiceContext.get(IInvolvedPartyService.class);
		return (INameType<?>) service.findNameType(typeName, enterprise, identityToken);
	}
	
	@Override
	public void configureIPNameTypesAddable(InvolvedPartyXInvolvedPartyNameType linkTable, InvolvedParty primary, InvolvedPartyNameType secondary, IClassification<?> classificationValue, INameType<?> type, String value, IEnterprise<?> enterprise)
	{
		linkTable.setInvolvedPartyID(primary);
		linkTable.setInvolvedPartyNameTypeID(secondary);
		linkTable.setClassificationID((Classification) classificationValue);
		linkTable.setValue(value);
		linkTable.setEnterpriseID((Enterprise) enterprise);
	}
	
	@Override
	public ITypeValue<?> stringToIPTypesType(String typeName, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IInvolvedPartyService<?> service = GuiceContext.get(IInvolvedPartyService.class);
		return (ITypeValue<?>) service.findType(typeName, enterprise, identityToken);
	}
	
	@Override
	public InvolvedPartyType stringToIPTypesSecondary(String typeName, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IInvolvedPartyService<?> service = GuiceContext.get(IInvolvedPartyService.class);
		return (InvolvedPartyType) service.findType(typeName, enterprise, identityToken);
	}
	
	@Override
	public void configureIPTypesAddable(InvolvedPartyXInvolvedPartyType linkTable, InvolvedParty primary, InvolvedPartyType secondary, IClassification<?> classificationValue, ITypeValue<?> type, String value, IEnterprise<?> enterprise)
	{
		linkTable.setInvolvedPartyID(primary);
		linkTable.setInvolvedPartyTypeID(secondary);
		linkTable.setClassificationID((Classification) classificationValue);
		linkTable.setValue(value);
		linkTable.setEnterpriseID((Enterprise) enterprise);
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
		return "InvolvedParty - " + getId();
	}
	
	@Override
	public void configureNewHierarchyItem(InvolvedPartyXInvolvedParty newLink, IInvolvedParty<?> parent, IInvolvedParty<?> child, String value)
	{
		newLink.setParentInvolvedPartyID((InvolvedParty) parent);
		newLink.setChildInvolvedPartyID((InvolvedParty) child);
		if (value == null)
		{
			value = STRING_EMPTY;
		}
		newLink.setValue(value);
	}
	
	@Override
	public void configureAddableProduct(InvolvedPartyXProduct linkTable, InvolvedParty primary, Product secondary, IClassificationValue<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setInvolvedPartyID(primary);
		linkTable.setProductID(secondary);
		linkTable.setEnterpriseID((Enterprise) enterprise);
		linkTable.setValue(value);
		linkTable.setClassificationID((Classification) classificationValue);
	}
	
	@Override
	public void configureAddableRule(InvolvedPartyXRules linkTable, InvolvedParty primary, Rules secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setInvolvedPartyID(primary);
		linkTable.setRulesID(secondary);
		linkTable.setEnterpriseID((Enterprise) enterprise);
		linkTable.setValue(value);
		linkTable.setClassificationID((Classification) classificationValue);
	}
}
