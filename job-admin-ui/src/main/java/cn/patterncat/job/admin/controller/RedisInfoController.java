package cn.patterncat.job.admin.controller;

import cn.patterncat.job.admin.model.RestResp;
import net.greghaines.jesque.meta.KeyInfo;
import net.greghaines.jesque.meta.dao.KeysDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
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
    public RestResp<Map<String, String>> redisInfo(){
        Map<String, String> info = keysDAO.getRedisInfo();
        return RestResp.<Map<String, String>>builder().success(true).data(info).build();
    }

    /**
     * keys * 慎用
     * @return
     */
    @GetMapping("/keys")
    public RestResp<List<KeyInfo>> getAllKeys(){
        List<KeyInfo> keyInfos = keysDAO.getKeyInfos();
        return RestResp.<List<KeyInfo>>builder().success(true).data(keyInfos).build();
    }

    /**
     * 如果type为none则返回null
     * @param key
     * @return
     */
    @GetMapping("/key/{key}")
    public RestResp<KeyInfo> getKeyInfo(@PathVariable String key){
        KeyInfo keyInfo = keysDAO.getKeyInfo(key);
        return RestResp.<KeyInfo>builder().success(true).data(keyInfo).build();
    }

    /**
     * 如果type为none则返回null
     * @param key
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/key-values/{key}")
    public RestResp<KeyInfo> getKeyWithValues(@PathVariable String key,
                                              @RequestParam(required = false,defaultValue = "0") int page,
                                              @RequestParam(required = false,defaultValue = "10") int size){
        KeyInfo keyInfo = keysDAO.getKeyInfo(key,page,size);
        return RestResp.<KeyInfo>builder().success(true).data(keyInfo).build();
    }
}
