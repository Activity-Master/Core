package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemDataQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ResourceItemData")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ResourceItemData
		extends WarehouseTable<ResourceItemData, ResourceItemDataQueryBuilder, Long, ResourceItemDataSecurityToken>
		implements IContainsClassifications<ResourceItemData, Classification, ResourceItemDataXClassification, IResourceItemClassification>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemDataID")
	@Getter
	@Setter
	private Long id;
	@Lob()
	@Column(nullable = false,
			name = "ResourceItemData")
	@Getter
	@Setter
	private byte[] resourceItemData;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataSecurityToken> securities;

	@JoinColumn(name = "ResourceItemID",
			referencedColumnName = "ResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	@Getter
	@Setter
	private ResourceItem resource;

	@OneToMany(
			mappedBy = "resourceItemDataID",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataXClassification> classifications;

	public ResourceItemData()
	{

	}

	public ResourceItemData(Long resourceItemDataID)
	{
		this.id = resourceItemDataID;
	}

	@Override
	protected ResourceItemDataSecurityToken configureDefaultsForNewToken(ResourceItemDataSecurityToken stAdmin, IEnterprise enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(ResourceItemDataXClassification classificationLink, Enterprise enterprise)
	{
		classificationLink.setResourceItemDataID(this);
	}
}
