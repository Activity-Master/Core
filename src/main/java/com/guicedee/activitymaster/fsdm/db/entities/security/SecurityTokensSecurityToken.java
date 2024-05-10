package com.guicedee.activitymaster.fsdm.db.entities.security;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.security.builders.SecurityTokensSecurityTokenQueryBuilder;
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
@Table(schema = "Security", name = "SecurityTokensSecurityToken")
@XmlRootElement
@Getter
@Setter

@Access(AccessType.FIELD)
public class SecurityTokensSecurityToken
		extends IWarehouseSecurityTable<SecurityTokensSecurityToken,
		SecurityTokensSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	
	@Column(nullable = false,
	        name = "SecurityTokenAccessID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "SecurityTokenToID",
	            referencedColumnName = "SecurityTokenID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private SecurityToken base;
	
	public SecurityTokensSecurityToken()
	{
	
	}
	
}
