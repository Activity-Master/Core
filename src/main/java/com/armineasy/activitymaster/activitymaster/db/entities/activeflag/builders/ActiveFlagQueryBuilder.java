package com.armineasy.activitymaster.activitymaster.db.entities.activeflag.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.assists.QueryBuilderNameDescription;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag_;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.jwebmp.entityassist.enumerations.Operand.*;

@SuppressWarnings("Duplicates")
public class ActiveFlagQueryBuilder
		extends QueryBuilderNameDescription<ActiveFlagQueryBuilder, ActiveFlag, Long, ActiveFlagSecurityToken>
{

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public ActiveFlagQueryBuilder inActiveRange(IEnterprise<?> enterprise)
	{
		return this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public ActiveFlagQueryBuilder inVisibleRange(IEnterprise<?> enterprise)
	{
		return this;
	}
}
