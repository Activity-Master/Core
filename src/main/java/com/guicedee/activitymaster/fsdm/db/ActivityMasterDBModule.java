package com.guicedee.activitymaster.fsdm.db;

import com.guicedee.client.Environment;
import com.guicedee.vertxpersistence.ConnectionBaseInfo;
import com.guicedee.vertxpersistence.DatabaseModule;
import com.guicedee.vertxpersistence.annotations.EntityManager;
import com.guicedee.vertxpersistence.implementations.postgres.PostgresConnectionBaseInfo;
import jakarta.validation.constraints.NotNull;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

import java.util.Properties;

@EntityManager(value = "ActivityMaster", defaultEm = false)
public class ActivityMasterDBModule
		extends DatabaseModule<ActivityMasterDBModule>
{
	public static String persistenceUnitName = "ActivityMaster";
	public static Boolean forTests = false;
	
	@Override
	protected @NotNull String getPersistenceUnitName()
	{
		return persistenceUnitName;
	}


	@Override
	protected void configure()
	{
		if(!forTests)
			super.configure();
	}

	@Override
	protected @NotNull ConnectionBaseInfo getConnectionBaseInfo(PersistenceUnitDescriptor persistenceUnit, Properties properties)
	{
		 PostgresConnectionBaseInfo connectionInfo = new PostgresConnectionBaseInfo();
        connectionInfo.setServerName(Environment.getProperty("FSDM_DBSERVER","localhost"));
        connectionInfo.setPort(Environment.getProperty("FSDM_DBPORT","5432"));
        connectionInfo.setDatabaseName(Environment.getProperty("FSDM_DBNAME","fsdm"));
        connectionInfo.setUsername(Environment.getProperty("FSDM_USER","fsdm"));
        connectionInfo.setPassword(Environment.getProperty("FSDM_PASSWORD","fsdm"));
        connectionInfo.setDefaultConnection(true);
        connectionInfo.setReactive(true);
        return connectionInfo;
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

	@Override
	public boolean enabled()
	{
		return !forTests;
	}
}
