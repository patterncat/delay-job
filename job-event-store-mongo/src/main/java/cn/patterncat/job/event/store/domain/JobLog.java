package cn.patterncat.job.event.store.domain;

import cn.patterncat.job.event.JobEvent;
import cn.patterncat.job.event.JobEventType;
import cn.patterncat.job.event.JobType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Created by patterncat on 2017-11-17.
 */
@Data
@Builder
@Document
public class JobLog {

    @Id
    private String id;

    String jobId;

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

    String runner;

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
                .jobId(Objects.toString(jobEvent.getJobUnknownFields().get("jobId"),""))
                .jobArgs(jobEvent.getJobArgs())
                .jobVars(jobEvent.getJobVars())
                .jobUnknownFields(jobEvent.getJobUnknownFields())
                .runner(jobEvent.getRunnerString())
                .result(jobEvent.getResult())
                .throwable(jobEvent.getThrowableString(Integer.MAX_VALUE))
                .build();
        return log;

    }
}
