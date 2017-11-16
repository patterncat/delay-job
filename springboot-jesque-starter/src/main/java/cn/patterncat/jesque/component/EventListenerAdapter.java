package cn.patterncat.jesque.component;

import cn.patterncat.jesque.event.JobEvent;
import cn.patterncat.jesque.event.JobEventType;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.worker.Worker;
import net.greghaines.jesque.worker.WorkerEvent;
import net.greghaines.jesque.worker.WorkerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Created by patterncat on 2017-11-16.
 */
public class EventListenerAdapter implements WorkerListener{

    private static final Logger LOGGER = LoggerFactory.getLogger(EventListenerAdapter.class);

    ApplicationEventPublisher applicationEventPublisher;

    boolean logEventEnabled;

    public EventListenerAdapter(ApplicationEventPublisher applicationEventPublisher, boolean logEventEnabled) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.logEventEnabled = logEventEnabled;
    }

    @Override
    public void onEvent(WorkerEvent event, Worker worker, String queue, Job job, Object runner, Object result, Throwable t) {
        if(WorkerEvent.WORKER_POLL == event){
            //poll event too frequent
            return ;
        }

        applicationEventPublisher.publishEvent(JobEvent.builder(this)
                .jobEventType(JobEventType.valueOf(event.name()))
                .job(job)
                .queue(queue)
                .worker(worker)
                .runner(runner)
                .result(result)
                .throwable(t)
                .build()
        );

        if(!logEventEnabled){
            return ;
        }

        if(WorkerEvent.JOB_FAILURE == event ||
                WorkerEvent.WORKER_ERROR == event ||
                WorkerEvent.WORKER_STOP == event){
            LOGGER.error("WorkerEvent {}; {}; {}; {}; {}; {}; {}",event, worker, queue, job, runner, result, t);
            return ;
        }

        LOGGER.info("WorkerEvent {}; {}; {}; {}; {}; {}; {}",event, worker, queue, job, runner, result, t);
    }
}
