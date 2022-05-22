package com.guicedee.activitymaster.fsdm.async;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import com.guicedee.guicedinjection.GuiceContext;

import java.util.function.Consumer;

public class AsyncPersist implements Runnable {
    
    private WarehouseBaseTable<?,?,?> persistable;
    
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
    
    public WarehouseBaseTable<?, ?, ?> getPersistable()
    {
        return persistable;
    }
    
    public AsyncPersist setPersistable(WarehouseBaseTable<?, ?, ?> persistable)
    {
        this.persistable = persistable;
        return this;
    }
    
    public Consumer<Throwable> getOnException()
    {
        return onException;
    }
    
    public AsyncPersist setOnException(Consumer<Throwable> onException)
    {
        this.onException = onException;
        return this;
    }
    
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
