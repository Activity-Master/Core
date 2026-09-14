package com.guicedee.activitymaster.fsdm.db;

import com.guicedee.vertx.spi.VertXPreStartup;
import com.guicedee.persistence.ConnectionBaseInfo;
import com.guicedee.persistence.DatabaseModule;
import jakarta.validation.constraints.NotNull;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

import java.util.Properties;

public class ActivityMasterDBModule
		extends DatabaseModule<ActivityMasterDBModule>
{
	public static String persistenceUnitName = "ActivityMaster";
    private ActivityMasterPoolConfiguration configuration;

    @Override protected void configure() {
        // Resolve before the generic module's catch/log boundary, so invalid policy
        // is a Guice construction failure rather than a partially registered database.
        configuration=ActivityMasterPoolConfiguration.configured();
        super.configure();
    }

	@Override
	protected @NotNull String getPersistenceUnitName()
	{
		return persistenceUnitName;
	}

	@Override
	protected @NotNull ConnectionBaseInfo getConnectionBaseInfo(PersistenceUnitDescriptor persistenceUnit, Properties properties)
	{
        return (configuration==null?ActivityMasterPoolConfiguration.configured():configuration)
                .attach(VertXPreStartup.getVertx(),properties);
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
		return true;
	}
}
