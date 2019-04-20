package com.armineasy.activitymaster.activitymaster.db.entities.enterprise;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.builders.EnterpriseSecurityTokenQueryBuilder;
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
@Table(name = "EnterpriseSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class EnterpriseSecurityToken
		extends WarehouseSecurityTable<EnterpriseSecurityToken, EnterpriseSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EnterpriseSecurityTokenID")
	private Long id;

	@JoinColumn(name = "EnterpriseID",
			referencedColumnName = "EnterpriseID",
			nullable = false,insertable = false,updatable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Enterprise base;

	public EnterpriseSecurityToken()
	{

	}

	public EnterpriseSecurityToken(Long enterpriseSecurityTokenID)
	{
		this.id = enterpriseSecurityTokenID;
	}
}
