package com.arbitr.parser;


public interface DexParser {

    Double getFundingRate(String string);
    default Double getNextFundingRate(String string) {
        return 0.;
    }

    String getFundingTime(String string);
}
