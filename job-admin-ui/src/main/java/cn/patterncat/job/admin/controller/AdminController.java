package cn.patterncat.job.admin.controller;

import cn.patterncat.job.admin.model.RestResp;
import net.greghaines.jesque.meta.QueueInfo;
import net.greghaines.jesque.meta.dao.FailureDAO;
import net.greghaines.jesque.meta.dao.KeysDAO;
import net.greghaines.jesque.meta.dao.QueueInfoDAO;
import net.greghaines.jesque.meta.dao.WorkerInfoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by patterncat on 2017-11-18.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    QueueInfoDAO queueInfoDAO;

    @Autowired
    KeysDAO keysDAO;

    @Autowired
    FailureDAO failureDAO;

    @Autowired
    WorkerInfoDAO workerInfoDAO;

    @GetMapping("/queue-info")
    public RestResp<List<QueueInfo>> queueInfos(){
        return RestResp.<List<QueueInfo>>builder()
                .success(true)
                .data(queueInfoDAO.getQueueInfos())
                .build();
    }
}
