package com.wordz;

import com.wordz.adapters.api.WordzEndpoint;
import com.wordz.adapters.db.GameRepositoryPostgres;
import com.wordz.adapters.db.WordRepositoryPostgres;
import com.wordz.domain.Wordz;
import org.flywaydb.core.Flyway;

public class WordzApplication {
    private static final String LOCATION_FLYWAY_CONFIG
            = "classpath:db/postgres";

    public static void main(String[] args) {
        var config = new WordzConfiguration(args);
        new WordzApplication().run(config);
    }

    private void run(WordzConfiguration config) {
        runDatabaseUpdates(config);

        var gameRepository = new GameRepositoryPostgres(config.getDataSource());
        var wordRepository = new WordRepositoryPostgres(config.getDataSource());
        var randomNumbers = new ProductionRandomNumbers();

        var wordz = new Wordz(gameRepository, wordRepository, randomNumbers);

        var api = new WordzEndpoint(wordz,
                config.getEndpointHost(),
                config.getEndpointPort());

        waitUntilTerminated();
    }

    private void runDatabaseUpdates(WordzConfiguration config) {
        Flyway flyway =
                Flyway.configure()
                        .locations(LOCATION_FLYWAY_CONFIG)
                        .dataSource(config.getDataSource())
                        .baselineOnMigrate(true)
                        .load();

        flyway.migrate();
    }

    private void waitUntilTerminated() {
        try {
            while (true) {
                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {
            return;
        }
    }
}
