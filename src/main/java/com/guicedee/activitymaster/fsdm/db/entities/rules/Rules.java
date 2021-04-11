package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRulesType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;
import static jakarta.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Rules",
       name = "Rules")
@XmlRootElement
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Rules
		extends WarehouseSCDNameDescriptionTable<Rules, RulesQueryBuilder, UUID>
		implements IRules<Rules, RulesQueryBuilder>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesID")
	@JsonValue
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 150)
	@Column(nullable = false,
	        length = 150,
	        name = "RuleSetName")
	private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 250)
	@Column(nullable = false,
	        length = 250,
	        name = "RuleSetDescription")
	private String description;
	
	@OneToMany(
			mappedBy = "rulesID",
			fetch = FetchType.LAZY)
	private List<RulesXClassification> classifications;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<RulesSecurityToken> securities;
	@OneToMany(
			mappedBy = "rulesID",
			fetch = FetchType.LAZY)
	private List<RulesXResourceItem> resources;
	@OneToMany(
			mappedBy = "childRulesID",
			fetch = FetchType.LAZY)
	private List<RulesXRules> rulesXRulesList;
	@OneToMany(
			mappedBy = "parentRulesID",
			fetch = FetchType.LAZY)
	private List<RulesXRules> rulesXRulesList1;
	
	@OneToMany(
			mappedBy = "rulesID",
			fetch = FetchType.LAZY)
	private List<RulesXInvolvedParty> parties;
	@OneToMany(
			mappedBy = "rulesID",
			fetch = FetchType.LAZY)
	private List<RulesXArrangement> arrangements;
	@OneToMany(
			mappedBy = "rulesID",
			fetch = FetchType.LAZY)
	private List<RulesXProduct> products;
	
	public Rules()
	{
	
	}
	
	public Rules(UUID rulesID)
	{
		id = rulesID;
	}
	
	public Rules(UUID rulesID, String rulesName, String rulesDesc)
	{
		id = rulesID;
		name = rulesName;
		description = rulesDesc;
	}
	
	public List<RulesXClassification> getClassifications()
	{
		return classifications;
	}
	
	public List<RulesSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public List<RulesXResourceItem> getResources()
	{
		return resources;
	}
	
	
	public List<RulesXRules> getRulesXRulesList()
	{
		return rulesXRulesList;
	}
	
	public List<RulesXRules> getRulesXRulesList1()
	{
		return rulesXRulesList1;
	}
	
	public Rules setClassifications(List<RulesXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}
	
	public Rules setSecurities(List<RulesSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public Rules setResources(List<RulesXResourceItem> resources)
	{
		this.resources = resources;
		return this;
	}
	
	public Rules setRulesXRulesList(List<RulesXRules> rulesXRulesList)
	{
		this.rulesXRulesList = rulesXRulesList;
		return this;
	}
	
	public Rules setRulesXRulesList1(List<RulesXRules> rulesXRulesList1)
	{
		this.rulesXRulesList1 = rulesXRulesList1;
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
		Rules rules = (Rules) o;
		return Objects.equals(getName(), rules.getName());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getName(), getName());
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	@Override
	public UUID getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	@Override
	public Rules setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public Rules setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public Rules setDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	
	@Override
	public void configureNewHierarchyItem(IWarehouseRelationshipClassificationTable<?, ?, Rules, Rules, UUID> newLink, Rules parent, Rules child, String value)
	{
		RulesXRules r = (RulesXRules) newLink;
		r.setParentRulesID(parent);
		r.setChildRulesID(child);
		r.setValue(value);
	}
	
	@Override
	public void configureArrangementAddable(IWarehouseRelationshipTable linkTable, Rules primary, IArrangement<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		RulesXArrangement r = (RulesXArrangement) linkTable;
		r.setRulesID(primary);
		r.setArrangementID((Arrangement) secondary);
		r.setClassificationID(classificationValue);
		r.setValue(value);
		
	}
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
	{
		RulesXClassification rxc = (RulesXClassification) linkTable;
		rxc.setRulesID(this);
	}
	
	@Override
	public void configureProductAddable(IWarehouseRelationshipTable linkTable, Rules primary, IProduct<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		RulesXProduct rxp = (RulesXProduct) linkTable;
		rxp.setRulesID(primary);
		rxp.setProductID((Product) secondary);
		rxp.setClassificationID(classificationValue);
		rxp.setValue(value);
	}
	
	@Override
	public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, Rules primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		RulesXResourceItem r = (RulesXResourceItem) linkTable;
		r.setRulesID(primary);
		r.setResourceItemID((ResourceItem) secondary);
		r.setClassificationID(classificationValue);
		r.setValue(value);
		
	}
	
	@Override
	public void configureRuleTypeLinkValue(IWarehouseRelationshipTable linkTable, Rules primary, IRulesType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, IEnterprise<?, ?> enterprise)
	{
		RulesXRulesType r = (RulesXRulesType) linkTable;
		r.setRulesID(primary);
		r.setRulesTypeID((RulesType) secondary);
		r.setClassificationID(classificationValue);
		r.setValue(value);
	}
}
