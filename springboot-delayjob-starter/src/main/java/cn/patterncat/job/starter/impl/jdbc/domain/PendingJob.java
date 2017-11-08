package cn.patterncat.job.starter.impl.jdbc.domain;

import cn.patterncat.job.starter.model.JobStatus;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by patterncat on 2017-11-07.
 */
@Entity
@Data
@Builder
public class PendingJob {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String jobGroup;

    private String handlerClz;

    private Date dueTime;

    private JobStatus jobStatus;

    private long delayInMillis;

    private int retryCount;

    private String createBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Version
    private Long version;
}
