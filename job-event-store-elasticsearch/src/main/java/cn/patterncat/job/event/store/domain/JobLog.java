package cn.patterncat.job.event.store.domain;

import cn.patterncat.job.event.JobEvent;
import cn.patterncat.job.event.JobEventType;
import cn.patterncat.job.event.JobType;
import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Created by patterncat on 2017-11-17.
 */
@Data
@Builder
@Document(indexName="joblog",type = "event")
//@Mapping(mappingPath = "elasticsearch/mappings.json")
public class JobLog {

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

    String jobArgs;

    String jobVars;

    String jobUnknownFields;

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
                .jobArgs(object2Json(jobEvent.getJobArgs()))
                .jobVars(object2Json(jobEvent.getJobVars()))
                .jobUnknownFields(object2Json(jobEvent.getJobUnknownFields()))
                .runner(jobEvent.getRunnerString())
                .result(jobEvent.getResult())
                .throwable(jobEvent.getThrowableString(Integer.MAX_VALUE))
                .build();
        return log;

    }

    public static String object2Json(Object object){
        if(object == null){
            return "";
        }
        return JSON.toJSONString(object);
    }
}
