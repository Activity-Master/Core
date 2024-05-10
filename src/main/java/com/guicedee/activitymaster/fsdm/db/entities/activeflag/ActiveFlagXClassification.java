package com.guicedee.activitymaster.fsdm.db.entities.activeflag;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders.ActiveFlagXClassificationQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Getter
@EqualsAndHashCode(of = "id",callSuper = false)
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
}
