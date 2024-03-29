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

import static com.arbitr.utils.Names.LBANK;
import static com.arbitr.utils.Names.USDT;

@Getter
@Setter
@Log4j2
@ApplicationScoped
public class LBank implements Dex {

    private static final String FUNDING_BASE_PATH = "https://lbkperp.lbank.com";
    private static final String FUNDING = "/cfd/openApi/v1/pub/marketData?productGroup=SwapU";

    @Override
    public String getDexName() {
        return LBANK;
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
                    .setUri(FUNDING_BASE_PATH + FUNDING)
                    .build();
            var httpResponse = client.execute(request);
            result = EntityUtils.toString(httpResponse.getEntity());
            result = result.substring(result.indexOf(currency + SeparatorEnum.LBANK.getFundingSeparator() + USDT));
            result = result.substring(0, result.indexOf("}"));

            if (result.contains("success")) {
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
