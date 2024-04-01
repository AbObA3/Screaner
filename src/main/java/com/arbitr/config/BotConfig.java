package com.arbitr.config;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Data;
import org.eclipse.microprofile.config.inject.ConfigProperty;


@Data
@ApplicationScoped
public class BotConfig {

    @ConfigProperty(name = "bot.username")
    String botUsername;

    @ConfigProperty(name = "bot.token")
    String token;
}