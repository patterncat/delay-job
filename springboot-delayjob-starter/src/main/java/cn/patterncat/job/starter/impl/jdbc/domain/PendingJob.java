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

    private String jobClass;

    private String lockOwner;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lockTime;

    private boolean enabled = true;

    private int jobPriority = 0;

    /**
     * 是否幂等,幂等的话,可以重试
     */
    private boolean idempotent;

    private Date dueTime;

    private JobStatus jobStatus;

    private long delayInMillis;

    @Builder.Default
    private int retryCount = 0;

    private String createBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Version
    private Long version;
}
