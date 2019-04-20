package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemDataXClassificationSecurityTokenQueryBuilder;
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
@Table(name = "ResourceItemDataXClassificationSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ResourceItemDataXClassificationSecurityToken
		extends WarehouseSecurityTable<ResourceItemDataXClassificationSecurityToken, ResourceItemDataXClassificationSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemDataXClassificationSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ResourceItemDataXClassificationID",
			referencedColumnName = "ResourceItemDataXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ResourceItemDataXClassification base;

	public ResourceItemDataXClassificationSecurityToken()
	{

	}

	public ResourceItemDataXClassificationSecurityToken(Long resourceItemDataSecurityTokenID)
	{
		this.id = resourceItemDataSecurityTokenID;
	}
}
