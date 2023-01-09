package com.wordz.adapters.db;

import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

public class DatabaseMigration {
    private static final String LOCATION_FLYWAY_CONFIG
            = "classpath:db/postgres";

    private final DataSource dataSource;

    public DatabaseMigration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void execute() {
        Flyway flyway =
                Flyway.configure()
                        .locations(LOCATION_FLYWAY_CONFIG)
                        .dataSource(dataSource)
                        .baselineOnMigrate(true)
                        .load();

        flyway.migrate();
    }
}
