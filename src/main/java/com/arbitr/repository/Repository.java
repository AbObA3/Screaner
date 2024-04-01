package com.arbitr.repository;

import com.arbitr.model.DexCurrency;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface Repository {

    Multi<DexCurrency> findAllByCurrency(String currency);

    Multi<DexCurrency> findAllByAbsoluteValue(Double value);

    Multi<DexCurrency> findAll();

    Uni<Boolean> putDex(DexCurrency dexCurrency);

    void deleteAll();
}
