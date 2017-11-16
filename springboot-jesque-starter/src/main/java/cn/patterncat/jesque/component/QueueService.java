package cn.patterncat.jesque.component;


import cn.patterncat.jesque.event.JobEvent;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by patterncat on 2017-11-16.
 */
public class QueueService {

    @Autowired
    Client jesqueClient;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public void delayedEnqueue(String queue,Class jobClz,Object[] params,long futureFireTimeInMillis){
        applicationEventPublisher.publishEvent(new JobEvent(this));
        jesqueClient.delayedEnqueue(queue,new Job(jobClz.getName(),params),futureFireTimeInMillis);
    }
}
