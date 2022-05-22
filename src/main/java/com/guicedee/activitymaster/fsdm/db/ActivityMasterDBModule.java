package com.guicedee.activitymaster.fsdm.db;

import bitronix.tm.TransactionManagerServices;
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
		if (System.getProperty("fsdm_integratedSecurity") == null)
		{
			System.setProperty("fsdm_integratedSecurity", "true");
		}
		TransactionManagerServices.getConfiguration()
		                          .setAllowMultipleLrc(true)
		                          .setAsynchronous2Pc(true)
		                          .setDisableJmx(true)
		                          .setMaxLogSizeInMb(20)
		                          .setSkipCorruptedLogs(true)
		                          .setWarnAboutZeroResourceTransaction(true);
		
		return new BTMConnectionBaseInfo()
				.setEnableJdbc4ConnectionTest(true)
				.setMaxPoolSize(Integer.parseInt(System.getProperty("fsdm_db_connections", "50")))
				.setMinPoolSize(1)
				.setPrefill(false)
				.setShareTransactionConnections(true)
				.setAllowLocalTransactions(true)
				
				
				//.setXa(true)
			//	.setClassName("com.microsoft.sqlserver.jdbc.SQLServerXADataSource")
			//	.setDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver")
				
				/*.setServerName(System.getProperty("fsdm_dbserver", "localhost"))
				.setDatabaseName(System.getProperty("fsdm_dbname", "FSDM"))
			//	.setInstanceName(System.getProperty("fsdm_dbinstance", ""))
				.setUsername(System.getProperty("fsdm_username", ""))
				.setPassword(System.getProperty("fsdm_password", ""))
				
				.setUrl("jdbc:sqlserver://" + System.getProperty("fsdm_dbserver", "localhost") +
				        ";DatabaseName=" + System.getProperty("fsdm_dbname", "FSDM")
				        + ";integratedSecurity=" + System.getProperty("fsdm_integratedSecurity", "true") + ";" +
				        "trustServerCertificate=true;"
				        )*/
				
				.setTransactionIsolation(BTMTransactionIsolation.READ_COMMITTED.toString())
				;
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
	
	@Override
	public Integer sortOrder()
	{
		return 20;
	}
}
