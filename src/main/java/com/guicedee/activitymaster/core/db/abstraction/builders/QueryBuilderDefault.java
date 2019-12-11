package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.entityassist.querybuilder.QueryBuilderSCD;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.guicedinjection.GuiceContext;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Logger;

import static com.entityassist.enumerations.Operand.*;

public abstract class QueryBuilderDefault<J extends QueryBuilderDefault<J, E, I>,
		                                         E extends WarehouseBaseTable<E, J, I>,
		                                         I extends Serializable>
		extends QueryBuilderSCD<J, E, I>
{
	private static final Logger log = Logger.getLogger(QueryBuilderDefault.class.getName());

	public QueryBuilderDefault()
	{
		setRunDetached(true);
		setReturnFirst(true);
		setDetach(true);
	}

	@Override
	public EntityManager getEntityManager()
	{
		return GuiceContext.get(EntityManager.class, ActivityMasterDB.class);
	}

	@Override
	protected boolean isIdGenerated()
	{
		return true;
	}

	@javax.validation.constraints.NotNull
	public J withEnterprise(IEnterprise enterprise)
	{
		where(getAttribute("enterpriseID"), Equals, enterprise);
		return (J) this;
	}

	@javax.validation.constraints.NotNull
	public J inActiveRange(IEnterprise<?> enterprise, UUID... identityToken)
	{
		Collection<IActiveFlag<?>> flags = GuiceContext.get(IActiveFlagService.class)
		                                               .findActiveRange(enterprise, identityToken);
		Collection<ActiveFlag> flagss = new ArrayList<>();
		for (IActiveFlag<?> flag : flags)
		{
			flagss.add((ActiveFlag) flag);
		}
		where((SingularAttribute<E, ActiveFlag>) getAttribute("activeFlagID"), InList, flagss);
		return (J) this;
	}

	@javax.validation.constraints.NotNull
	public J inVisibleRange(IEnterprise<?> enterprise, UUID... identityToken)
	{
		Collection<IActiveFlag<?>> flags = GuiceContext.get(IActiveFlagService.class)
		                                               .getVisibleRange(enterprise, identityToken);
		Collection<ActiveFlag> flagss = new ArrayList<>();
		for (IActiveFlag<?> flag : flags)
		{
			flagss.add((ActiveFlag) flag);
		}
		where((SingularAttribute<E, ActiveFlag>) getAttribute("activeFlagID"), InList, flagss);
		return (J) this;
	}

	@javax.validation.constraints.NotNull
	@Override
	public @javax.validation.constraints.NotNull J inDateRange()
	{
		where(getAttribute("effectiveToDate"), GreaterThanEqualTo, LocalDateTime.now());
		where(getAttribute("effectiveFromDate"), LessThanEqualTo, LocalDateTime.now());
		return (J) this;
	}

	@Override
	public E update(E entity)
	{
		if (entity instanceof WarehouseSCDTable)
		{
			WarehouseSCDTable w = (WarehouseSCDTable) entity;
			w.setEffectiveToDate(LocalDateTime.now());
			w.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
		}
		E ent = super.update(entity);
		return ent;
	}
}
