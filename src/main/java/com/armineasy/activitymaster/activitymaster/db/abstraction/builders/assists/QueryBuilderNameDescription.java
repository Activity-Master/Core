package com.armineasy.activitymaster.activitymaster.db.abstraction.builders.assists;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;

import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.Collection;

import static com.entityassist.enumerations.Operand.*;

public abstract class QueryBuilderNameDescription<J extends QueryBuilderNameDescription<J, E, I, S>,
		                                                 E extends WarehouseNameDescriptionTable<E, J, I, S>,
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
