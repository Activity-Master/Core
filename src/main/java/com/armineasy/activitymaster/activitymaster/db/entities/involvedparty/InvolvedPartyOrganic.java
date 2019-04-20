package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyOrganicQueryBuilder;
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
@Table(name = "InvolvedPartyOrganic")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class InvolvedPartyOrganic
		extends WarehouseTable<InvolvedPartyOrganic, InvolvedPartyOrganicQueryBuilder, Long, InvolvedPartyOrganicSecurityToken>

{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
			name = "InvolvedPartyOrganicID")
	@Getter
	@Setter
	private Long id;

	@JoinColumn(name = "InvolvedPartyOrganicID",
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
	private List<InvolvedPartyOrganicSecurityToken> securities;

	public InvolvedPartyOrganic()
	{

	}

	public InvolvedPartyOrganic(Long involvedPartyOrganicID)
	{
		this.id = involvedPartyOrganicID;
	}

	@Override
	protected InvolvedPartyOrganicSecurityToken configureDefaultsForNewToken(InvolvedPartyOrganicSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
