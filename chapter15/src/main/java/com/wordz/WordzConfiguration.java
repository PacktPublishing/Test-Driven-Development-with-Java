package com.wordz;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

class WordzConfiguration {
    public static final int NUMBER_OF_COMMAND_LINE_OPTIONS = 7;
    private final PGSimpleDataSource dataSource;
    private int endpointPort = 8080;
    private String endpointHost = "localhost";
    private String databaseName = "wordzdb";
    private String databaseSchemaName = "public";
    private String databaseUser = "ciuser";
    private String databaseUserPassword = "cipassword";
    private String databaseHost = "localhost";

    public WordzConfiguration(String[] args) {
        extractValuesFromCommandLine(args);
        dataSource = createDataSource();
    }

    private PGSimpleDataSource createDataSource() {
        var dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{databaseHost});
        dataSource.setDatabaseName(databaseName);
        dataSource.setCurrentSchema(databaseSchemaName);
        dataSource.setUser(databaseUser);
        dataSource.setPassword(databaseUserPassword);
        return dataSource;
    }

    private void extractValuesFromCommandLine(String[] args) {
        if (!(args.length == NUMBER_OF_COMMAND_LINE_OPTIONS)) {
            return;
        }

        endpointHost = args[0];
        endpointPort = Integer.parseInt(args[1]);
        databaseName = args[2];
        databaseHost = args[3];
        databaseSchemaName = args[4];
        databaseUser = args[5];
        databaseUserPassword = args[6];
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public int getEndpointPort() {
        return endpointPort;
    }

    public String getEndpointHost() {
        return endpointHost;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseSchemaName() {
        return databaseSchemaName;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabaseUserPassword() {
        return databaseUserPassword;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }
}
