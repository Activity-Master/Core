package com.armineasy.activitymaster.activitymaster.db.entities.classifications;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders.ClassificationXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ClassificationXClassification")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ClassificationXClassification
		extends WarehouseClassificationRelationshipTable<Classification, Classification, ClassificationXClassification, ClassificationXClassificationQueryBuilder, Long, ClassificationXClassificationSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationXClassificationID")
	private Long id;

	@JoinColumn(name = "ChildClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Classification childClassificationID;

	@JoinColumn(name = "ParentClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Classification parentClassificationID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassificationSecurityToken> securities;

	public ClassificationXClassification()
	{

	}

	public ClassificationXClassification(Long classificationXClassificationID)
	{
		this.id = classificationXClassificationID;
	}

	@Override
	protected ClassificationXClassificationSecurityToken configureDefaultsForNewToken(ClassificationXClassificationSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
