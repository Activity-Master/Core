package com.guicedee.activitymaster.core.services.capabilities;

import com.entityassist.RootEntity;
import com.entityassist.SCDEntity;
import com.entityassist.enumerations.SelectAggregrate;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.entityassist.querybuilder.statements.InsertStatement;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.*;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.capabilities.bases.IAddableType;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.events.EventThread;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;

import com.guicedee.guicedinjection.GuiceContext;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.entityassist.SCDEntity.*;
import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.core.services.classifications.events.EventInvolvedPartiesClassifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;
import static jakarta.persistence.criteria.JoinType.*;

@SuppressWarnings("Duplicates")
public interface IContainsClassifications<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable<?, ? extends QueryBuilderDefault, ?, ?>,
		Q extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?, L, R>,
		C extends IClassificationValue<?>,
		L, R,
		J extends IContainsClassifications<P, S, Q, C, L, R, J>>
{
	default double sumAll(C value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		List<Object[]> results = getValues(value, null, originatingSystem, identityToken);
		double d = 0.0d;
		for (Object[] result : results)
		{
			Double D = Double.parseDouble(result[0].toString());
			d += D;
		}
		return d;
	}
	
	default String listToSqlString(Collection<String> values)
	{
		StringBuilder sb = new StringBuilder();
		for (String value : values)
		{
			sb.append("'")
			  .append(value.replace("'", "''"))
			  .append("',");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	default String listToPivotString(Collection<String> values)
	{
		StringBuilder sb = new StringBuilder();
		for (String value : values)
		{
			sb.append("[")
			  .append(value.replace("'", "''"))
			  .append("],");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	default List<Object[]> getClassificationsValuePivot(String value, Set<String> searchValue, ISystems<?> system, UUID[] identityToken, String... values)
	{
		return getClassificationsValuePivot(SelectAggregrate.Max,value, searchValue, system, identityToken, values);
	}
	
	default List<Object[]> getClassificationsValuePivot(String value, String searchValue, ISystems<?> system, UUID[] identityToken, String... values)
	{
		if(searchValue == null)
			return getClassificationsValuePivot(SelectAggregrate.Max,value, (Set<String>)null, system, identityToken, values);
		else
			return getClassificationsValuePivot(SelectAggregrate.Max,value, Set.of(searchValue), system, identityToken, values);
	}
	
	default List<Object[]> getClassificationsValuePivot(SelectAggregrate aggregrate, String value, Set<String> searchValue, ISystems<?> system, UUID[] identityToken, String... values)
	{
		List<String> cStrings = new ArrayList<>();
		cStrings.add(value);
		cStrings.addAll(Arrays.asList(values));
		
		String classificationValuesInList = listToSqlString(cStrings);
		String classificationPivotInList = listToPivotString(cStrings);
		
		List<IActiveFlag<?>> flags = GuiceContext.get(IActiveFlagService.class).findActiveRange(system.getEnterprise(), identityToken);
		List<String> fString = new ArrayList<>();
		flags.forEach(a -> fString.add(a.toString()));
		
		String activeFlagsInList = listToSqlString(fString);
		
		@SuppressWarnings("rawtypes")
		RootEntity me = (RootEntity) this;
		
		String myTableName = new InsertStatement(me).getTableName();
		String idColumnName = new InsertStatement(me).getIdPair()
		                                             .getKey();
		String joinTableName = myTableName + "XClassification";
		String targetTableName = "Classification.Classification";
		
		String s = "WITH Req(ClassificationName, ID, Value)\n" +
				"AS\n" +
				"(\n" +
				"\tselect c.ClassificationName, ri." + idColumnName + ", ric.Value\n" +
				"\tfrom " + myTableName + "  ri\n" +
				"\t\tleft join " + joinTableName + " ric\n" +
				"\t\t\ton ri." + idColumnName + " = ric." + idColumnName + "\n" +
				"\t\tleft join " + targetTableName + " c\n" +
				"\t\t\ton ric.ClassificationID = c.ClassificationID\n" +
				"\t\tfull outer join dbo.ActiveFlag af\n" +
				"\t\t\ton ri.ActiveFlagID = af.ActiveFlagID\n" +
				"\t\t\tand ric.ActiveFlagID = af.ActiveFlagID\n" +
				"\t\t\tand c.ActiveFlagID = af.ActiveFlagID\n" +
				"\t\tWHERE ClassificationName in (" + classificationValuesInList + ")\n" +
				"\t\tand af.ActiveFlagName in (" + activeFlagsInList + ")\n" +
				"\t\tand ri.EffectiveFromDate <= getDate()\n" +
				"\t\tand ri.EffectiveToDate >= getDate()\n" +
				"\t\tand ric.EffectiveFromDate <= getDate()\n" +
				"\t\tand ric.EffectiveToDate >= getDate()\n" +
				"\t\tand c.EffectiveFromDate <= getDate()\n" +
				"\t\tand c.EffectiveToDate >= getDate()\n";
		s += ")\n" +
				"SELECT PivotTable.* from Req c\n" +
				"PIVOT (\n" +
				"\t" + aggregrate.name()+ "(value)\n" +
				"\tFOR c.ClassificationName in (" + classificationPivotInList + ")\n" +
				") AS PivotTable\n ";
		if(searchValue != null && !searchValue.isEmpty())
		{
			StringBuilder searchInClause = new StringBuilder();
			for (String s1 : searchValue)
			{
				searchInClause.append("'")
				              .append(s1.replace("'", "''"))
				              .append("',");
			}
			searchInClause.deleteCharAt(searchInClause.length() - 1);
			s += " WHERE ID IN (" + searchInClause + ") ";
		}
		
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = me.builder()
		                              .getEntityManager()
		                              .createNativeQuery(s)
		                              .getResultList();
		for (Object[] objects : resultList)
		{
			for (int i = 0; i < objects.length; i++)
			{
				Object o = objects[i];
				if (o == null)
				{
					objects[i] = "";
				}
			}
		}
		return resultList;
	}
	
	/**
	 * Returns either a List with Strings, or a List with Object[] for each row and values returned
	 *
	 * @param value             The classification value to use
	 * @param searchValue       The search Value to use
	 * @param originatingSystem The system coming from
	 * @param identityToken     The identity token to use
	 * @param values            Any additional values to select
	 *
	 * @return The result of List&gt;String&lt; or List&gt;Object[]&lt;
	 */
	default List<Object[]> getValues(C value, String searchValue, ISystems<?> originatingSystem, UUID[] identityToken, C... values)
	{
		Q activityMasterIdentity = get(findClassificationQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		List<C> fetching = new ArrayList<>();
		fetching.add(value);
		if (values != null)
		{
			fetching.addAll(Arrays.asList(values));
		}
		
		WarehouseBaseTable base = (WarehouseBaseTable) this;
		QueryBuilderTable baseTableBuilder = (QueryBuilderTable) base.builder();
		
		QueryBuilderRelationship builder = activityMasterIdentity.builder();
		List<JoinExpression> joins = new ArrayList<>();
		
		for (C classificationValue : fetching)
		{
			IClassification<?> classification = classificationService.find(classificationValue.toString(), originatingSystem, identityToken);
			
			JoinExpression<?, ?, ?> newExpression = new JoinExpression<>();
			baseTableBuilder.join(baseTableBuilder.getAttribute("classifications"), LEFT, newExpression);
			
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("classificationID")), Equals, classification);
			
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("activeFlagID")), InList, get(IActiveFlagService.class).findActiveRange(originatingSystem.getEnterpriseID(), identityToken));
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("enterpriseID")), Equals, originatingSystem.getEnterpriseID());
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("effectiveFromDate")), LessThanEqualTo, LocalDateTime.now());
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get("effectiveToDate")), GreaterThanEqualTo, LocalDateTime.now());
			
			baseTableBuilder.where((newExpression.getGeneratedRoot()
			                                     .get(builder.getPrimaryAttribute()
			                                                 .getName())), Equals, base);
			
			baseTableBuilder.selectColumn(newExpression.getGeneratedRoot()
			                                           .get("value"));
			joins.add(newExpression);
		}
		
		if (searchValue != null && !searchValue.isEmpty())
		{
			baseTableBuilder.where(baseTableBuilder.getRoot()
			                                       .get("value"), Equals, searchValue);
		}
		
		List list = baseTableBuilder.getAll();
		return list;
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findClassifications(C classification, ISystems<?> system, UUID... identityToken)
	{
		return findClassifications(classification, null, system, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findClassifications(C classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return findClassifications(classification.classificationName(), value, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findClassifications(String classification, ISystems<?> system, UUID... identityToken)
	{
		return findClassifications(classification, null, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findClassifications(String classification, String searchValue, ISystems<?> system, UUID... identityToken)
	{
		return findClassifications(classification, searchValue, system, false, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findClassifications(C classification, String value, boolean first, ISystems<?> system, UUID... identityToken)
	{
		return findClassifications(classification.classificationName(), value, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findClassifications(String classification, boolean first, ISystems<?> system, UUID... identityToken)
	{
		return findClassifications(classification, null, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findClassifications(String classification, String searchValue, boolean first, ISystems<?> system, UUID... identityToken)
	{
		return findClassifications(classification, searchValue, system, first, false, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findClassifications(C classification, String value, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findClassifications(classification.classificationName(), value, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findClassifications(String classification, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findClassifications(classification, null, system, first, latest, identityToken);
	}
	
	default Optional<IRelationshipValue<L, R, ?>> findClassifications(String classification, String searchValue, boolean first, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findClassifications(classification, searchValue, system, first, latest, identityToken);
	}

	@SuppressWarnings("unchecked")
	default Optional<IRelationshipValue<L, R, ?>> findClassifications(String classification, String searchValue, ISystems<?> system, boolean first, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findClassificationQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findLink((P) this, (S) iClassification, searchValue)
				                   .inActiveRange(system, identityToken)
				                   .inDateRange()
				                   .withEnterprise(system)
				                   .canRead(system, identityToken);
		if (first)
		{
			queryBuilderRelationshipClassification.setMaxResults(1);
		}
		if (latest)
		{
			queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate"));
		}
		
		//noinspection rawtypes
		return (Optional) queryBuilderRelationshipClassification.get();
	}
	
	default List<IRelationshipValue<L, R, ?>> findClassificationsAll(C classification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return findClassificationsAll(classification.classificationName(), null, originatingSystem, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findClassificationsAll(C classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findClassificationsAll(classification.classificationName(), null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findClassificationsAll(C classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return findClassificationsAll(classification.classificationName(), value, originatingSystem, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findClassificationsAll(C classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findClassificationsAll(classification.classificationName(), value, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findClassificationsAll(String classification, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return findClassificationsAll(classification, null, originatingSystem, false, identityToken);
	}
	
	
	default List<IRelationshipValue<L, R, ?>> findClassificationsAll(String classification, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findClassificationsAll(classification, null, system, latest, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findClassificationsAll(String classification, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return findClassificationsAll(classification, value, originatingSystem, false, identityToken);
	}
	
	default List<IRelationshipValue<L, R, ?>> findClassificationsAll(String classification, String value, boolean latest, ISystems<?> system, UUID... identityToken)
	{
		return findClassificationsAll(classification, value, system, latest, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default @NotNull List<IRelationshipValue<L, R, ?>> findClassificationsAll(String classification, String searchValue, ISystems<?> system, boolean latest, UUID... identityToken)
	{
		Q relationshipTable = get(findClassificationQueryRelationshipTableType());
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> iClassification = classificationService.find(classification, system, identityToken);
		var queryBuilderRelationshipClassification
				= relationshipTable.builder()
				                   .findParentLink((P) this)
				                   .inActiveRange(system, identityToken)
				                   .withValue(searchValue)
				                   .withClassification(iClassification)
				                   .inDateRange()
				                   .canRead(system, identityToken);
		if (latest)
		{
			queryBuilderRelationshipClassification.orderBy(queryBuilderRelationshipClassification.getAttribute("effectiveFromDate"));
		}
		return (List) queryBuilderRelationshipClassification.getAll();
	}
	
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findClassificationQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsClassifications.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<P> findClassificationsPrimaryTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsClassifications.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<P>) genericTypes[0];
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	default List<IRelationshipValue<L, R, ?>> findAllClassifications(ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findClassificationQueryRelationshipTableType());
		return (List<IRelationshipValue<L, R, ?>>) activityMasterIdentity.builder()
		                                                                 .findParentLink((P) this, null)
		                                                                 .inActiveRange(system.getEnterpriseID())
		                                                                 .inDateRange()
		                                                                 .canRead(system, identityToken)
		                                                                 .getAll();
	}
	
	
	default boolean hasClassificationsBefore(C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfAllClassifications(classificationValue, system, identityToken) > 0;
	}
	
	default boolean hasClassificationsBefore(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfAllClassifications(classificationValue, value, system, identityToken) > 0;
	}
	
	default boolean hasClassifications(C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		return numberOfClassifications(classificationValue, system, identityToken) > 0;
	}
	
	default boolean hasClassifications(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		return numberOfClassifications(classificationValue, value, system, identityToken) > 0;
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfClassifications(C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findClassificationCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue.classificationName(), system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(system)
		                             .inDateRange()
		                             .canRead(system, identityToken)
		                             .getCount();
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfClassifications(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findClassificationCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue.classificationName(), system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, value)
		                             .inActiveRange(system)
		                             .inDateRange()
		                             .canRead(system, identityToken)
		                             .getCount();
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfAllClassifications(C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findClassificationCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue.classificationName(), system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .getCount();
	}
	
	@SuppressWarnings("unchecked")
	default long numberOfAllClassifications(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q activityMasterIdentity = get(findClassificationCountableQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue.classificationName(), system, identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, value)
		                             .getCount();
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default Class<Q> findClassificationCountableQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsClassifications.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<Q>) genericTypes[2];
			}
		}
		return null;
	}
	
	
	
	
	
	
	default IRelationshipValue<L, R, ?> add(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue.classificationName(), system, identityToken);
		
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setClassificationID((Classification) classification);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
		configureForClassification(tableForClassification, system);
		
		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				.isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(system, identityToken);
		}
		
		return tableForClassification;
	}
	
	void configureForClassification(Q classificationLink, ISystems<?> system);
	
	default IRelationshipValue<L, R, ?> addOrUpdate(C classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return addOrUpdate(classificationValue, null, value, originatingSystem, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrUpdate(C classificationValue, String searchValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue.classificationName(), system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, searchValue)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canCreate(system.getEnterpriseID(), identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification = (Q) add(classificationValue, value, system, identityToken);
		}
		else
		{
			tableForClassification = exists.get();
			if (Strings.nullToEmpty(value)
			           .equals(exists.get()
			                         .getValue()))
			{
				return exists.get();
			}
			
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();
			
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(system.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
			
			Q newTableForClassification = get(findClassificationQueryRelationshipTableType());
			
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) system);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			configureForClassification(newTableForClassification, system);
			newTableForClassification.persist();
			
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			}
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuse(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue.classificationName(), system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .withEnterprise(system.getEnterpriseID())
		                                                         .canCreate(system.getEnterpriseID(), identityToken)
		                                                         .get();
		
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			tableForClassification.setClassificationID((Classification) classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
			configureForClassification(tableForClassification, system);
			
			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(system, identityToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> add(IClassification<?> classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		
		tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
		tableForClassification.setClassificationID((Classification) classificationValue);
		tableForClassification.setValue(value);
		tableForClassification.setSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemID((Systems) system);
		tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
		tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
		configureForClassification(tableForClassification, system);
		
		tableForClassification.persist();
		if (get(ActivityMasterConfiguration.class)
				.isSecurityEnabled())
		{
			tableForClassification.createDefaultSecurity(system, identityToken);
		}
		
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrReuse(IClassification<?> classification, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(system, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			tableForClassification.setClassificationID((Classification) classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
			configureForClassification(tableForClassification, system);
			
			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(system, identityToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrReuse(String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification1 = classificationService.find(classification, system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification1, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(system, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			tableForClassification.setClassificationID((Classification) classification1);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
			configureForClassification(tableForClassification, system);
			
			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(system, identityToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(IClassification<?> classification, String value, ISystems<?> system, UUID... identityToken)
	{
		return addOrUpdate(classification, null, value, system, identityToken);
	}
	
	default IRelationshipValue<L, R, ?> addOrUpdate(String classification,String value, ISystems<?> system, UUID... identityToken)
	{
		return addOrUpdate(classification, null, value, system, identityToken);
	}
	default IRelationshipValue<L, R, ?> addOrUpdate(String classification, String searchValue, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> ic = classificationService.find(classification, system, identityToken);
		return addOrUpdate(ic, searchValue, value, system, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> addOrUpdate(IClassification<?> classification, String searchValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, searchValue)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(system, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			tableForClassification.setClassificationID((Classification) classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemID((Systems) system);
			tableForClassification.setOriginalSourceSystemUniqueID(STRING_EMPTY);
			tableForClassification.setActiveFlagID(((Systems) system).getActiveFlagID());
			configureForClassification(tableForClassification, system);
			
			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(system, identityToken);
			}
		}
		else
		{
			if (Strings.nullToEmpty(value)
			           .equals(exists.get()
			                         .getValue()))
			{
				return exists.get();
			}
			tableForClassification = exists.get();
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();
			
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(system.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
			
			Q newTableForClassification = get(findClassificationQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) system);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			configureForClassification(newTableForClassification, system);
			newTableForClassification.persist();
			
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			}
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> update(C classificationValue, String value, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue.classificationName(), system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(system, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			//do nothing to the object
		}
		else
		{
			tableForClassification = exists.get();
			if (Strings.nullToEmpty(value)
			           .equals(exists.get()
			                         .getValue()))
			{
				return exists.get();
			}
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();
			
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(system.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
			
			Q newTableForClassification = get(findClassificationQueryRelationshipTableType());
			newTableForClassification.setId(null);
			newTableForClassification.setClassificationID(tableForClassification.getClassificationID());
			newTableForClassification.setSystemID((Systems) system);
			newTableForClassification.setOriginalSourceSystemID(originalSystem);
			newTableForClassification.setOriginalSourceSystemUniqueID(tableForClassification.getId() + "");
			newTableForClassification.setWarehouseCreatedTimestamp(LocalDateTime.now());
			newTableForClassification.setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
			newTableForClassification.setEffectiveFromDate(LocalDateTime.now());
			newTableForClassification.setEffectiveToDate(EndOfTime);
			newTableForClassification.setActiveFlagID((ActiveFlag) flagService.getActiveFlag(originalSystem.getEnterpriseID(), identityToken));
			newTableForClassification.setValue(value);
			newTableForClassification.setEnterpriseID((Enterprise) system.getEnterpriseID());
			configureForClassification(newTableForClassification, system);
			newTableForClassification.persist();
			
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				newTableForClassification.createDefaultSecurity(originalSystem, identityToken);
			}
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> expire(C classificationValue, Duration duration, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		
		IClassificationService classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue.classificationName(), originatingSystem, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			//do nothing to the object
		}
		else
		{
			tableForClassification = exists.get();
			tableForClassification.setEffectiveToDate(LocalDateTime.now()
			                                                       .plus(duration));
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> archive(C classificationValue, String value, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue.classificationName(), originatingSystem, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, value)
		                                                         .inActiveRange(originatingSystem.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			//do nothing to the object
		}
		else
		{
			tableForClassification = exists.get();
			Systems originalSystem = tableForClassification.getOriginalSourceSystemID();
			
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getArchivedFlag(originatingSystem.getEnterpriseID(), identityToken));
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}
	
	@SuppressWarnings("unchecked")
	default IRelationshipValue<L, R, ?> remove(C classificationValue, ISystems<?> system, UUID... identityToken)
	{
		Q tableForClassification = get(findClassificationQueryRelationshipTableType());
		
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue.classificationName(), system, identityToken);
		
		Optional<Q> exists = (Optional<Q>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, null)
		                                                         .inActiveRange(system.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(system, identityToken)
		                                                         .get();
		if (exists.isEmpty())
		{
		}
		else
		{
			tableForClassification = exists.get();
			IActiveFlagService flagService = get(IActiveFlagService.class);
			tableForClassification.setActiveFlagID((ActiveFlag) flagService.getDeletedFlag(system.getEnterpriseID(), identityToken));
			tableForClassification.setEffectiveToDate(LocalDateTime.now());
			tableForClassification.updateNow();
		}
		return tableForClassification;
	}
	
}
