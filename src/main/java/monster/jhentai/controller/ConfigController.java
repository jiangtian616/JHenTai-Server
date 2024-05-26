package monster.jhentai.controller;

import lombok.extern.slf4j.Slf4j;
import monster.jhentai.annotation.LoginRequired;
import monster.jhentai.biz.ConfigBiz;
import monster.jhentai.enums.ErrorCodeEnum;
import monster.jhentai.exception.CheckArgumentException;
import monster.jhentai.model.bo.JHenTaiUser;
import monster.jhentai.model.request.GetConfigRequest;
import monster.jhentai.model.request.ListConfigRequest;
import monster.jhentai.model.request.UploadConfigRequest;
import monster.jhentai.model.response.ConfigResponse;
import monster.jhentai.model.response.ListConfigResponse;
import monster.jhentai.model.response.Result;
import monster.jhentai.model.response.UploadConfigResponse;
import monster.jhentai.threadlocal.JHenTaiUserThreadLocal;
import monster.jhentai.util.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author JTMonster
 * @date 2024/5/25
 */
@RestController
@RequestMapping("/api/config/")
@Slf4j
public class ConfigController {

    @Autowired
    private ConfigBiz configBiz;

    @LoginRequired
    @GetMapping("/list")
    public Result<ListConfigResponse> list(ListConfigRequest request) {
        JHenTaiUser user = JHenTaiUserThreadLocal.get();
        log.info("ConfigController.list, user:{}, request:{}", user.toMd5(), request);

        ListConfigResponse response = null;
        try {
            response = configBiz.list(request, user);
            return Result.success(response);
        } catch (Exception e) {
            log.error("ConfigController.list error", e);
            return Result.error();
        } finally {
            log.info("ConfigController.list, user:{}, response size:{}", user.toMd5(), response != null ? response.getConfigs().size() : 0);
        }
    }

    @GetMapping("/getByShareCode")
    public Result<ConfigResponse> getByShareCode(GetConfigRequest request) {
        log.info("ConfigController.getByShareCode, request:{}", request);

        ConfigResponse response = null;
        try {
            CheckUtil.checkArgument(request != null, "request is null");
            CheckUtil.checkArgument(request.getShareCode() != null, "shareCode is null");
            response = configBiz.getByShareCode(request.getShareCode());
            return Result.success(response);
        } catch (CheckArgumentException e) {
            log.error("ConfigController.getByShareCode param error", e);
            return Result.error(ErrorCodeEnum.PARAM_ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("ConfigController.getByShareCode error", e);
            return Result.error();
        } finally {
            log.info("ConfigController.getByShareCode, response:{}", response != null);
        }
    }

    @LoginRequired
    @PostMapping("/upload")
    public Result<UploadConfigResponse> upload(@RequestBody UploadConfigRequest request) {
        JHenTaiUser user = JHenTaiUserThreadLocal.get();
        log.info("ConfigController.upload, user:{}, request:{}", user.toMd5(), request);

        UploadConfigResponse response = null;
        try {
            CheckUtil.checkArgument(request != null, "request is null");
            CheckUtil.checkArgument(request.getType() != null, "type is null");
            CheckUtil.checkArgument(request.getVersion() != null, "version is null");
            CheckUtil.checkArgument(request.getConfig() != null, "config is null");

            response = configBiz.upload(request, user);
            return Result.success(response);
        } catch (CheckArgumentException e) {
            log.error("ConfigController.upload param error", e);
            return Result.error(ErrorCodeEnum.PARAM_ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("ConfigController.upload error", e);
            return Result.error();
        } finally {
            log.info("ConfigController.upload, user:{}, request:{}, response:{}", user.toMd5(), request, response);
        }
    }

    @LoginRequired
    @PostMapping("/delete")
    public Result<Void> delete(@RequestParam("id") Long id) {
        JHenTaiUser user = JHenTaiUserThreadLocal.get();
        log.info("ConfigController.delete, user:{}, id:{}", user.toMd5(), id);

        try {
            CheckUtil.checkArgument(id != null, "id is null");

            configBiz.deleteConfig(id, user);
            return Result.success();
        } catch (CheckArgumentException e) {
            log.error("ConfigController.delete param error", e);
            return Result.error(ErrorCodeEnum.PARAM_ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("ConfigController.delete error", e);
            return Result.error();
        } finally {
            log.info("ConfigController.delete, user:{}, id:{}", user.toMd5(), id);
        }
    }
}
