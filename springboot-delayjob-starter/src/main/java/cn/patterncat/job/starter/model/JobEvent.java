package cn.patterncat.job.starter.model;

import org.springframework.context.ApplicationEvent;

/**
 * Created by patterncat on 2017-11-08.
 */
public class JobEvent extends ApplicationEvent {
    public JobEvent(Object source) {
        super(source);
    }
}
