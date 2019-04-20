package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "InvolvedPartyIdentificationType")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class InvolvedPartyIdentificationType
		extends WarehouseSCDNameDescriptionTable<InvolvedPartyIdentificationType, InvolvedPartyIdentificationTypeQueryBuilder, Long, InvolvedPartyIdentificationTypeSecurityToken>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyIdentificationTypeID")
	@Getter
	@Setter
	private Long id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 150)
	@Column(nullable = false,
			length = 150,
			name = "InvolvedPartyIdentificationName")
	@Getter
	@Setter
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "InvolvedPartyIdentificationDesc")
	@Getter
	@Setter
	private String description;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyIdentificationTypeSecurityToken> securities;

	@OneToMany(
			mappedBy = "involvedPartyIdentificationTypeID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyIdentificationType> involvedPartyXInvolvedPartyIdentificationTypeList;

	public InvolvedPartyIdentificationType()
	{

	}

	public InvolvedPartyIdentificationType(Long involvedPartyIdentificationTypeID)
	{
		this.id = involvedPartyIdentificationTypeID;
	}

	public InvolvedPartyIdentificationType(Long involvedPartyIdentificationTypeID, String involvedPartyIdentificationName, String involvedPartyIdentificationDesc)
	{
		this.id = involvedPartyIdentificationTypeID;
		this.name = involvedPartyIdentificationName;
		this.description = involvedPartyIdentificationDesc;
	}

	@Override
	protected InvolvedPartyIdentificationTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyIdentificationTypeSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public String toString() {
		return getName();
	}
}
