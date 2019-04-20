package com.armineasy.activitymaster.activitymaster.db.entities.classifications;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders.ClassificationDataConceptXResourceItemQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ClassificationDataConceptXResourceItem")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ClassificationDataConceptXResourceItem
		extends WarehouseClassificationRelationshipTable<ClassificationDataConcept, ResourceItem, ClassificationDataConceptXResourceItem, ClassificationDataConceptXResourceItemQueryBuilder
						                                  , Long, ClassificationDataConceptXResourceItemSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationDataConceptXResourceItemID")
	private Long id;

	@JoinColumn(name = "ClassificationDataConceptID",
			referencedColumnName = "ClassificationDataConceptID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ClassificationDataConcept classificationDataConceptID;



	@JoinColumn(name = "ResourceItemID",
			referencedColumnName = "ResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ResourceItem resourceItemID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXResourceItemSecurityToken> securities;

	public ClassificationDataConceptXResourceItem()
	{

	}

	public ClassificationDataConceptXResourceItem(Long classificationDataConceptXResourceItemID)
	{
		this.id = classificationDataConceptXResourceItemID;
	}

	@Override
	protected ClassificationDataConceptXResourceItemSecurityToken configureDefaultsForNewToken(ClassificationDataConceptXResourceItemSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
