package com.guicedee.activitymaster.core.async;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

public class AsyncUpdate implements Runnable {
    @Getter@Setter
    private WarehouseBaseTable<?,?,?> persistable;
    @Getter@Setter
    private Consumer<Throwable> onException;

    AsyncUpdate(){}

    @Override
    public void run() {
        persist();
    }

    public static AsyncUpdate getInstance(WarehouseBaseTable<?,?,?> persistable, Consumer<Throwable> throwableConsumer)
    {
        AsyncUpdate ap = GuiceContext.get(AsyncUpdate.class);
        ap.setPersistable(persistable);
        ap.setOnException(throwableConsumer);
        return ap;
    }

    @Transactional(entityManagerAnnotation = ActivityMasterDB.class)
    void persist()
    {
        try {
            persistable.update();
        }catch (Throwable T)
        {
            if(onException != null)
            {
                onException.accept(T);
            }
        }
    }
}
