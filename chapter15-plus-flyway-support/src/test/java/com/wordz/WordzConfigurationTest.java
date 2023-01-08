package com.wordz;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WordzConfigurationTest {

    private static final String[] NO_ARGS = {};
    private static final String[] FULL_ARGS = {
            "endpoint-host",
            "9999",
            "db-name",
            "db-host",
            "db-schema",
            "db-user",
            "db-password"
    };

    @Test
    void defaultsDatabaseName() {
        var config = new WordzConfiguration(NO_ARGS);
        assertThat(config.getDatabaseName()).isEqualTo("wordzdb");
    }

    @Test
    void defaultsDatabaseSchemaName() {
        var config = new WordzConfiguration(NO_ARGS);
        assertThat(config.getDatabaseSchemaName()).isEqualTo("public");
    }

    @Test
    void defaultsDatabaseHost() {
        var config = new WordzConfiguration(NO_ARGS);
        assertThat(config.getDatabaseHost()).isEqualTo("localhost");
    }

    @Test
    void defaultsDatabaseUser() {
        var config = new WordzConfiguration(NO_ARGS);
        assertThat(config.getDatabaseUser()).isEqualTo("ciuser");
    }

    @Test
    void defaultsDatabaseUserPassword() {
        var config = new WordzConfiguration(NO_ARGS);
        assertThat(config.getDatabaseUserPassword()).isEqualTo("cipassword");
    }

    @Test
    void defaultsEndpointHost() {
        var config = new WordzConfiguration(NO_ARGS);
        assertThat(config.getEndpointHost()).isEqualTo("localhost");
    }

    @Test
    void defaultsEndpointPort() {
        var config = new WordzConfiguration(NO_ARGS);
        assertThat(config.getEndpointPort()).isEqualTo(8080);
    }

    @Test
    void extractsEndpointHost() {
        var config = new WordzConfiguration(FULL_ARGS);
        assertThat(config.getEndpointHost()).isEqualTo("endpoint-host");
    }

    @Test
    void extractsEndpointPort() {
        var config = new WordzConfiguration(FULL_ARGS);
        assertThat(config.getEndpointPort()).isEqualTo(9999);
    }

    @Test
    void extractsDatabaseName() {
        var config = new WordzConfiguration(FULL_ARGS);
        assertThat(config.getDatabaseName()).isEqualTo("db-name");
    }

    @Test
    void extractsDatabaseHost() {
        var config = new WordzConfiguration(FULL_ARGS);
        assertThat(config.getDatabaseHost()).isEqualTo("db-host");
    }

    @Test
    void extractsDatabaseSchemaName() {
        var config = new WordzConfiguration(FULL_ARGS);
        assertThat(config.getDatabaseSchemaName()).isEqualTo("db-schema");
    }

    @Test
    void extractsDatabaseUser() {
        var config = new WordzConfiguration(FULL_ARGS);
        assertThat(config.getDatabaseUser()).isEqualTo("db-user");
    }

    @Test
    void extractsDatabaseUserPassword() {
        var config = new WordzConfiguration(FULL_ARGS);
        assertThat(config.getDatabaseUserPassword()).isEqualTo("db-password");
    }
}