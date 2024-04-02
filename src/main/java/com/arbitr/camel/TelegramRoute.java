package com.arbitr.camel;

import com.arbitr.config.BotConfig;
import com.arbitr.config.CoinCollection;
import com.arbitr.model.DexCurrency;
import com.arbitr.repository.Repository;
import com.arbitr.utils.Builder;
import com.arbitr.utils.ListChecker;
import com.arbitr.utils.TelegramRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.model.IncomingMessage;
import org.apache.camel.component.telegram.model.InlineKeyboardButton;
import org.apache.camel.component.telegram.model.OutgoingTextMessage;
import org.apache.camel.component.telegram.model.ReplyKeyboardMarkup;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@ApplicationScoped
public class TelegramRoute extends RouteBuilder {

    @Inject
    Repository repository;

    @Inject
    TelegramRequest telegramRequest;

    @Inject
    CoinCollection coinCollection;

    @Inject
    BotConfig botConfig;

    Boolean isRequestValue = Boolean.FALSE;

    Boolean isRequestCurrency = Boolean.FALSE;

    Boolean isRequestFile = Boolean.FALSE;

    @Override
    public void configure() {
        from(String.format("telegram:bots?authorizationToken=%s", botConfig.getToken()))
                .process(exchange -> {
                    IncomingMessage message = (IncomingMessage) exchange.getMessage().getBody();
                    OutgoingTextMessage msg = new OutgoingTextMessage();
                    try {
                        InlineKeyboardButton buttonValue = InlineKeyboardButton.builder()
                                .text("Показать по значению ставки").build();
                        InlineKeyboardButton buttonCurrency = InlineKeyboardButton.builder()
                                .text("Показать по монете").build();
                        InlineKeyboardButton buttonFile = InlineKeyboardButton.builder()
                                .text("Загрузить файл").build();
                        ReplyKeyboardMarkup replyMarkup = ReplyKeyboardMarkup.builder()
                                .keyboard()
                                .addRow(Collections.singletonList(buttonValue))
                                .addRow(Collections.singletonList(buttonCurrency))
                                .addRow(Collections.singletonList(buttonFile))
                                .close()
                                .oneTimeKeyboard(true)
                                .build();
                        msg.setReplyMarkup(replyMarkup);
                        if ("Загрузить файл".equals(message.getText())) {
                            replyMarkup = ReplyKeyboardMarkup.builder()
                                    .removeKeyboard(true)
                                    .build();
                            msg.setReplyMarkup(replyMarkup);
                            msg.setText("Загружай");
                            isRequestFile = Boolean.TRUE;
                            Builder.clean();
                            exchange.getIn().setBody(msg);
                        } else if (message.getDocument() != null && isRequestFile) {
                            var filePath = telegramRequest.getGetFilePath(message.getDocument().getFileId());
                            var result = telegramRequest.getGetFile(filePath);
                            var coinArr = result.split(",\r\n");
                            coinCollection.clearCollection();
                            Arrays.stream(coinArr)
                                    .forEach(string ->
                                            coinCollection.addToMap(string.substring(0, string.indexOf(" -")), Arrays.stream(string.substring(string.indexOf("- ") + 2).split(","))
                                                    .collect(Collectors.toList())));
                            isRequestFile = Boolean.FALSE;
                            msg.setText("Загружен");
                            exchange.getIn().setBody(msg);
                        } else if ("Показать по монете".equals(message.getText())) {
                            replyMarkup = ReplyKeyboardMarkup.builder()
                                    .removeKeyboard(true)
                                    .build();
                            msg.setReplyMarkup(replyMarkup);
                            msg.setText("Введи название монеты");
                            isRequestCurrency = Boolean.TRUE;
                            Builder.clean();
                            exchange.getIn().setBody(msg);
                        } else if (coinCollection.getList().stream().anyMatch(coin -> coin.equals(message.getText())) && isRequestCurrency) {
                            Builder.appendList(repository
                                    .findAllByCurrency(message.getText())
                                    .subscribe().asStream().toList());
                            isRequestCurrency = Boolean.FALSE;
                            if (!Builder.isEmpty()) msg.setText(Builder.getStringBuilder());
                            else msg.setText("Пусто");
                            exchange.getIn().setBody(msg);
                        } else if ("Показать по значению ставки".equals(message.getText())) {
                            replyMarkup = ReplyKeyboardMarkup.builder()
                                    .removeKeyboard(true)
                                    .build();
                            msg.setReplyMarkup(replyMarkup);
                            msg.setText("Введи значение ставки");
                            isRequestValue = Boolean.TRUE;
                            Builder.clean();
                            exchange.getIn().setBody(msg);
                        } else if ("Закончить".equals(message.getText())) {
                            isRequestValue = Boolean.FALSE;
                            Builder.clean();
                            msg.setText("Закончили");
                            exchange.getIn().setBody(msg);
                        } else if (NumberUtils.isCreatable(message.getText()) && isRequestValue) {
                            repository
                                    .findAll()
                                    .subscribe().asStream()
                                    .collect(Collectors.groupingBy(DexCurrency::getCurrency)).values()
                                    .stream().filter(dexList -> ListChecker.checkList(dexList, Double.valueOf(message.getText())))
                                    .forEach(Builder::appendList);
                            if (!Builder.isEmpty()) {
                                InlineKeyboardButton nextButton = InlineKeyboardButton.builder()
                                        .text("Дальше").build();
                                InlineKeyboardButton endButton = InlineKeyboardButton.builder()
                                        .text("Закончить").build();
                                replyMarkup = ReplyKeyboardMarkup.builder()
                                        .keyboard()
                                        .addRow(Collections.singletonList(buttonValue))
                                        .addRow(Collections.singletonList(nextButton))
                                        .addRow(Collections.singletonList(endButton))
                                        .close()
                                        .oneTimeKeyboard(true)
                                        .build();
                                msg.setReplyMarkup(replyMarkup);
                                msg.setText(Builder.getStringBuilder());
                            } else {
                                msg.setText("Пусто");
                            }
                            exchange.getIn().setBody(msg);
                        } else if ("Дальше".equals(message.getText()) && isRequestValue) {
                            if (!Builder.isEmpty()) {
                                InlineKeyboardButton nextButton = InlineKeyboardButton.builder()
                                        .text("Дальше").build();
                                InlineKeyboardButton endButton = InlineKeyboardButton.builder()
                                        .text("Закончить").build();
                                replyMarkup = ReplyKeyboardMarkup.builder()
                                        .keyboard()
                                        .addRow(Collections.singletonList(buttonValue))
                                        .addRow(Collections.singletonList(nextButton))
                                        .addRow(Collections.singletonList(endButton))
                                        .close()
                                        .oneTimeKeyboard(true)
                                        .build();
                                msg.setReplyMarkup(replyMarkup);
                                msg.setText(Builder.getStringBuilder());
                            } else {
                                msg.setText("Пусто");
                            }
                            exchange.getIn().setBody(msg);
                        } else {
                            msg.setText("Не понял");
                            exchange.getIn().setBody(msg);
                        }
                    } catch (Exception e) {
                        msg.setText(e.getMessage());
                        exchange.getIn().setBody(msg);
                    }
                })
                .to(String.format("telegram:bots?authorizationToken=%s", botConfig.getToken()));
    }
}
