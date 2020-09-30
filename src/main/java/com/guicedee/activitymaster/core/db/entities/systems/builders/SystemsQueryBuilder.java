package com.guicedee.activitymaster.core.db.entities.systems.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderNameDescription;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsClassificationsQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXClassification;
import com.guicedee.activitymaster.core.db.entities.security.builders.SecurityTokenQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.systems.SystemXClassification;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.db.entities.systems.SystemsSecurityToken;

public class SystemsQueryBuilder
		extends QueryBuilderNameDescription<SystemsQueryBuilder, Systems, Long, SystemsSecurityToken>
		implements IContainsClassificationsQueryBuilder<SystemsQueryBuilder, Systems, Long, SystemXClassification>
{
}
