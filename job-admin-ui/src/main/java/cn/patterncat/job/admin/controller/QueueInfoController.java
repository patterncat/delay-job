package cn.patterncat.job.admin.controller;

import cn.patterncat.job.admin.model.RestResp;
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
    public RestResp<List<String>> getQueueNames(){
        List<String> queueNames = queueInfoDAO.getQueueNames();
        return RestResp.<List<String>>builder().success(true).data(queueNames).build();
    }

    /**
     * 返回queue的size以及底下的job列表
     * @param queueName
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/{queueName}")
    public RestResp<QueueInfo> queueWithJobInfo(@PathVariable String queueName,
                                                @RequestParam(required = false,defaultValue = "0") int page,
                                                @RequestParam(required = false,defaultValue = "10") int size){
        Pageable pageable = new PageRequest(page,size);
        QueueInfo queueInfo = queueInfoDAO.getQueueInfo(queueName,pageable.getOffset(),size);
        return RestResp.<QueueInfo>builder().success(true).data(queueInfo).build();
    }

    /**
     * 返回queue名称
     * 每个queue的job个数 -- pending count
     *
     * @return
     */
    @GetMapping("")
    public RestResp<List<QueueInfo>> queueInfos(){
        return RestResp.<List<QueueInfo>>builder()
                .success(true)
                .data(queueInfoDAO.getQueueInfos())
                .build();
    }

    @GetMapping("/stat/processed")
    public RestResp<Long> getProcessedJobCount(){
        Long processedCount = queueInfoDAO.getProcessedCount();
        return RestResp.<Long>builder().success(true).data(processedCount).build();
    }

    @DeleteMapping("/{queueName}")
    public RestResp removeQueue(@PathVariable String queueName){
        queueInfoDAO.removeQueue(queueName);
        return RestResp.builder().success(true).build();
    }
}
