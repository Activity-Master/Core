package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXInvolvedPartySecurityTokenQueryBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ArrangementXInvolvedPartySecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Access(FIELD)@lombok.Data
public class ArrangementXInvolvedPartySecurityToken
		extends WarehouseSecurityTable<ArrangementXInvolvedPartySecurityToken, ArrangementXInvolvedPartySecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementXInvolvedPartySecurityTokenID")
	private Long id;

	@JoinColumn(name = "ArrangementXInvolvedPartyID",
			referencedColumnName = "ArrangementXInvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ArrangementXInvolvedParty base;

	public ArrangementXInvolvedPartySecurityToken()
	{

	}

	public ArrangementXInvolvedPartySecurityToken(Long arrangementXInvolvedPartySecurityTokenID)
	{
		this.id = arrangementXInvolvedPartySecurityTokenID;
	}
}
