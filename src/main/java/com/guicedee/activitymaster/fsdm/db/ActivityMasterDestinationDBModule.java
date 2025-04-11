package com.guicedee.activitymaster.fsdm.db;

import bitronix.tm.TransactionManagerServices;
import com.guicedee.activitymaster.fsdm.ActivityMasterStatics;
import com.guicedee.client.Environment;
import com.guicedee.guicedpersistence.btm.BTMConnectionBaseInfo;
import com.guicedee.guicedpersistence.btm.BTMTransactionIsolation;
import com.guicedee.guicedpersistence.db.ConnectionBaseInfo;
import com.guicedee.guicedpersistence.db.DatabaseModule;
import jakarta.validation.constraints.NotNull;
import org.hibernate.jpa.boot.internal.ParsedPersistenceXmlDescriptor;

import java.util.Properties;

public class ActivityMasterDestinationDBModule
		extends DatabaseModule<ActivityMasterDestinationDBModule>
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
/*		if (System.getProperty("fsdm_integratedSecurity") == null)
		{
			System.setProperty("fsdm_integratedSecurity", "true");
		}*/
		
		//properties.put("hibernate.enhancer.enableDirtyTracking", "false");
		//properties.put("hibernate.enhancer.enableLazyInitialization", "false");
		TransactionManagerServices.getConfiguration()
		                          .setAllowMultipleLrc(false)
		                          .setAsynchronous2Pc(true)
		                          .setDisableJmx(true)
		                          .setMaxLogSizeInMb(100)
		                          .setSkipCorruptedLogs(true)
		                          .setDefaultTransactionTimeout(ActivityMasterStatics.transactionDebugTimeout)
		                          .setWarnAboutZeroResourceTransaction(false);
		
		return new BTMConnectionBaseInfo()
				.setEnableJdbc4ConnectionTest(true)
				.setMaxPoolSize(100)
				.setMinPoolSize(5)
				.setPrefill(true)
				.setMaxIdleTime(300)
				.setApplyTransactionTimeout(true)
				.setMaxLifeTime(60)
				.setShareTransactionConnections(true)
				.setAllowLocalTransactions(true)
				.setAcquireIncrement(5)
				.setTestQuery("SELECT 1")
				.setUrl("jdbc:postgresql://" + Environment.getProperty("FSDM_DBSERVER_2","localhost:5432") + "/" +Environment.getProperty("FSDM_DBNAME2","fsdm") )
				.setTransactionIsolation(BTMTransactionIsolation.READ_COMMITTED.toString())
				.setXa(true)
				.setDriver("org.postgresql.Driver")
				.setClassName("org.postgresql.xa.PGXADataSource")
				//.setDriverClass("org.postgresql.xa.PGXADataSource")
				.setUsername(Environment.getProperty("FSDM_USER_2","fsdm"))
				.setServerName(Environment.getProperty("FSDM_DBSERVER_2","localhost"))
				.setDatabaseName(Environment.getProperty("FSDM_DBNAME_2","fsdm"))
				//.setPort("5432")
				.setPassword(Environment.getProperty("PG_PASSWORD_2","nopassword"))
				;
	}
	
	@Override
	protected @NotNull String getJndiMapping()
	{
		return "jdbc/activitymaster2";
	}


	@Override
	public Integer sortOrder()
	{
		return 20;
	}
}
