package com.guicedee.activitymaster.core.db.entities.arrangement;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementXArrangementTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IArrangement;
import com.guicedee.activitymaster.core.services.dto.IArrangementType;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IArrangementTypes;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Arrangement",
       name = "ArrangementXArrangementType")
@XmlRootElement

@Access(FIELD)
public class ArrangementXArrangementType
		extends WarehouseClassificationRelationshipTypesTable<Arrangement,
		ArrangementType,
		ArrangementXArrangementType,
		ArrangementXArrangementTypeQueryBuilder,
		IArrangementTypes<?>,
		java.util.UUID,
		ArrangementXArrangementTypeSecurityToken,
		IArrangement<?>,
		IArrangementType<?>>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
	        name = "ArrangementXArrangementTypeID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@ManyToOne
	@JoinColumn(name = "ArrangementID",
	            referencedColumnName = "ArrangementID")
	private Arrangement arrangement;
	@JoinColumn(name = "ArrangementTypeID",
	            referencedColumnName = "ArrangementTypeID")
	@ManyToOne()
	private ArrangementType type;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementTypeSecurityToken> securities;
	
	public ArrangementXArrangementType()
	{
	}
	
	public ArrangementXArrangementType(UUID arrangementXArrangementTypeID)
	{
		this.id = arrangementXArrangementTypeID;
	}
	
	@Override
	protected ArrangementXArrangementTypeSecurityToken configureDefaultsForNewToken(ArrangementXArrangementTypeSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	@Override
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public Arrangement getArrangement()
	{
		return this.arrangement;
	}
	
	public ArrangementType getType()
	{
		return this.type;
	}
	
	public List<ArrangementXArrangementTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	@Override
	public ArrangementXArrangementType setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementXArrangementType setArrangement(Arrangement arrangement)
	{
		this.arrangement = arrangement;
		return this;
	}
	
	public ArrangementXArrangementType setType(ArrangementType type)
	{
		this.type = type;
		return this;
	}
	
	public ArrangementXArrangementType setSecurities(List<ArrangementXArrangementTypeSecurityToken> securities)
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
		ArrangementXArrangementType that = (ArrangementXArrangementType) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public IArrangement<?> getPrimary()
	{
		return getArrangement();
	}
	
	@Override
	public IArrangementType<?> getSecondary()
	{
		return getType();
	}
}
