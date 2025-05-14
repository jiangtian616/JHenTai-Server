package monster.jhentai.service;

import lombok.extern.slf4j.Slf4j;
import monster.jhentai.exception.HTTPRequestException;
import monster.jhentai.util.parser.EHPageParser;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author JTMonster
 * @date 2025/5/14
 */
@Service
@Slf4j
public class HTTPService {

    private static final int DEFAULT_TIMEOUT_MS = 3000;

    public <T> T get(String url, Map<String, String> headers, Map<String, String> queryParams,
                     EHPageParser<T> parser) throws IOException {
        return get(url, headers, queryParams, DEFAULT_TIMEOUT_MS, parser);
    }

    public <T> T get(String url, Map<String, String> headers, Map<String, String> queryParams,
                     int timeoutMs, EHPageParser<T> parser) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();

        HttpUrl.Builder urlBuilder = HttpUrl.get(url).newBuilder();
        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        Request.Builder requestBuilder = new Request.Builder().url(urlBuilder.build());

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.header(entry.getKey(), entry.getValue());
            }
        }

        Request request = requestBuilder.build();

        Call call = client.newCall(request);
        call.timeout().timeout(timeoutMs, TimeUnit.MILLISECONDS);

        try (Response response = call.execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                log.error("HTTP request failed, url: {}, response: {}", url, response);
                throw new HTTPRequestException("HTTP request failed");
            }

            return parser.parse(response);
        }
    }
}
