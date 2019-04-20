package com.armineasy.activitymaster.activitymaster.db.abstraction;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

import static com.jwebmp.entityassist.querybuilder.EntityAssistStrings.*;

/**
 * @param <S>
 * @param <J>
 * @param <S>
 *
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@MappedSuperclass
@Accessors(chain = true)
@Getter
@Setter
public abstract class WarehouseTable<J extends WarehouseTable<J, Q, I, S>,
		                                    Q extends QueryBuilder<Q, J, I, S>,
		                                    I extends Serializable,
		                                    S extends WarehouseSecurityTable>
		extends WarehouseSCDTable<J, Q, I, S>
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
			name = "OriginalSourceSystemUniqueID")
	private String originalSourceSystemUniqueID = STRING_EMPTY;

	@JoinColumn(name = "OriginalSourceSystemID",
			referencedColumnName = "SystemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Systems originalSourceSystemID;

	public WarehouseTable()
	{

	}

	@Override
	@NotNull
	@SuppressWarnings("unchecked")
	protected J configureDefaultsSystemValues(Systems requestingSystem)
	{
		super.configureDefaultsSystemValues(requestingSystem);
		setOriginalSourceSystemID(requestingSystem);
		setOriginalSourceSystemUniqueID("");
		return (J) this;
	}
}
