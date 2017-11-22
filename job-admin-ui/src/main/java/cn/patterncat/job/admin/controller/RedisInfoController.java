package cn.patterncat.job.admin.controller;

import cn.patterncat.rest.ApiResult;
import net.greghaines.jesque.meta.KeyInfo;
import net.greghaines.jesque.meta.dao.KeysDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by patterncat on 2017-11-19.
 */
@RestController
@RequestMapping("/redis-info")
public class RedisInfoController {

    @Autowired
    KeysDAO keysDAO;

    @GetMapping("")
    public ApiResult<Map<String, String>> redisInfo(){
        Map<String, String> info = keysDAO.getRedisInfo();
        return ApiResult.success(info);
    }

    /**
     * keys * 慎用
     * @return
     */
    @GetMapping("/keys")
    public ApiResult<List<KeyInfo>> getAllKeys(){
        List<KeyInfo> keyInfos = keysDAO.getKeyInfos();
        return ApiResult.success(keyInfos);
    }

    /**
     * 如果type为none则返回null
     * @param key
     * @return
     */
    @GetMapping("/key/{key}")
    public ApiResult<KeyInfo> getKeyInfo(@PathVariable String key){
        KeyInfo keyInfo = keysDAO.getKeyInfo(key);
        return ApiResult.success(keyInfo);
    }

    /**
     * 如果type为none则返回null
     * @param key
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/key-values/{key}")
    public ApiResult<KeyInfo> getKeyWithValues(@PathVariable String key,
                                              @RequestParam(required = false,defaultValue = "0") int page,
                                              @RequestParam(required = false,defaultValue = "10") int size){
        KeyInfo keyInfo = keysDAO.getKeyInfo(key,page,size);
        return ApiResult.success(keyInfo);
    }
}
