package com.arbitr.service;

import com.arbitr.config.CoinCollection;
import com.arbitr.dex.*;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.vertx.core.impl.ConcurrentHashSet;
import jakarta.inject.Inject;
import com.arbitr.model.DexCurrency;
import com.arbitr.parser.ValueParser;
import io.quarkus.scheduler.Scheduled;
import com.arbitr.repository.Repository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

@ApplicationScoped
@Log4j2
public class DexRequestService {
    ConcurrentHashSet<Dex> dexes;

    @Inject
    CoinCollection coinCollection;

    @Inject
    BingX bingX;

    @Inject
    OKX okx;

    @Inject
    Mexc mexc;

    @Inject
    KuCoin kuCoin;

    @Inject
    HTX htx;

    @Inject
    Gate gate;

    @Inject
    ByBit byBit;

    @Inject
    BitGet bitGet;

    @Inject
    LBank lBank;

    @Inject
    ValueParser valueParser;

    @Inject
    Repository repository;


    @PostConstruct
    void init() {
        dexes = new ConcurrentHashSet<>();
        dexes.addAll(Arrays.asList(gate, bingX, bitGet, byBit, mexc, okx, htx, kuCoin, lBank));
    }

    @Scheduled(every = "1s")
    @RunOnVirtualThread
    void checkDexes() {
        try {
            if (!coinCollection.getHashMap().isEmpty()) {
                var currency = coinCollection.getQueue().poll();
                dexes.parallelStream().filter(dex -> coinCollection.getHashMap().get(currency).contains(dex.getDexName()))
                        .forEach(dex -> {
                            var fundingRate = valueParser.parseFundingData(dex, currency);
                            repository.putDex(
                                    DexCurrency.builder()
                                            .currency(currency)
                                            .name(dex.getDexName())
                                            .currentValue(fundingRate.getCurrentValue())
                                            .nextValue(fundingRate.getNextValue())
                                            .fundingInterval(fundingRate.getFundingInterval())
                                            .nextRateTimestamp(fundingRate.getNextRateTimestamp())
                                            .build());

                        });
                coinCollection.getQueue().add(currency);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
