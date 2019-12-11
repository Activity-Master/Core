package com.guicedee.activitymaster.core.db.entities.arrangement;

import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IArrangementType;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
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
@Table(schema="Arrangement",name = "ArrangementType")
@XmlRootElement

@Access(FIELD)
public class ArrangementType
		extends WarehouseSCDNameDescriptionTable<ArrangementType, ArrangementTypeQueryBuilder, Long, ArrangementTypeSecurityToken>
		implements IArrangementType<ArrangementType>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementTypeID")
	private Long id;
	@Basic(optional = false,
			fetch = FetchType.EAGER)
	@NotNull
	@Size(min = 1,
			max = 150)
	@Column(nullable = false,
			length = 150,
			name = "ArrangementTypeName")
	private String name;
	@Basic(optional = false,
			fetch = FetchType.EAGER)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "ArrangementTypeDescription")
	private String description;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementTypeSecurityToken> securities;

	@OneToMany(
			mappedBy = "type",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementType> arrangementsList;

	public ArrangementType()
	{

	}

	public ArrangementType(Long arrangementTypeID)
	{
		this.id = arrangementTypeID;
	}

	public ArrangementType(Long arrangementTypeID, String arrangementTypeName, String arrangementTypeDescription)
	{
		this.id = arrangementTypeID;
		this.name = arrangementTypeName;
		this.description = arrangementTypeDescription;
	}

	@Override
	protected ArrangementTypeSecurityToken configureDefaultsForNewToken(ArrangementTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public List<ArrangementTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public List<ArrangementXArrangementType> getArrangementsList()
	{
		return this.arrangementsList;
	}

	public ArrangementType setSecurities(List<ArrangementTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public ArrangementType setArrangementsList(List<ArrangementXArrangementType> arrangementsList)
	{
		this.arrangementsList = arrangementsList;
		return this;
	}

	public String toString()
	{
		return "ArrangementType - " + getName();
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
		ArrangementType that = (ArrangementType) o;
		return Objects.equals(getName(), that.getName());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	public Long getId()
	{
		return this.id;
	}

	public   String getName()
	{
		return this.name;
	}

	public    String getDescription()
	{
		return this.description;
	}

	public ArrangementType setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ArrangementType setName(  String name)
	{
		this.name = name;
		return this;
	}

	public ArrangementType setDescription(@NotNull @Size(min = 1,
			max = 500) String description)
	{
		this.description = description;
		return this;
	}
}
