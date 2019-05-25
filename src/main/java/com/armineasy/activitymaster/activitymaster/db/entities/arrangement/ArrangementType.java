package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
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
@Table(name = "ArrangementType")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ArrangementType
		extends WarehouseSCDNameDescriptionTable<ArrangementType, ArrangementTypeQueryBuilder, Long, ArrangementTypeSecurityToken>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementTypeID")
	@Getter
	@Setter
	private Long id;
	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@NotNull
	@Size(min = 1,
			max = 150)
	@Column(nullable = false,
			length = 150,
			name = "ArrangementTypeName")
	@Getter
	@Setter
	private String name;
	@Basic(optional = false,
			fetch = FetchType.LAZY)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "ArrangementTypeDescription")
	@Getter
	@Setter
	private String description;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementTypeSecurityToken> securities;

	@OneToMany(
			mappedBy = "type",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementType> arrangementsList;

	public ArrangementType()
	{

	}

	public ArrangementType(Long arrangementTypeID)
	{
		this.id = arrangementTypeID;
	}

	public ArrangementType(Long arrangementTypeID, String arrangementTypeName, String arrangementTypeDescription)
	{
		this.id = arrangementTypeID;
		this.name = arrangementTypeName;
		this.description = arrangementTypeDescription;
	}

	@Override
	protected ArrangementTypeSecurityToken configureDefaultsForNewToken(ArrangementTypeSecurityToken stAdmin, IEnterprise enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
