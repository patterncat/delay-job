package cn.patterncat.job.starter.api;

import cn.patterncat.job.starter.model.JobResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by patterncat on 2017-11-06.
 */
public interface JobClient<K,T> {

    public JobResp<Page<T>> fetchByJobGroup(String group, Pageable pageable);

    public JobResp<Page<T>> fetchByJobClass(String jobClass, Pageable pageable);

    public JobResp<T> newJob(T jobInfo);

    public JobResp claimJob(K jobId,String instanceId);

    public JobResp updateJob(T jobInfo);
}
