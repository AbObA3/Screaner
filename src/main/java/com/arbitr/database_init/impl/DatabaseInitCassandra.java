package com.arbitr.database_init.impl;

import com.arbitr.database_init.DatabaseInit;
import com.datastax.oss.quarkus.runtime.api.session.QuarkusCqlSession;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DatabaseInitCassandra implements DatabaseInit {

    private static final String DEX_ACTUAL_TABLE_NAME = "dexes_actual";
    private static final String DEX_HISTORY_TABLE_NAME = "dexes_history";
    private static final String DEX_KEYSPACE = "dex";

    @Inject
    QuarkusCqlSession session;

    @Startup
    @Override
    public void init() {
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + DEX_KEYSPACE +" WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : '1' };");
        session.execute("CREATE TABLE IF NOT EXISTS " + DEX_KEYSPACE + "." + DEX_ACTUAL_TABLE_NAME + "\n" +
                "(\n" +
                "    name                   TEXT,\n" +
                "    currency               TEXT,\n" +
                "    current_value          DOUBLE,\n" +
                "    next_value             DOUBLE,\n" +
                "    absolute_current_value DOUBLE,\n" +
                "    funding_interval       INT,\n" +
                "    next_rate_timestamp    BIGINT,\n" +
                "    last_update_timestamp  TIMESTAMP ,\n" +
                "    primary key (currency, name)\n" +
                ");");
    }

}
