package cn.patterncat.jesque.component;


import cn.patterncat.jesque.event.JobEvent;
import cn.patterncat.jesque.event.JobEventType;
import cn.patterncat.jesque.event.JobType;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by patterncat on 2017-11-16.
 */
public class JesqueService {

    @Autowired
    Client jesqueClient;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public void delayedEnqueue(String queue,Class jobClz,Object[] params,long futureFireTimeInMillis){
        Job job = new Job(jobClz.getName(),params);
        applicationEventPublisher.publishEvent(JobEvent.builder(this)
                .jobEventType(JobEventType.JOB_SUBMITT)
                .queue(queue)
                .jobType(JobType.DELAYED)
                .future(futureFireTimeInMillis)
                .runner(Thread.currentThread())
                .job(job)
                .build()
        );
        jesqueClient.delayedEnqueue(queue,job,futureFireTimeInMillis);
    }

    public void immediateEnqueue(String queue, Job job){
        applicationEventPublisher.publishEvent(JobEvent.builder(this)
                .jobEventType(JobEventType.JOB_SUBMITT)
                .queue(queue)
                .jobType(JobType.IMMEDIATE)
                .runner(Thread.currentThread())
                .job(job)
                .build()
        );
        jesqueClient.enqueue(queue, job);
    }

    public void priorityEnqueue(String queue, Job job){
        applicationEventPublisher.publishEvent(JobEvent.builder(this)
                .jobEventType(JobEventType.JOB_SUBMITT)
                .queue(queue)
                .jobType(JobType.PRIORITY)
                .runner(Thread.currentThread())
                .job(job)
                .build()
        );
        jesqueClient.priorityEnqueue(queue, job);
    }

    public void recurringEnqueue(String queue, Job job, long future, long frequency){
        applicationEventPublisher.publishEvent(JobEvent.builder(this)
                .jobEventType(JobEventType.JOB_SUBMITT)
                .queue(queue)
                .jobType(JobType.RECURRING)
                .future(future)
                .frequency(frequency)
                .runner(Thread.currentThread())
                .job(job)
                .build()
        );
        jesqueClient.recurringEnqueue(queue, job, future, frequency);
    }

    public void removeDelayedEnqueue(String queue, Job job){
        applicationEventPublisher.publishEvent(JobEvent.builder(this)
                .jobEventType(JobEventType.JOB_REMOVE)
                .queue(queue)
                .jobType(JobType.DELAYED)
                .runner(Thread.currentThread())
                .job(job)
                .build()
        );
        jesqueClient.removeDelayedEnqueue(queue, job);
    }

    public void removeRecurringEnqueue(String queue, Job job) {
        applicationEventPublisher.publishEvent(JobEvent.builder(this)
                .jobEventType(JobEventType.JOB_REMOVE)
                .queue(queue)
                .jobType(JobType.RECURRING)
                .runner(Thread.currentThread())
                .job(job)
                .build()
        );
        jesqueClient.removeRecurringEnqueue(queue, job);
    }
}
