package com.guicedee.activitymaster.fsdm.db;

import bitronix.tm.TransactionManagerServices;
import com.guicedee.activitymaster.fsdm.ActivityMasterStatics;
import com.guicedee.guicedpersistence.btm.BTMConnectionBaseInfo;
import com.guicedee.guicedpersistence.btm.BTMTransactionIsolation;
import com.guicedee.guicedpersistence.db.ConnectionBaseInfo;
import com.guicedee.guicedpersistence.db.DatabaseModule;
import jakarta.validation.constraints.NotNull;
import org.hibernate.jpa.boot.internal.ParsedPersistenceXmlDescriptor;

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
/*		if (System.getProperty("fsdm_integratedSecurity") == null)
		{
			System.setProperty("fsdm_integratedSecurity", "true");
		}*/
		
		//properties.put("hibernate.enhancer.enableDirtyTracking", "false");
		//properties.put("hibernate.enhancer.enableLazyInitialization", "false");
		TransactionManagerServices.getConfiguration()
		                          .setAllowMultipleLrc(true)
		                          .setAsynchronous2Pc(true)
		                          .setDisableJmx(true)
		                          .setMaxLogSizeInMb(100)
		                          .setSkipCorruptedLogs(true)
		                          .setDefaultTransactionTimeout(ActivityMasterStatics.transactionDebugTimeout)
		                          .setWarnAboutZeroResourceTransaction(false);
		
		return new BTMConnectionBaseInfo()
				.setEnableJdbc4ConnectionTest(true)
				.setMaxPoolSize(600)
				.setMinPoolSize(1)
				.setPrefill(false)
				.setMaxIdleTime(10)
				.setShareTransactionConnections(true)
				.setAllowLocalTransactions(true)
				.setTransactionIsolation(BTMTransactionIsolation.READ_COMMITTED.toString())
				;
	}
	
	@Override
	protected @NotNull String getJndiMapping()
	{
		return "jdbc/activitymaster";
	}

	@Override
	public Integer sortOrder()
	{
		return 20;
	}
}
