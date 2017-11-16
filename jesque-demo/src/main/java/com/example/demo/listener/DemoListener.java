package com.example.demo.listener;

import cn.patterncat.jesque.event.JobEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by patterncat on 2017-11-16.
 */
@Component
public class DemoListener implements ApplicationListener<JobEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoListener.class);

    @Override
    public void onApplicationEvent(JobEvent event) {
        LOGGER.info("receive event:{}",event);
    }
}
