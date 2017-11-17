package cn.patterncat.job.event.store;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by patterncat on 2017-11-17.
 */
@ConfigurationProperties(prefix = "jesque.store.jdbc")
public class JdbcJobStoreProperties {

    private boolean enabled = true;

    private String tableName = "job_event_log";

    private String dataSourceName = "dataSource";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
}
