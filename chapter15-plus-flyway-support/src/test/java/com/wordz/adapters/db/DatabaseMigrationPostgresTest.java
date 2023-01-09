package com.wordz.adapters.db;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

@DBRider
@DBUnit(caseSensitiveTableNames = true,
        caseInsensitiveStrategy= Orthography.LOWERCASE)
public class DatabaseMigrationPostgresTest {

    private DataSource dataSource;

    @SuppressWarnings("unused") // Used by DBRider framework
    private final ConnectionHolder connectionHolder = () -> dataSource.getConnection();
    private DatabaseMigration migration;

    @BeforeEach
    void setupConnection() {
        this.dataSource = new PostgresTestDataSource();
        this.migration = new DatabaseMigration(dataSource);
    }

    /**
     * Scaffolding test
     *  At present, this test allows some exploration with the Flyway library.
     *
     *  It has two areas needing improvement:
     * 1. It tests one case only - empty database
     * 2. It is destructive - data will be lost in the environment it runs in
     * 3. Use of @ExpectedDataSet may be an overly precise test
     *
     *  The first issue is simple enough in principle. Add tests to drive out
     * behavior relating to having the flyway_schema_history table already present
     * and having the various tables present or not.
     *
     *  Destroying data needs to be considered as part of a wider testing plan.
     * It is convenient for this test code to destroy data. That would be
     * unacceptable if we were testing in production, and probably if we were
     * using a shared developer database or test environment. Such environments
     * exist to store a quantity of production-like data and need managing. Using
     * automation on such environments is worthwhile - perhaps creating them as
     * Docker images or using Terraform or similar to script the setup and population
     * of data. All tasks that are part and parcel of both DevOps and Data Engineering
     * responsibilities. As thi is a big subject in its own right, it's outside the
     * scope of this test.
     *
     *  The idea of testing too strongly is related to the overall strategy of
     * whether we use test environments or not. It checks for a precise list of
     * words. In reality, we may well populate these in some other way, which would
     * almost certainly change what the best approach to testing is.
     *
     *  As a result, the test overall is labelled @Disabled and will not normally
     * run as part of the test suite. It has to be enabled case-by-case by a
     * developer.
     */
    @Disabled
    @Test
    @ExpectedDataSet("adapters/data/initialSchema.json")
    public void createsSchemaOnEmptyDatabase() throws SQLException {
        dropTables();

        migration.execute();
    }

    private void dropTables() throws SQLException {
        dropTableIfExists("flyway_schema_history");
        dropTableIfExists("word");
        dropTableIfExists("game");
    }

    private void dropTableIfExists(String tableName) throws SQLException {
        var stmt = dataSource.getConnection().createStatement();
        stmt.execute("drop table if exists " + tableName);
    }
}
