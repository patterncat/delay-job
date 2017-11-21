package cn.patterncat.job.event.store.dao;

import cn.patterncat.job.event.store.JdbcJobStoreProperties;
import cn.patterncat.job.event.store.domain.JobLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by patterncat on 2017-11-17.
 */
@Repository
public class JobLogDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BeanPropertyRowMapper<JobLog> jobLogBeanPropertyRowMapper;

    @Autowired
    JdbcJobStoreProperties properties;

    private static final String INSERT = "insert into %s (create_time, event_timestamp, frequency,"
            + "future, job_args, job_class, job_event_type, job_type, job_unknown_fields, job_vars, namespace,"
            + "queue, result, runner, throwable, worker_name, job_id) "
            + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public void insert(JobLog jobLog) {
        jdbcTemplate.update(new PreparedStatementCreator() {
                                public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                                    PreparedStatement ps = conn.prepareStatement(String.format(INSERT,properties.getTableName()));
                                    ps.setDate(1, new Date(jobLog.getCreateTime().getTime()));
                                    ps.setLong(2, jobLog.getEventTimestamp());
                                    ps.setLong(3, jobLog.getFrequency());
                                    ps.setLong(4, jobLog.getFuture());
                                    ps.setString(5, jobLog.getJobArgs());
                                    ps.setString(6, jobLog.getJobClass());
                                    ps.setInt(7, jobLog.getJobEventType().ordinal());
                                    ps.setInt(8, jobLog.getJobType().ordinal());
                                    ps.setString(9, jobLog.getJobUnknownFields());
                                    ps.setString(10, jobLog.getJobVars());
                                    ps.setString(11, jobLog.getNamespace());
                                    ps.setString(12, jobLog.getQueue());
                                    ps.setString(13, jobLog.getResult());
                                    ps.setString(14, jobLog.getRunner());
                                    ps.setString(15, jobLog.getThrowable());
                                    ps.setString(16, jobLog.getWorkerName());
                                    ps.setString(17, jobLog.getJobId());
                                    return ps;
                                }
                            }
        );
    }

    /**
     * 查询列表
     * @return
     */
    public List<JobLog> selectList() {
        return jdbcTemplate.query("select * from user", jobLogBeanPropertyRowMapper);
    }
}
