package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyTypeQueryBuilder;
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
@Table(uniqueConstraints =
		       {
				       @UniqueConstraint(columnNames =
						                         {
								                         "InvolvedPartyTypeName"
						                         })
		       },
		name = "InvolvedPartyType")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class InvolvedPartyType
		extends WarehouseSCDNameDescriptionTable<InvolvedPartyType, InvolvedPartyTypeQueryBuilder, Long, InvolvedPartyTypeSecurityToken>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyTypeID")
	@Getter
	@Setter
	private Long id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 100)
	@Column(nullable = false,
			length = 100,
			name = "InvolvedPartyTypeName")
	@Getter
	@Setter
	private String name;
	@Basic(optional = false)
	@NotNull
	@Lob
	@Column(nullable = false,
			name = "InvolvedPartyTypeDesc")
	@Getter
	@Setter
	private String description;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyTypeSecurityToken> securities;

	@OneToMany(
			mappedBy = "involvedPartyTypeID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList;

	public InvolvedPartyType()
	{

	}

	public InvolvedPartyType(Long involvedPartyTypeID)
	{
		this.id = involvedPartyTypeID;
	}

	public InvolvedPartyType(Long involvedPartyTypeID, String involvedPartyTypeName, String involvedPartyTypeDesc)
	{
		this.id = involvedPartyTypeID;
		this.name = involvedPartyTypeName;
		this.description = involvedPartyTypeDesc;
	}

	@Override
	protected InvolvedPartyTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyTypeSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
