package com.guicedee.activitymaster.core.db.abstraction.builders.handlers;

import com.entityassist.services.querybuilders.IQueryBuilderSCD;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderDefault;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.guicedinjection.GuiceContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static com.entityassist.enumerations.Operand.Equals;
import static com.entityassist.enumerations.Operand.InList;

public interface IQueryBuilderDefault<J extends QueryBuilderDefault<J, E, I>, E extends WarehouseBaseTable<E, J, I>, I extends Serializable>
		extends IQueryBuilderSCD<J,E,I>
{

	@javax.validation.constraints.NotNull
	default J withEnterprise(IEnterprise<?> enterprise)
	{
		where(getAttribute("enterpriseID"), Equals, enterprise);
		//noinspection unchecked
		return (J) this;
	}
	
	@javax.validation.constraints.NotNull
	default J inActiveRange(IEnterprise<?> enterprise, UUID... identityToken)
	{
		Collection<IActiveFlag<?>> flags = GuiceContext.get(IActiveFlagService.class)
		                                               .findActiveRange(enterprise, identityToken);
		Collection<ActiveFlag> flagss = new ArrayList<>();
		for (IActiveFlag<?> flag : flags)
		{
			flagss.add((ActiveFlag) flag);
		}
		where(this.<E,ActiveFlag>getAttribute("activeFlagID"), InList, flagss);
		//noinspection unchecked
		return (J) this;
	}
	
	@javax.validation.constraints.NotNull
	default J inVisibleRange(IEnterprise<?> enterprise, UUID... identityToken)
	{
		Collection<IActiveFlag<?>> flags = GuiceContext.get(IActiveFlagService.class)
		                                               .getVisibleRange(enterprise, identityToken);
		Collection<ActiveFlag> flagss = new ArrayList<>();
		for (IActiveFlag<?> flag : flags)
		{
			flagss.add((ActiveFlag) flag);
		}
		where(this.<E,ActiveFlag>getAttribute("activeFlagID"), InList, flagss);
		//noinspection unchecked
		return (J) this;
	}
}
