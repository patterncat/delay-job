package cn.patterncat.jesque;

import cn.patterncat.jesque.component.*;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.greghaines.jesque.Config;
import net.greghaines.jesque.ConfigBuilder;
import net.greghaines.jesque.client.Client;
import net.greghaines.jesque.client.ClientPoolImpl;
import net.greghaines.jesque.utils.PoolUtils;
import net.greghaines.jesque.worker.ReflectiveJobFactory;
import net.greghaines.jesque.worker.Worker;
import net.greghaines.jesque.worker.WorkerImpl;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by patterncat on 2017-11-16.
 */
@Configuration
@ConditionalOnProperty(
        prefix = "jesque",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false
)
@EnableConfigurationProperties(JesqueProperties.class)
public class JesqueAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JesqueAutoConfiguration.class);

    public static final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            LOGGER.error(String.format("job-async thread:%s error",t),e);
        }
    };

    private final JesqueProperties properties;

    public JesqueAutoConfiguration(JesqueProperties jesqueProperties) {
        this.properties = jesqueProperties;
    }

    //common config

    @Bean
    public Config jesqueConfig() {
        return new ConfigBuilder()
                .withHost(properties.getHost())
                .withPort(properties.getPort())
                .withPassword(properties.getPassword())
                .withNamespace(properties.getNamespace())
                .build();
    }

    @Bean(destroyMethod = "close")
    public Pool<Jedis> jedisPool(Config jesqueConfig){
        //这里可以去自定义连接池的属性,默认最大10个连接,testOnBorrow
        GenericObjectPoolConfig objectPoolConfig = PoolUtils.getDefaultPoolConfig();
        objectPoolConfig.setMaxIdle(properties.getPoolMaxIdle());
        objectPoolConfig.setMinIdle(properties.getPoolMinIdle());
        objectPoolConfig.setMaxTotal(properties.getPoolMaxTotal());
        objectPoolConfig.setTestOnBorrow(properties.isTestOnBorrow());
        objectPoolConfig.setTestOnCreate(properties.isTestOnCreate());
        objectPoolConfig.setTestOnReturn(properties.isTestOnReturn());
        objectPoolConfig.setBlockWhenExhausted(properties.isBlockWhenExhausted());
        Pool<Jedis> jedisPool = PoolUtils.createJedisPool(jesqueConfig,objectPoolConfig);
        return jedisPool;
    }

    //worker config

    @Bean
    public Worker jesqueWorker(Config config, Pool<Jedis> jedisPool, ApplicationEventPublisher applicationEventPublisher) {
        Collection<String> queues = Arrays.asList(properties.getListenQueues().split(","));

        Callable<? extends Worker> workerFactory = null;
        if(properties.isPoolWorkerEnabled()){
            workerFactory = () -> new RedisPooledWorkerImpl(config,queues,
                    new ReflectiveJobFactory(),
                    jedisPool,
                    properties.getPoolIntervalInMillis());
        }else{
            workerFactory = () -> new WorkerImpl(config,
                    queues,
                    new ReflectiveJobFactory() /**根据全类名来实例化job,例如DemoJob.class.getName()*/);
        }
        Worker workerPool = new RobustWorkerPool(workerFactory,
                properties.getWorkersNum());

        workerPool.getWorkerEventEmitter().addListener(new EventListenerAdapter(applicationEventPublisher,properties.isLogEventEnabled()));

        return workerPool;
    }

    @Bean(name = "workerPoolActivator")
    public WorkerActivator workerPoolActivator(Worker jesqueWorker){
        return new WorkerActivator(jesqueWorker,properties.getShutdownAwaitMillis());
    }

    //client config

    @Bean
    @ConditionalOnProperty(
            value = "jesque.client-enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public JesqueService jesqueService(){
        return new JesqueService();
    }

    /**
     * 调用end优化关闭
     * @param jesqueConfig
     * @return
     */
    @Bean(destroyMethod = "end")
    @ConditionalOnProperty(
            value = "jesque.client-enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public Client jesqueClient(Config jesqueConfig, Pool<Jedis> jedisPool) {
        return new ClientPoolImpl(jesqueConfig,jedisPool);
    }

    //spring event config

    @Bean(name = "jobExecutor")
    @ConditionalOnProperty(value = "jesque.asyncEvent",havingValue = "true",matchIfMissing = false)
    protected TaskExecutor jobExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getEventPoolCoreSize());
        executor.setMaxPoolSize(properties.getEventPoolMaxSize());
        executor.setQueueCapacity(properties.getEventPoolQueueCapacity());
        executor.setThreadNamePrefix(properties.getEventThreadNamePrefix());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
        threadFactoryBuilder.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        executor.setThreadFactory(threadFactoryBuilder.build());
        executor.initialize();
        return executor;
    }

    @Bean(name = "applicationEventMulticaster")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "jesque.asyncEvent",havingValue = "true",matchIfMissing = false)
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster
                = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(jobExecutor());
        return eventMulticaster;
    }
}
