package monster.jhentai.service;

import lombok.extern.slf4j.Slf4j;
import monster.jhentai.config.ApiKeyConfig;
import monster.jhentai.util.HmacUtil;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class ApiKeyService {

    @Autowired
    private ApiKeyConfig apiKeyConfig;

    public static final long EXPIRATION_SECONDS = 5 * 60;


    public boolean validateSignature(String appId, long timestamp, String nonce, String signature) {
        try {
            if (MapUtils.isEmpty(apiKeyConfig.getAppSecrets())) {
                log.error("App secrets are not configured");
                return false;
            }

            String secret = apiKeyConfig.getAppSecrets().get(appId);
            if (secret == null) {
                log.warn("API key not found");
                return false;
            }

            long now = Instant.now().getEpochSecond();
            if (Math.abs(now - timestamp) > EXPIRATION_SECONDS) {
                log.warn("Timestamp expired: {}", timestamp);
                return false;
            }

            String stringToSign = appId + "-" + timestamp + "-" + nonce;
            String expectedSignature = HmacUtil.hmacSha256(stringToSign, secret);

            if (!expectedSignature.equals(signature)) {
                log.warn("Signature mismatch: expected {}, got {}", expectedSignature, signature);
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("Validate signature failed", e);
            return false;
        }
    }
} 