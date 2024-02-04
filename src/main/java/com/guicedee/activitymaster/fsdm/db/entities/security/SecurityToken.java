package com.guicedee.activitymaster.fsdm.db.entities.security;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlagSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.security.builders.SecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Security",
       name = "SecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class SecurityToken
		extends WarehouseTable<SecurityToken, SecurityTokenQueryBuilder, java.lang.String>
		implements ISecurityToken<SecurityToken, SecurityTokenQueryBuilder>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "SecurityTokenID")
	@JsonValue
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Size(min = 1,
	      max = 128)
	@Column(nullable = false,
	        length = 128,
	        name = "SecurityToken")
	private String securityToken;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Column(nullable = false,
	        name = "SecurityTokenFriendlyName")
	private String name;
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Column(nullable = false,
	        name = "SecurityTokenFriendlyDescription")
	private String description;
	@JoinColumn(name = "SecurityTokenClassificationID",
	            referencedColumnName = "ClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Classification securityTokenClassificationID;
	
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXClassification> classifications;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<SecurityTokensSecurityToken> securities;
	
	@OneToMany(
			mappedBy = "childSecurityTokenID",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenChildList;
	
	
	//===================================================================================================================================================
	
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	private List<ActiveFlagSecurityToken> activeFlagSecurityTokens;
	
	
	public SecurityToken()
	{
	
	}
	
	public SecurityToken(java.lang.String securityTokenID)
	{
		this.id = securityTokenID;
	}
	
	public SecurityToken(java.lang.String securityTokenID, String securityToken, String securityTokenFriendlyName, String securityTokenFriendlyDescription, String originalSourceSystemUniqueID)
	{
		this.id = securityTokenID;
		this.securityToken = securityToken;
		this.name = securityTokenFriendlyName;
		this.description = securityTokenFriendlyDescription;
	}
	
	@Override
	public String toString()
	{
		return getSecurityToken();
	}
	
	public List<SecurityTokenXClassification> getClassifications()
	{
		return this.classifications;
	}
	
	public SecurityToken setClassifications(List<SecurityTokenXClassification> classifications)
	{
		this.classifications = classifications;
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
		SecurityToken that = (SecurityToken) o;
		return Objects.equals(getSecurityToken(), that.getSecurityToken());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getSecurityToken());
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public SecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public String getSecurityToken()
	{
		return this.securityToken;
	}
	
	public SecurityToken setSecurityToken(String securityToken)
	{
		this.securityToken = securityToken;
		return this;
	}
	
	public @NotNull String getName()
	{
		return this.name;
	}
	
	public SecurityToken setName(@NotNull String name)
	{
		this.name = name;
		return this;
	}
	
	public @NotNull String getDescription()
	{
		return this.description;
	}
	
	public SecurityToken setDescription(@NotNull String description)
	{
		this.description = description;
		return this;
	}
	
	public Classification getSecurityTokenClassificationID()
	{
		return this.securityTokenClassificationID;
	}
	
	public SecurityToken setSecurityTokenClassificationID(Classification securityTokenClassificationID)
	{
		this.securityTokenClassificationID = securityTokenClassificationID;
		return this;
	}
	
	
	@Override
	public void configureNewHierarchyItem(IWarehouseRelationshipClassificationTable<?, ?, SecurityToken, SecurityToken, java.lang.String> newLink, SecurityToken parent, SecurityToken child, String value)
	{
		((SecurityTokenXSecurityToken) newLink).setParentSecurityTokenID(parent);
		((SecurityTokenXSecurityToken) newLink).setChildSecurityTokenID(child);
		newLink.setValue(value);
		
	}
}
