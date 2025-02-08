package com.guicedee.activitymaster.fsdm.db.entities.systems;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.systems.builders.SystemsXClassificationQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "SystemXClassification",
       schema = "dbo")
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
public class SystemsXClassification
		extends WarehouseClassificationRelationshipTable<Systems,
		Classification,
		SystemsXClassification,
		SystemsXClassificationQueryBuilder,
		java.lang.String, SystemsXClassificationSecurityToken>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "SystemXClassificationID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<SystemsXClassificationSecurityToken> securities;
	
	public SystemsXClassification()
	{
	
	}
	
	public SystemsXClassification(java.lang.String systemXClassificationID)
	{
		id = systemXClassificationID;
	}
	
	@Override
	public void configureSecurityEntity(SystemsXClassificationSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	@Override
	public java.lang.String getId()
	{
		return id;
	}
	
	@Override
	public SystemsXClassification setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public List<SystemsXClassificationSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public SystemsXClassification setSecurities(List<SystemsXClassificationSecurityToken> securities)
	{
		this.securities = securities;
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
		SystemsXClassification that = (SystemsXClassification) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public Systems getPrimary()
	{
		return getSystemID();
	}
	
	@Override
	public Classification getSecondary()
	{
		return getClassificationID();
	}
}
