package cn.patterncat.jesque.component;

import net.greghaines.jesque.worker.Worker;
import net.greghaines.jesque.worker.WorkerPool;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by patterncat on 2017-11-15.
 */
public class WorkerActivator implements InitializingBean, DisposableBean {

    private Worker jesqueWorker;

    private long awaitShutdownInMillis;

    public WorkerActivator(Worker jesqueWorker, long awaitShutdownInMillis) {
        this.jesqueWorker = jesqueWorker;
        this.awaitShutdownInMillis = awaitShutdownInMillis;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(jesqueWorker).start();
    }

    @Override
    public void destroy() throws Exception {
        if(jesqueWorker instanceof WorkerPool){
            ((WorkerPool) jesqueWorker).endAndJoin(true,awaitShutdownInMillis);
        }else if(jesqueWorker instanceof RobustWorkerPool){
            ((RobustWorkerPool) jesqueWorker).endAndJoin(true,awaitShutdownInMillis);
        }else{
            this.jesqueWorker.end(true);
        }
    }
}
