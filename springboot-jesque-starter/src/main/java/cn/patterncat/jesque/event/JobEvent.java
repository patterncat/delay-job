package cn.patterncat.jesque.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by patterncat on 2017-11-16.
 */
public class JobEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public JobEvent(Object source) {
        super(source);
    }
}
