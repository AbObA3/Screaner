package com.arbitr.parser;

import com.arbitr.dex.Dex;
import com.arbitr.utils.FundingRate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static com.arbitr.utils.Names.*;

@ApplicationScoped
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


    public FundingRate parseFundingData(Dex dex, String currency) {

        var response  = dex.getFundingData(currency);

        switch (dex.getDexName()) {
            case BITGET -> {
                var time = dex.getFundingTime(currency);
                return FundingRate.builder()
                                  .currentValue(bitGetParser.getFundingRate(response))
                                  .nextValue(bitGetParser.getNextFundingRate(response))
                                  .fundingTime(bitGetParser.getFundingTime(time))
                                  .build();
            }
            case BYBIT -> {
                return FundingRate.builder()
                                  .currentValue(byBitParser.getFundingRate(response))
                                  .nextValue(byBitParser.getNextFundingRate(response))
                                  .fundingTime(byBitParser.getFundingTime(response))
                                  .build();
            }
            case GATE -> {
                var time = dex.getFundingTime(currency);
                return FundingRate.builder()
                                  .currentValue(gateParser.getFundingRate(response))
                                  .nextValue(gateParser.getNextFundingRate(response))
                                  .fundingTime(gateParser.getFundingTime(time))
                                  .build();
            }
            case HTX -> {
                return FundingRate.builder()
                                  .currentValue(htxParser.getFundingRate(response))
                                  .nextValue(htxParser.getNextFundingRate(response))
                                  .fundingTime(htxParser.getFundingTime(response))
                                  .build();
            }
            case KUCOIN -> {
                return FundingRate.builder()
                                  .currentValue(kuCoinParser.getFundingRate(response))
                                  .nextValue(kuCoinParser.getNextFundingRate(response))
                                  .fundingTime(kuCoinParser.getFundingTime(response))
                                  .build();
            }
            case MEXC -> {
                return FundingRate.builder()
                                  .currentValue(mexcParser.getFundingRate(response))
                                  .nextValue(mexcParser.getNextFundingRate(response))
                                  .fundingTime(mexcParser.getFundingTime(response))
                                  .build();
            }
            case OKX -> {
                return FundingRate.builder()
                                  .currentValue(okxParser.getFundingRate(response))
                                  .nextValue(okxParser.getNextFundingRate(response))
                                  .fundingTime(okxParser.getFundingTime(response))
                                  .build();
            }
            case BINGX -> {
                return FundingRate.builder()
                                  .currentValue(bingXParser.getFundingRate(response))
                                  .nextValue(bingXParser.getNextFundingRate(response))
                                  .fundingTime(bingXParser.getFundingTime(response))
                                  .build();
            }
            case LBANK -> {
                return FundingRate.builder()
                                  .currentValue(lBankParser.getFundingRate(response))
                                  .nextValue(lBankParser.getNextFundingRate(response))
                                  .fundingTime(lBankParser.getFundingTime(response))
                                  .build();
            }
            default -> {
                return null;
            }
        }

    }

}
