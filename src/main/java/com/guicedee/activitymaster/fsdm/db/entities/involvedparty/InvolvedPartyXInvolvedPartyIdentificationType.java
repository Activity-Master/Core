package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.fsdm.api.Passwords;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder;
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
@Table(schema = "Party",
       name = "InvolvedPartyXInvolvedPartyIdentificationType")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class InvolvedPartyXInvolvedPartyIdentificationType
		extends WarehouseClassificationRelationshipTypesTable<InvolvedParty, InvolvedPartyIdentificationType,
		InvolvedPartyXInvolvedPartyIdentificationType,
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder,
		java.lang.String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXInvolvedPartyIdentificationTypeID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> securities;
	
	@JoinColumn(name = "InvolvedPartyID",
	            referencedColumnName = "InvolvedPartyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;
	
	@JoinColumn(name = "InvolvedPartyIdentificationTypeID",
	            referencedColumnName = "InvolvedPartyIdentificationTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private InvolvedPartyIdentificationType involvedPartyIdentificationTypeID;
	
	public InvolvedPartyXInvolvedPartyIdentificationType()
	{
	
	}
	
	public InvolvedPartyXInvolvedPartyIdentificationType(java.lang.String involvedPartyXInvolvedPartyIdentificationTypeID)
	{
		this.id = involvedPartyXInvolvedPartyIdentificationTypeID;
	}
	
	public InvolvedPartyXInvolvedPartyIdentificationType(java.lang.String involvedPartyXInvolvedPartyIdentificationTypeID, String value)
	{
		this.id = involvedPartyXInvolvedPartyIdentificationTypeID;
		setValue(value);
	}
	
	
	@Override
	public java.lang.String getId()
	{
		return this.id;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyIdentificationType setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public InvolvedPartyXInvolvedPartyIdentificationType setSecurities(List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}
	
	public InvolvedPartyXInvolvedPartyIdentificationType setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}
	
	public InvolvedPartyIdentificationType getInvolvedPartyIdentificationTypeID()
	{
		return this.involvedPartyIdentificationTypeID;
	}
	
	public InvolvedPartyXInvolvedPartyIdentificationType setInvolvedPartyIdentificationTypeID(InvolvedPartyIdentificationType involvedPartyIdentificationTypeID)
	{
		this.involvedPartyIdentificationTypeID = involvedPartyIdentificationTypeID;
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
		InvolvedPartyXInvolvedPartyIdentificationType that = (InvolvedPartyXInvolvedPartyIdentificationType) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public InvolvedParty getPrimary()
	{
		return getInvolvedPartyID();
	}
	
	@Override
	public InvolvedPartyIdentificationType getSecondary()
	{
		return getInvolvedPartyIdentificationTypeID();
	}
	
	@Override
	public void setValue(String value)
	{
		if (!Strings.isNullOrEmpty(value) && "true".equals(System.getProperty("encrypt", "true")))
		{
			super.setValue(new Passwords().integerEncrypt(value.getBytes()));
		}
		else
		{
			super.setValue(value);
		}
	}
}
