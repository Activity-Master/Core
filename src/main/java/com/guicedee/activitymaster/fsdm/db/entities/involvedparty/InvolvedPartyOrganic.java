package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyOrganicQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Party", name = "InvolvedPartyOrganic")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class InvolvedPartyOrganic
		extends WarehouseTable<InvolvedPartyOrganic, InvolvedPartyOrganicQueryBuilder, UUID>

{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
	        name = "InvolvedPartyOrganicID")
	@JsonValue
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@JoinColumn(name = "InvolvedPartyOrganicID",
	            referencedColumnName = "InvolvedPartyID",
	            nullable = false,
	            insertable = false,
	            updatable = false)
	@OneToOne(optional = false,
	          fetch = FetchType.LAZY)
	private InvolvedParty involvedParty;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicSecurityToken> securities;
	
	public InvolvedPartyOrganic()
	{
	
	}
	
	public InvolvedPartyOrganic(UUID involvedPartyOrganicID)
	{
		this.id = involvedPartyOrganicID;
	}
	
	public List<InvolvedPartyOrganicSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public InvolvedPartyOrganic setSecurities(List<InvolvedPartyOrganicSecurityToken> securities)
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
		InvolvedPartyOrganic that = (InvolvedPartyOrganic) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public String toString()
	{
		return "OrganicParty - " + getId();
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public InvolvedParty getInvolvedParty()
	{
		return this.involvedParty;
	}
	
	public InvolvedPartyOrganic setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyOrganic setInvolvedParty(InvolvedParty involvedParty)
	{
		this.involvedParty = involvedParty;
		return this;
	}
}
