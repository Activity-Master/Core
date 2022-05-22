package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationXClassificationQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

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
@Access(AccessType.FIELD)
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
		UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationXClassificationID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
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
	public UUID getId()
	{
		return id;
	}
	
	@Override
	public ClassificationXClassification setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public Classification getPrimary()
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
	public Classification getSecondary()
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
