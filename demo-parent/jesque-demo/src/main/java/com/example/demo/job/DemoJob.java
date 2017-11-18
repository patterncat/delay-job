package com.example.demo.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by patterncat on 2017-11-15.
 */
public class DemoJob implements Runnable{

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
        throw new RuntimeException("demo ex");
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
