package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemXClassificationSecurityTokenQueryBuilder;
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
@Table(name = "ResourceItemXClassificationSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ResourceItemXClassificationSecurityToken
		extends WarehouseSecurityTable<ResourceItemXClassificationSecurityToken, ResourceItemXClassificationSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemXClassificationSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ResourceItemXClassificationID",
			referencedColumnName = "ResourceItemXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ResourceItemXClassification base;

	public ResourceItemXClassificationSecurityToken()
	{

	}

	public ResourceItemXClassificationSecurityToken(Long resourceItemXClassificationSecurityTokenID)
	{
		this.id = resourceItemXClassificationSecurityTokenID;
	}
}
