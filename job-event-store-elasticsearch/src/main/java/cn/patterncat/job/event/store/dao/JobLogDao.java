package cn.patterncat.job.event.store.dao;

import cn.patterncat.job.event.store.domain.JobLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by patterncat on 2017-11-17.
 */
public interface JobLogDao extends ElasticsearchRepository<JobLog,String>{
}
