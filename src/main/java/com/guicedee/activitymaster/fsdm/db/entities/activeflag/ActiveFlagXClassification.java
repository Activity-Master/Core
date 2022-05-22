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
import java.util.*;

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
		extends WarehouseClassificationRelationshipTable<ActiveFlag,
		Classification,
		ActiveFlagXClassification,
		ActiveFlagXClassificationQueryBuilder,
		UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ActiveFlagXClassificationID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
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
	
	public ActiveFlagXClassification(UUID activeFlagXClassificationID)
	{
		this.id = activeFlagXClassificationID;
	}
	
	
	public UUID getId()
	{
		return this.id;
	}
	
	public ActiveFlagXClassification setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public Systems getSystemID()
	{
		return this.systemID;
	}
	
	public ActiveFlagXClassification setSystemID(Systems systemID)
	{
		this.systemID = systemID;
		return this;
	}
	
	public List<ActiveFlagXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public ActiveFlagXClassification setSecurities(List<ActiveFlagXClassificationSecurityToken> securities)
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
		ActiveFlagXClassification that = (ActiveFlagXClassification) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
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
}
