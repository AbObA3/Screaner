package com.arbitr.parser;


import jakarta.enterprise.context.ApplicationScoped;
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
@ApplicationScoped
public class GateParser implements DexParser {
    @Override
    public Double getFundingRate(String string) {
        Pattern pattern = Pattern.compile("\"funding_rate\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1)) * 100.;
        } else {
            log.error("Не найдено");
            return 0.;
        }
    }



    @Override
    public Double getNextFundingRate(String string) {
        Pattern pattern = Pattern.compile("\"funding_rate_indicative\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1)) * 100.;
        } else {
            log.error("Не найдено");
            return 0.;
        }
    }

    @Override
    public String getFundingTime(String string) {
        Pattern pattern = Pattern.compile("\"funding_next_apply\"\\s*:\\s*([^,]+),");
        Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            Instant instant = Instant.ofEpochSecond(Long.parseLong(matcher.group(1)));
            return LocalDateTime.ofInstant(instant, ZoneId.of("Europe/Moscow")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        } else {
            log.error("Не найдено");
            return LocalDateTime.MIN.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        }
    }
}
