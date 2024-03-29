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

import static com.arbitr.utils.Names.OKX;
import static com.arbitr.utils.Names.USDT;


@Getter
@Setter
@Log4j2
@ApplicationScoped
public class OKX implements Dex {

    private static final String BASE_PATH = "https://www.okx.com";
    private static final String FUNDING = "/api/v5/public/funding-rate";
    private static final String SWAP = "SWAP";

    @Override
    public String getDexName() {
        return OKX;
    }


    @Override
    public String getFundingData(String currency) {
        var result = Strings.EMPTY;
        try (var client = HttpClients.custom().build()) {
            var request = RequestBuilder.get()
                    .setUri(String.format(BASE_PATH + FUNDING + "?instId=%s"
                            , currency + SeparatorEnum.OKX.getSpotSeparator() + USDT + SeparatorEnum.OKX.getSpotSeparator() + SWAP))
                    .build();
            var httpResponse = client.execute(request);
            result = EntityUtils.toString(httpResponse.getEntity());
            if (result.contains("Instrument ID doesn't exist")) {
                throw new Exception("400");
            }
        } catch (Exception e) {
            if (e.getMessage().equals("400")) {
                return null;
            }
            log.error(e.getMessage());
        }
        return result;
    }
    @Override
    public Double getBalance() {
        return null;
    }
}
