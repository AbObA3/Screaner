package com.arbitr.parser;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@NoArgsConstructor
@Log4j2
@ApplicationScoped
public class BitGetParser implements DexParser {

    @Override
    public Double getFundingRate(String string) {

        Pattern pattern = Pattern.compile("\"fundingRate\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1)) * 100.;
        } else {
            log.error("Не найдено");
            return 0.;
        }
    }

}
