package com.example.demo.controller;

import cn.patterncat.jesque.component.JesqueService;
import com.example.demo.job.DemoJob;
import net.greghaines.jesque.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by patterncat on 2017-11-15.
 */
@RestController
@RequestMapping("/job")
public class DemoJobController {

    @Autowired
    JesqueService jesqueService;

    @GetMapping(value = "")
    public String addDelayJob(@RequestParam String content){
        Object[] params = new Object[]{content,System.currentTimeMillis()};
        Job job = new Job(DemoJob.class.getName(),params);
        job.setUnknownField("jobId", UUID.randomUUID().toString());
        jesqueService.delayedEnqueue("delayed-queue",job,System.currentTimeMillis() + 10*1000);
        return "ok";
    }
}
