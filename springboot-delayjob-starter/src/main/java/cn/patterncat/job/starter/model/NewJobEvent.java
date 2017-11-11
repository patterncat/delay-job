package cn.patterncat.job.starter.model;

import cn.patterncat.job.starter.impl.jdbc.domain.PendingJob;

/**
 * Created by patterncat on 2017-11-11.
 */
public class NewJobEvent extends JobEvent{

    private PendingJob pendingJob;

    public NewJobEvent(Object source) {
        super(source);
    }
}
