package com.guicedee.activitymaster.core.db.entities.rules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRulesType;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
@Table(schema = "Rules",
       name = "RulesType")
@XmlRootElement

@Access(FIELD)
public class RulesType
		extends WarehouseSCDNameDescriptionTable<RulesType, RulesTypeQueryBuilder, Long, RulesTypeSecurityToken>
		implements IRulesType<RulesType>,
		           IActivityMasterEntity<RulesType>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
	        name = "RulesTypeID")
	@JsonValue
	private Long id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 200)
	@Column(nullable = false,
	        length = 200,
	        name = "RulesTypeName")
	@JsonIgnore
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 200)
	@Column(nullable = false,
	        length = 200,
	        name = "RulesTypeDesc")
	@JsonIgnore
	private String description;
	
	@OneToMany(
			mappedBy = "rulesTypeID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<RulesXRulesType> rulesXRulesTypeList;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<RulesTypeSecurityToken> securities;
	
	public RulesType()
	{
	
	}
	
	public RulesType(Long rulesTypeID)
	{
		this.id = rulesTypeID;
	}
	
	public RulesType(Long rulesTypeID, String rulesTypName, String rulesTypeDesc)
	{
		this.id = rulesTypeID;
		this.name = rulesTypName;
		this.description = rulesTypeDesc;
	}
	
	@Override
	protected RulesTypeSecurityToken configureDefaultsForNewToken(RulesTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	public List<RulesXRulesType> getRulesXRulesTypeList()
	{
		return this.rulesXRulesTypeList;
	}
	
	public List<RulesTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public RulesType setRulesXRulesTypeList(List<RulesXRulesType> rulesXRulesTypeList)
	{
		this.rulesXRulesTypeList = rulesXRulesTypeList;
		return this;
	}
	
	public RulesType setSecurities(List<RulesTypeSecurityToken> securities)
	{
		this.securities = securities;
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
		RulesType rulesType = (RulesType) o;
		return Objects.equals(getName(), rulesType.getName());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public String toString()
	{
		return "RulesType - " + getName();
	}
	
	@Override
	public Long getId()
	{
		return this.id;
	}
	
	@Override
	public @NotNull @Size(min = 1,
	                      max = 200) String getName()
	{
		return this.name;
	}
	
	@Override
	public @NotNull @Size(min = 1,
	                      max = 200) String getDescription()
	{
		return this.description;
	}
	
	@Override
	public RulesType setId(Long id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public RulesType setName(@NotNull @Size(min = 1,
	                                          max = 200) String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public RulesType setDescription(@NotNull @Size(min = 1,
	                                                 max = 200) String description)
	{
		this.description = description;
		return this;
	}
}
