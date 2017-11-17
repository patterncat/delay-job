package cn.patterncat.job.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by patterncat on 2017-11-16.
 */
@Data
public class JobEvent extends ApplicationEvent {

    String namespace;

    JobEventType jobEventType = JobEventType.UNKNOWN;

    JobType jobType = JobType.UNKNOWN;

    long future;

    long frequency;

    String worker;

    String queue;

    String jobClassName;

    Object[] jobArgs;

    Map<String,Object> jobVars;

    Map<String,Object> jobUnknownFields;

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

        public Builder worker(String worker){
            jobEvent.setWorker(worker);
            return this;
        }

        public Builder queue(String queue){
            jobEvent.setQueue(queue);
            return this;
        }

        public Builder jobClassName(String jobClassName){
            jobEvent.setJobClassName(jobClassName);
            return this;
        }

        public Builder jobArgs(Object[] jobArgs){
            jobEvent.setJobArgs(jobArgs);
            return this;
        }

        public Builder jobVars(Map<String,Object> jobVars){
            jobEvent.setJobVars(jobVars);
            return this;
        }

        public Builder jobUnknownFields(Map<String,Object> jobUnknownFields){
            jobEvent.setJobUnknownFields(jobUnknownFields);
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

        public Builder namespace(String namespace){
            jobEvent.setNamespace(namespace);
            return this;
        }

        public JobEvent build(){
            return jobEvent;
        }
    }

    public String getThrowableString(int limit){
        if(throwable == null){
            return "";
        }
        String trace = getStackTrace(throwable);
        if(trace.length() <= limit){
            return trace;
        }
        return trace.substring(0,limit);
    }

    public String getRunnerString(){
        if(runner == null){
            return "";
        }
        if(runner instanceof Thread){
            return String.format("thread id:%s,name:%s,state:%s",((Thread) runner).getId(),((Thread) runner).getName(),((Thread) runner).getState());
        }
        return runner.getClass().getName();
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
