package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyNonOrganicQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "InvolvedPartyNonOrganic")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class InvolvedPartyNonOrganic
		extends WarehouseTable<InvolvedPartyNonOrganic, InvolvedPartyNonOrganicQueryBuilder, Long, InvolvedPartyNonOrganicSecurityToken>
{

	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
			name = "InvolvedPartyNonOrganicID")
	@Getter
	@Setter
	private Long id;

	@JoinColumn(name = "InvolvedPartyNonOrganicID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false,
			insertable = false,
			updatable = false)
	@OneToOne(optional = false,
			fetch = FetchType.LAZY)
	@Getter
	@Setter
	private InvolvedParty involvedParty;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNonOrganicSecurityToken> securities;

	public InvolvedPartyNonOrganic()
	{

	}

	public InvolvedPartyNonOrganic(Long involvedPartyNonOrganicID)
	{
		this.id = involvedPartyNonOrganicID;
	}

	@Override
	protected InvolvedPartyNonOrganicSecurityToken configureDefaultsForNewToken(InvolvedPartyNonOrganicSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
