package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedParty;
import com.guicedee.activitymaster.core.services.dto.IInvolvedPartyIdentificationType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IIdentificationType;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Party",
       name = "InvolvedPartyXInvolvedPartyIdentificationType")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyXInvolvedPartyIdentificationType
		extends WarehouseClassificationRelationshipTypesTable<InvolvedParty, InvolvedPartyIdentificationType,
		InvolvedPartyXInvolvedPartyIdentificationType,
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder,
		IIdentificationType<?>,
		java.util.UUID,
		InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken,
		IInvolvedParty<?>, IInvolvedPartyIdentificationType<?>
		>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXInvolvedPartyIdentificationTypeID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
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
	
	public InvolvedPartyXInvolvedPartyIdentificationType(UUID involvedPartyXInvolvedPartyIdentificationTypeID)
	{
		this.id = involvedPartyXInvolvedPartyIdentificationTypeID;
	}
	
	public InvolvedPartyXInvolvedPartyIdentificationType(UUID involvedPartyXInvolvedPartyIdentificationTypeID, String value)
	{
		this.id = involvedPartyXInvolvedPartyIdentificationTypeID;
		setValue(value);
	}
	
	@Override
	protected InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	@Override
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}
	
	public InvolvedPartyIdentificationType getInvolvedPartyIdentificationTypeID()
	{
		return this.involvedPartyIdentificationTypeID;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyIdentificationType setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyXInvolvedPartyIdentificationType setSecurities(List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public InvolvedPartyXInvolvedPartyIdentificationType setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
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
	public IInvolvedParty<?> getPrimary()
	{
		return getInvolvedPartyID();
	}
	
	@Override
	public IInvolvedPartyIdentificationType<?> getSecondary()
	{
		return getInvolvedPartyIdentificationTypeID();
	}
}
