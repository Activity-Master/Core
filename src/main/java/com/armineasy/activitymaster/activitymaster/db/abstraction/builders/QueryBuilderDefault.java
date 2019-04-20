package com.armineasy.activitymaster.activitymaster.db.abstraction.builders;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseBaseTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSCDTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.jwebmp.entityassist.querybuilder.QueryBuilderSCD;
import com.jwebmp.guicedinjection.GuiceContext;
import lombok.extern.java.Log;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

import static com.jwebmp.entityassist.enumerations.Operand.*;

@Log
public abstract class QueryBuilderDefault<J extends QueryBuilderDefault<J, E, I>,
		                                         E extends WarehouseBaseTable<E, J, I>,
		                                         I extends Serializable>
		extends QueryBuilderSCD<J, E, I>
{
	public QueryBuilderDefault()
	{
		setRunDetached(true);
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
	public J withEnterprise(Enterprise enterprise)
	{
		where(getAttribute("enterpriseID"), Equals, enterprise);
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J inActiveRange(Enterprise enterprise)
	{
		Collection<ActiveFlag> flags = GuiceContext.get(IActiveFlagService.class)
		                                           .getActiveRange(enterprise);

		where((SingularAttribute<E, ActiveFlag>) getAttribute("activeFlagID"), InList, flags);
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J inVisibleRange(Enterprise enterprise)
	{
		Collection<ActiveFlag> flags = GuiceContext.get(IActiveFlagService.class)
		                                           .getVisibleRange(enterprise);
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
	public E delete(E entity)
	{
		if(entity instanceof WarehouseSCDTable)
		{
			WarehouseSCDTable w = (WarehouseSCDTable) entity;
			w.setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
			                              .getDeletedFlag(w.getEnterpriseID()));
		}
		return super.delete(entity);
	}

	@Override
	public boolean onDeleteUpdate(E originalEntity, E newEntity)
	{
		if(originalEntity instanceof WarehouseTable)
		{
			WarehouseTable orig = (WarehouseTable) originalEntity;
			WarehouseTable news = (WarehouseTable) originalEntity;

			news.setOriginalSourceSystemUniqueID(orig.getId()
			                                         .toString());
		}
		return super.onDeleteUpdate(originalEntity, newEntity);
	}

	@Override
	public E update(E entity)
	{
		if(entity instanceof WarehouseSCDTable)
		{
			WarehouseSCDTable w = (WarehouseSCDTable) entity;
			w.setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
			                              .getArchivedFlag(w.getEnterpriseID()));
		}
		return super.update(entity);
	}
}
