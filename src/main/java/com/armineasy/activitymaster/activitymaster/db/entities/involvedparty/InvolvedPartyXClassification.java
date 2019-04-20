package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
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
@Table(name = "InvolvedPartyXClassification")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class InvolvedPartyXClassification
		extends WarehouseClassificationRelationshipTable<InvolvedParty, Classification, InvolvedPartyXClassification, InvolvedPartyXClassificationQueryBuilder, Long, InvolvedPartyXClassificationSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXClassificationID")
	private Long id;
	@JoinColumn(name = "InvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.EAGER)
	private InvolvedParty involvedPartyID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXClassificationSecurityToken> securities;

	public InvolvedPartyXClassification()
	{

	}

	public InvolvedPartyXClassification(Long involvedPartyXClassificationID)
	{
		this.id = involvedPartyXClassificationID;
	}

	@Override
	protected InvolvedPartyXClassificationSecurityToken configureDefaultsForNewToken(InvolvedPartyXClassificationSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
