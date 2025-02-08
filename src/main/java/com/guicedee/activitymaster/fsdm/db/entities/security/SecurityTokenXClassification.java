package com.guicedee.activitymaster.fsdm.db.entities.security;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.security.builders.SecurityTokenXClassificationQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

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
@Table(schema = "Security", name = "SecurityTokenXClassification")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class SecurityTokenXClassification
		extends WarehouseClassificationRelationshipTable<SecurityToken,
		Classification,
		SecurityTokenXClassification,
		SecurityTokenXClassificationQueryBuilder,
		java.lang.String, SecurityTokenXClassificationSecurityToken>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "SecurityTokenXClassificationID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "SecurityTokenID",
	            referencedColumnName = "SecurityTokenID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private SecurityToken securityTokenID;
	
@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<SecurityTokenXClassificationSecurityToken> securities;
	
	public SecurityTokenXClassification()
	{
	
	}
	
	public SecurityTokenXClassification(java.lang.String securityTokenXClassificationID)
	{
		this.id = securityTokenXClassificationID;
	}
	
	@Override
	public void configureSecurityEntity(SecurityTokenXClassificationSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public SecurityTokenXClassification setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public SecurityToken getSecurityTokenID()
	{
		return this.securityTokenID;
	}
	
	public SecurityTokenXClassification setSecurityTokenID(SecurityToken securityTokenID)
	{
		this.securityTokenID = securityTokenID;
		return this;
	}
	
	public List<SecurityTokenXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public SecurityTokenXClassification setSecurities(List<SecurityTokenXClassificationSecurityToken> securities)
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
		SecurityTokenXClassification that = (SecurityTokenXClassification) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public SecurityToken getPrimary()
	{
		return getSecurityTokenID();
	}
	
	@Override
	public Classification getSecondary()
	{
		return getClassificationID();
	}
}
