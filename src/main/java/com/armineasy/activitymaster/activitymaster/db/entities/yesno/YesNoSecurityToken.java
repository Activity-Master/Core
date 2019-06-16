package com.armineasy.activitymaster.activitymaster.db.entities.yesno;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.builders.YesNoSecurityTokenQueryBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "YesNoSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Access(FIELD)@lombok.Data
public class YesNoSecurityToken
		extends WarehouseSecurityTable<YesNoSecurityToken, YesNoSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "YesNoSecurityTokenID")
	private Long id;

	@JoinColumn(name = "YesNoID",
			referencedColumnName = "YesNoID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private YesNo base;

	public YesNoSecurityToken()
	{

	}

	public YesNoSecurityToken(Long systemsSecurityTokenID)
	{
		this.id = systemsSecurityTokenID;
	}
}
