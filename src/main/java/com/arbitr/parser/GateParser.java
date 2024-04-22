package com.arbitr.parser;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@Log4j2
@Singleton
public class GateParser implements DexParser {
    @Override
    public Double getFundingRate(String string) {
        var pattern = Pattern.compile("\"funding_rate\"\\s*:\\s*\"([^\"]+)\"");
        var matcher = pattern.matcher(string);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1)) * 100.;
        } else {
            log.error("Не найдено");
            return 0.;
        }
    }



    @Override
    public Double getNextFundingRate(String string) {
        var pattern = Pattern.compile("\"funding_rate_indicative\"\\s*:\\s*\"([^\"]+)\"");
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
        var pattern = Pattern.compile("\"funding_next_apply\"\\s*:\\s*([^,]+),");
        var matcher = pattern.matcher(string);

        if (matcher.find()) {
            var time = Long.parseLong(matcher.group(1)) * 1000L;
            return time;
        } else {
            log.error("Не найдено");
            return 0L;
        }
    }

    @Override
    public Integer getFundingInterval(String string) {
        var pattern = Pattern.compile("\"funding_interval\"\\s*:\\s*([^,]+),");
        var matcher = pattern.matcher(string);

        if (matcher.find()) {
            var instant = Instant.ofEpochSecond(Long.parseLong(matcher.group(1)));
            var hours = LocalDateTime.ofInstant(instant, ZoneId.of("GMT")).getHour();
            return hours;
        } else {
            log.error("Не найдено");
            return 0;
        }
    }
}
