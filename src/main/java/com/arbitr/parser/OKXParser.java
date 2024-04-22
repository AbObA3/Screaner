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
public class OKXParser implements DexParser {

    private Integer timeInterval = 0;
    private Long time = 0L;

    @Override
    public Double getFundingRate(String string) {
        var pattern = Pattern.compile("\"fundingRate\":\\s*\"([^\"]+)\"");
        var matcher = pattern.matcher(string);

        setTime(string);
        setTimeInterval(string);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1)) * 100.;
        } else {
            log.error("Не найдено");
            return 0.;
        }
    }


    @Override
    public Double getNextFundingRate(String string) {
        var pattern = Pattern.compile("\"nextFundingRate\":\\s*\"([^\"]+)\"");
        var matcher = pattern.matcher(string);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1)) * 100.;
        } else {
            log.error("Не найдено");
            return 0.;
        }
    }

    private void setTimeInterval(String string) {
        var pattern = Pattern.compile("\"nextFundingTime\"\\s*:\\s*\"([^\"]+)\"");
        var matcher = pattern.matcher(string);

        if (matcher.find()) {
            var instant = Instant.ofEpochMilli(Long.parseLong(matcher.group(1)) - this.time);
            var hours = LocalDateTime.ofInstant(instant, ZoneId.of("GMT")).getHour();
            this.timeInterval = hours;
        } else {
            log.error("Не найдено");
            this.timeInterval = 0;
        }
    }

    private void setTime(String string) {
        var pattern = Pattern.compile("\"fundingTime\"\\s*:\\s*\"([^\"]+)\"");
        var matcher = pattern.matcher(string);

        if (matcher.find()) {
            var time = Long.parseLong(matcher.group(1));
            this.time = time;
        } else {
            log.error("Не найдено");
            this.time = 0L;
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
}

