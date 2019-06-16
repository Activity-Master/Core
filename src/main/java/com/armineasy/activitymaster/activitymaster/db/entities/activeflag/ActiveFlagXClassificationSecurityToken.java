package com.armineasy.activitymaster.activitymaster.db.entities.activeflag;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.builders.ActiveFlagXClassificationSecurityTokenQueryBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ActiveFlagXClassificationSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Access(FIELD)@lombok.Data
public class ActiveFlagXClassificationSecurityToken
		extends WarehouseSecurityTable<ActiveFlagXClassificationSecurityToken, ActiveFlagXClassificationSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ActiveFlagXClassificationSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ActiveFlagXClassificationID",
			referencedColumnName = "ActiveFlagXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ActiveFlagXClassification base;

	public ActiveFlagXClassificationSecurityToken()
	{

	}

	public ActiveFlagXClassificationSecurityToken(Long activeFlagXClassificationSecurityTokenID)
	{
		this.id = activeFlagXClassificationSecurityTokenID;
	}
}
