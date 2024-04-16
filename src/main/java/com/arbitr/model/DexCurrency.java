package com.arbitr.model;


import lombok.*;
import lombok.experimental.FieldDefaults;

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

    String fundingTime;

    @Override
    public String toString() {
        return  "Биржа: " + name + "\n" +
                "Валюта: " + currency + "\n" +
                "Текущее значение: " + currentValue + "\n" +
                "Следующее значение: " + nextValue + "\n" +
                "Расчет финансирования: " + fundingTime + "\n" +
                "-------------------------------\n";
    }
}
