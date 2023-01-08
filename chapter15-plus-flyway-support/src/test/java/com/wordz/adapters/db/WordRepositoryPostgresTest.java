package com.wordz.adapters.db;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.wordz.domain.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@DBUnit(caseSensitiveTableNames = true,
        caseInsensitiveStrategy= Orthography.LOWERCASE)
public class WordRepositoryPostgresTest {

    private DataSource dataSource;

    @SuppressWarnings("unused") // Used by DBRider framework
    private final ConnectionHolder connectionHolder = () -> dataSource.getConnection();

    /**
     * As this method only sets up a connection to the database and does not
     * create any stored data, there is a good argument for using the @BeforeAll
     * annotation and doing this once for all tests in this class
     */
    @BeforeEach
    void setupConnection() {
        this.dataSource = new PostgresTestDataSource();
    }

    @Test
    @DataSet("adapters/data/wordTable.json")
    public void fetchesWord()  {
        WordRepository repository = new WordRepositoryPostgres(dataSource);

        String actual = repository.fetchWordByNumber(27);

        assertThat(actual).isEqualTo("ARISE");
    }

    @Test
    @DataSet("adapters/data/threeWords.json")
    public void returnsHighestWordNumber()  {
        WordRepository repository = new WordRepositoryPostgres(dataSource);

        int actual = repository.highestWordNumber();

        assertThat(actual).isEqualTo(3);
    }
}
