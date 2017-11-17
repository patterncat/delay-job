package cn.patterncat.jesque.component;

import cn.patterncat.jesque.JesqueProperties;
import cn.patterncat.job.event.JobEvent;
import cn.patterncat.job.event.JobEventType;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.worker.Worker;
import net.greghaines.jesque.worker.WorkerEvent;
import net.greghaines.jesque.worker.WorkerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by patterncat on 2017-11-16.
 */
public class EventListenerAdapter implements WorkerListener{

    private static final Logger LOGGER = LoggerFactory.getLogger(EventListenerAdapter.class);

    ApplicationEventPublisher applicationEventPublisher;

    JesqueProperties properties;

    public EventListenerAdapter(ApplicationEventPublisher applicationEventPublisher, JesqueProperties properties) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.properties = properties;
    }

    @Override
    public void onEvent(WorkerEvent event, Worker worker, String queue, Job job, Object runner, Object result, Throwable t) {
        if(WorkerEvent.WORKER_POLL == event){
            //poll event too frequent
            return ;
        }

        JobEvent.Builder builder = JobEvent.builder(this)
                .jobEventType(JobEventType.valueOf(event.name()))
                .namespace(properties.getNamespace())
                .queue(queue)
                .runner(runner)
                .result(result)
                .throwable(t);

        if(job != null){
            builder.jobClassName(job.getClassName())
                    .jobArgs(job.getArgs())
                    .jobVars(job.getVars())
                    .jobUnknownFields(job.getUnknownFields());
        }
        if(worker != null){
            builder.worker(worker.getName());
        }

        applicationEventPublisher.publishEvent(builder.build());

        if(!properties.isLogEventEnabled()){
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
