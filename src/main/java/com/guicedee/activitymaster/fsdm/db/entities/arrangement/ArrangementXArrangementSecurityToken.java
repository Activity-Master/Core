/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXArrangementSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Getter
@Entity
@Table(schema = "Arrangement", name = "ArrangementXArrangementSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@EqualsAndHashCode(of = "id", callSuper = false)
public class ArrangementXArrangementSecurityToken
		extends IWarehouseSecurityTable<ArrangementXArrangementSecurityToken, ArrangementXArrangementSecurityTokenQueryBuilder, String>
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
}
