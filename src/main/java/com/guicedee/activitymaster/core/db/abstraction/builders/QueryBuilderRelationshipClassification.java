/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IHasValueQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IHasClassificationQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXClassification;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXClassification_;
import com.guicedee.activitymaster.core.db.entities.rules.RulesType_;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesTypeXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.criteria.JoinType;

import java.io.Serializable;
import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

/**
 * @param <P>
 * @param <S>
 * @param <J>
 * @author Marc Magon
 * @since 01 May 2017
 */
public abstract class QueryBuilderRelationshipClassification<P extends WarehouseCoreTable, S extends WarehouseCoreTable,
		J extends QueryBuilderRelationshipClassification<P, S, J, E, I, ST>,
		E extends WarehouseClassificationRelationshipTable<P, S, E, J, I, ST, ?, ?>,
		I extends Serializable, ST extends WarehouseSecurityTable>
		extends QueryBuilderRelationship<P, S, J, E, I, ST>
		implements IHasClassificationQueryBuilder<J, E, I>,
		           IHasValueQueryBuilder<J,E,I>
{
}
