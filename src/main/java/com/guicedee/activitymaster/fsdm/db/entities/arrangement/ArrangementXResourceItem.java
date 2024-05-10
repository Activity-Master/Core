package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXResourceItemQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
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
@Entity
@Table(schema = "Arrangement", name = "ArrangementXResourceItem")
@XmlRootElement
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@EqualsAndHashCode(of = "id", callSuper = false)
public class ArrangementXResourceItem
		extends WarehouseClassificationRelationshipTable<Arrangement,
		ResourceItem,
		ArrangementXResourceItem,
		ArrangementXResourceItemQueryBuilder,
		java.lang.String,
		ArrangementXResourceItemSecurityToken
		>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXResourceItemID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList;
	
	@JoinColumn(name = "ArrangementID",
	            referencedColumnName = "ArrangementID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Arrangement arrangementID;
	
	@JoinColumn(name = "ResourceItemID",
	            referencedColumnName = "ResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ResourceItem resourceItemID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItemSecurityToken> securities;
	
	public ArrangementXResourceItem()
	{
	
	}
	
	public ArrangementXResourceItem(java.lang.String arrangementXResourceItemID)
	{
		this.id = arrangementXResourceItemID;
	}
	
	public ArrangementXResourceItem setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementXResourceItem setArrangementXResourceItemSecurityTokenList(List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList)
	{
		this.arrangementXResourceItemSecurityTokenList = arrangementXResourceItemSecurityTokenList;
		return this;
	}
	
	public ArrangementXResourceItem setArrangementID(Arrangement arrangementID)
	{
		this.arrangementID = arrangementID;
		return this;
	}
	
	public ArrangementXResourceItem setResourceItemID(ResourceItem resourceItemID)
	{
		this.resourceItemID = resourceItemID;
		return this;
	}
	
	public ArrangementXResourceItem setSecurities(List<ArrangementXResourceItemSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	@Override
	public Arrangement getPrimary()
	{
		return getArrangementID();
	}
	
	@Override
	public ResourceItem getSecondary()
	{
		return getResourceItemID();
	}
}
