package com.guicedee.activitymaster.fsdm.async;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import com.guicedee.guicedinjection.GuiceContext;

import java.util.function.Consumer;

public class AsyncUpdate implements Runnable {
    
    private WarehouseBaseTable<?,?,?> persistable;
    
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
    
    public WarehouseBaseTable<?, ?, ?> getPersistable()
    {
        return persistable;
    }
    
    public AsyncUpdate setPersistable(WarehouseBaseTable<?, ?, ?> persistable)
    {
        this.persistable = persistable;
        return this;
    }
    
    public Consumer<Throwable> getOnException()
    {
        return onException;
    }
    
    public AsyncUpdate setOnException(Consumer<Throwable> onException)
    {
        this.onException = onException;
        return this;
    }
    
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
