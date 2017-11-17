package cn.patterncat.jesque.component;


import cn.patterncat.jesque.JesqueProperties;
import cn.patterncat.job.event.JobEvent;
import cn.patterncat.job.event.JobEventType;
import cn.patterncat.job.event.JobType;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by patterncat on 2017-11-16.
 */
public class JesqueService {

    @Autowired
    JesqueProperties jesqueProperties;

    @Autowired
    Client jesqueClient;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public void delayedEnqueue(String queue,Job job,long futureFireTimeInMillis){
        applicationEventPublisher.publishEvent(from(queue,job)
                .jobEventType(JobEventType.JOB_SUBMITT)
                .jobType(JobType.DELAYED)
                .future(futureFireTimeInMillis)
                .build()
        );
        jesqueClient.delayedEnqueue(queue,job,futureFireTimeInMillis);
    }

    public void immediateEnqueue(String queue, Job job){
        applicationEventPublisher.publishEvent(from(queue,job)
                        .jobEventType(JobEventType.JOB_SUBMITT)
                        .jobType(JobType.IMMEDIATE)
                        .build()
        );
        jesqueClient.enqueue(queue, job);
    }

    public void priorityEnqueue(String queue, Job job){
        applicationEventPublisher.publishEvent(from(queue,job)
                .jobEventType(JobEventType.JOB_SUBMITT)
                .jobType(JobType.PRIORITY)
                .build()
        );
        jesqueClient.priorityEnqueue(queue, job);
    }

    public void recurringEnqueue(String queue, Job job, long future, long frequency){
        applicationEventPublisher.publishEvent(from(queue,job)
                .jobEventType(JobEventType.JOB_SUBMITT)
                .jobType(JobType.RECURRING)
                .future(future)
                .frequency(frequency)
                .build()
        );
        jesqueClient.recurringEnqueue(queue, job, future, frequency);
    }

    public void removeDelayedEnqueue(String queue, Job job){
        applicationEventPublisher.publishEvent(from(queue,job)
                .jobEventType(JobEventType.JOB_REMOVE)
                .jobType(JobType.DELAYED)
                .build()
        );
        jesqueClient.removeDelayedEnqueue(queue, job);
    }

    public void removeRecurringEnqueue(String queue, Job job) {
        applicationEventPublisher.publishEvent(from(queue,job)
                .jobEventType(JobEventType.JOB_REMOVE)
                .jobType(JobType.RECURRING)
                .build()
        );
        jesqueClient.removeRecurringEnqueue(queue, job);
    }

    public JobEvent.Builder from(String queue,Job job){
        return JobEvent.builder(this)
                .queue(queue)
                .namespace(jesqueProperties.getNamespace())
                .runner(Thread.currentThread())
                .jobClassName(job.getClassName())
                .jobArgs(job.getArgs())
                .jobVars(job.getVars())
                .jobUnknownFields(job.getUnknownFields());
    }
}
