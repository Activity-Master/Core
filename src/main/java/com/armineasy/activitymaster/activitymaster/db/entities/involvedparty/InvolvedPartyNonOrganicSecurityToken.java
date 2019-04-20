package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyNonOrganicSecurityTokenQueryBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "InvolvedPartyNonOrganicSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class InvolvedPartyNonOrganicSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyNonOrganicSecurityToken, InvolvedPartyNonOrganicSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyNonOrganicSecurityTokenID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyNonOrganicID",
			referencedColumnName = "InvolvedPartyNonOrganicID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private InvolvedPartyNonOrganic base;

	public InvolvedPartyNonOrganicSecurityToken()
	{

	}

	public InvolvedPartyNonOrganicSecurityToken(Long involvedPartyNonOrganicSecurityTokenID)
	{
		this.id = involvedPartyNonOrganicSecurityTokenID;
	}
}
