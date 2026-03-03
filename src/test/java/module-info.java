import com.guicedee.activitymaster.PostgreSQLTestDBModule;
import com.guicedee.client.services.lifecycle.IGuiceModule;

open module activity.master.test {
    requires transitive com.entityassist;
    requires transitive com.guicedee.persistence;

    requires org.junit.jupiter.api;
    requires junit;

    requires jakarta.xml.bind;
    requires jakarta.persistence;



    requires transitive org.hibernate.reactive;
    requires io.smallrye.mutiny;
    requires com.google.guice;
    requires static lombok;

    requires org.testcontainers;
    requires io.vertx.sql.client.pg;
    requires com.guicedee.activitymaster.fsdm;

    provides IGuiceModule with PostgreSQLTestDBModule;

}
