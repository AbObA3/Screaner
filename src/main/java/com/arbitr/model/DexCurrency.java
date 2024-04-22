package com.arbitr.model;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DexCurrency {

    String name;

    String currency;

    Double currentValue;

    Double nextValue;

    Double absoluteCurrentValue;

    Integer fundingInterval;

    Long nextRateTimestamp;
    
    @Override
    public String toString() {
        Instant instant = Instant.ofEpochMilli(nextRateTimestamp.equals(0L) ? 0 : nextRateTimestamp - System.currentTimeMillis());
        var ldt = LocalDateTime.ofInstant(instant, ZoneId.of("GMT")).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return  "Биржа: " + name + "\n" +
                "Валюта: " + currency + "\n" +
                "Текущее значение: " + currentValue + "\n" +
                "Следующее значение: " + nextValue + "\n" +
                "Период: " + fundingInterval + " часа(-ов)\n" +
                "До следующей выплаты: " + ldt + " часа(-ов)\n" +
                "-------------------------------\n";
    }
}
