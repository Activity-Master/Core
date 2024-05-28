package com.guicedee.activitymaster.fsdm.db.entities.activeflag;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders.ActiveFlagSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ActiveFlagSecurityToken",
       schema = "dbo")
@XmlRootElement
@Access(AccessType.FIELD)
public class ActiveFlagSecurityToken
		extends WarehouseSecurityTable<ActiveFlagSecurityToken, ActiveFlagSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ActiveFlagSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "SecurityTokenActiveFlagID",
	            referencedColumnName = "ActiveFlagID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY,
	           cascade = CascadeType.ALL)
	
	private ActiveFlag base;
	
	public ActiveFlagSecurityToken()
	{
	
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	@Override
	public ActiveFlagSecurityToken setId(String id)
	{
		this.id = id;
		return this;
	}
	
	public ActiveFlag getBase()
	{
		return base;
	}
	
	public ActiveFlagSecurityToken setBase(ActiveFlag base)
	{
		this.base = base;
		return this;
	}
}
