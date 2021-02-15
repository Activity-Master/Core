package com.guicedee.activitymaster.core.db.abstraction.builders.handlers;

import com.entityassist.services.querybuilders.IQueryBuilderSCD;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderDefault;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.guicedinjection.GuiceContext;

import java.io.Serializable;
import java.util.*;

import static com.entityassist.enumerations.Operand.*;

public interface IQueryBuilderDefault<J extends QueryBuilderDefault<J, E, I>, E extends WarehouseBaseTable<E, J, I>, I extends Serializable>
		extends IQueryBuilderSCD<J, E, I>
{
	@jakarta.validation.constraints.NotNull
	default J withEnterprise(ISystems<?> system)
	{
		where(getAttribute("enterpriseID"), Equals, system.getEnterprise());
		//noinspection unchecked
		return (J) this;
	}
	
	@jakarta.validation.constraints.NotNull
	default J withEnterprise(IEnterprise<?> enterprise)
	{
		where(getAttribute("enterpriseID"), Equals, enterprise);
		//noinspection unchecked
		return (J) this;
	}
	
	@jakarta.validation.constraints.NotNull
	default J inActiveRange(ISystems<?> system, UUID... identityToken)
	{
		return inActiveRange(system.getEnterpriseID(), identityToken);
	}
	
	@jakarta.validation.constraints.NotNull
	default J inActiveRange(IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (enterprise.isFake())
		{
			return (J) this;
		}
		IActiveFlagService<?> flagService = GuiceContext.get(IActiveFlagService.class);
		Collection<IActiveFlag<?>> flags = flagService.findActiveRange(enterprise, identityToken);
		Collection<ActiveFlag> flagss = new ArrayList<>();
		for (IActiveFlag<?> flag : flags)
		{
			flagss.add((ActiveFlag) flag);
		}
		where(this.<E, ActiveFlag>getAttribute("activeFlagID"), InList, flagss);
		//noinspection unchecked
		return (J) this;
	}
	
	@jakarta.validation.constraints.NotNull
	default J inVisibleRange(IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (enterprise.isFake())
		{
			return (J) this;
		}
		IActiveFlagService<?> flagService = GuiceContext.get(IActiveFlagService.class);
		Collection<IActiveFlag<?>> flags = flagService.getVisibleRange(enterprise, identityToken);
		Collection<ActiveFlag> flagss = new ArrayList<>();
		for (IActiveFlag<?> flag : flags)
		{
			flagss.add((ActiveFlag) flag);
		}
		where(this.<E, ActiveFlag>getAttribute("activeFlagID"), InList, flagss);
		//noinspection unchecked
		return (J) this;
	}
}
