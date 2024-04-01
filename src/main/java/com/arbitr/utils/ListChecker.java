package com.arbitr.utils;

import com.arbitr.model.DexCurrency;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public final class ListChecker {

    public Boolean checkList(List<DexCurrency> dexCurrencyList, Double value) {
        return dexCurrencyList.stream().anyMatch(dexCurrency -> dexCurrency.getAbsoluteCurrentValue() > value);
    }
}
