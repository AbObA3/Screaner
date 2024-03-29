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

import static com.arbitr.utils.Names.KUCOIN;
import static com.arbitr.utils.Names.USDT;

@Getter
@Setter
@Log4j2
@ApplicationScoped
public class KuCoin implements Dex {

    private static final String BASE_PATH = "https://openapi-v2.kucoin.com";
    private static final String FUNDING_BASE_PATH = "https://api-futures.kucoin.com";
    private static final String FUNDING = "/api/v1/funding-rate/%sM/current";


    @Override
    public String getDexName() {
        return KUCOIN;
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
                    .setUri(String.format(FUNDING_BASE_PATH + FUNDING, currency + SeparatorEnum.KUCOIN.getFundingSeparator() + USDT))
                    .build();
            var httpResponse = client.execute(request);
            result = EntityUtils.toString(httpResponse.getEntity());
            if (result.contains("funding rate is not supported")) {
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
