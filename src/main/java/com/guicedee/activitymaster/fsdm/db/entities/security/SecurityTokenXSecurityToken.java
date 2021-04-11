package com.guicedee.activitymaster.fsdm.db.entities.security;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.security.builders.SecurityTokenXSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
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
@Table(schema = "Security", name = "SecurityTokenXSecurityToken")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class SecurityTokenXSecurityToken
		extends WarehouseClassificationRelationshipTable<SecurityToken,
		SecurityToken,
		SecurityTokenXSecurityToken,
		SecurityTokenXSecurityTokenQueryBuilder,
		UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "SecurityTokenXSecurityTokenID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@JoinColumn(name = "ParentSecurityTokenID",
	            referencedColumnName = "SecurityTokenID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private SecurityToken parentSecurityTokenID;
	
	@JoinColumn(name = "ChildSecurityTokenID",
	            referencedColumnName = "SecurityTokenID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private SecurityToken childSecurityTokenID;
	
	public SecurityTokenXSecurityToken()
	{
	
	}
	
	public SecurityTokenXSecurityToken(UUID securityTokenXSecurityTokenID)
	{
		this.id = securityTokenXSecurityTokenID;
	}
	
	
	public UUID getId()
	{
		return this.id;
	}
	
	public SecurityToken getParentSecurityTokenID()
	{
		return this.parentSecurityTokenID;
	}
	
	public SecurityToken getChildSecurityTokenID()
	{
		return this.childSecurityTokenID;
	}
	
	public SecurityTokenXSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public SecurityTokenXSecurityToken setParentSecurityTokenID(SecurityToken parentSecurityTokenID)
	{
		this.parentSecurityTokenID = parentSecurityTokenID;
		return this;
	}
	
	public SecurityTokenXSecurityToken setChildSecurityTokenID(SecurityToken childSecurityTokenID)
	{
		this.childSecurityTokenID = childSecurityTokenID;
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
		SecurityTokenXSecurityToken that = (SecurityTokenXSecurityToken) o;
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
		return getParentSecurityTokenID();
	}
	
	@Override
	public SecurityToken getSecondary()
	{
		return getChildSecurityTokenID();
	}
}
