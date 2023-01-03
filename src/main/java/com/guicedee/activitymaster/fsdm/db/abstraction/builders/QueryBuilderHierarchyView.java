package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.entityassist.querybuilder.QueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseHierarchyView;
import com.guicedee.activitymaster.fsdm.db.hierarchies.SecurityHierarchyView_;
import jakarta.persistence.TypedQuery;

import java.io.Serializable;

import static com.entityassist.enumerations.Operand.*;

public abstract class QueryBuilderHierarchyView<J extends QueryBuilderHierarchyView<J, E, I>, E extends WarehouseHierarchyView<E, J, I>, I extends java.lang.String>
		extends QueryBuilder<J, E, I>
{
	@Override
	public void onSelectExecution(TypedQuery<?> query)
	{
		org.hibernate.query.Query<?> q = query.unwrap(org.hibernate.query.Query.class);
		q.addQueryHint("MAXRECURSION 0");
	}
	
	@SuppressWarnings("unchecked")
	public J findMyHierarchy(Serializable token)
	{
		where(getAttribute("path"), Like, "%" + token);
		return (J) this;
	}
	
	@SuppressWarnings("unchecked")
	public J findMyChildrenRecursive(Serializable token)
	{
		where(getAttribute("path"), Like, "%" + token + "%");
		where(getAttribute("path"), NotLike, "%" + token);
		return (J) this;
	}
	
	
	@SuppressWarnings("unchecked")
	public J findMyChildren(java.lang.String securityTokenID)
	{
		where(SecurityHierarchyView_.parentID, Equals, securityTokenID);
		return (J) this;
	}
	
	
	@SuppressWarnings("unchecked")
	public J withValue(String value)
	{
		where(this.<E, String>getAttribute("value"), Equals, value);
		return (J) this;
	}
}
