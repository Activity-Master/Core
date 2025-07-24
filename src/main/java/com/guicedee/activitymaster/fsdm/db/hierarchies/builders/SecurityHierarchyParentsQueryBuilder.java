package com.guicedee.activitymaster.fsdm.db.hierarchies.builders;

import com.entityassist.querybuilder.QueryBuilder;
import com.guicedee.activitymaster.fsdm.db.hierarchies.SecurityHierarchyParents;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.EntityManager;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class SecurityHierarchyParentsQueryBuilder
		extends QueryBuilder<SecurityHierarchyParentsQueryBuilder, SecurityHierarchyParents, UUID>
{

	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
