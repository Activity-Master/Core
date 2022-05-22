package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXProductTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductType;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
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

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class InvolvedPartyXProductType
		extends WarehouseClassificationRelationshipTypesTable<InvolvedParty,
		ProductType,
		InvolvedPartyXProductType,
		InvolvedPartyXProductTypeQueryBuilder,
		UUID>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXProductTypeID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
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
	public UUID getId()
	{
		return this.id;
	}
	
	@Override
	public InvolvedPartyXProductType setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}
	
	public InvolvedPartyXProductType setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}
	
	public ProductType getProductTypeID()
	{
		return this.involvedPartyTypeID;
	}
	
	public InvolvedPartyXProductType setProductTypeID(ProductType involvedPartyTypeID)
	{
		this.involvedPartyTypeID = involvedPartyTypeID;
		return this;
	}
	
	public List<InvolvedPartyXProductTypeSecurityToken> getSecurities()
	{
		return this.securities;
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
	public InvolvedParty getPrimary()
	{
		return getInvolvedPartyID();
	}
	
	@Override
	public ProductType getSecondary()
	{
		return getProductTypeID();
	}
}
