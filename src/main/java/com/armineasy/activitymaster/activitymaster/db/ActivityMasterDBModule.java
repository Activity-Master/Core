package com.armineasy.activitymaster.activitymaster.db;

import com.jwebmp.guicedpersistence.btm.BTMConnectionBaseInfo;
import com.jwebmp.guicedpersistence.db.ConnectionBaseInfo;
import com.jwebmp.guicedpersistence.db.DatabaseModule;
import com.oracle.jaxb21.PersistenceUnit;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.Properties;

public class ActivityMasterDBModule
		extends DatabaseModule<ActivityMasterDBModule>
{
	public static String persistenceUnitName = "ActivityMaster";

	@Override
	protected @NotNull String getPersistenceUnitName()
	{
		return persistenceUnitName;
	}

	@Override
	protected @NotNull ConnectionBaseInfo getConnectionBaseInfo(PersistenceUnit persistenceUnit, Properties properties)
	{
		//BTMAutomatedTransactionHandler.setActive(true);
		return new BTMConnectionBaseInfo()
				       .setEnableJdbc4ConnectionTest(true)
				       .setMaxPoolSize(4)
				       .setMinPoolSize(2)
				       .setPrefill(true)
				       .setShareTransactionConnections(true)
				       .setTransactionIsolation("READ_UNCOMMITTED")
				       .setAllowLocalTransactions(true);
	}

	@Override
	protected @NotNull String getJndiMapping()
	{
		return "jdbc/activitymaster";
	}

	@Override
	protected @NotNull Class<? extends Annotation> getBindingAnnotation()
	{
		return ActivityMasterDB.class;
	}
}
