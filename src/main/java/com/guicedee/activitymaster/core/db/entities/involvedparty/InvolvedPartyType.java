package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IHasActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedPartyType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;
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
@Entity
@Table(name = "InvolvedPartyType",
       schema = "Party")
@XmlRootElement
@Access(FIELD)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InvolvedPartyType
		extends WarehouseSCDNameDescriptionTable<InvolvedPartyType, InvolvedPartyTypeQueryBuilder, Long, InvolvedPartyTypeSecurityToken>
		implements IInvolvedPartyType<InvolvedPartyType>,
		           INameAndDescription<InvolvedPartyType>,
		           IContainsEnterprise<InvolvedPartyType>,
		           IActivityMasterEntity<InvolvedPartyType>,
		           IHasActiveFlags<InvolvedPartyType>,
		           ITypeValue
{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
	        name = "InvolvedPartyTypeID")
	@JsonValue
	private Long id;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 100)
	@Column(nullable = false,
	        length = 100,
	        name = "InvolvedPartyTypeName")
	@JsonIgnore
	private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Column(nullable = false,
	        name = "InvolvedPartyTypeDesc")
	@JsonIgnore
	private String description;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyTypeSecurityToken> securities;
	
	@OneToMany(
			mappedBy = "involvedPartyTypeID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList;
	
	public InvolvedPartyType()
	{
	
	}
	
	public InvolvedPartyType(Long involvedPartyTypeID)
	{
		id = involvedPartyTypeID;
	}
	
	public InvolvedPartyType(Long involvedPartyTypeID, String involvedPartyTypeName, String involvedPartyTypeDesc)
	{
		id = involvedPartyTypeID;
		name = involvedPartyTypeName;
		description = involvedPartyTypeDesc;
	}
	
	@Override
	protected InvolvedPartyTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	public List<InvolvedPartyTypeSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public List<InvolvedPartyXInvolvedPartyType> getInvolvedPartyXInvolvedPartyTypeList()
	{
		return involvedPartyXInvolvedPartyTypeList;
	}
	
	public InvolvedPartyType setSecurities(List<InvolvedPartyTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public InvolvedPartyType setInvolvedPartyXInvolvedPartyTypeList(List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList)
	{
		this.involvedPartyXInvolvedPartyTypeList = involvedPartyXInvolvedPartyTypeList;
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
		InvolvedPartyType that = (InvolvedPartyType) o;
		return Objects.equals(getName(), that.getName());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public String toString()
	{
		return "Party Type - " + getName();
	}
	
	@Override
	public Long getId()
	{
		return id;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public @NotNull String getDescription()
	{
		return description;
	}
	
	@Override
	public InvolvedPartyType setId(Long id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public InvolvedPartyType setName(String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public InvolvedPartyType setDescription(@NotNull String description)
	{
		this.description = description;
		return this;
	}
	
	@Override
	public String name()
	{
		return getName();
	}
	
	@Override
	public String classificationValue()
	{
		return getName();
	}
}
