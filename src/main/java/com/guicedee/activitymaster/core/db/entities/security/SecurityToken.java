package com.guicedee.activitymaster.core.db.entities.security;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlagSecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressSecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressXGeographySecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.*;
import com.guicedee.activitymaster.core.db.entities.classifications.*;
import com.guicedee.activitymaster.core.db.entities.events.*;
import com.guicedee.activitymaster.core.db.entities.arrangement.*;
import com.guicedee.activitymaster.core.db.entities.classifications.*;
import com.guicedee.activitymaster.core.db.entities.events.*;
import com.guicedee.activitymaster.core.db.entities.geography.GeographySecurityToken;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXGeographySecurityToken;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.db.entities.product.ProductSecurityToken;
import com.guicedee.activitymaster.core.db.entities.product.ProductXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.product.ProductXProductSecurityToken;
import com.guicedee.activitymaster.core.db.entities.product.ProductXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemDataSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.builders.SecurityTokenQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.systems.SystemsSecurityToken;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISecurityToken;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.logger.LogFactory;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;
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

@Access(FIELD)@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class SecurityToken
		extends WarehouseSCDNameDescriptionTable<SecurityToken, SecurityTokenQueryBuilder, java.util.UUID, SecurityTokensSecurityToken>
		implements IContainsClassifications<SecurityToken, Classification, SecurityTokenXClassification, IClassificationValue<?>, ISecurityToken<?>, IClassification<?>, SecurityToken>,
				           IActivityMasterEntity<SecurityToken>,
				           ISecurityToken<SecurityToken>
{
	private static final Logger log = LogFactory.getLog("SecurityToken");

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "SecurityTokenID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
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

	public SecurityToken()
	{

	}

	public SecurityToken(UUID securityTokenID)
	{
		this.id = securityTokenID;
	}

	public SecurityToken(UUID securityTokenID, String securityToken, String securityTokenFriendlyName, String securityTokenFriendlyDescription, String originalSourceSystemUniqueID)
	{
		this.id = securityTokenID;
		this.securityToken = securityToken;
		this.name = securityTokenFriendlyName;
		this.description = securityTokenFriendlyDescription;
	}

	@Override
	public String toString()
	{
		return "Security - " + getSecurityToken();
	}

	@Override
	public void configureForClassification(SecurityTokenXClassification classificationLink, ISystems<?> system)
	{
		classificationLink.setSecurityTokenID(this);
	}

	@Override
	protected SecurityTokensSecurityToken configureDefaultsForNewToken(SecurityTokensSecurityToken stAdmin, ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);
		stAdmin.setBase(this);
		return stAdmin;
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

	public java.util.UUID getId()
	{
		return this.id;
	}

	public String getSecurityToken()
	{
		return this.securityToken;
	}

	public @NotNull String getName()
	{
		return this.name;
	}

	public @NotNull String getDescription()
	{
		return this.description;
	}

	public Classification getSecurityTokenClassificationID()
	{
		return this.securityTokenClassificationID;
	}

	public SecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public SecurityToken setSecurityToken(String securityToken)
	{
		this.securityToken = securityToken;
		return this;
	}

	public SecurityToken setName(@NotNull String name)
	{
		this.name = name;
		return this;
	}

	public SecurityToken setDescription(@NotNull String description)
	{
		this.description = description;
		return this;
	}

	public SecurityToken setSecurityTokenClassificationID(Classification securityTokenClassificationID)
	{
		this.securityTokenClassificationID = securityTokenClassificationID;
		return this;
	}
}
