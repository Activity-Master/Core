package com.guicedee.activitymaster.fsdm.async;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import com.guicedee.guicedinjection.GuiceContext;

import java.util.List;
import java.util.function.Consumer;

public class AsyncBulkPersist implements Runnable {
    
    private List<WarehouseBaseTable<?,?,?>> persistable;
    
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
    
    public List<WarehouseBaseTable<?, ?, ?>> getPersistable()
    {
        return persistable;
    }
    
    public AsyncBulkPersist setPersistable(List<WarehouseBaseTable<?, ?, ?>> persistable)
    {
        this.persistable = persistable;
        return this;
    }
    
    public Consumer<Throwable> getOnException()
    {
        return onException;
    }
    
    public AsyncBulkPersist setOnException(Consumer<Throwable> onException)
    {
        this.onException = onException;
        return this;
    }
    
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
