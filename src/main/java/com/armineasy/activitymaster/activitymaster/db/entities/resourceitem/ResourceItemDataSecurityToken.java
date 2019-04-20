package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemDataSecurityTokenQueryBuilder;
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
@Table(name = "ResourceItemDataSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ResourceItemDataSecurityToken
		extends WarehouseSecurityTable<ResourceItemDataSecurityToken, ResourceItemDataSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemDataSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ResourceItemDataID",
			referencedColumnName = "ResourceItemDataID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ResourceItemData base;

	public ResourceItemDataSecurityToken()
	{

	}

	public ResourceItemDataSecurityToken(Long resourceItemDataSecurityTokenID)
	{
		this.id = resourceItemDataSecurityTokenID;
	}
}
