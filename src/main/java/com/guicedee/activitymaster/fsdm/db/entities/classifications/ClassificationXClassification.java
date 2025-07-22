package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationXClassificationQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationXClassification
		extends WarehouseClassificationRelationshipTable<Classification,
                                Classification,
                                ClassificationXClassification,
                                ClassificationXClassificationQueryBuilder,
                                UUID,
                                ClassificationXClassificationSecurityToken>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationXClassificationID")
	
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
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<ClassificationXClassificationSecurityToken> securities;

	public ClassificationXClassification setSecurities(List<ClassificationXClassificationSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	@Override
	public void configureSecurityEntity(ClassificationXClassificationSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}

	@Override
	public Classification getPrimary()
	{
		return getParentClassificationID();
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
	
	public ClassificationXClassification setChildClassificationID(Classification childClassificationID)
	{
		this.childClassificationID = childClassificationID;
		return this;
	}
	
	public Classification getChildClassificationID()
	{
		return childClassificationID;
	}
	
	public Classification getParentClassificationID()
	{
		return parentClassificationID;
	}
	
	public List<ClassificationXClassificationSecurityToken> getSecurities()
	{
		return securities;
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
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
