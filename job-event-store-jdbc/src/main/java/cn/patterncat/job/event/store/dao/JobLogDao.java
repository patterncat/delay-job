package cn.patterncat.job.event.store.dao;

import cn.patterncat.helper.sql.criteria.WhereClause;
import cn.patterncat.helper.sql.util.SqlUtils;
import cn.patterncat.helper.sql.util.ValueUtils;
import cn.patterncat.job.event.store.JdbcJobStoreProperties;
import cn.patterncat.job.event.store.domain.JobLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by patterncat on 2017-11-17.
 */
@Repository
public class JobLogDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private BeanPropertyRowMapper<JobLog> jobLogBeanPropertyRowMapper;

    @Autowired
    JdbcJobStoreProperties properties;

    public int insert(JobLog jobLog) {
        Map<String,Object> valueMap = ValueUtils.beanToMap(jobLog);
        String sql = SqlUtils.buildInsertNamedSql(properties.getTableName(),valueMap,true);
        return namedParameterJdbcTemplate.update(sql,valueMap);
    }

    public long count(WhereClause whereClause){
        String sql = whereClause.toCountSql();
        Map<String,Object> params = whereClause.getNamedParams();
        return namedParameterJdbcTemplate.queryForObject(sql,params,Long.class);
    }

    public Page<JobLog> find(WhereClause whereClause){
        Pageable pageable = whereClause.getPageable();
        long count = count(whereClause);
        if(count <= 0){
            return new PageImpl<JobLog>(Collections.emptyList(),pageable,count);
        }

        String sql = whereClause.toSelectSql();
        Map<String,Object> params = whereClause.getNamedParams();
        List<JobLog> data = namedParameterJdbcTemplate.query(sql,params,jobLogBeanPropertyRowMapper);
        return new PageImpl<JobLog>(data,pageable,count);
    }

    public List<JobLog> findAll() {
        String sql = WhereClause.newInstance(properties.getTableName())
                .build()
                .toSelectSql();
        return namedParameterJdbcTemplate.query(sql, jobLogBeanPropertyRowMapper);
    }
}
