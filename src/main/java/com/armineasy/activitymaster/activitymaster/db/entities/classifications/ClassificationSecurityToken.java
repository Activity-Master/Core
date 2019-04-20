package com.armineasy.activitymaster.activitymaster.db.entities.classifications;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders.ClassificationsSecurityTokenQueryBuilder;
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
@Table(name = "ClassificationSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ClassificationSecurityToken
		extends WarehouseSecurityTable<ClassificationSecurityToken, ClassificationsSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Classification base;

	public ClassificationSecurityToken()
	{

	}

	public ClassificationSecurityToken(Long classificationSecurityTokenID)
	{
		this.id = classificationSecurityTokenID;
	}
}
