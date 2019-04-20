package com.armineasy.activitymaster.activitymaster.db.entities.security;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.security.builders.SecurityTokenXSecurityTokenSecurityTokenQueryBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "SecurityTokensXSecurityTokenSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class SecurityTokenXSecurityTokenSecurityToken
		extends WarehouseSecurityTable<SecurityTokenXSecurityTokenSecurityToken, SecurityTokenXSecurityTokenSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "SecurityTokenXSecurityTokenSecurityTokenID")
	private Long id;

	@JoinColumn(name = "SecurityTokenXSecurityTokenID",
			referencedColumnName = "SecurityTokenXSecurityTokenID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private SecurityTokenXSecurityToken base;

	public SecurityTokenXSecurityTokenSecurityToken()
	{

	}

	public SecurityTokenXSecurityTokenSecurityToken(Long resourceItemDataSecurityTokenID)
	{
		this.id = resourceItemDataSecurityTokenID;
	}
}
