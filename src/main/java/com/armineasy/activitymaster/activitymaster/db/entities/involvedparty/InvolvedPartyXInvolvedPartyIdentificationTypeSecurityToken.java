package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenQueryBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken,
				                              InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyXInvolvedPartyIdentificationTypeID",
			referencedColumnName = "InvolvedPartyXInvolvedPartyIdentificationTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private InvolvedPartyXInvolvedPartyIdentificationType base;

	public InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken()
	{

	}

	public InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken(Long involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenID)
	{
		this.id = involvedPartyXInvolvedPartyIdentificationTypeSecurityTokenID;
	}
}
