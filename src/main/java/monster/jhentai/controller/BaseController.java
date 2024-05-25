package monster.jhentai.controller;

import monster.jhentai.model.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
@RestController
public class BaseController {
    @GetMapping("/")
    public String home() {
        return "JHenTai Server Home Page";
    }

    @GetMapping("/alive")
    public Result<Void> alive() {
        return Result.success();
    }
}
