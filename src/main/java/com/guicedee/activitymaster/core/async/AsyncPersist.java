package com.guicedee.activitymaster.core.async;

import com.guicedee.activitymaster.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

public class AsyncPersist implements Runnable {
    @Getter@Setter
    private WarehouseBaseTable<?,?,?> persistable;
    @Getter@Setter
    private Consumer<Throwable> onException;

    AsyncPersist(){}

    @Override
    public void run() {
        persist();
    }

    public static AsyncPersist getInstance( WarehouseBaseTable<?,?,?> persistable,Consumer<Throwable> throwableConsumer)
    {
        AsyncPersist ap = GuiceContext.get(AsyncPersist.class);
        ap.setPersistable(persistable);
        ap.setOnException(throwableConsumer);
        return ap;
    }

    @Transactional(entityManagerAnnotation = ActivityMasterDB.class)
    void persist()
    {
        try {
            persistable.persist();
        }catch (Throwable T)
        {
            if(onException != null)
            {
                onException.accept(T);
            }
        }
    }
}
