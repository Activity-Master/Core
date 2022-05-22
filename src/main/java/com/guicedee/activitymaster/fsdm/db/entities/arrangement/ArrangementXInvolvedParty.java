package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXInvolvedPartyQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
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
@Table(schema = "Arrangement", name = "ArrangementXInvolvedParty")
@XmlRootElement
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ArrangementXInvolvedParty
		extends WarehouseClassificationRelationshipTable<Arrangement,
		InvolvedParty,
		ArrangementXInvolvedParty,
		ArrangementXInvolvedPartyQueryBuilder,
		UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXInvolvedPartyID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
	@JoinColumn(name = "ArrangementID",
	            referencedColumnName = "ArrangementID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Arrangement arrangementID;
	
	@JoinColumn(name = "InvolvedPartyID",
	            referencedColumnName = "InvolvedPartyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private InvolvedParty involvedPartyID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXInvolvedPartySecurityToken> securities;
	
	public ArrangementXInvolvedParty()
	{
	
	}
	
	public ArrangementXInvolvedParty(UUID arrangementXInvolvedPartyID)
	{
		this.id = arrangementXInvolvedPartyID;
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public ArrangementXInvolvedParty setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public Arrangement getArrangementID()
	{
		return this.arrangementID;
	}
	
	public ArrangementXInvolvedParty setArrangementID(Arrangement arrangementID)
	{
		this.arrangementID = arrangementID;
		return this;
	}
	
	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}
	
	public ArrangementXInvolvedParty setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}
	
	public List<ArrangementXInvolvedPartySecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public ArrangementXInvolvedParty setSecurities(List<ArrangementXInvolvedPartySecurityToken> securities)
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
		ArrangementXInvolvedParty that = (ArrangementXInvolvedParty) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public Arrangement getPrimary()
	{
		return getArrangementID();
	}
	
	@Override
	public InvolvedParty getSecondary()
	{
		return getInvolvedPartyID();
	}
}
