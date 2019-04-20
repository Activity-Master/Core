package com.armineasy.activitymaster.activitymaster.db.entities.activeflag;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.builders.ActiveFlagSecurityTokenQueryBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@Table(name = "ActiveFlagSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Cacheable
@org.hibernate.annotations.Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActiveFlagSecurityToken
		extends WarehouseSecurityTable<ActiveFlagSecurityToken, ActiveFlagSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ActiveFlagSecurityTokenID")
	private Long id;

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

	public ActiveFlagSecurityToken(Long activeFlagSecurityTokenID)
	{
		this.id = activeFlagSecurityTokenID;
	}
}
