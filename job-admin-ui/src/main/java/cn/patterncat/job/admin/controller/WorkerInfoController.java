package cn.patterncat.job.admin.controller;

import cn.patterncat.rest.ApiResult;
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
    public ApiResult<Map<String, List<WorkerInfo>>> getWorkerHostMap(){
        Map<String, List<WorkerInfo>> data = workerInfoDAO.getWorkerHostMap();
        return ApiResult.success(data);
    }

    @DeleteMapping("/{worker}")
    public ApiResult removeWorker(@PathVariable String worker){
        workerInfoDAO.removeWorker(worker);
        return ApiResult.success();
    }
}
