package monster.jhentai.util.parser;

import okhttp3.Response;

import java.io.IOException;

/**
 * @author JTMonster
 * @date 2025/5/14
 */
public interface EHPageParser<T> {

    T parse(Response pageResponse) throws IOException;
}
