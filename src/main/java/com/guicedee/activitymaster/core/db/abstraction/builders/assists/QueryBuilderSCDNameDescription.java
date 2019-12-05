package com.guicedee.activitymaster.core.db.abstraction.builders.assists;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilder;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.Collection;

import static com.entityassist.enumerations.Operand.*;

public abstract class QueryBuilderSCDNameDescription<J extends QueryBuilderSCDNameDescription<J, E, I, S>,
		                                                    E extends WarehouseSCDNameDescriptionTable<E, J, I, S>,
		                                                    I extends Serializable,
		                                                    S extends WarehouseSecurityTable>
		extends QueryBuilder<J, E, I, S>
{
	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J findByName(String name)
	{
		where(getAttribute("name"), Equals, name);
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J findByName(IClassificationValue<?> name)
	{
		where(getAttribute("name"), Equals, name.classificationName());
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J findByDescription(String name)
	{
		where(getAttribute("description"), Equals, name);
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J findByDescriptionLike(String name)
	{
		where(getAttribute("description"), Like, "%" + name + "%");
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J findByName(String... name)
	{
		where((SingularAttribute<E, String>) getAttribute("name"), InList, name);
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J findByName(Collection<String> name)
	{
		where((SingularAttribute<E, String>) getAttribute("name"), InList, name);
		return (J) this;
	}
}
