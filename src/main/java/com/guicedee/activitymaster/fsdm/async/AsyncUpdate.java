package com.guicedee.activitymaster.fsdm.async;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import com.guicedee.guicedinjection.GuiceContext;
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
