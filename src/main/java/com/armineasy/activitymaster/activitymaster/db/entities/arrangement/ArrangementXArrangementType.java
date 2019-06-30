package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXArrangementTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ArrangementXArrangementType")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ArrangementXArrangementType
		extends WarehouseClassificationRelationshipTable<Arrangement, ArrangementType,
				                                  ArrangementXArrangementType,
				                                  ArrangementXArrangementTypeQueryBuilder, Long,
				                                  ArrangementXArrangementTypeSecurityToken>
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

	public String toString()
	{
		return "ArrangementXArrangementType(id=" + this.getId() + ", arrangement=" + this.getArrangement() + ", type=" + this.getType() + ", securities=" + this.getSecurities() +
		       ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ArrangementXArrangementType))
		{
			return false;
		}
		final ArrangementXArrangementType other = (ArrangementXArrangementType) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof ArrangementXArrangementType;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
