package com.arbitr.parser;

import com.arbitr.dex.Dex;
import com.arbitr.utils.FundingRate;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ConcurrentHashMap;

import static com.arbitr.utils.Names.*;

@ApplicationScoped
@Log4j2
public class ValueParser {

    @Inject
    BitGetParser bitGetParser;

    @Inject
    ByBitParser byBitParser;

    @Inject
    GateParser gateParser;

    @Inject
    HTXParser htxParser;

    @Inject
    KuCoinParser kuCoinParser;

    @Inject
    OKXParser okxParser;

    @Inject
    MexcParser mexcParser;

    @Inject
    BingXParser bingXParser;

    @Inject
    LBankParser lBankParser;

    ConcurrentHashMap<String, DexParser> parsers;

    @PostConstruct
    void init() {
        parsers = new ConcurrentHashMap<>();
        parsers.put(BITGET, bitGetParser);
        parsers.put(BYBIT, byBitParser);
        parsers.put(GATE, gateParser);
        parsers.put(HTX, htxParser);
        parsers.put(KUCOIN, kuCoinParser);
        parsers.put(MEXC, mexcParser);
        parsers.put(OKX, okxParser);
        parsers.put(BINGX, bingXParser);
        parsers.put(LBANK, lBankParser);
    }

    public FundingRate parseFundingData(Dex dex, String currency) {

        try {
            var responseData = dex.getFundingData(currency);
            var responseInterval = dex.getFundingTime(currency);

            var parser = parsers.get(dex.getDexName());

            return FundingRate.builder()
                    .currentValue(parser.getFundingRate(responseData))
                    .nextValue(parser.getNextFundingRate(responseData))
                    .fundingInterval(parser.getFundingInterval(responseInterval))
                    .nextRateTimestamp(parser.getFundingNextTime(responseInterval))
                    .build();
        } catch ( Exception ex) {
            log.error(ex.getMessage());
            return null;
        }

    }

}