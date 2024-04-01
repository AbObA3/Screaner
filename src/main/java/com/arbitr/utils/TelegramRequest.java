package com.arbitr.utils;

import com.arbitr.config.BotConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@ApplicationScoped
public final class TelegramRequest {

    static final String GET_FILE_PATH_QUERY = "https://api.telegram.org/bot%s/getFile?file_id=%s";
    static final String GET_FILE = "https://api.telegram.org/file/bot%s/%s";

    @Inject
    BotConfig botConfig;


    public String getGetFilePath(String fileId) {
        try (var client = HttpClients.custom().build()) {
            var request = RequestBuilder.get()
                    .setUri(String.format(GET_FILE_PATH_QUERY, botConfig.getToken(), fileId))
                    .build();
            var httpResponse = client.execute(request);
            var result = EntityUtils.toString(httpResponse.getEntity());
            if (result.contains("error_code")) {
                Pattern pattern = Pattern.compile("\"error_code\"\\s*:\\s*([^,]+),");
                Matcher matcher = pattern.matcher(result);
                if (matcher.find()) {
                    throw new Exception(matcher.group(1));
                }

            }
            Pattern pattern = Pattern.compile("\"file_path\"\\s*:\\s*\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(result);
            if (matcher.find()) {
                return matcher.group(1);
            } else {
                throw new Exception("Не найдено");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public String getGetFile(String filePath) {
        try (var client = HttpClients.custom().build()) {
            var request = RequestBuilder.get()
                    .setUri(String.format(GET_FILE, botConfig.getToken(), filePath))
                    .build();
            var httpResponse = client.execute(request);
            var result = EntityUtils.toString(httpResponse.getEntity());
            if (result.contains("error_code")) {
                Pattern pattern = Pattern.compile("\"error_code\"\\s*:\\s*([^,]+),");
                Matcher matcher = pattern.matcher(result);
                if (matcher.find()) {
                    throw new Exception(matcher.group(1));
                }
            }
            return result;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }


}
