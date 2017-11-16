package cn.patterncat.jesque.event;

import lombok.Data;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.worker.Worker;
import org.springframework.context.ApplicationEvent;

/**
 * Created by patterncat on 2017-11-16.
 */
@Data
public class JobEvent extends ApplicationEvent {

    JobEventType jobEventType;

    JobType jobType;

    long future;

    long frequency;

    Worker worker;

    String queue;

    Job job;

    Object runner;

    Object result;

    Throwable throwable;

    public JobEvent(Object source) {
        super(source);
    }

    public static Builder builder(Object source){
        return new Builder(source);
    }

    public static class Builder {
        JobEvent jobEvent;

        public Builder(Object source) {
            jobEvent = new JobEvent(source);
        }

        public Builder jobEventType(JobEventType jobEventType){
            jobEvent.setJobEventType(jobEventType);
            return this;
        }

        public Builder worker(Worker worker){
            jobEvent.setWorker(worker);
            return this;
        }

        public Builder queue(String queue){
            jobEvent.setQueue(queue);
            return this;
        }

        public Builder job(Job job){
            jobEvent.setJob(job);
            return this;
        }

        public Builder jobType(JobType jobType){
            jobEvent.setJobType(jobType);
            return this;
        }

        public Builder future(long future){
            jobEvent.setFuture(future);
            return this;
        }

        public Builder frequency(long frequency){
            jobEvent.setFrequency(frequency);
            return this;
        }

        public Builder runner(Object runner){
            jobEvent.setRunner(runner);
            return this;
        }

        public Builder result(Object result){
            jobEvent.setResult(result);
            return this;
        }

        public Builder throwable(Throwable throwable){
            jobEvent.setThrowable(throwable);
            return this;
        }

        public JobEvent build(){
            return jobEvent;
        }
    }
}
