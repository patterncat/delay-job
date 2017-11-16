package cn.patterncat.jesque.component;

import net.greghaines.jesque.worker.WorkerPool;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by patterncat on 2017-11-15.
 */
public class WorkerPoolActivator implements InitializingBean, DisposableBean {

    private WorkerPool workerPool;

    private long awaitShutdownInMillis;

    public WorkerPoolActivator(WorkerPool workerPool, long awaitShutdownInMillis) {
        this.workerPool = workerPool;
        this.awaitShutdownInMillis = awaitShutdownInMillis;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(workerPool).start();
    }

    @Override
    public void destroy() throws Exception {
        this.workerPool.endAndJoin(true,awaitShutdownInMillis);
    }
}
