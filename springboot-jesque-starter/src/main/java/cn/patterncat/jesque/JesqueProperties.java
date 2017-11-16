package cn.patterncat.jesque;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by patterncat on 2017-11-16.
 */
@ConfigurationProperties(prefix = "jesque")
public class JesqueProperties {

    private boolean enabled = true;

    private String host;

    private Integer port = 9221;

    private String password;

    private String namespace;

    private String listenQueues;

    private Integer workersNum = 10;

    private Long shutdownAwaitMillis = 30000L;

    //event

    private boolean asyncEvent = true;

    private boolean logEventEnabled = true;

    private Integer eventPoolCoreSize = Runtime.getRuntime().availableProcessors();

    private Integer eventPoolMaxSize = 20;

    private Integer eventPoolQueueCapacity = 1000;

    private String eventThreadNamePrefix = "job-async-";

    //pool worker
    private boolean poolWorkerEnabled = true;

    private long poolIntervalInMillis = 500;

    //redis object pool config

    private boolean testOnBorrow = false;

    private boolean testOnReturn = false;

    private boolean testOnCreate = false;

    private boolean testWhileIdle = false;

    private int poolMaxTotal = 20;

    private int poolMaxIdle = 8; //生产上建议跟max total一致,避免makeObject开销

    private int poolMinIdle = 4;

    private boolean blockWhenExhausted = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getListenQueues() {
        return listenQueues;
    }

    public void setListenQueues(String listenQueues) {
        this.listenQueues = listenQueues;
    }

    public Integer getWorkersNum() {
        return workersNum;
    }

    public void setWorkersNum(Integer workersNum) {
        this.workersNum = workersNum;
    }

    public Long getShutdownAwaitMillis() {
        return shutdownAwaitMillis;
    }

    public void setShutdownAwaitMillis(Long shutdownAwaitMillis) {
        this.shutdownAwaitMillis = shutdownAwaitMillis;
    }

    public boolean isLogEventEnabled() {
        return logEventEnabled;
    }

    public void setLogEventEnabled(boolean logEventEnabled) {
        this.logEventEnabled = logEventEnabled;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestOnCreate() {
        return testOnCreate;
    }

    public void setTestOnCreate(boolean testOnCreate) {
        this.testOnCreate = testOnCreate;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public int getPoolMaxTotal() {
        return poolMaxTotal;
    }

    public void setPoolMaxTotal(int poolMaxTotal) {
        this.poolMaxTotal = poolMaxTotal;
    }

    public int getPoolMaxIdle() {
        return poolMaxIdle;
    }

    public void setPoolMaxIdle(int poolMaxIdle) {
        this.poolMaxIdle = poolMaxIdle;
    }

    public int getPoolMinIdle() {
        return poolMinIdle;
    }

    public void setPoolMinIdle(int poolMinIdle) {
        this.poolMinIdle = poolMinIdle;
    }

    public boolean isBlockWhenExhausted() {
        return blockWhenExhausted;
    }

    public void setBlockWhenExhausted(boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }

    public boolean isAsyncEvent() {
        return asyncEvent;
    }

    public void setAsyncEvent(boolean asyncEvent) {
        this.asyncEvent = asyncEvent;
    }

    public Integer getEventPoolCoreSize() {
        return eventPoolCoreSize;
    }

    public void setEventPoolCoreSize(Integer eventPoolCoreSize) {
        this.eventPoolCoreSize = eventPoolCoreSize;
    }

    public Integer getEventPoolMaxSize() {
        return eventPoolMaxSize;
    }

    public void setEventPoolMaxSize(Integer eventPoolMaxSize) {
        this.eventPoolMaxSize = eventPoolMaxSize;
    }

    public Integer getEventPoolQueueCapacity() {
        return eventPoolQueueCapacity;
    }

    public void setEventPoolQueueCapacity(Integer eventPoolQueueCapacity) {
        this.eventPoolQueueCapacity = eventPoolQueueCapacity;
    }

    public String getEventThreadNamePrefix() {
        return eventThreadNamePrefix;
    }

    public void setEventThreadNamePrefix(String eventThreadNamePrefix) {
        this.eventThreadNamePrefix = eventThreadNamePrefix;
    }

    public boolean isPoolWorkerEnabled() {
        return poolWorkerEnabled;
    }

    public void setPoolWorkerEnabled(boolean poolWorkerEnabled) {
        this.poolWorkerEnabled = poolWorkerEnabled;
    }

    public long getPoolIntervalInMillis() {
        return poolIntervalInMillis;
    }

    public void setPoolIntervalInMillis(long poolIntervalInMillis) {
        this.poolIntervalInMillis = poolIntervalInMillis;
    }
}
