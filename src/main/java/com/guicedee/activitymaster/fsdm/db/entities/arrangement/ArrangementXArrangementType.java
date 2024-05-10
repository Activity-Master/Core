package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXArrangementTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.util.List;

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
@EqualsAndHashCode(of = "id", callSuper = false)
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
	@Getter
	@ManyToOne
	@JoinColumn(name = "ArrangementID",
	            referencedColumnName = "ArrangementID")
	
	private Arrangement arrangement;
	@Getter
	@JoinColumn(name = "ArrangementTypeID",
	            referencedColumnName = "ArrangementTypeID")
	@ManyToOne()
	
	private ArrangementType type;
	@Getter
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
}
