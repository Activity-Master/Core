package com.armineasy.activitymaster.activitymaster.db.entities.classifications;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders.ClassificationDataConceptQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.*;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IResourceType;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

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
@Access(FIELD)
public class ClassificationDataConcept
		extends WarehouseSCDNameDescriptionTable<ClassificationDataConcept, ClassificationDataConceptQueryBuilder, Long, ClassificationDataConceptSecurityToken>
		implements// IContainsClassifications<ClassificationDataConcept, Classification, ClassificationDataConceptXClassification, IClassificationClassificationDataConceptTypes<?>>,
				          IContainsResourceItems<ClassificationDataConcept, ResourceItem, ClassificationDataConceptXResourceItem,IResourceItemClassification<?>,IClassificationDataConcept<?>, IResourceItem<?>, ClassificationDataConcept>,
				          IActivityMasterEntity<ClassificationDataConcept>,
				          IClassificationDataConcept<ClassificationDataConcept>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationDataConceptID")
	private Long id;

	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 100)
	@Column(nullable = false,
			length = 100,
			name = "classificationDataConceptName")
	private String name;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 1500)
	@Column(nullable = false,
			length = 1500,
			name = "ClassificationDataConceptDesc")
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
		return "ClassificationDataConcept - " + getName();
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
	public void configureResourceItemLinkValue(ClassificationDataConceptXResourceItem linkTable, ClassificationDataConcept primary, ResourceItem secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setClassificationDataConceptID(this);
		linkTable.setResourceItemID(secondary);
	}

	public List<Classification> getClassificationList()
	{
		return this.classificationList;
	}

	public List<ClassificationDataConceptXClassification> getClassifications()
	{
		return this.classifications;
	}

	public List<ClassificationDataConceptSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public List<ClassificationDataConceptXResourceItem> getClassificationDataConceptXResourceItemList()
	{
		return this.classificationDataConceptXResourceItemList;
	}

	public ClassificationDataConcept setClassificationList(List<Classification> classificationList)
	{
		this.classificationList = classificationList;
		return this;
	}

	public ClassificationDataConcept setClassifications(List<ClassificationDataConceptXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}

	public ClassificationDataConcept setSecurities(List<ClassificationDataConceptSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public ClassificationDataConcept setClassificationDataConceptXResourceItemList(List<ClassificationDataConceptXResourceItem> classificationDataConceptXResourceItemList)
	{
		this.classificationDataConceptXResourceItemList = classificationDataConceptXResourceItemList;
		return this;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		ClassificationDataConcept that = (ClassificationDataConcept) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	public Long getId()
	{
		return this.id;
	}

	public @NotNull @Size(min = 1,
			max = 100) String getName()
	{
		return this.name;
	}

	public @NotNull @Size(min = 1,
			max = 1500) String getDescription()
	{
		return this.description;
	}

	public ClassificationDataConcept setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ClassificationDataConcept setName(@NotNull @Size(min = 1,
			max = 100) String name)
	{
		this.name = name;
		return this;
	}

	public ClassificationDataConcept setDescription(@NotNull @Size(min = 1,
			max = 1500) String description)
	{
		this.description = description;
		return this;
	}

}
