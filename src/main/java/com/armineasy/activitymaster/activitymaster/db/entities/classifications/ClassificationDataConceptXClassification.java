package com.armineasy.activitymaster.activitymaster.db.entities.classifications;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders.ClassificationDataConceptXClassificationQueryBuilder;
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
@Table(name = "ClassificationDataConceptXClassification")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ClassificationDataConceptXClassification
		extends WarehouseClassificationRelationshipTable<ClassificationDataConcept, Classification, ClassificationDataConceptXClassification,
				                                                ClassificationDataConceptXClassificationQueryBuilder, Long
				                                                , ClassificationDataConceptXClassificationSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationDataConceptXClassificationID")
	private Long id;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXClassificationSecurityToken> securities;

	@JoinColumn(name = "ClassificationDataConceptID",
			referencedColumnName = "ClassificationDataConceptID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ClassificationDataConcept classificationDataConceptID;

	public ClassificationDataConceptXClassification()
	{

	}

	public ClassificationDataConceptXClassification(Long classificationDataConceptXClassificationID)
	{
		this.id = classificationDataConceptXClassificationID;
	}

}
