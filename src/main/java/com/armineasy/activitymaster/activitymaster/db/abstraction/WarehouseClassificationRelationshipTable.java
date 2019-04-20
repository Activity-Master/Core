package com.armineasy.activitymaster.activitymaster.db.abstraction;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * @param <S>
 * @param <J>
 * @param <P>
 *
 * @author GedMarc
 * @since 08 Dec 2016
 */
@MappedSuperclass
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
public abstract class WarehouseClassificationRelationshipTable<P extends WarehouseCoreTable, S extends WarehouseCoreTable,
		                                                              J extends WarehouseClassificationRelationshipTable<P, S, J, Q, I, ST>,
		                                                              Q extends QueryBuilderRelationshipClassification<P, S, Q, J, I, ST>,
		                                                              I extends Serializable,
		                                                              ST extends WarehouseSecurityTable>
		extends WarehouseRelationshipTable<P, S, J, Q, I, ST>
{

	private static final long serialVersionUID = 1L;

	@JoinColumn(name = "ClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Classification classificationID;

	public WarehouseClassificationRelationshipTable()
	{

	}

	@Override
	@SuppressWarnings("unchecked")
	protected @NotNull Class<Q> getClassQueryBuilderClass() {
		return (Class<Q>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
	}

	@Override
	@SuppressWarnings("unchecked")
	protected @NotNull Class<ST> findPersistentSecurityClass()
	{
		return (Class<ST>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[5];
	}
}
