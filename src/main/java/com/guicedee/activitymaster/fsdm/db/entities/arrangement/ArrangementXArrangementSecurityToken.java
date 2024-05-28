/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXArrangementSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Arrangement", name = "ArrangementXArrangementSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
public class ArrangementXArrangementSecurityToken
		extends WarehouseSecurityTable<ArrangementXArrangementSecurityToken, ArrangementXArrangementSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXArrangementSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ArrangementXArrangementID",
	            referencedColumnName = "ArrangementXArrangementID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ArrangementXArrangement base;
	
	public ArrangementXArrangementSecurityToken()
	{
	
	}
	
	public ArrangementXArrangementSecurityToken(java.lang.String arrangementXArrangementSecurityTokenID)
	{
		this.id = arrangementXArrangementSecurityTokenID;
	}
	
	public String toString()
	{
		return "ArrangementXArrangementSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public ArrangementXArrangementSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementXArrangementSecurityToken setBase(ArrangementXArrangement base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public ArrangementXArrangement getBase()
	{
		return base;
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
		ArrangementXArrangementSecurityToken that = (ArrangementXArrangementSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
