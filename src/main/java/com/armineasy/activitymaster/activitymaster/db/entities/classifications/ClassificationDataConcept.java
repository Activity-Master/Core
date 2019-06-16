package com.armineasy.activitymaster.activitymaster.db.entities.classifications;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders.ClassificationDataConceptQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassificationDataConcept;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "ClassificationDataConcept")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Access(FIELD)@lombok.Data
public class ClassificationDataConcept
		extends WarehouseSCDNameDescriptionTable<ClassificationDataConcept, ClassificationDataConceptQueryBuilder, Long, ClassificationDataConceptSecurityToken>
		implements// IContainsClassifications<ClassificationDataConcept, Classification, ClassificationDataConceptXClassification, IClassificationClassificationDataConceptTypes<?>>,
				          IContainsResourceItems<ClassificationDataConcept, ResourceItem, ClassificationDataConceptXResourceItem>,
				          IActivityMasterEntity<ClassificationDataConcept>,
				          IClassificationDataConcept<ClassificationDataConcept>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationDataConceptID")
	@Getter
	@Setter
	private Long id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 100)
	@Column(nullable = false,
			length = 100,
			name = "classificationDataConceptName")
	@Getter
	@Setter
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 1500)
	@Column(nullable = false,
			length = 1500,
			name = "ClassificationDataConceptDesc")
	@Getter
	@Setter
	private String description;

	@OneToMany(
			mappedBy = "concept",
			fetch = FetchType.LAZY)
	private List<Classification> classificationList;

	@OneToMany(
			mappedBy = "classificationDataConceptID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXClassification> classifications;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptSecurityToken> securities;

	@OneToMany(
			mappedBy = "classificationDataConceptID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXResourceItem> classificationDataConceptXResourceItemList;

	public ClassificationDataConcept()
	{

	}

	public ClassificationDataConcept(Long classificationDataConceptID)
	{
		this.id = classificationDataConceptID;
	}

	public ClassificationDataConcept(Long classificationDataConceptID, String classificationDataConceptName, String classificationDataConceptDesc, String originalSourceSystemUniqueID)
	{
		this.id = classificationDataConceptID;
		this.name = classificationDataConceptName;
		this.description = classificationDataConceptDesc;

	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	protected ClassificationDataConceptSecurityToken configureDefaultsForNewToken(ClassificationDataConceptSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		ClassificationDataConceptSecurityToken token = super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);
		token.setBase(this);
		return token;
	}

	public void configureForClassification(ClassificationDataConceptXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setClassificationDataConceptID(this);
	}

	@Override
	public void setMyResourceItemLinkValue(ClassificationDataConceptXResourceItem classificationLink, ResourceItem resourceItem, IEnterprise<?> enterprise)
	{
		classificationLink.setClassificationDataConceptID(this);
		classificationLink.setResourceItemID(resourceItem);
	}
}
