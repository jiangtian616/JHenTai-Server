package monster.jhentai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author JTMonster
 * @date 2025/5/15
 */
@Service
@Slf4j
public class NonceService {

    // Use redis for cluster environments
    private final Map<String, Long> nonceMap = new ConcurrentHashMap<>();

    public boolean isValidNonce(String appId, String nonce, long timestamp) {
        String key = appId + ":" + nonce;
        if (nonceMap.containsKey(key)) {
            return false;
        }

        nonceMap.put(key, timestamp);

        return true;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES, initialDelay = 1)
    public void cleanup() {
        log.info("Cleaning up nonces, before size:{}", nonceMap.size());

        long currentTime = System.currentTimeMillis();
        nonceMap.entrySet().removeIf(entry -> currentTime - entry.getValue() > ApiKeyService.EXPIRATION_SECONDS * 1000);

        log.info("Cleaning up nonces, after size:{}", nonceMap.size());
    }
}
