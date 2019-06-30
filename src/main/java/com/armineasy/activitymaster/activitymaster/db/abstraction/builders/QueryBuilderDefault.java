package com.armineasy.activitymaster.activitymaster.db.abstraction.builders;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseBaseTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSCDTable;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.jwebmp.entityassist.querybuilder.QueryBuilderSCD;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Logger;

import static com.jwebmp.entityassist.enumerations.Operand.*;

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

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J withEnterprise(IEnterprise enterprise)
	{
		where(getAttribute("enterpriseID"), Equals, enterprise);
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J inActiveRange(IEnterprise<?> enterprise, UUID...identityToken)
	{
		Collection<ActiveFlag> flags = GuiceContext.get(IActiveFlagService.class)
		                                           .findActiveRange(enterprise, identityToken);

		where((SingularAttribute<E, ActiveFlag>) getAttribute("activeFlagID"), InList, flags);
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J inVisibleRange(IEnterprise<?> enterprise, UUID...identityToken)
	{
		Collection<ActiveFlag> flags = GuiceContext.get(IActiveFlagService.class)
		                                           .getVisibleRange(enterprise,identityToken);
		where((SingularAttribute<E, ActiveFlag>) getAttribute("activeFlagID"), InList, flags);
		return (J) this;
	}

	@SuppressWarnings("unchecked")
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
		if(entity instanceof WarehouseSCDTable)
		{
			WarehouseSCDTable w = (WarehouseSCDTable) entity;
			w.setEffectiveToDate(LocalDateTime.now());
			w.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
		}
		E ent =  super.update(entity);
		ent.builder()
		   .getEntityManager()
		   .flush();
		return ent;
	}
}
