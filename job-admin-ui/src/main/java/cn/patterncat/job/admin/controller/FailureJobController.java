package cn.patterncat.job.admin.controller;

import cn.patterncat.job.admin.model.RestResp;
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
    public RestResp<Long> getFailJobCount(){
        long count = failureDAO.getCount();
        return RestResp.<Long>builder().success(true).data(count).build();
    }

    @GetMapping("")
    public RestResp<Page<JobFailure>> failJobList(@RequestParam(required = false,defaultValue = "0") int page,
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

        return RestResp.<Page<JobFailure>>builder().success(true).data(pageData).build();
    }

    @PostMapping("/clear")
    public RestResp clearJobFailure(){
        failureDAO.clear();
        return RestResp.builder().success(true).build();
    }

    @PostMapping("/remove/{idx}")
    public RestResp remove(@PathVariable Long idx){
        failureDAO.remove(idx);
        return RestResp.builder().success(true).build();
    }

    @PostMapping("/requeue/{idx}")
    public RestResp requeue(@PathVariable Long idx){
        failureDAO.requeue(idx);
        return RestResp.builder().success(true).build();
    }
}
