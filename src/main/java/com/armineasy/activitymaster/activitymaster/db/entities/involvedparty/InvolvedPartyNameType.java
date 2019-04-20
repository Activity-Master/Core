package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyNameTypeQueryBuilder;
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
								                         "InvolvedPartyNameTypeName"
						                         })
		       },
		name = "InvolvedPartyNameType")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class InvolvedPartyNameType
		extends WarehouseSCDNameDescriptionTable<InvolvedPartyNameType, InvolvedPartyNameTypeQueryBuilder, Long, InvolvedPartyNameTypeSecurityToken>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyNameTypeID")
	@Getter
	@Setter
	private Long id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "InvolvedPartyNameTypeName")
	@Getter
	@Setter
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "InvolvedPartyNameTypeDescr")
	@Getter
	@Setter
	private String description;

	@OneToMany(
			mappedBy = "involvedPartyNameTypeID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyNameType> involvedPartyXInvolvedPartyNameTypeList;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNameTypeSecurityToken> securities;

	public InvolvedPartyNameType()
	{

	}

	public InvolvedPartyNameType(Long involvedPartyNameTypeID)
	{
		this.id = involvedPartyNameTypeID;
	}

	public InvolvedPartyNameType(Long involvedPartyNameTypeID, String involvedPartyName, String involvedPartyNameDescr)
	{
		this.id = involvedPartyNameTypeID;
		this.name = involvedPartyName;
		this.description = involvedPartyNameDescr;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	protected InvolvedPartyNameTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyNameTypeSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
