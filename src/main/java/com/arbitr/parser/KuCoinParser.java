package com.arbitr.parser;


import jakarta.inject.Singleton;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Pattern;

@NoArgsConstructor
@Log4j2
@Singleton
public class KuCoinParser implements DexParser {

    private Integer timeInterval = 0;
    private Long time = 0L;

    @Override
    public Double getFundingRate(String string) {
        var pattern = Pattern.compile("\"value\"\\s*:\\s*([^,]+),");
        var matcher = pattern.matcher(string);

        setTimeInterval(string);
        setTime(string);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1)) * 100.;
        } else {
            log.error("Не найдено");
            return 0.;
        }
    }


    @Override
    public Double getNextFundingRate(String string) {
        var pattern = Pattern.compile("\"predictedValue\"\\s*:\\s*([^}]+)}");
        var matcher = pattern.matcher(string);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1)) * 100.;
        } else {
            log.error("Не найдено");
            return 0.;
        }
    }

    @Override
    public Long getFundingNextTime(String string) {
        return this.time;
    }

    @Override
    public Integer getFundingInterval(String string) {
        return this.timeInterval;
    }


    private void setTimeInterval(String string) {
        var pattern = Pattern.compile("\"granularity\"\\s*:\\s*([^,]+),");
        var matcher = pattern.matcher(string);

        if (matcher.find()) {
            var instant = Instant.ofEpochMilli(Long.parseLong(matcher.group(1)));
            var hours = LocalDateTime.ofInstant(instant, ZoneId.of("GMT")).getHour();
            this.timeInterval = hours;
        } else {
            log.error("Не найдено");
            this.timeInterval = 0;
        }
    }

    private void setTime(String string) {
        var pattern = Pattern.compile("\"timePoint\"\\s*:\\s*([^,]+),");
        var matcher = pattern.matcher(string);

        if (matcher.find()) {
            var time = (this.timeInterval * 3600000L) + Long.parseLong(matcher.group(1));
            this.time = time;
        } else {
            log.error("Не найдено");
            this.time = 0L;
        }
    }
}
