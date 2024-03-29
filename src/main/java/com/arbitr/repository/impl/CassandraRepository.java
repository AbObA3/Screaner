package com.arbitr.repository.impl;

import com.arbitr.model.DexCurrency;
import com.arbitr.repository.Repository;
import com.datastax.dse.driver.api.core.cql.reactive.ReactiveRow;
import com.datastax.oss.driver.api.core.cql.AsyncResultSet;
import com.datastax.oss.quarkus.runtime.api.session.QuarkusCqlSession;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CassandraRepository implements Repository {

    @Inject
    QuarkusCqlSession session;

    private DexCurrency from(ReactiveRow reactiveRow) {
        return DexCurrency
                .builder()
                .name(reactiveRow.getString("name"))
                .currency(reactiveRow.getString("currency"))
                .currentValue(reactiveRow.getDouble("current_value"))
                .nextValue(reactiveRow.getDouble("next_value"))
                .absoluteCurrentValue(reactiveRow.getDouble("absolute_current_value"))
                .build();
    }

    @Override
    public void deleteAll() {
        session.executeReactive("truncate dex.dexes_actual");
    }

    public Multi<DexCurrency> findAll() {
        Multi<ReactiveRow> dexes = session.executeReactive("select name, currency, current_value, next_value, absolute_current_value from dex.dexes_actual");
        return dexes.onItem().transform(this::from);
    }

    @Override
    public Multi<DexCurrency> findAllByCurrency(String currency) {
        Multi<ReactiveRow> dexes = session.executeReactive(String.format("select name, currency, current_value, next_value, absolute_current_value from dex.dexes_actual where currency = '%s'", currency));
        return dexes.onItem().transform(this::from);
    }

    @Override
    public Multi<DexCurrency> findAllByAbsoluteValue(Double value) {
        Multi<ReactiveRow> dexes = session.executeReactive(String.format("select name, currency, current_value, next_value, absolute_current_value from dex.dexes_actual where absolute_current_value >= %f allow filtering", value));
        return dexes.onItem().transform(this::from);
    }


    @Override
    public Uni<Boolean> putDex(DexCurrency dexCurrency) {
        var insert = session.executeAsync(String.format("insert into dex.dexes_actual(currency, name, last_update_timestamp, current_value, next_value, absolute_current_value) values ('%s','%s', totimestamp(now()), %f, %f, %f)"
                , dexCurrency.getCurrency(), dexCurrency.getName(), dexCurrency.getCurrentValue(), dexCurrency.getNextValue(), Math.abs(dexCurrency.getCurrentValue())));
        return Uni.createFrom().completionStage(insert).map(AsyncResultSet::wasApplied);
    }



}
