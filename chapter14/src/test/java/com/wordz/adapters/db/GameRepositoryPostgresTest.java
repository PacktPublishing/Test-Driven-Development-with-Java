package com.wordz.adapters.db;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.wordz.domain.Game;
import com.wordz.domain.GameAssert;
import com.wordz.domain.GameRepository;
import com.wordz.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@DBUnit(caseSensitiveTableNames = true,
        caseInsensitiveStrategy = Orthography.LOWERCASE)
public class GameRepositoryPostgresTest {

    private DataSource dataSource;

    @SuppressWarnings("unused") // Used by DBRider framework
    private final ConnectionHolder connectionHolder = () -> dataSource.getConnection();

    @BeforeEach
    void setupConnection() {
        this.dataSource = new PostgresTestDataSource();
    }


    @Test
    @DataSet(value = "adapters/data/emptyGame.json",
            cleanBefore = true
    )
    @ExpectedDataSet(value = "/adapters/data/createGame.json")
    public void storesNewGame() {
        var player = new Player("player1");
        var game = new Game(player, "BONUS", 0, false);

        GameRepository games = new GameRepositoryPostgres(dataSource);
        games.create(game);
    }

    @Test
    @DataSet(value = "adapters/data/createGame.json",
            cleanBefore = true
    )
    public void fetchesGame() {
        GameRepository games = new GameRepositoryPostgres(dataSource);

        var player = new Player("player1");
        Optional<Game> game = games.fetchForPlayer(player);

        assertThat(game.isPresent()).isTrue();

        GameAssert.assertThat(game.get())
                .hasPlayer(player)
                .hasWord("BONUS")
                .hasAttemptNumber(0)
                .hasGameOver(false);
    }

    @Test
    @DataSet(value = "adapters/data/emptyGame.json",
            cleanBefore = true
    )
    public void reportsGameNotFoundForPlayer() {
        GameRepository games = new GameRepositoryPostgres(dataSource);

        var player = new Player("player1");
        Optional<Game> game = games.fetchForPlayer(player);

        assertThat(game.isEmpty()).isTrue();
    }

    @Test
    @DataSet(value = "adapters/data/createGame.json",
            cleanBefore = true
    )
    @ExpectedDataSet(value = "/adapters/data/updatedGame.json")
    public void updatesGame() {
        var player = new Player("player1");
        var game = new Game(player, "BONUS", 0, false);

        game.attempt("AAAAA");
        GameRepository games = new GameRepositoryPostgres(dataSource);

        games.update(game);
    }

}
