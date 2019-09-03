package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXArrangementTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IArrangement;
import com.armineasy.activitymaster.activitymaster.services.dto.IArrangementType;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ArrangementXArrangementType")
@XmlRootElement

@Access(FIELD)
public class ArrangementXArrangementType
		extends WarehouseClassificationRelationshipTable<Arrangement,
				                                                ArrangementType,
				                                                ArrangementXArrangementType,
				                                                ArrangementXArrangementTypeQueryBuilder, Long,
				                                                ArrangementXArrangementTypeSecurityToken,
				                                                IArrangement<?>, IArrangementType<?>>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementXArrangementTypeID")
	private Long id;
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

	public ArrangementXArrangementType(Long arrangementXArrangementTypeID)
	{
		this.id = arrangementXArrangementTypeID;
	}

	@Override
	protected ArrangementXArrangementTypeSecurityToken configureDefaultsForNewToken(ArrangementXArrangementTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
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

	public ArrangementXArrangementType setId(Long id)
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
