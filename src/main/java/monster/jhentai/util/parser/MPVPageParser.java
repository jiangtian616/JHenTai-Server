package monster.jhentai.util.parser;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author JTMonster
 */
@Component
@Slf4j
public class MPVPageParser implements EHPageParser<List<String>> {

    @Override
    public List<String> parse(Response pageResponse) throws IOException {
        Document document = Jsoup.parse(pageResponse.body().string());

        Elements elements = document.select("#pane_thumbs > a > div[data-orghash]");
        if (elements.isEmpty()) {
            return Collections.emptyList();
        }

        return elements.stream()
                .map(element -> element.attr("data-orghash"))
                .filter(hash -> !StringUtils.isBlank(hash))
                .toList();
    }
} 