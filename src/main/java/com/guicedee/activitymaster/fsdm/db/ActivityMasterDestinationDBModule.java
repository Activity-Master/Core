package com.guicedee.activitymaster.fsdm.db;

import com.guicedee.client.Environment;
import com.guicedee.persistence.ConnectionBaseInfo;
import com.guicedee.persistence.DatabaseModule;
import com.guicedee.persistence.implementations.postgres.PostgresConnectionBaseInfo;
import jakarta.validation.constraints.NotNull;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

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
	protected @NotNull ConnectionBaseInfo getConnectionBaseInfo(PersistenceUnitDescriptor persistenceUnit, Properties properties)
	{
		PostgresConnectionBaseInfo connectionInfo = new PostgresConnectionBaseInfo();
		connectionInfo.setServerName(Environment.getProperty("FSDM_DBSERVER_2","localhost"));
		connectionInfo.setPort(Environment.getProperty("FSDM_DBPORT_2","5432"));
		connectionInfo.setDatabaseName(Environment.getProperty("FSDM_DBNAME_2","fsdm"));
		connectionInfo.setUsername(Environment.getProperty("FSDM_USER_2","fsdm"));
		connectionInfo.setPassword(Environment.getProperty("PG_PASSWORD_2","nopassword"));
		connectionInfo.setDefaultConnection(true);
		connectionInfo.setReactive(true);
		return connectionInfo;
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
