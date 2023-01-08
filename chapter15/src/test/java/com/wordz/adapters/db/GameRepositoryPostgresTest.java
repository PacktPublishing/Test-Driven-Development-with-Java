package com.wordz.adapters.db;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.wordz.domain.Game;
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

    /**
     * Setting up a connection to the test database
     *   As this method only sets up a connection to the database and does not
     * create any stored data, there is a good argument for using the @BeforeAll
     * annotation and doing this once for all tests in this class
     */
    @BeforeEach
    void setupConnection() {
        this.dataSource = new PostgresTestDataSource();
    }

    /**
     * Avoiding inserting duplicate data
     *   One problem in the arrange step of a database test is that we would
     * like to insert a specific row in the database so that we can write our
     * assert step. The simplest idea would be to write the same row each time.
     *
     *   A database will store that row until it is either deleted or the whole
     * table dropped. This causes a problem with our FIRST test isolation. If
     * we run that same test again, the second run will fail. The INSERT fails
     * due to the row already existing from the previous test.
     *
     * There are two good approaches to avoiding this:
     * - drop the table (or delete the row) before each test runs
     * - create rows using unique values
     *
     *   The drop table approach works well for development-like test environments.
     * So long as we can recreate the initaial state of the the table at the
     * start of each test, we can use this approach. It is the one used in the following
     * test. Clearly, for in-production testing, it's not appropriate. Similar concerns
     * arise with deleting rows.
     *
     *   Creating rows with unique values is a vaiable approach. A random generator
     * is used to create artificial values, like a surname "surname-03defa21" for example.
     * The idea is that running the test will insert a new and unique value each time,
     * avoiding any conflicts. Periodically, the rows would need to be deleted.
     *
     *   It is tempting to think that dropping the table after the test runs would
     * be a good practice. We normally cleanup after our tests. For database testing,
     * it is not uncommon for the cleanup to never run. If some fatal error occurs
     * during the test run of our code, the cleanup will get skipped. The
     * recommendation is to place cleanup code before the test starts.
     *
     * Dropping tables with DBRider framework
     *  The following attribute of the @DataSet annotation determines behavior:
     * cleanBefore = true instructs the DBRider framework to delete all rows
     * of the table at the start of the test.
     */
    @Test
    @DataSet(value = "adapters/data/emptyGame.json",
            cleanBefore = true)
    @ExpectedDataSet(value = "/adapters/data/createGame.json")
    public void storesNewGame() {
        var player = new Player("player1");
        var game = new Game(player, "BONUS", 0, false);

        GameRepository games = new GameRepositoryPostgres(dataSource);
        games.create(game);
    }

    /**
     * Defining initial and expected values in the database
     *  One small challenge in writing database tests is getting some readability
     * into the way we express initial data in our arrange step and the expected
     * data for our assert step.
     *
     * There are three useful ways of doing this:
     * - Using the Fluent Builder design pattern
     * - Raw SQL statements in the test itself
     * - Data files
     *
     *  The readability issue arises from databases being external to the Java
     * domain code. THey tend to use their own languages - such as SQL - and
     * require some kind of access library (like JDBC) to connect to. So we
     * cannot use plain Java code as we do in our domain model.
     *
     *  The Fluent Builder design pattern is described at
     *  https://en.wikipedia.org/wiki/Fluent_interface#Java
     *  and is an object that allows us to build up a complex data structure
     *  piece by piece. It's advantage is that it uses Java to describe the
     *  building process and can be used in the arrange step. We can then easily read
     *  all the values that have been created. This localization of what data to use
     *  with the test code is important.
     *
     *   In a similar manner, using raw SQL statements in the arrange step will
     *  provide visibility. Using a method from our JDBC library, we can write a line
     *  similar to:
     *    executeInsert( "INSERT INTO users VALUES (1, 'Alan')" );
     *  This also keeps the data near to the test code that depends upon it.
     *
     *   The final approach trades-off having data close to the test for having data
     *  in its own natural format. Here, we use data files, placed in the test/resources
     *  folder of our Java project. The test code refers to these file by name, meaning
     *  we have to open up the file to discover what specific data is being used. But
     *  the data itself will be in some typical format. Maybe a CSV file, or a JSON file.
     *  The data itself can be easier to provide and work with in external tools. We
     *  trade-off that localization, however.
     *
     *   The approach taken in these tests is the data file approach. This is well
     *  supported by the DBRider framework out of the box. It provides a file for
     *  both the initial setup up of the database in the arrange step as well as
     *  the expected results data. It provides two annotations to hook these files
     *  up to the test: @DataSet() relates to the initial data and @ExpectedDataSet()
     *  refers to the data we assert against.
     *
     *   Notably, these annotations perform parts of the test itself.
     *  @DataSet() will insert data into our database. @ExpectedDatSet() runs
     *  the assertion code for us. As a result, our test looks rather incomplete at
     *  first sight - the arrange and assert steps are mostly performed by DBRider
     *  code activated by the annotations.
     */
    @Test
    @DataSet(value = "adapters/data/createGame.json",
            cleanBefore = true)
    public void fetchesGame() {
        GameRepository games = new GameRepositoryPostgres(dataSource);

        var player = new Player("player1");
        Optional<Game> game = games.fetchForPlayer(player);

        assertThat(game.isPresent()).isTrue();
        var actual = game.get();
        assertThat(actual.getPlayer()).isEqualTo(player);
        assertThat(actual.getWord()).isEqualTo("BONUS");
        assertThat(actual.getAttemptNumber()).isZero();
        assertThat(actual.isGameOver()).isFalse();
    }

    @Test
    @DataSet(value = "adapters/data/emptyGame.json",
            cleanBefore = true)
    public void reportsGameNotFoundForPlayer() {
        GameRepository games = new GameRepositoryPostgres(dataSource);

        var player = new Player("player1");
        Optional<Game> game = games.fetchForPlayer(player);

        assertThat(game.isEmpty()).isTrue();
    }

    @Test
    @DataSet(value = "adapters/data/createGame.json",
            cleanBefore = true)
    @ExpectedDataSet(value = "/adapters/data/updatedGame.json")
    public void updatesGame() {
        var player = new Player("player1");
        var game = new Game(player, "BONUS", 0, false);

        game.attempt("AAAAA");
        GameRepository games = new GameRepositoryPostgres(dataSource);

        games.update(game);
    }
}
