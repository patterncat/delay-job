package cn.patterncat.jesque.component;

import net.greghaines.jesque.Job;
import net.greghaines.jesque.worker.Worker;
import net.greghaines.jesque.worker.WorkerEvent;
import net.greghaines.jesque.worker.WorkerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by patterncat on 2017-11-16.
 */
public class LogEventListener implements WorkerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogEventListener.class);

    @Override
    public void onEvent(WorkerEvent event, Worker worker, String queue, Job job, Object runner, Object result, Throwable t) {
        if(WorkerEvent.WORKER_POLL == event){
            //poll event too frequent
            return ;
        }
        if(WorkerEvent.JOB_FAILURE == event ||
                WorkerEvent.WORKER_ERROR == event ||
                WorkerEvent.WORKER_STOP == event){
            LOGGER.error("WorkerEvent {}; {}; {}; {}; {}; {}; {}",event, worker, queue, job, runner, result, t);
            return ;
        }

        LOGGER.info("WorkerEvent {}; {}; {}; {}; {}; {}; {}",event, worker, queue, job, runner, result, t);
    }
}
