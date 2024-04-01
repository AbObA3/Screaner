package com.arbitr.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public enum SeparatorEnum {


    GATE("_","_"),
    MEXC("","_"),
    BITGET("",""),
    BYBIT("",""),
    HTX("","-"),
    KUCOIN("-",""),
    OKX("-","-"),
    LBANK("",""),
    BINGX("","-"); //TODO найти сепаратор для спота




    private String spotSeparator;
    private String fundingSeparator;
}
