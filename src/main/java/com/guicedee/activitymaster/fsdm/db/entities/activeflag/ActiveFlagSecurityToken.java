package com.guicedee.activitymaster.fsdm.db.entities.activeflag;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders.ActiveFlagSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
public class ActiveFlagSecurityToken
		extends IWarehouseSecurityTable<ActiveFlagSecurityToken, ActiveFlagSecurityTokenQueryBuilder, String>
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
}
