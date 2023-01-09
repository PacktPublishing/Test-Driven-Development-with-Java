package com.wordz;

import com.wordz.adapters.api.WordzEndpoint;
import com.wordz.adapters.db.DatabaseMigration;
import com.wordz.adapters.db.GameRepositoryPostgres;
import com.wordz.adapters.db.WordRepositoryPostgres;
import com.wordz.domain.Wordz;

public class WordzApplication {

    public static void main(String[] args) {
        var config = new WordzConfiguration(args);
        new WordzApplication().run(config);
    }

    private void run(WordzConfiguration config) {
        new DatabaseMigration(config.getDataSource()).execute();

        var gameRepository = new GameRepositoryPostgres(config.getDataSource());
        var wordRepository = new WordRepositoryPostgres(config.getDataSource());
        var randomNumbers = new ProductionRandomNumbers();

        var wordz = new Wordz(gameRepository, wordRepository, randomNumbers);

        var api = new WordzEndpoint(wordz,
                config.getEndpointHost(),
                config.getEndpointPort());

        waitUntilTerminated();
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
