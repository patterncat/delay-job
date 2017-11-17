package cn.patterncat.job.event.store.component;

import cn.patterncat.job.event.JobEvent;
import cn.patterncat.job.event.store.dao.JobLogDao;
import cn.patterncat.job.event.store.domain.JobLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

/**
 * Created by patterncat on 2017-11-17.
 */
public class MongoStoreEventListener implements ApplicationListener<JobEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoStoreEventListener.class);

    JobLogDao jobLogDao;

    public MongoStoreEventListener(JobLogDao jobLogDao) {
        this.jobLogDao = jobLogDao;
    }

    public void onApplicationEvent(JobEvent event) {
        try{
            JobLog log = JobLog.from(event);
            jobLogDao.save(log);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
    }
}
