package com.example.demo.controller;

import cn.patterncat.jesque.component.JesqueService;
import net.greghaines.jesque.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public static class DemoJob implements Runnable{

        private static final Logger LOGGER = LoggerFactory.getLogger(DemoJob.class);

        private String content;

        private long timestamp;

        public DemoJob(String content, long timestamp) {
            this.content = content;
            this.timestamp = timestamp;
        }

        @Override
        public void run() {
            LOGGER.info("job got fired,content:{},timestamp:{}",content,timestamp);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
