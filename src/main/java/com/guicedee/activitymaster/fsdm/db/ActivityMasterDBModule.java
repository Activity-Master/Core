package com.guicedee.activitymaster.fsdm.db;

import com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB;
import com.guicedee.guicedpersistence.btm.BTMConnectionBaseInfo;
import com.guicedee.guicedpersistence.btm.BTMTransactionIsolation;
import com.guicedee.guicedpersistence.db.ConnectionBaseInfo;
import com.guicedee.guicedpersistence.db.DatabaseModule;
import jakarta.validation.constraints.NotNull;
import org.hibernate.jpa.boot.internal.ParsedPersistenceXmlDescriptor;

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
	protected @NotNull ConnectionBaseInfo getConnectionBaseInfo(ParsedPersistenceXmlDescriptor persistenceUnit, Properties properties)
	{
		//BTMAutomatedTransactionHandler.setActive(true);
		if (System.getProperty("fsdm_integratedSecurity") == null)
		{
			System.setProperty("fsdm_integratedSecurity","true");
		}
		return new BTMConnectionBaseInfo()
				       .setEnableJdbc4ConnectionTest(true)
				       .setMaxPoolSize(Integer.parseInt(System.getProperty("fsdm_db_connections","5")))
				       .setMinPoolSize(1)
				       .setPrefill(true)
				       .setServerName(System.getProperty("fsdm_dbserver","localhost"))
				       .setDatabaseName(System.getProperty("fsdm_dbname","FSDM"))
				       .setInstanceName(System.getProperty("fsdm_dbinstance",""))
				       .setShareTransactionConnections(true)
				       .setUsername(System.getProperty("fsdm_username",""))
				       .setPassword(System.getProperty("fsdm_password",""))
				       .setUrl("jdbc:sqlserver://" + System.getProperty("fsdm_dbserver","localhost")+
				               ";DatabaseName=" + System.getProperty("fsdm_dbname","FSDM")
				               + ";integratedSecurity=" + System.getProperty("fsdm_integratedSecurity","true")+ ";")
				       .setTransactionIsolation(BTMTransactionIsolation.READ_COMMITTED.toString())
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
