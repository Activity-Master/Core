package com.guicedee.activitymaster.core.db.entities.rules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.IContainsResourceItems;
import com.guicedee.activitymaster.core.services.classifications.rules.IRulesClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

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
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class Rules
		extends WarehouseSCDNameDescriptionTable<Rules, RulesQueryBuilder, Long, RulesSecurityToken>
		implements IContainsClassifications<Rules, Classification, RulesXClassification, IRulesClassification<?>, IRules<?>, IClassification<?>, Rules>,
		           IContainsResourceItems<Rules, ResourceItem, RulesXResourceItem, IClassificationValue<?>, IRules<?>, IResourceItem<?>, Rules>,
		           IActivityMasterEntity<Rules>,
		           IRules<Rules>
{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
	        name = "RulesID")
	@JsonValue
	private Long id;
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
	
	public Rules()
	{
	
	}
	
	public Rules(Long rulesID)
	{
		id = rulesID;
	}
	
	public Rules(Long rulesID, String rulesName, String rulesDesc, String rulesCode)
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
	public Long getId()
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
	public Rules setId(Long id)
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
}
