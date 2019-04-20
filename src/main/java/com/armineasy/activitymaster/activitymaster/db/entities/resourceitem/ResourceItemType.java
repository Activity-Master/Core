package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(uniqueConstraints =
		       {
				       @UniqueConstraint(columnNames =
						                         {
								                         "ResourceItemTypeName"
						                         })
		       },
		name = "ResourceItemType")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ResourceItemType
		extends WarehouseSCDNameDescriptionTable<ResourceItemType, ResourceItemTypeQueryBuilder, Long, ResourceItemTypeSecurityToken>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemTypeID")
	@Getter
	@Setter
	private Long id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 100)
	@Column(nullable = false,
			length = 100,
			name = "ResourceItemTypeName")
	@Getter
	@Setter
	private String name;
	@Basic(optional = false)
	@NotNull
	@Lob
	@Column(nullable = false,
			name = "ResourceItemTypeDesc")
	@Getter
	@Setter
	private String description;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ResourceItemTypeSecurityToken> securities;

	@OneToMany(
			mappedBy = "resourceItemTypeID",
			fetch = FetchType.LAZY)
	private List<ResourceItemXResourceItemType> involvedPartyXResourceItemTypeList;

	public ResourceItemType()
	{

	}

	public ResourceItemType(Long resourceItemTypeID)
	{
		this.id = resourceItemTypeID;
	}

	public ResourceItemType(Long resourceItemTypeID, String resourceItemTypeName, String resourceItemTypeDesc)
	{
		this.id = resourceItemTypeID;
		this.name = resourceItemTypeName;
		this.description = resourceItemTypeDesc;
	}

	@Override
	protected ResourceItemTypeSecurityToken configureDefaultsForNewToken(ResourceItemTypeSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
