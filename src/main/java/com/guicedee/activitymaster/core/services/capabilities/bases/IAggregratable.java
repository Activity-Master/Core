package com.guicedee.activitymaster.core.services.capabilities.bases;

import com.entityassist.RootEntity;
import com.entityassist.enumerations.SelectAggregrate;
import com.entityassist.querybuilder.statements.InsertStatement;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderDefault;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IHasValueQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IActiveFlag;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.guicedinjection.GuiceContext;

import java.io.Serializable;
import java.util.*;

public interface IAggregratable<J extends QueryBuilderDefault<J, E, I>,
		E extends WarehouseBaseTable<E, J, I>,
		I extends Serializable,
		T extends IClassificationValue<?>>
		extends IHasValueQueryBuilder<J, E, I>
{
	
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
	
	default List<Object[]> getValuePivot(String value, Set<String> searchValue, ISystems<?> system, UUID[] identityToken, String... values)
	{
		return getValuePivot(SelectAggregrate.Max,value, searchValue, system, identityToken, values);
	}
	
	default List<Object[]> getValuePivot(String value, String searchValue, ISystems<?> system, UUID[] identityToken, String... values)
	{
		return getValuePivot(SelectAggregrate.Max,value, Set.of(searchValue), system, identityToken, values);
	}
	
	default List<Object[]> getValuePivot(SelectAggregrate aggregrate, String value, Set<String> searchValue, ISystems<?> system, UUID[] identityToken, String... values)
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
}
