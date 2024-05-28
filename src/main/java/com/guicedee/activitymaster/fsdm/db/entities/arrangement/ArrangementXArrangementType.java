package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXArrangementTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Arrangement",
       name = "ArrangementXArrangementType")
@XmlRootElement
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ArrangementXArrangementType
		extends WarehouseClassificationRelationshipTypesTable<Arrangement,
		ArrangementType,
		ArrangementXArrangementType,
		ArrangementXArrangementTypeQueryBuilder,
		java.lang.String,
		ArrangementXArrangementTypeSecurityToken
		>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
	        name = "ArrangementXArrangementTypeID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
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
	
	public ArrangementXArrangementType(java.lang.String arrangementXArrangementTypeID)
	{
		this.id = arrangementXArrangementTypeID;
	}
	
	@Override
	public java.lang.String getId()
	{
		return this.id;
	}
	
	@Override
	public ArrangementXArrangementType setId(java.lang.String id)
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
	public Arrangement getPrimary()
	{
		return getArrangement();
	}
	
	@Override
	public ArrangementType getSecondary()
	{
		return getType();
	}
	
	public Arrangement getArrangement()
	{
		return arrangement;
	}
	
	public ArrangementType getType()
	{
		return type;
	}
	
	public List<ArrangementXArrangementTypeSecurityToken> getSecurities()
	{
		return securities;
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
		return Objects.hashCode(getId());
	}
}
