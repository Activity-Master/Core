package com.armineasy.activitymaster.activitymaster.db.entities.enterprise;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.builders.EnterpriseXClassificationSecurityTokenQueryBuilder;
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
@Table(name = "EnterpriseXClassificationSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class EnterpriseXClassificationSecurityToken
		extends WarehouseSecurityTable<EnterpriseXClassificationSecurityToken, EnterpriseXClassificationSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EnterpriseXClassificationSecurityTokenID")
	private Long id;

	@JoinColumn(name = "EnterpriseXClassificationID",
			referencedColumnName = "EnterpriseXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private EnterpriseXClassification base;

	public EnterpriseXClassificationSecurityToken()
	{

	}

	public EnterpriseXClassificationSecurityToken(Long classificationXResourceItemSecurityTokenID)
	{
		this.id = classificationXResourceItemSecurityTokenID;
	}
}
