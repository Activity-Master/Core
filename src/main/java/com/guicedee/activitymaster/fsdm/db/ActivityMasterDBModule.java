package com.guicedee.activitymaster.fsdm.db;

import com.guicedee.vertx.spi.VertXPreStartup;
import com.guicedee.persistence.ConnectionBaseInfo;
import com.guicedee.persistence.DatabaseModule;
import com.guicedee.persistence.implementations.VertxPersistenceModule;
import jakarta.validation.constraints.NotNull;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;
import io.smallrye.mutiny.Uni;
import io.vertx.sqlclient.Pool;

import java.util.List;
import java.util.Properties;

public class ActivityMasterDBModule
		extends DatabaseModule<ActivityMasterDBModule>
{
	public static String persistenceUnitName = "ActivityMaster";
    private ActivityMasterPoolConfiguration configuration;
    private Pool schemaPool;

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
		ConnectionBaseInfo info = (configuration==null?ActivityMasterPoolConfiguration.configured():configuration)
                .attach(VertXPreStartup.getVertx(),properties);
        schemaPool = (Pool) properties.get("guicedee.persistence.ownedPool");
        return info;
	}

    @Override
    public List<Uni<Boolean>> postLoad() {
        List<Uni<Boolean>> startup = super.postLoad();
        if (!FsdmSchemaUpdates.enabled()) return startup;
        // Lazy and awaited: migrations finish before PersistService creates Hibernate's factory.
        return startup.stream().map(start -> Uni.createFrom()
                .completionStage(() -> FsdmSchemaUpdates.installUpdates(schemaPool != null ? schemaPool :
                        (Pool) VertxPersistenceModule.getSqlClientByEntityManager(getPersistenceUnitName())).toCompletionStage())
                .chain(() -> start)).toList();
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
