package cn.patterncat.job.event.store.domain;

import cn.patterncat.job.event.JobEvent;
import cn.patterncat.job.event.JobEventType;
import cn.patterncat.job.event.JobType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

/**
 * Created by patterncat on 2017-11-17.
 */
@Data
@Builder
@Document
public class JobLog {

    @Id
    private String id;

    JobEventType jobEventType;

    JobType jobType;

    long eventTimestamp;

    long future;

    long frequency;

    String namespace;

    String workerName;

    String queue;

    String jobClass;

    Object[] jobArgs;

    Map<String,Object> jobVars;

    Map<String,Object> jobUnknownFields;

    Object runner;

    Object result;

    String throwable;

    private Date createTime;

    @Version
    private Long version;

    public static JobLog from(JobEvent jobEvent){
        JobLog log = JobLog.builder()
                .jobType(jobEvent.getJobType())
                .jobEventType(jobEvent.getJobEventType())
                .queue(jobEvent.getQueue())
                .namespace(jobEvent.getNamespace())
                .workerName(jobEvent.getWorker())
                .createTime(new Date())
                .eventTimestamp(jobEvent.getTimestamp())
                .frequency(jobEvent.getFrequency())
                .future(jobEvent.getFuture())
                .jobClass(jobEvent.getJobClassName())
                .jobArgs(jobEvent.getJobArgs())
                .jobVars(jobEvent.getJobVars())
                .jobUnknownFields(jobEvent.getJobUnknownFields())
                .runner(jobEvent.getRunner())
                .result(jobEvent.getResult())
                .throwable(getStackTrace(jobEvent.getThrowable()))
                .build();
        return log;

    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
