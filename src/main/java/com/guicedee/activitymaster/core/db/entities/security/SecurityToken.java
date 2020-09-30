package com.guicedee.activitymaster.core.db.entities.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

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

@Access(FIELD)
public class SecurityToken
		extends WarehouseSCDNameDescriptionTable<SecurityToken, SecurityTokenQueryBuilder, Long, SecurityTokensSecurityToken>
		implements IContainsClassifications<SecurityToken, Classification, SecurityTokenXClassification, IClassificationValue<?>, ISecurityToken<?>, IClassification<?>, SecurityToken>,
				           IActivityMasterEntity<SecurityToken>,
				           ISecurityToken<SecurityToken>
{
	private static final Logger log = LogFactory.getLog("SecurityToken");

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "SecurityTokenID")
	@JsonValue
	private Long id;
	@Basic(optional = false,
			fetch = FetchType.EAGER)
	@NotNull
	@Size(min = 1,
			max = 128)
	@Column(nullable = false,
			length = 128,
			name = "SecurityToken")
	@JsonIgnore
	private String securityToken;
	@Basic(optional = false,
			fetch = EAGER)
	@NotNull
	@Column(nullable = false,
			name = "SecurityTokenFriendlyName")
	@JsonIgnore
	private String name;
	@Basic(optional = false,
			fetch = FetchType.EAGER)
	@NotNull
	@Column(nullable = false,
			name = "SecurityTokenFriendlyDescription")
	@JsonIgnore
	private String description;
	@JoinColumn(name = "SecurityTokenClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	@JsonIgnore
	private Classification securityTokenClassificationID;

	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<SecurityTokenXClassification> classifications;
	@OneToMany(
			mappedBy = "securityTokenID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<SecurityTokensSecurityToken> securities;

	@OneToMany(
			mappedBy = "childSecurityTokenID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<SecurityTokenXSecurityToken> securityTokenXSecurityTokenChildList;

	public SecurityToken()
	{

	}

	public SecurityToken(Long securityTokenID)
	{
		this.id = securityTokenID;
	}

	public SecurityToken(Long securityTokenID, String securityToken, String securityTokenFriendlyName, String securityTokenFriendlyDescription, String originalSourceSystemUniqueID)
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
	public void configureForClassification(SecurityTokenXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setSecurityTokenID(this);
	}

	@Override
	protected SecurityTokensSecurityToken configureDefaultsForNewToken(SecurityTokensSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
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

	public Long getId()
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

	public SecurityToken setId(Long id)
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
