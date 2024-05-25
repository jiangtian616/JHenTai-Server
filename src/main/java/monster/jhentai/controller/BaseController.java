package monster.jhentai.controller;

import lombok.extern.slf4j.Slf4j;
import monster.jhentai.model.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
@RestController
@Slf4j
public class BaseController {
    @GetMapping("/")
    public String home() {
        log.info("BaseController.home");
        return "JHenTai Server Home Page";
    }

    @GetMapping("/alive")
    public Result<Void> alive() {
        log.info("BaseController.alive");
        return Result.success();
    }
}
