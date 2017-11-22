package cn.patterncat.job.admin.controller;

import cn.patterncat.rest.ApiResult;
import net.greghaines.jesque.JobFailure;
import net.greghaines.jesque.meta.dao.FailureDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * Created by patterncat on 2017-11-19.
 */
@RestController
@RequestMapping("/failure")
public class FailureJobController {

    @Autowired
    FailureDAO failureDAO;

    @GetMapping("/count")
    public ApiResult<Long> getFailJobCount(){
        long count = failureDAO.getCount();
        return ApiResult.success(count);
    }

    @GetMapping("")
    public ApiResult<Page<JobFailure>> failJobList(@RequestParam(required = false,defaultValue = "0") int page,
                                                        @RequestParam(required = false,defaultValue = "10") int size){
        Pageable pageable = new PageRequest(page,size);
        long count = failureDAO.getCount();
        Page<JobFailure> pageData = null;
        if(count <= 0){
            pageData = new PageImpl<>(Collections.emptyList(),pageable,count);
        }else{
            List<JobFailure> jobFailures = failureDAO.getFailures(pageable.getOffset(),size);
            pageData = new PageImpl<JobFailure>(jobFailures,pageable,count);
        }

        return ApiResult.success(pageData);
    }

    @PostMapping("/clear")
    public ApiResult clearJobFailure(){
        failureDAO.clear();
        return ApiResult.success();
    }

    @PostMapping("/remove/{idx}")
    public ApiResult remove(@PathVariable Long idx){
        failureDAO.remove(idx);
        return ApiResult.success();
    }

    @PostMapping("/requeue/{idx}")
    public ApiResult requeue(@PathVariable Long idx){
        failureDAO.requeue(idx);
        return ApiResult.success();
    }
}
