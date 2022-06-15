package com.orimwulong.gamefinder.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.CharStreams;

public class HttpsApi {

    private static final Logger logger = LoggerFactory.getLogger(HttpsApi.class);

    public static final String getHttpsContent(String httpsUrl) {
        String content = null;
        try {
            URL url = new URL(httpsUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                Reader reader = new InputStreamReader(is);
                content = CharStreams.toString(reader);
                reader.close();
            } else {
                logger.error("Content retrieval failed. [Code=" + responseCode + "][Message=" + responseMessage + "]");
            }
            conn.disconnect();
        } catch (IOException e) {
            logger.error("Exception while getting HTTPS content", e);
        }

        return content;
    }

}
