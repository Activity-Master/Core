package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "InvolvedPartyXInvolvedPartyIdentificationType")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class InvolvedPartyXInvolvedPartyIdentificationType
		extends WarehouseRelationshipTable<InvolvedParty, InvolvedPartyIdentificationType,
				                                  InvolvedPartyXInvolvedPartyIdentificationType,
				                                  InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder, Long,
				                                  InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXInvolvedPartyIdentificationTypeID")
	private Long id;

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

	public InvolvedPartyXInvolvedPartyIdentificationType(Long involvedPartyXInvolvedPartyIdentificationTypeID)
	{
		this.id = involvedPartyXInvolvedPartyIdentificationTypeID;
	}

	public InvolvedPartyXInvolvedPartyIdentificationType(Long involvedPartyXInvolvedPartyIdentificationTypeID, String value)
	{
		this.id = involvedPartyXInvolvedPartyIdentificationTypeID;
		setValue(value);
	}

	@Override
	protected InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken stAdmin, IEnterprise enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
