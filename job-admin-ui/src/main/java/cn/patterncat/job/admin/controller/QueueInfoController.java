package cn.patterncat.job.admin.controller;

import cn.patterncat.rest.ApiResult;
import net.greghaines.jesque.meta.QueueInfo;
import net.greghaines.jesque.meta.dao.QueueInfoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by patterncat on 2017-11-19.
 */
@RestController
@RequestMapping("/queue-info")
public class QueueInfoController {

    @Autowired
    QueueInfoDAO queueInfoDAO;

    /**
     * 用queueInfos即可
     * @return
     */
    @GetMapping("/names")
    @Deprecated
    public ApiResult<List<String>> getQueueNames(){
        List<String> queueNames = queueInfoDAO.getQueueNames();
        return ApiResult.success(queueNames);
    }

    /**
     * 返回queue的size以及底下的job列表
     * @param queueName
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/{queueName}")
    public ApiResult<QueueInfo> queueWithJobInfo(@PathVariable String queueName,
                                                @RequestParam(required = false,defaultValue = "0") int page,
                                                @RequestParam(required = false,defaultValue = "10") int size){
        Pageable pageable = new PageRequest(page,size);
        QueueInfo queueInfo = queueInfoDAO.getQueueInfo(queueName,pageable.getOffset(),size);
        return ApiResult.success(queueInfo);
    }

    /**
     * 返回queue名称
     * 每个queue的job个数 -- pending count
     *
     * @return
     */
    @GetMapping("")
    public ApiResult<List<QueueInfo>> queueInfos(){
        return ApiResult.success(queueInfoDAO.getQueueInfos());
    }

    @GetMapping("/stat/processed")
    public ApiResult<Long> getProcessedJobCount(){
        Long processedCount = queueInfoDAO.getProcessedCount();
        return ApiResult.success(processedCount);
    }

    @DeleteMapping("/{queueName}")
    public ApiResult removeQueue(@PathVariable String queueName){
        queueInfoDAO.removeQueue(queueName);
        return ApiResult.success();
    }
}
