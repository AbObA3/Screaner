package com.arbitr.dex;


import com.arbitr.enums.SeparatorEnum;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;

import static com.arbitr.utils.Names.BITGET;
import static com.arbitr.utils.Names.USDT;


@Getter
@Setter
@Log4j2
@ApplicationScoped
public class BitGet implements Dex {

    private static final String BASE_PATH = "https://api.bitget.com";
    private static final String FUNDING = "/api/mix/v1/market/current-fundRate?symbol=%S_UMCBL";

    @Override
    public String getDexName() {
        return BITGET;
    }

    @Override
    public Double getBalance() {
        return null;
    }

    @Override
    public String getFundingData(String currency) {
        var result = Strings.EMPTY;
        try (var client = HttpClients.custom().build()) {
            var request = RequestBuilder.get()
                    .setUri(String.format(BASE_PATH + FUNDING, currency + SeparatorEnum.BITGET.getFundingSeparator() + USDT))
                    .build();
            var httpResponse = client.execute(request);
            result = EntityUtils.toString(httpResponse.getEntity());
            if (result.contains("does not exist") || result.contains("\"data\":null")) {
                throw new Exception("400");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            if (e.getMessage().contains("400")) {
                return null;
            }
        }
        return result;
    }
}
