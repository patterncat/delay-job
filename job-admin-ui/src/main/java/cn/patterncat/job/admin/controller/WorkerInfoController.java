package cn.patterncat.job.admin.controller;

import cn.patterncat.job.admin.model.RestResp;
import net.greghaines.jesque.meta.WorkerInfo;
import net.greghaines.jesque.meta.dao.WorkerInfoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by patterncat on 2017-11-19.
 */
@RestController
@RequestMapping("/worker-info")
public class WorkerInfoController {

    @Autowired
    WorkerInfoDAO workerInfoDAO;

    @GetMapping("")
    public RestResp<Map<String, List<WorkerInfo>>> getWorkerHostMap(){
        Map<String, List<WorkerInfo>> data = workerInfoDAO.getWorkerHostMap();
        return RestResp.<Map<String, List<WorkerInfo>>>builder().success(true).data(data).build();
    }

    @DeleteMapping("/{worker}")
    public RestResp removeWorker(@PathVariable String worker){
        workerInfoDAO.removeWorker(worker);
        return RestResp.builder().success(true).build();
    }
}
