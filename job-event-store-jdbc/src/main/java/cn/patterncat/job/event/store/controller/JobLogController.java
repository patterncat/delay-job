package cn.patterncat.job.event.store.controller;

import cn.patterncat.helper.sql.criteria.Restrictions;
import cn.patterncat.helper.sql.criteria.WhereClause;
import cn.patterncat.job.event.JobEventType;
import cn.patterncat.job.event.store.JdbcJobStoreProperties;
import cn.patterncat.job.event.store.dao.JobLogDao;
import cn.patterncat.job.event.store.domain.JobLog;
import cn.patterncat.rest.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by patterncat on 2017-11-22.
 */
@ConditionalOnProperty(value = "jesque.store.jdbc.web-enabled",
        havingValue = "true",
        matchIfMissing = false)
@RestController
@RequestMapping("/job-log")
public class JobLogController {

    @Autowired
    JobLogDao jobLogDao;

    @Autowired
    JdbcJobStoreProperties jdbcJobStoreProperties;

    @GetMapping("")
    public ApiResult<Page<JobLog>> list(@RequestParam(required = false) String jobId,
                                        @RequestParam(required = false) String namespace,
                                        @RequestParam(required = false) String queue,
                                        @RequestParam(required = false) String jobClass,
                                        @RequestParam(required = false) Long eventTimeGte,
                                        @RequestParam(required = false) Long eventTimeLte,
                                        @RequestParam(required = false) JobEventType jobEventType,
                                        @RequestParam(required = false,defaultValue = "0")int page,
                                        @RequestParam(required = false,defaultValue = "10") int size){
        Pageable pageable = new PageRequest(page,size, Sort.Direction.DESC,"eventTimestamp");
        WhereClause whereClause = WhereClause.newInstance(jdbcJobStoreProperties.getTableName())
                .page(pageable);
        if(StringUtils.isNotBlank(jobId)){
            whereClause.add(Restrictions.equalTo("jobId",jobId));
        }
        if(StringUtils.isNotBlank(namespace)){
            whereClause.add(Restrictions.equalTo("namespace",namespace));
        }
        if(StringUtils.isNotBlank(queue)){
            whereClause.add(Restrictions.equalTo("queue",queue));
        }
        if(StringUtils.isNotBlank(jobClass)){
            whereClause.add(Restrictions.equalTo("jobClass",jobClass));
        }
        if(jobEventType != null){
            whereClause.add(Restrictions.equalTo("jobEventType",jobEventType.ordinal()));
        }
        if(eventTimeLte != null){
            whereClause.add(Restrictions.lessThanOrEqualTo("eventTimestamp",eventTimeLte));
        }
        if(eventTimeGte != null){
            whereClause.add(Restrictions.greaterThanOrEqualTo("eventTimestamp",eventTimeGte));
        }

        Page<JobLog> data = jobLogDao.find(whereClause);
        return ApiResult.success(data);
    }
}
