package com.armineasy.activitymaster.activitymaster.db.entities.systems;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.builders.SystemsSecurityTokenQueryBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "SystemsSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Access(FIELD)@lombok.Data
public class SystemsSecurityToken
		extends WarehouseSecurityTable<SystemsSecurityToken, SystemsSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "SystemsSecurityTokenID")
	private Long id;

	@JoinColumn(name = "SystemID",
			referencedColumnName = "SystemID",
			nullable = false,
			updatable = false,
			insertable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Systems base;

	public SystemsSecurityToken()
	{

	}

	public SystemsSecurityToken(Long systemsSecurityTokenID)
	{
		this.id = systemsSecurityTokenID;
	}
}
