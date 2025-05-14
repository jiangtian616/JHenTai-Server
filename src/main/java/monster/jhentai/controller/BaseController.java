package monster.jhentai.controller;

import monster.jhentai.annotation.LogAndCatch;
import monster.jhentai.model.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
@RestController
@LogAndCatch
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
