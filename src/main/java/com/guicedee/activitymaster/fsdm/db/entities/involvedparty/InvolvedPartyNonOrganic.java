package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyNonOrganicQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@Table(schema = "Party",
       name = "InvolvedPartyNonOrganic")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class InvolvedPartyNonOrganic
		extends WarehouseTable<InvolvedPartyNonOrganic, InvolvedPartyNonOrganicQueryBuilder, UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
	        name = "InvolvedPartyNonOrganicID")
	@JsonValue
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@JoinColumn(name = "InvolvedPartyNonOrganicID",
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
	private List<InvolvedPartyNonOrganicSecurityToken> securities;
	
	public InvolvedPartyNonOrganic()
	{
	
	}
	
	public InvolvedPartyNonOrganic(UUID involvedPartyNonOrganicID)
	{
		id = involvedPartyNonOrganicID;
	}
	
	public List<InvolvedPartyNonOrganicSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public InvolvedPartyNonOrganic setSecurities(List<InvolvedPartyNonOrganicSecurityToken> securities)
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
		InvolvedPartyNonOrganic that = (InvolvedPartyNonOrganic) o;
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
		return getId() + "";
	}
	
	@Override
	public UUID getId()
	{
		return id;
	}
	
	public InvolvedParty getInvolvedParty()
	{
		return involvedParty;
	}
	
	@Override
	public InvolvedPartyNonOrganic setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyNonOrganic setInvolvedParty(InvolvedParty involvedParty)
	{
		this.involvedParty = involvedParty;
		return this;
	}
}
