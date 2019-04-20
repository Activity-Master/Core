package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeSecurityTokenQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.SecurityHierarchyView;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

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
@Table(name = "InvolvedPartyIdentificationTypeSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class InvolvedPartyIdentificationTypeSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyIdentificationTypeSecurityToken, InvolvedPartyIdentificationTypeSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyIdentificationTypeSecurityTokenID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyIdentificationTypeID",
			referencedColumnName = "InvolvedPartyIdentificationTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedPartyIdentificationType base;

	public InvolvedPartyIdentificationTypeSecurityToken()
	{

	}

	public InvolvedPartyIdentificationTypeSecurityToken(Long involvedPartyIdentificationTypeSecurityTokenID)
	{
		this.id = involvedPartyIdentificationTypeSecurityTokenID;
	}
}
