package com.armineasy.activitymaster.activitymaster.db.entities.security;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.builders.SecurityTokenXSecurityTokenQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "SecurityTokenXSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Access(FIELD)@lombok.Data
public class SecurityTokenXSecurityToken
		extends WarehouseClassificationRelationshipTable<SecurityToken, SecurityToken, SecurityTokenXSecurityToken, SecurityTokenXSecurityTokenQueryBuilder, Long, SecurityTokenXSecurityTokenSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "SecurityTokenXSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ParentSecurityTokenID",
			referencedColumnName = "SecurityTokenID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private SecurityToken parentSecurityTokenID;

	@JoinColumn(name = "ChildSecurityTokenID",
			referencedColumnName = "SecurityTokenID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private SecurityToken childSecurityTokenID;

	public SecurityTokenXSecurityToken()
	{

	}

	public SecurityTokenXSecurityToken(Long securityTokenXSecurityTokenID)
	{
		this.id = securityTokenXSecurityTokenID;
	}


	@Override
	protected SecurityTokenXSecurityTokenSecurityToken configureDefaultsForNewToken(SecurityTokenXSecurityTokenSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
