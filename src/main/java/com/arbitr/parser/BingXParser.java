package com.arbitr.parser;

import jakarta.enterprise.context.ApplicationScoped;
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
public class BingXParser implements DexParser {

    private Long time = 0L;

    @Override
    public Double getFundingRate(String string) {
        var pattern = Pattern.compile("\"lastFundingRate\"\\s*:\\s*\"([^\"]+)\"");
        var matcher = pattern.matcher(string);

        setTime(string);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1)) * 100.;
        } else {
            log.error("Не найдено");
            return 0.;
        }
    }

    private void setTime(String string) {
        var pattern = Pattern.compile("\"nextFundingTime\"\\s*:\\s*([^}]+)}");
        var matcher = pattern.matcher(string);

        if (matcher.find()) {
            this.time = Long.parseLong(matcher.group(1));
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
        var pattern = Pattern.compile("\"fundingTime\"\\s*:\\s*([^,]+),");
        var matcher = pattern.matcher(string);

        if (matcher.find() && this.time != 0L) {
            var instant = Instant.ofEpochMilli(this.time - Long.parseLong(matcher.group(1)));
            var hours = LocalDateTime.ofInstant(instant, ZoneId.of("GMT")).getHour();
            return hours;
        } else {
            log.error("Не найдено");
            return 0;
        }
    }
}
