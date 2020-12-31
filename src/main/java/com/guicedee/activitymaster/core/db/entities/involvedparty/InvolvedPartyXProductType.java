package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXProductTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.product.ProductType;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedParty;
import com.guicedee.activitymaster.core.services.dto.IProductType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;
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
@Table(schema = "Party",
       name = "InvolvedPartyXProductType")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyXProductType
		extends WarehouseClassificationRelationshipTypesTable<InvolvedParty,
		ProductType,
		InvolvedPartyXProductType,
		InvolvedPartyXProductTypeQueryBuilder,
		IProductTypeValue<?>,
		UUID,
		InvolvedPartyXProductTypeSecurityToken,
		IInvolvedParty<?>, IProductType<?>>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXProductTypeID")@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@JoinColumn(name = "InvolvedPartyID",
	            referencedColumnName = "InvolvedPartyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;
	@JoinColumn(name = "ProductTypeID",
	            referencedColumnName = "ProductTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private ProductType involvedPartyTypeID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProductTypeSecurityToken> securities;
	
	public InvolvedPartyXProductType()
	{
	
	}
	
	public InvolvedPartyXProductType(UUID involvedPartyXProductTypeID)
	{
		this.id = involvedPartyXProductTypeID;
	}
	
	@Override
	protected InvolvedPartyXProductTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyXProductTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	@Override
	public UUID getId()
	{
		return this.id;
	}
	
	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}
	
	public ProductType getProductTypeID()
	{
		return this.involvedPartyTypeID;
	}
	
	public List<InvolvedPartyXProductTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	@Override
	public InvolvedPartyXProductType setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyXProductType setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}
	
	public InvolvedPartyXProductType setProductTypeID(ProductType involvedPartyTypeID)
	{
		this.involvedPartyTypeID = involvedPartyTypeID;
		return this;
	}
	
	public InvolvedPartyXProductType setSecurities(List<InvolvedPartyXProductTypeSecurityToken> securities)
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
		InvolvedPartyXProductType that = (InvolvedPartyXProductType) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public IInvolvedParty<?> getPrimary()
	{
		return getInvolvedPartyID();
	}
	
	@Override
	public IProductType<?> getSecondary()
	{
		return getProductTypeID();
	}
}
