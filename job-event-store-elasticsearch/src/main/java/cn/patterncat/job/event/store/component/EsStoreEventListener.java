package cn.patterncat.job.event.store.component;

import cn.patterncat.job.event.JobEvent;
import cn.patterncat.job.event.store.dao.JobLogDao;
import cn.patterncat.job.event.store.domain.JobLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by patterncat on 2017-11-17.
 */
@Component
public class EsStoreEventListener implements ApplicationListener<JobEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsStoreEventListener.class);

    @Autowired
    JobLogDao jobLogDao;

    public void onApplicationEvent(JobEvent event) {
        try{
            JobLog log = JobLog.from(event);
            jobLogDao.save(log);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
    }
}
