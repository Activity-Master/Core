package com.guicedee.activitymaster.fsdm.db.entities.enterprise;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.assists.WarehouseNameDescriptionTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Enterprise",
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
@EqualsAndHashCode(of="id",callSuper = false)
public class Enterprise
		extends WarehouseNameDescriptionTable<Enterprise, EnterpriseQueryBuilder, java.lang.String,EnterpriseSecurityToken>
		implements //IContainsClassifications<Enterprise, Classification, EnterpriseXClassification, IEnterpriseClassification<?>, IEnterprise, IClassification<?,?>, Enterprise>,
		IEnterprise<Enterprise, EnterpriseQueryBuilder>
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(nullable = false,
	        name = "EnterpriseID")
	@JsonValue
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Column(nullable = false,
	        name = "EnterpriseName")
	private String name;
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Column(nullable = false,
	        name = "EnterpriseDesc")
	private String description;
	
	@Getter
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EnterpriseSecurityToken> securities;
	
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EnterpriseXClassification> classifications;
	
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<ActiveFlag> activeFlags;
	
	
	public Enterprise()
	{
	
	}
	
	public Enterprise(java.lang.String id)
	{
		this.id = id;
	}
	
	public Enterprise(java.lang.String id, String enterpriseName, String enterpriseDesc)
	{
		this.id = id;
		name = enterpriseName;
		description = enterpriseDesc;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	//@Override
	public void configureForClassification(EnterpriseXClassification classificationLink, ISystems<?, ?> system)
	{
	
	}
	
	@Override
	public java.lang.String getId()
	{
		return id;
	}
	
	@Override
	public Enterprise setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public @NotNull String getName()
	{
		return name;
	}
	
	@Override
	public Enterprise setName(@NotNull String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public @NotNull String getDescription()
	{
		return description;
	}
	
	@Override
	public Enterprise setDescription(@NotNull String description)
	{
		this.description = description;
		return this;
	}
	
	public String name()
	{
		return getName();
	}
	
	public String classificationDescription()
	{
		return getDescription();
	}
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
	{
		EnterpriseXClassification x = (EnterpriseXClassification) linkTable;
		x.setEnterpriseID(this);
	}
	
	@Override
	public @NotNull boolean isFake()
	{
		return getId() == null;
	}
}
