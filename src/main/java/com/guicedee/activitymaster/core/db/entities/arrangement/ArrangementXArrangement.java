/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.arrangement;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementXArrangementQueryBuilder;
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
@Table(schema = "Arrangement", name = "ArrangementXArrangement")
@XmlRootElement
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ArrangementXArrangement
		extends WarehouseClassificationRelationshipTable<Arrangement,
		Arrangement,
		ArrangementXArrangement,
		ArrangementXArrangementQueryBuilder,
		java.util.UUID>

{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXArrangementID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
	@JoinColumn(name = "ChildArrangementID",
	            referencedColumnName = "ArrangementID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Arrangement childArrangementID;
	@JoinColumn(name = "ParentArrangementID",
	            referencedColumnName = "ArrangementID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Arrangement parentArrangementID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementSecurityToken> securities;
	
	public ArrangementXArrangement()
	{
	
	}
	
	public ArrangementXArrangement(UUID arrangementXArrangementID)
	{
		this.id = arrangementXArrangementID;
	}
	
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public Arrangement getChildArrangementID()
	{
		return this.childArrangementID;
	}
	
	public Arrangement getParentArrangementID()
	{
		return this.parentArrangementID;
	}
	
	public List<ArrangementXArrangementSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public ArrangementXArrangement setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementXArrangement setChildArrangementID(Arrangement childArrangementID)
	{
		this.childArrangementID = childArrangementID;
		return this;
	}
	
	public ArrangementXArrangement setParentArrangementID(Arrangement parentArrangementID)
	{
		this.parentArrangementID = parentArrangementID;
		return this;
	}
	
	public ArrangementXArrangement setSecurities(List<ArrangementXArrangementSecurityToken> securities)
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
		ArrangementXArrangement that = (ArrangementXArrangement) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public Arrangement getPrimary()
	{
		return getParentArrangementID();
	}
	
	@Override
	public Arrangement getSecondary()
	{
		return getChildArrangementID();
	}
}
