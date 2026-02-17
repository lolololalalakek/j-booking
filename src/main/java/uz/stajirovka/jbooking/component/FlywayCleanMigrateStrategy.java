package uz.stajirovka.jbooking.component;

import org.flywaydb.core.Flyway;
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.stereotype.Component;

@Component
public class FlywayCleanMigrateStrategy implements FlywayMigrationStrategy {

    @Override
    public void migrate(Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }
}
