package com.guicedee.activitymaster.core.db.entities.classifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.classifications.builders.ClassificationQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.hierarchies.ClassificationHierarchyView;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsHierarchy;
import com.guicedee.activitymaster.core.services.capabilities.IContainsResourceItems;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IResourceItem;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.guicedinjection.GuiceContext;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Classification",
		name = "Classification")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class Classification
		extends WarehouseTable<Classification, ClassificationQueryBuilder, Long, ClassificationSecurityToken>
		implements IContainsHierarchy<Classification, ClassificationXClassification, ClassificationHierarchyView, IClassification<?>>,
				           IContainsResourceItems<Classification, ResourceItem, ClassificationXResourceItem, IResourceItemClassification<?>, IClassification<?>, IResourceItem<?>, Classification>,
				           IActivityMasterEntity<Classification>,
				           IClassification<Classification>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationID")
	@JsonValue
	private Long id;

	@Basic(optional = false,
			fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 100)
	@Column(nullable = false,
			length = 100,
			name = "ClassificationName")
	@JsonIgnore
	private String name;
	@Basic(optional = false,
			fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "ClassificationDesc")
	@JsonIgnore
	private String description;
	@Basic(optional = false,
			fetch = EAGER)
	@NotNull
	@Column(nullable = false,
			name = "ClassificationSequenceNumber")
	@JsonIgnore
	@OrderBy
	private Short classificationSequenceNumber;
	@JoinColumn(name = "ClassificationDataConceptID",
			referencedColumnName = "ClassificationDataConceptID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.EAGER)
	@JsonIgnore
	private ClassificationDataConcept concept;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ClassificationSecurityToken> securities;

	public Classification()
	{

	}

	public Classification(Long classificationID)
	{
		id = classificationID;
	}

	public Classification(Long classificationID, String classificationName, String classificationDesc, short classificationSequenceNumber)
	{
		id = classificationID;
		name = classificationName;
		description = classificationDesc;
		this.classificationSequenceNumber = classificationSequenceNumber;
	}

	@Override
	protected ClassificationSecurityToken configureDefaultsForNewToken(ClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		ClassificationSecurityToken token = super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);
		token.setBase(this);
		return token;
	}

	public void configureForClassification(ClassificationXClassification classificationLink, IEnterprise<?> enterprise)
	{
		Classification hierarchyClassification = (Classification) GuiceContext.get(ClassificationService.class)
		                                                                      .getHierarchyType(classificationLink.getEnterpriseID());
		Classification incomingClassification = classificationLink.getClassificationID();

		classificationLink.setChildClassificationID(incomingClassification);
		classificationLink.setParentClassificationID(this);
		classificationLink.setClassificationID(hierarchyClassification);
	}

	@Override
	public void configureNewHierarchyItem(ClassificationXClassification newLink, IClassification<?> parent, IClassification<?> child, String value)
	{
		newLink.setParentClassificationID(this);
		newLink.setChildClassificationID((Classification) child);
		newLink.setEnterpriseID(getEnterpriseID());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
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
		Classification that = (Classification) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public String toString()
	{
		return "Classification - " + getName() + " - " + getDescription();
	}

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public Classification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public @NotNull Short getClassificationSequenceNumber()
	{
		return classificationSequenceNumber;
	}	@Override
	public String getName()
	{
		return name;
	}

	public Classification setClassificationSequenceNumber(@NotNull Short classificationSequenceNumber)
	{
		this.classificationSequenceNumber = classificationSequenceNumber;
		return this;
	}

	public ClassificationDataConcept getConcept()
	{
		return concept;
	}	@Override
	public String getDescription()
	{
		return description;
	}

	public Classification setConcept(ClassificationDataConcept concept)
	{
		this.concept = concept;
		return this;
	}

	@Override
	public void configureResourceItemLinkValue(ClassificationXResourceItem linkTable, Classification primary, ResourceItem secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setClassificationID(this);
		linkTable.setResourceItemID(secondary);
	}





	@Override
	public Classification setName(String name)
	{
		this.name = name;
		return this;
	}

	@Override
	public Classification setDescription(@NotNull @Size(min = 1,
			max = 500) String description)
	{
		this.description = description;
		return this;
	}

}
