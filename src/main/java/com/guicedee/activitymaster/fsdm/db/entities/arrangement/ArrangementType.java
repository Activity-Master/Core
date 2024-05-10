package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangementType;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.List;

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
@EqualsAndHashCode(of = "id",callSuper = false)
public class ArrangementType
		extends WarehouseSCDNameDescriptionTable<ArrangementType, ArrangementTypeQueryBuilder, java.lang.String, ArrangementTypeSecurityToken>
		implements IArrangementType<ArrangementType, ArrangementTypeQueryBuilder>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
	        name = "ArrangementTypeID")
	@JsonValue
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	@Getter
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Size(min = 1,
	      max = 150)
	@Column(nullable = false,
	        length = 150,
	        name = "ArrangementTypeName")
	private String name;
	@Getter
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Size(min = 1,
	      max = 500)
	@Column(nullable = false,
	        length = 500,
	        name = "ArrangementTypeDescription")
	private String description;
	@Getter
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementTypeSecurityToken> securities;
	
	@OneToMany(
			mappedBy = "arrangementID",
			fetch = FetchType.LAZY)
	private List<ArrangementTypeXClassification> classifications;
	
	@Getter
	@OneToMany(
			mappedBy = "type",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementType> arrangementsList;
	
	
	public ArrangementType()
	{
	
	}
	
	public ArrangementType(java.lang.String arrangementTypeID)
	{
		this.id = arrangementTypeID;
	}
	
	public ArrangementType(java.lang.String arrangementTypeID, String arrangementTypeName, String arrangementTypeDescription)
	{
		this.id = arrangementTypeID;
		this.name = arrangementTypeName;
		this.description = arrangementTypeDescription;
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
	
	@Override
	public java.lang.String getId()
	{
		return this.id;
	}
	
	@Override
	public ArrangementType setId(java.lang.String id)
	{
		this.id = id;
		return this;
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
}
