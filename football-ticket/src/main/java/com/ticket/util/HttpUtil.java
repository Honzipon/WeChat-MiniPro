package com.ticket.util;

import org.apache.commons.lang3.StringUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpUtil {
    public static String get(String urlStr, Map<String, String> params) {
        try {
            if (params != null && !params.isEmpty()) {
                StringBuilder sb = new StringBuilder(urlStr);
                sb.append("?");
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    sb.append(entry.getKey()).append("=")
                            .append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
                }
                urlStr = sb.substring(0, sb.length() - 1);
            }
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}