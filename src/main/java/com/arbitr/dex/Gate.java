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

import static com.arbitr.utils.Names.GATE;
import static com.arbitr.utils.Names.USDT;


@Getter
@Setter
@Log4j2
@ApplicationScoped
public class Gate implements Dex {

    private static final String BASE_PATH = "https://api.gateio.ws/api/v4";
    private static final String FUNDING = "/futures/usdt/tickers?contract=%s";


    @Override
    public Double getBalance() {
        return null;
    }
    public String getDexName() {
        return GATE;
    }

    @Override
    public String getFundingData(String currency) {
        var result = Strings.EMPTY;
        try (var client = HttpClients.custom().build()) {
            var request = RequestBuilder.get()
                    .setUri(String.format(BASE_PATH + FUNDING, currency + SeparatorEnum.GATE.getFundingSeparator() + USDT))
                    .build();
            var httpResponse = client.execute(request);
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return result;
    }
}
