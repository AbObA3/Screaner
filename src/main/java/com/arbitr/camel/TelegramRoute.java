package com.arbitr.camel;

import com.arbitr.config.BotConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class TelegramRoute extends RouteBuilder {

    @Inject
    Telegram telegram;

    @Inject
    BotConfig botConfig;

    @Override
    public void configure() {
        from(String.format("telegram:bots?authorizationToken=%s", botConfig.getToken()))
                .bean(telegram)
                .to(String.format("telegram:bots?authorizationToken=%s", botConfig.getToken()));
    }
}
