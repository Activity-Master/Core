package com.guicedee.activitymaster.fsdm.db.entities.enterprise;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseXClassificationQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EnterpriseXClassification",
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
public class EnterpriseXClassification
		extends WarehouseClassificationRelationshipTable<Enterprise,
		Classification,
		EnterpriseXClassification,
		EnterpriseXClassificationQueryBuilder,
		java.lang.String>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EnterpriseXClassificationID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EnterpriseXClassificationSecurityToken> securities;
	
	public EnterpriseXClassification()
	{
	
	}
	
	public EnterpriseXClassification(java.lang.String enterpriseXClassificationID)
	{
		id = enterpriseXClassificationID;
	}
	
	@Override
	public java.lang.String getId()
	{
		return id;
	}
	
	@Override
	public EnterpriseXClassification setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public List<EnterpriseXClassificationSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public EnterpriseXClassification setSecurities(List<EnterpriseXClassificationSecurityToken> securities)
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
		EnterpriseXClassification that = (EnterpriseXClassification) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public Enterprise getPrimary()
	{
		return getEnterpriseID();
	}
	
	@Override
	public Classification getSecondary()
	{
		return getClassificationID();
	}
}
