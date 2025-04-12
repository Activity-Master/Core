package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXInvolvedPartySecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Arrangement", name = "ArrangementXInvolvedPartySecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArrangementXInvolvedPartySecurityToken
		extends WarehouseSecurityTable<ArrangementXInvolvedPartySecurityToken, ArrangementXInvolvedPartySecurityTokenQueryBuilder, UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXInvolvedPartySecurityTokenID")

	private java.util.UUID id;
	
	@JoinColumn(name = "ArrangementXInvolvedPartyID",
	            referencedColumnName = "ArrangementXInvolvedPartyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ArrangementXInvolvedParty base;

	public String toString()
	{
		return "ArrangementXInvolvedPartySecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public ArrangementXInvolvedPartySecurityToken setBase(ArrangementXInvolvedParty base)
	{
		this.base = base;
		return this;
	}

	public ArrangementXInvolvedParty getBase()
	{
		return base;
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
		ArrangementXInvolvedPartySecurityToken that = (ArrangementXInvolvedPartySecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
