package com.guicedee.activitymaster.core.db.abstraction.builders.handlers;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderDefault;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.logger.LogFactory;

import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;

import static com.entityassist.enumerations.Operand.Equals;
import static com.guicedee.guicedinjection.json.StaticStrings.STRING_EMPTY;

public interface IContainsClassificationsQueryBuilder<J extends QueryBuilderDefault<J, E, I>, E extends WarehouseBaseTable<E, J, I>, I extends Serializable,
		X extends WarehouseRelationshipTable<E, Classification, ?, ?, I, ?, ?, ?>>
		extends IHasClassificationQueryBuilder<J, E, I>
{
	
	default J hasClassification(IClassification<?> classification, UUID... identityToken)
	{
		return hasClassification(classification, STRING_EMPTY, identityToken);
	}
	
	default J hasClassification(IClassification<?> classification, String value, UUID... identityToken)
	{
		String className = getEntityClass().getCanonicalName() + "XClassification";
		try
		{
			@SuppressWarnings("unchecked")
			Class<X> relationshipTable = (Class<X>) Class.forName(className);
			X instance = GuiceContext.get(relationshipTable);
			
			QueryBuilderRelationshipClassification<?, ?, ?, ?, ?, ?> qbr = (QueryBuilderRelationshipClassification<?, ?, ?, ?, ?, ?>) instance.builder();
			qbr.withClassification(classification);
			qbr.inActiveRange(classification.getEnterprise(), identityToken);
			qbr.inDateRange();
			qbr.canRead(classification.getSystemID(), identityToken);
			qbr.withValue(value);
			
			join(getAttribute("classifications"), qbr);
		}
		catch (ClassNotFoundException e)
		{
			LogFactory.getLog(IContainsClassificationsQueryBuilder.class)
			          .log(Level.SEVERE, "Cannot search", e);
		}
		//noinspection unchecked
		return (J) this;
	}
	
	
}
