package com.guicedee.activitymaster.fsdm.db;

import com.guicedee.client.Environment;
import com.guicedee.vertx.spi.VertXPreStartup;
import com.guicedee.persistence.ConnectionBaseInfo;
import com.guicedee.persistence.DatabaseModule;
import com.guicedee.persistence.annotations.EntityManager;
import com.guicedee.persistence.implementations.postgres.PostgresConnectionBaseInfo;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import jakarta.validation.constraints.NotNull;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

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
		connectionInfo.setMinPoolSize(50);
		connectionInfo.setMaxPoolSize(300);
		connectionInfo.setMaxIdleTime(30000);

		PgConnectOptions connectOptions = new PgConnectOptions()
				.setPort(Integer.parseInt(Environment.getProperty("FSDM_DBPORT","5432")))
				.setHost(Environment.getProperty("FSDM_DBSERVER","localhost"))
				.setDatabase(Environment.getProperty("FSDM_DBNAME","fsdm"))
				.setUser(Environment.getProperty("FSDM_USER","fsdm"))
				.setPassword(Environment.getProperty("FSDM_PASSWORD","fsdm"))
				.setPipeliningLimit(16);

		PoolOptions poolOptions = new PoolOptions()
				.setShared(true)
				.setName("activity-master-pool")
				.setMaxSize(500)
				.setShared(true)
				.setIdleTimeout(300)
				.setPoolCleanerPeriod(300)
				.setEventLoopSize(10)
				.setMaxLifetimeUnit(TimeUnit.MINUTES)
				.setMaxLifetime(3);

		Pool pool = PgBuilder
				.pool()
				.connectingTo(connectOptions.setPipeliningLimit(16))
				.with(poolOptions)
				.using(VertXPreStartup.getVertx())
				.build();
		properties.put("hibernate.vertx.pool", pool);

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
