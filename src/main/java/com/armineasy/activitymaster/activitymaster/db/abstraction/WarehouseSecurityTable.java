package com.armineasy.activitymaster.activitymaster.db.abstraction;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.SecurityHierarchyView;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.FetchType.*;

/**
 * @author GedMarc
 * @since 08 Dec 2016
 */
@MappedSuperclass
@Accessors(chain = true)
@Getter
@Setter
public abstract class WarehouseSecurityTable<J extends WarehouseSecurityTable<J, Q, I>,
		                                            Q extends QueryBuilderSecurities<Q, J, I>, I extends Serializable>
		extends WarehouseBaseTable<J, Q, I>
{

	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
			name = "CreateAllowed")
	private boolean createAllowed;
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
			name = "UpdateAllowed")
	private boolean updateAllowed;
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
			name = "DeleteAllowed")
	private boolean deleteAllowed;
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
			name = "ReadAllowed")
	private boolean readAllowed;

	@JoinColumn(name = "SecurityTokenID",
			referencedColumnName = "SecurityTokenID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = LAZY)
	@JoinFormula(value = "SecurityTokenID")
	private SecurityToken securityTokenID;

	@JoinColumn(name = "ActiveFlagID",
			referencedColumnName = "ActiveFlagID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = LAZY)
	private ActiveFlag activeFlagID;

	@JoinColumn(name = "EnterpriseID",
			referencedColumnName = "EnterpriseID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = LAZY)
	private Enterprise enterpriseID;

	@JoinColumn(name = "SystemID",
			referencedColumnName = "SystemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = LAZY)
	private Systems systemID;

	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
			name = "OriginalSourceSystemUniqueID")
	private String originalSourceSystemUniqueID;

	@JoinColumn(name = "OriginalSourceSystemID",
			referencedColumnName = "SystemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = LAZY)
	private Systems originalSourceSystemID;

	public WarehouseSecurityTable()
	{

	}
}
