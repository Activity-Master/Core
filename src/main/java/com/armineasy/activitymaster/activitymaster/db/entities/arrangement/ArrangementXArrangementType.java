package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXArrangementTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ArrangementXArrangementType")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ArrangementXArrangementType
		extends WarehouseRelationshipTable<Arrangement, ArrangementType,
				                                  ArrangementXArrangementType,
				                                  ArrangementXArrangementTypeQueryBuilder, Long,
				                                  ArrangementXArrangementTypeSecurityToken>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementXArrangementTypeID")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "ArrangementID",
			referencedColumnName = "ArrangementID")
	private Arrangement arrangement;
	@JoinColumn(name = "ArrangementTypeID",
			referencedColumnName = "ArrangementTypeID")
	@ManyToOne()
	private ArrangementType type;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementTypeSecurityToken> securities;

	public ArrangementXArrangementType()
	{
	}

	public ArrangementXArrangementType(Long arrangementXArrangementTypeID)
	{
		this.id = arrangementXArrangementTypeID;
	}

	@Override
	protected ArrangementXArrangementTypeSecurityToken configureDefaultsForNewToken(ArrangementXArrangementTypeSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
