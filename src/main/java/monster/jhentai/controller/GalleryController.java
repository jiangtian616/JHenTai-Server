package monster.jhentai.controller;

import monster.jhentai.annotation.LogAndCatch;
import monster.jhentai.annotation.LoginRequired;
import monster.jhentai.biz.EHBiz;
import monster.jhentai.model.request.FetchImageHashRequest;
import monster.jhentai.model.response.FetchImageHashResponse;
import monster.jhentai.model.response.Result;
import monster.jhentai.util.parser.MPVPageParser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

/**
 * @author JTMonster
 * @date 2025/5/14
 */
@RestController
@RequestMapping("/api/gallery")
@LogAndCatch
public class GalleryController {

    @Autowired
    private EHBiz ehBiz;


    @LoginRequired
    @GetMapping("/fetchImageHash")
    public Result<FetchImageHashResponse> fetchImageHash(@Valid FetchImageHashRequest request) throws IOException {
        List<String> hashes = ehBiz.requestMPVPage(request.getGid(), request.getToken(), new MPVPageParser());
        if (CollectionUtils.isEmpty(hashes)) {
            return Result.error("Empty image hashes");
        }

        FetchImageHashResponse response = FetchImageHashResponse.builder().hashes(hashes).build();
        return Result.success(response);
    }
}
