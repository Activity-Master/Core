package com.guicedee.activitymaster.fsdm.db.entities.activeflag;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders.ActiveFlagXClassificationQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
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
@Table(name = "ActiveFlagXClassification",
       schema = "dbo")
@XmlRootElement
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ActiveFlagXClassification
		extends WarehouseClassificationRelationshipTable<
		ActiveFlag,
		Classification,
		ActiveFlagXClassification,
		ActiveFlagXClassificationQueryBuilder,
		java.lang.String,
		ActiveFlagXClassificationSecurityToken>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ActiveFlagXClassificationID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "SystemID",
	            referencedColumnName = "SystemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Systems systemID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ActiveFlagXClassificationSecurityToken> securities;
	
	public ActiveFlagXClassification()
	{
	
	}
	
	public ActiveFlagXClassification(java.lang.String activeFlagXClassificationID)
	{
		this.id = activeFlagXClassificationID;
	}
	
	@Override
	public void configureSecurityEntity(ActiveFlagXClassificationSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public ActiveFlagXClassification setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ActiveFlagXClassification setSystemID(Systems systemID)
	{
		this.systemID = systemID;
		return this;
	}
	
	public ActiveFlagXClassification setSecurities(List<ActiveFlagXClassificationSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	@Override
	public ActiveFlag getPrimary()
	{
		return getActiveFlagID();
	}
	
	@Override
	public Classification getSecondary()
	{
		return getClassificationID();
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	@Override
	public Systems getSystemID()
	{
		return systemID;
	}
	
	public List<ActiveFlagXClassificationSecurityToken> getSecurities()
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
		ActiveFlagXClassification that = (ActiveFlagXClassification) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
