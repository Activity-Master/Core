/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementSecurityTokenQueryBuilder;
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
@Table(schema = "Arrangement", name = "ArrangementSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@EqualsAndHashCode(of = "id", callSuper = false)
public class ArrangementSecurityToken
		extends IWarehouseSecurityTable<ArrangementSecurityToken, ArrangementSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ArrangementID",
	            referencedColumnName = "ArrangementID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Arrangement base;
	
	public ArrangementSecurityToken()
	{
	
	}
	
	public ArrangementSecurityToken(java.lang.String arrangementSecurityTokenID)
	{
		this.id = arrangementSecurityTokenID;
	}
	
	public String toString()
	{
		return "ArrangementSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public ArrangementSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementSecurityToken setBase(Arrangement base)
	{
		this.base = base;
		return this;
	}
}
