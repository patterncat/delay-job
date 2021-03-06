package cn.patterncat.job.event.store;

import cn.patterncat.job.event.store.domain.JobLog;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by patterncat on 2017-11-17.
 */
@ConditionalOnProperty(
        value = "jesque.store.jdbc.enabled",
        havingValue = "true",
        matchIfMissing = false
)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(JdbcJobStoreProperties.class)
public class JdbcEventStoreAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext context;

    private final JdbcJobStoreProperties properties;

    public JdbcEventStoreAutoConfiguration(JdbcJobStoreProperties properties) {
        this.properties = properties;
    }

    public DataSource getJobDataSource() {
        return (DataSource) context.getBean(properties.getDataSourceName());
    }

    @Bean
    @ConditionalOnMissingBean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(getJobDataSource());
    }

    @Bean
    @ConditionalOnMissingBean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        return new NamedParameterJdbcTemplate(getJobDataSource());
    }

    @Bean
    @ConditionalOnMissingBean
    public BeanPropertyRowMapper<JobLog> jobLogBeanPropertyRowMapper(){
        return new BeanPropertyRowMapper<JobLog>(JobLog.class);
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
