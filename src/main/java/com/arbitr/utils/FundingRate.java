package com.arbitr.utils;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FundingRate {

    Double currentValue;

    Double nextValue;

    Integer fundingInterval;

    Long nextRateTimestamp;

}
