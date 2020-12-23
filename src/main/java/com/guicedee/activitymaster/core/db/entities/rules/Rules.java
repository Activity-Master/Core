package com.guicedee.activitymaster.core.db.entities.rules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.events.EventXArrangement;
import com.guicedee.activitymaster.core.db.entities.events.EventXInvolvedParty;
import com.guicedee.activitymaster.core.db.entities.events.EventXProduct;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.rules.IRulesClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
public class Rules
		extends WarehouseSCDNameDescriptionTable<Rules, RulesQueryBuilder, java.util.UUID, RulesSecurityToken>
		implements IContainsClassifications<Rules, Classification, RulesXClassification, IRulesClassification<?>, IRules<?>, IClassification<?>, Rules>,
		           IContainsResourceItems<Rules, ResourceItem, RulesXResourceItem, IClassificationValue<?>, IRules<?>, IResourceItem<?>, Rules>,
					IContainsInvolvedParties<Rules, InvolvedParty, RulesXInvolvedParty, IClassificationValue<?>, IRules<?>, IInvolvedParty<?>, Rules>,
                    IContainsProducts<Rules, Product, RulesXProduct, IClassificationValue<?>, IRules<?>, IProduct<?>, Rules>,
		           IActivityMasterEntity<Rules>,
		           IRules<Rules>
{

	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 150)
	@Column(nullable = false,
	        length = 150,
	        name = "RulesName")
	@JsonIgnore
	private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 250)
	@Column(nullable = false,
	        length = 250,
	        name = "RulesDesc")
	@JsonIgnore
	private String description;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 10)
	@Column(nullable = false,
	        length = 10,
	        name = "RulesCode")
	@JsonIgnore
	private String rulesCode;

	@OneToMany(
			mappedBy = "rulesID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<RulesXClassification> classifications;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<RulesSecurityToken> securities;
	@OneToMany(
			mappedBy = "rulesID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<RulesXResourceItem> resources;
	@OneToMany(
			mappedBy = "childRulesID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<RulesXRules> rulesXRulesList;
	@OneToMany(
			mappedBy = "parentRulesID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<RulesXRules> rulesXRulesList1;

	@OneToMany(
			mappedBy = "rulesID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<RulesXInvolvedParty> parties;
	@OneToMany(
			mappedBy = "rulesID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<RulesXArrangement> arrangements;
	@OneToMany(
			mappedBy = "rulesID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<RulesXProduct> products;

	public Rules()
	{

	}

	public Rules(UUID rulesID)
	{
		id = rulesID;
	}

	public Rules(UUID rulesID, String rulesName, String rulesDesc, String rulesCode)
	{
		id = rulesID;
		name = rulesName;
		description = rulesDesc;
		this.rulesCode = rulesCode;
	}

	@Override
	protected RulesSecurityToken configureDefaultsForNewToken(RulesSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(RulesXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setRulesID(this);
	}

	@Override
	public void configureResourceItemLinkValue(RulesXResourceItem linkTable, Rules primary, ResourceItem secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setResourceItemID(secondary);
		linkTable.setRulesID(this);
	}

	@Override
	public void configureResourceItemAddable(RulesXResourceItem linkTable, Rules primary, ResourceItem secondary, IClassificationValue<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setResourceItemID(secondary);
		linkTable.setRulesID(this);
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
		return Objects.equals(getName(), rules.getName()) &&
				Objects.equals(getRulesCode(), rules.getRulesCode());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getName(), getRulesCode());
	}

	@Override
	public String toString()
	{
		return "Rules - " + getRulesCode() + " - " + getName();
	}

	@Override
	public java.util.UUID getId()
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

	public @NotNull @Size(min = 1,
	                      max = 10) String getRulesCode()
	{
		return rulesCode;
	}

	@Override
	public Rules setId(java.util.UUID id)
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

	public Rules setRulesCode(@NotNull @Size(min = 1,
	                                         max = 10) String rulesCode)
	{
		this.rulesCode = rulesCode;
		return this;
	}

	@Override
	public void configureRulesTypeLinkValue(RulesXRulesType linkTable, Rules primary, RulesType secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setRulesID(primary);
		linkTable.setRulesTypeID(secondary);
		linkTable.setClassificationID((Classification) classificationValue);
		linkTable.setValue(value);
		linkTable.setEnterpriseID((Enterprise) enterprise);
	}

    @Override
    public void setMyInvolvedPartyLinkValue(RulesXInvolvedParty classificationLink, Rules first, InvolvedParty involvedParty, IEnterprise<?> enterprise) {
        classificationLink.setRulesID(first);
        classificationLink.setInvolvedPartyID(involvedParty);
    }

    @Override
    public void configureAddableProduct(RulesXProduct linkTable, Rules primary, Product secondary, IClassificationValue<?> classificationValue, String value, IEnterprise<?> enterprise) {
        linkTable.setRulesID(primary);
	    linkTable.setProductID(secondary);
	    linkTable.setClassificationID((Classification) classificationValue);
        linkTable.setValue(value);
        linkTable.setEnterpriseID((Enterprise) enterprise);
    }
}
