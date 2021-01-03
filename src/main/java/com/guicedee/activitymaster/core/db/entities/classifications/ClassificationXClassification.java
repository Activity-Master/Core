package com.guicedee.activitymaster.core.db.entities.classifications;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.builders.ClassificationXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Classification",
		name = "ClassificationXClassification")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ClassificationXClassification
		extends WarehouseClassificationRelationshipTable<Classification,
				                                                Classification,
				                                                ClassificationXClassification,
				                                                ClassificationXClassificationQueryBuilder,
				                                                java.util.UUID,
				                                                ClassificationXClassificationSecurityToken,
				                                                IClassification<?>, IClassification<?>>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "ClassificationXClassificationID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "ChildClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Classification childClassificationID;

	@JoinColumn(name = "ParentClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Classification parentClassificationID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassificationSecurityToken> securities;

	public ClassificationXClassification()
	{

	}

	public ClassificationXClassification(UUID classificationXClassificationID)
	{
		id = classificationXClassificationID;
	}

	@Override
	protected ClassificationXClassificationSecurityToken configureDefaultsForNewToken(ClassificationXClassificationSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public List<ClassificationXClassificationSecurityToken> getSecurities()
	{
		return securities;
	}

	public ClassificationXClassification setSecurities(List<ClassificationXClassificationSecurityToken> securities)
	{
		this.securities = securities;
		return this;
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
		ClassificationXClassification that = (ClassificationXClassification) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public java.util.UUID getId()
	{
		return id;
	}

	@Override
	public ClassificationXClassification setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	@Override
	public IClassification<?> getPrimary()
	{
		return getParentClassificationID();
	}

	public Classification getParentClassificationID()
	{
		return parentClassificationID;
	}

	public ClassificationXClassification setParentClassificationID(Classification parentClassificationID)
	{
		this.parentClassificationID = parentClassificationID;
		return this;
	}

	@Override
	public IClassification<?> getSecondary()
	{
		return getChildClassificationID();
	}

	public Classification getChildClassificationID()
	{
		return childClassificationID;
	}

	public ClassificationXClassification setChildClassificationID(Classification childClassificationID)
	{
		this.childClassificationID = childClassificationID;
		return this;
	}
}
