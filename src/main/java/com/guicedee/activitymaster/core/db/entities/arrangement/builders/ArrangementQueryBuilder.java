package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsClassificationsQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXClassification;

public class ArrangementQueryBuilder
		extends QueryBuilderTable<ArrangementQueryBuilder, Arrangement, java.util.UUID, ArrangementSecurityToken>
		implements IContainsClassificationsQueryBuilder<ArrangementQueryBuilder, Arrangement, java.util.UUID, ArrangementXClassification>
{

}
