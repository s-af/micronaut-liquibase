package com.example;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.MSSQLServerContainer;

import java.util.Map;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LiquiTest implements TestPropertyProvider {

    static MSSQLServerContainer<?> mssqlContainer = new MSSQLServerContainer("mcr.microsoft.com/mssql/server:2019-CU26-ubuntu-20.04")
            .withPassword("MSSQLServer_Password")
            .acceptLicense();

    static {
        mssqlContainer.start();
    }

    private String getMSSQLDbUri() {
        return mssqlContainer.getJdbcUrl() + ";user=sa";
    }

    @Override
    public @NonNull Map<String, String> getProperties() {
        return Map.of(
                "datasources.liquibaseds.url", getMSSQLDbUri()
        );
    }

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

}
