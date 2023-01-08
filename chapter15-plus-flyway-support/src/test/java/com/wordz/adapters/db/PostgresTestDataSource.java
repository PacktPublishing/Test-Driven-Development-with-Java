package com.wordz.adapters.db;

import org.postgresql.ds.PGSimpleDataSource;

public class PostgresTestDataSource extends PGSimpleDataSource {
    PostgresTestDataSource () {
        setServerNames(new String[]{"localhost"});
        setDatabaseName("wordzdb");
        setCurrentSchema("public");
        setUser("ciuser");
        setPassword("cipassword");
    }
}
