package com.orimwulong.gamefinder.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.CharStreams;

public final class HttpsHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpsHelper.class);

    private HttpsHelper() {
        throw new java.lang.UnsupportedOperationException("HttpsHelper is a utility class and cannot be instantiated");
    }

    public static String getHttpsContent(String httpsUrl) {
        int okResponseCode = 200;
        String content = null;
        InputStream is = null;
        Reader reader = null;
        try {
            URL url = new URL(httpsUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();
            if (responseCode == okResponseCode) {
                is = conn.getInputStream();
                reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                content = CharStreams.toString(reader);
                reader.close();
                is.close();
            } else {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Content retrieval failed. [Code=" + responseCode + "][Message=" + responseMessage + "]");
                }
            }
            conn.disconnect();
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Exception while getting HTTPS content", e);
            }
        } finally {
            try {
                reader.close();
                is.close();
            } catch (IOException e) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Exception while closing streams", e);
                }
            }
        }

        return content;
    }

}
