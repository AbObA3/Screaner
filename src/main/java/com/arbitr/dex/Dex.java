package com.arbitr.dex;


import org.apache.logging.log4j.util.Strings;

public interface Dex {

    String getDexName();


    Double getBalance();

    String getFundingData(String currency);

    default String getFundingTime(String currency) {
        return Strings.EMPTY;
    }
}
