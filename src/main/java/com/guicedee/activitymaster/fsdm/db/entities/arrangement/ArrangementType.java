package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseNameAndDescriptionTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangementType;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Arrangement",
       name = "ArrangementType")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArrangementType
		extends WarehouseSCDTable<ArrangementType, ArrangementTypeQueryBuilder, UUID, ArrangementTypeSecurityToken>
		implements IArrangementType<ArrangementType, ArrangementTypeQueryBuilder>,
		           IWarehouseNameAndDescriptionTable<ArrangementType,ArrangementTypeQueryBuilder,java.util.UUID>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
	        name = "ArrangementTypeID")
	@JsonValue
	
	private java.util.UUID id;
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
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<ArrangementTypeSecurityToken> securities;
	
@OneToMany(
			mappedBy = "arrangementID",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<ArrangementTypeXClassification> classifications;
	
	
@OneToMany(
			mappedBy = "type",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<ArrangementXArrangementType> arrangementsList;
	

	public ArrangementType(UUID arrangementTypeID, String arrangementTypeName, String arrangementTypeDescription)
	{
		this.id = arrangementTypeID;
		this.name = arrangementTypeName;
		this.description = arrangementTypeDescription;
	}
	
	@Override
	public void configureSecurityEntity(ArrangementTypeSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
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
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	public ArrangementType setName(String name)
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
	
	public EnterpriseClassificationDataConcepts concept()
	{
		return EnterpriseClassificationDataConcepts.ArrangementType;
	}
	
	@Override
	public @NotNull @Size(min = 1,
	                      max = 150) String getName()
	{
		return name;
	}
	
	@Override
	public @NotNull @Size(min = 1,
	                      max = 500) String getDescription()
	{
		return description;
	}
	
	public List<ArrangementTypeSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public List<ArrangementTypeXClassification> getClassifications()
	{
		return classifications;
	}
	
	public ArrangementType setClassifications(List<ArrangementTypeXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}
	
	public List<ArrangementXArrangementType> getArrangementsList()
	{
		return arrangementsList;
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
		return Objects.hashCode(getName());
	}
}
