package com.armineasy.activitymaster.activitymaster.db.entities.security;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.security.builders.SecurityTokenXClassificationSecurityTokenQueryBuilder;
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
@Table(name = "SecurityTokenXClassificationSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class SecurityTokenXClassificationSecurityToken
		extends WarehouseSecurityTable<SecurityTokenXClassificationSecurityToken, SecurityTokenXClassificationSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "SecurityTokenXClassificationSecurityTokenID")
	private Long id;

	@JoinColumn(name = "SecurityTokenXClassificationID",
			referencedColumnName = "SecurityTokenXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private SecurityTokenXClassification base;

	public SecurityTokenXClassificationSecurityToken()
	{

	}

	public SecurityTokenXClassificationSecurityToken(Long resourceItemDataSecurityTokenID)
	{
		this.id = resourceItemDataSecurityTokenID;
	}
}
