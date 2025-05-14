package monster.jhentai.util.parser;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author JTMonster
 * @date 2025/5/14
 */
@Component
@Slf4j
public class LoginPageParser implements EHPageParser<String> {

    @Override
    public String parse(Response pageResponse) throws IOException {
        Document document = Jsoup.parse(pageResponse.body().string());

        if (!document.getElementsByClass("pcen").isEmpty()) {
            return null;
        }

        Elements link = document.select(".home > b > a");
        if (link.isEmpty()) {
            return null;
        }

        return link.text();
    }
} 