package com.guicedee.activitymaster.core.async;

import com.guicedee.activitymaster.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Consumer;

public class AsyncBulkPersist implements Runnable {
    @Getter@Setter
    private List<WarehouseBaseTable<?,?,?>> persistable;
    @Getter@Setter
    private Consumer<Throwable> onException;

    AsyncBulkPersist(){}

    @Override
    public void run() {
        persist();
    }

    public static AsyncBulkPersist getInstance(List<WarehouseBaseTable<?,?,?>> persistable, Consumer<Throwable> throwableConsumer)
    {
        AsyncBulkPersist ap = GuiceContext.get(AsyncBulkPersist.class);
        ap.setPersistable(persistable);
        ap.setOnException(throwableConsumer);
        return ap;
    }

    @Transactional(entityManagerAnnotation = ActivityMasterDB.class)
    void persist()
    {
        persistable.stream().parallel().forEach(a->{
            try {
                a.persist();
            }catch (Throwable T)
            {
                if(onException != null)
                {
                    onException.accept(T);
                }
            }
        });
    }
}
