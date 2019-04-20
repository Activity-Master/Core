/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemXResourceItemTypeSecurityTokenQueryBuilder;
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
@Table(name = "ResourceItemXResourceItemTypeSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ResourceItemXResourceItemTypeSecurityToken
		extends WarehouseSecurityTable<ResourceItemXResourceItemTypeSecurityToken, ResourceItemXResourceItemTypeSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemXResourceItemTypeSecurityTokenID")
	private Long id;
	@JoinColumn(name = "ResourceItemXResourceItemTypeID",
			referencedColumnName = "ResourceItemXResourceItemTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ResourceItemXResourceItemType base;

	public ResourceItemXResourceItemTypeSecurityToken()
	{

	}

	public ResourceItemXResourceItemTypeSecurityToken(Long resourceItemXResourceItemTypeSecurityTokenID)
	{
		this.id = resourceItemXResourceItemTypeSecurityTokenID;
	}
}
