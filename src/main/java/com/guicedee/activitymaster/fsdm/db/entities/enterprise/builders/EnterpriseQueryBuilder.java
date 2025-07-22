package com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterpriseQueryBuilder;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderCore;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;

import java.util.UUID;

public class EnterpriseQueryBuilder
		extends QueryBuilderCore<EnterpriseQueryBuilder, Enterprise, UUID>
		implements IEnterpriseQueryBuilder<EnterpriseQueryBuilder, Enterprise>
{

}
